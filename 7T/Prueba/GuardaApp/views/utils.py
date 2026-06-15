
__all__ = ['_no_cache', '_usuario_sesion', '_es_guarda', '_acceso_guarda_o_login', '_render_guarda', '_replace_redirect', '_next_id', '_q_param', '_maybe_int', '_safe_q_part', '_get_ambientes_filters', '_aplicar_filtros_ambientes', '_get_minutas_filters', '_aplicar_filtros_minutas', '_get_incidentes_filters', '_aplicar_filtros_incidentes', '_get_traslados_filters', '_aplicar_filtros_traslados', '_filters_to_suffix', '_filtrar_minutas', '_filtrar_incidentes', '_filtrar_traslados', '_filtrar_ambientes', '_liberar_minutas_vencidas']
from datetime import datetime
from io import BytesIO
import json
import re

from django.contrib import messages
from django.db import IntegrityError, transaction
from django.db.models import Max, Q
from django.http import HttpResponse
from django.shortcuts import get_object_or_404, redirect, render
from django.urls import reverse
from django.views.decorators.cache import never_cache
from django.views.decorators.http import require_POST

from openpyxl import Workbook
from openpyxl.styles import Alignment, Font

from LoginApp.models import (
    Ambiente,
    GuardaSeguridad,
    Instructor,
    Recursos,
    RegistroIncidente,
    RegistroMinuta,
    Rol,
    TipoIncidente,
    TrasladoRecurso,
    UserRol,
    Usuario,
)



# Utils and constants

def _no_cache(response: HttpResponse) -> HttpResponse:
    response["Cache-Control"] = "no-cache, no-store, must-revalidate, max-age=0"
    response["Pragma"] = "no-cache"
    response["Expires"] = "0"
    return response


def _usuario_sesion(request):
    usuario_id = request.session.get("usuario_id")
    if not usuario_id:
        return None
    return Usuario.objects.filter(pk=usuario_id).first()


def _es_guarda(usuario: Usuario) -> bool:
    rol = (
        UserRol.objects.select_related("id_rol")
        .filter(id_usuario=usuario)
        .first()
    )
    if not rol:
        return False
    return rol.id_rol.nombre_rol.strip().lower() == "guarda de seguridad"


def _acceso_guarda_o_login(request):
    usuario = _usuario_sesion(request)
    if not usuario:
        return None, _no_cache(redirect('login:login'))
    if not _es_guarda(usuario):
        messages.error(request, "No tienes permisos para entrar al módulo de guarda.")
        return None, _no_cache(redirect('login:login'))
    return usuario, None


def _render_guarda(request, template, context=None):
    usuario, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    data = {
        "usuario_actual": usuario,
    }
    if context:
        data.update(context)
    return _no_cache(render(request, template, data))


def _replace_redirect(url_name: str, kwargs=None) -> HttpResponse:
    """
    Reemplaza la navegación (no hace PUSH al historial), para que el botón `Atrás`
    no devuelva al formulario que acabas de enviar.
    """
    kwargs = kwargs or {}
    url = reverse(url_name, kwargs=kwargs)
    payload_url = json.dumps(url)
    html = f"""
<!doctype html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
</head>
<body>
    <script>
        window.location.replace({payload_url});
    </script>
</body>
</html>
"""
    return _no_cache(HttpResponse(html))


def _next_id(model, field_name: str) -> int:
    current = model.objects.aggregate(mx=Max(field_name)).get("mx") or 0
    return int(current) + 1


def _q_param(request) -> str:
    return (request.GET.get("q") or "").strip()


def _maybe_int(value: str):
    if value and re.fullmatch(r"\d+", str(value).strip()):
        return int(value)
    return None


def _safe_q_part(q: str) -> str:
    """
    Convierte el filtro en una parte segura para el nombre del archivo.
    """
    q = (q or "").strip()
    if not q:
        return "todo"
    q = re.sub(r"[^0-9A-Za-z_-]+", "_", q).strip("_")
    return q[:50] if q else "todo"


def _filtrar_minutas(minutas_qs, q: str):
    if not q:
        return minutas_qs

    numero = _maybe_int(q)
    conds = (
        Q(estado__icontains=q)
        | Q(novedad__icontains=q)
        | Q(descripcion_min__icontains=q)
        | Q(responsable__usuario_id_usuario__p_nombre__icontains=q)
        | Q(responsable__usuario_id_usuario__p_apellido__icontains=q)
    )
    if numero is not None:
        conds |= Q(id_minuta=numero) | Q(ambiente__num_ambiente=numero)
    return minutas_qs.filter(conds)


def _filtrar_incidentes(incidentes_qs, q: str):
    if not q:
        return incidentes_qs

    numero = _maybe_int(q)
    conds = (
        Q(descripcion__icontains=q)
        | Q(tipo_inc__tipo_incidente__icontains=q)
        | Q(nivel_gravedad__icontains=q)
        | Q(usuario_id_usuario__p_nombre__icontains=q)
        | Q(usuario_id_usuario__p_apellido__icontains=q)
    )

    # Soporta búsqueda por fecha/hora en formato ISO (YYYY-MM-DD / HH:MM)
    try:
        fecha = datetime.strptime(q, "%Y-%m-%d").date()
        conds |= Q(fecha_incidente=fecha)
    except ValueError:
        pass
    try:
        hora = datetime.strptime(q, "%H:%M").time()
        conds |= Q(hora_incidente=hora)
    except ValueError:
        pass

    if numero is not None:
        conds |= Q(id_incidente=numero) | Q(ambiente__num_ambiente=numero)

    return incidentes_qs.filter(conds)


def _filtrar_traslados(traslados_qs, q: str):
    if not q:
        return traslados_qs

    numero = _maybe_int(q)
    conds = (
        Q(observacion__icontains=q)
        | Q(recurso__nombre_recurso__icontains=q)
        | Q(recurso__serial_recurso__icontains=q)
    )
    if numero is not None:
        conds |= (
            Q(id_traslado=numero)
            | Q(ambiente_origen__num_ambiente=numero)
            | Q(ambiente_destino=numero)
        )
    return traslados_qs.filter(conds)


def _filtrar_ambientes(ambientes_qs, q: str):
    if not q:
        return ambientes_qs

    numero = _maybe_int(q)
    conds = Q(tipo_ambiente__icontains=q) | Q(estado__icontains=q)
    if numero is not None:
        conds |= (
            Q(id_ambiente=numero)
            | Q(num_ambiente=numero)
            | Q(capacidad=numero)
        )
    return ambientes_qs.filter(conds)


def _get_str_param(request, key: str) -> str:
    return (request.GET.get(key) or "").strip()


def _filters_to_suffix(filtros: dict) -> str:
    partes = [f"{clave}_{valor}" for clave, valor in filtros.items() if valor]
    return _safe_q_part("_".join(partes))


def _get_ambientes_filters(request) -> dict:
    return {
        "id": _get_str_param(request, "id"),
        "numero": _get_str_param(request, "numero"),
        "capacidad": _get_str_param(request, "capacidad"),
        "tipo": _get_str_param(request, "tipo"),
        "estado": _get_str_param(request, "estado"),
    }


def _aplicar_filtros_ambientes(ambientes_qs, filtros: dict):
    ambiente_id = _maybe_int(filtros.get("id"))
    numero = _maybe_int(filtros.get("numero"))
    capacidad = _maybe_int(filtros.get("capacidad"))
    tipo = filtros.get("tipo")
    estado = filtros.get("estado")

    if ambiente_id is not None:
        ambientes_qs = ambientes_qs.filter(id_ambiente=ambiente_id)
    if numero is not None:
        ambientes_qs = ambientes_qs.filter(num_ambiente=numero)
    if capacidad is not None:
        ambientes_qs = ambientes_qs.filter(capacidad=capacidad)
    if tipo:
        ambientes_qs = ambientes_qs.filter(tipo_ambiente__icontains=tipo)
    if estado:
        ambientes_qs = ambientes_qs.filter(estado__icontains=estado)

    return ambientes_qs


def _get_minutas_filters(request) -> dict:
    return {
        "id": _get_str_param(request, "id"),
        "ambiente": _get_str_param(request, "ambiente"),
        "fecha_recibo": _get_str_param(request, "fecha_recibo"),
        "fecha_entrega": _get_str_param(request, "fecha_entrega"),
        "estado": _get_str_param(request, "estado"),
        "novedad": _get_str_param(request, "novedad"),
        "descripcion": _get_str_param(request, "descripcion"),
        "responsable": _get_str_param(request, "responsable"),
        "guarda": _get_str_param(request, "guarda"),
    }


def _aplicar_filtros_minutas(minutas_qs, filtros: dict):
    minuta_id = _maybe_int(filtros.get("id"))
    ambiente = _maybe_int(filtros.get("ambiente"))
    fecha_recibo = filtros.get("fecha_recibo")
    fecha_entrega = filtros.get("fecha_entrega")
    estado = filtros.get("estado")
    novedad = filtros.get("novedad")
    descripcion = filtros.get("descripcion")
    responsable = filtros.get("responsable")
    guarda = filtros.get("guarda")

    if minuta_id is not None:
        minutas_qs = minutas_qs.filter(id_minuta=minuta_id)
    if ambiente is not None:
        minutas_qs = minutas_qs.filter(ambiente__num_ambiente=ambiente)
    if fecha_recibo:
        minutas_qs = minutas_qs.filter(fecha_hora_recibo__date=fecha_recibo)
    if fecha_entrega:
        minutas_qs = minutas_qs.filter(fecha_hora_entrega__date=fecha_entrega)
    if estado:
        minutas_qs = minutas_qs.filter(estado__icontains=estado)
    if novedad:
        minutas_qs = minutas_qs.filter(novedad__icontains=novedad)
    if descripcion:
        minutas_qs = minutas_qs.filter(descripcion_min__icontains=descripcion)
    if responsable:
        minutas_qs = minutas_qs.filter(
            Q(responsable__usuario_id_usuario__p_nombre__icontains=responsable)
            | Q(responsable__usuario_id_usuario__p_apellido__icontains=responsable)
        )
    if guarda:
        minutas_qs = minutas_qs.filter(
            Q(guarda_seguridad_usuario_id_usuario__usuario_id_usuario__p_nombre__icontains=guarda)
            | Q(guarda_seguridad_usuario_id_usuario__usuario_id_usuario__p_apellido__icontains=guarda)
        )

    return minutas_qs


def _get_incidentes_filters(request) -> dict:
    return {
        "id": _get_str_param(request, "id"),
        "fecha": _get_str_param(request, "fecha"),
        "hora": _get_str_param(request, "hora"),
        "ambiente": _get_str_param(request, "ambiente"),
        "tipo": _get_str_param(request, "tipo"),
        "gravedad": _get_str_param(request, "gravedad"),
        "descripcion": _get_str_param(request, "descripcion"),
        "usuario": _get_str_param(request, "usuario"),
    }


def _aplicar_filtros_incidentes(incidentes_qs, filtros: dict):
    incidente_id = _maybe_int(filtros.get("id"))
    fecha = filtros.get("fecha")
    hora = filtros.get("hora")
    ambiente = _maybe_int(filtros.get("ambiente"))
    tipo = filtros.get("tipo")
    gravedad = filtros.get("gravedad")
    descripcion = filtros.get("descripcion")
    usuario = filtros.get("usuario")

    if incidente_id is not None:
        incidentes_qs = incidentes_qs.filter(id_incidente=incidente_id)
    if fecha:
        incidentes_qs = incidentes_qs.filter(fecha_incidente=fecha)
    if hora:
        try:
            hora_obj = datetime.strptime(hora, "%H:%M").time()
            incidentes_qs = incidentes_qs.filter(hora_incidente=hora_obj)
        except ValueError:
            incidentes_qs = incidentes_qs.none()
    if ambiente is not None:
        incidentes_qs = incidentes_qs.filter(ambiente__num_ambiente=ambiente)
    if tipo:
        incidentes_qs = incidentes_qs.filter(tipo_inc__tipo_incidente__icontains=tipo)
    if gravedad:
        incidentes_qs = incidentes_qs.filter(nivel_gravedad__iexact=gravedad)
    if descripcion:
        incidentes_qs = incidentes_qs.filter(descripcion__icontains=descripcion)
    if usuario:
        incidentes_qs = incidentes_qs.filter(
            Q(usuario_id_usuario__p_nombre__icontains=usuario)
            | Q(usuario_id_usuario__p_apellido__icontains=usuario)
        )

    return incidentes_qs


def _get_traslados_filters(request) -> dict:
    return {
        "id": _get_str_param(request, "id"),
        "recurso": _get_str_param(request, "recurso"),
        "serial": _get_str_param(request, "serial"),
        "origen": _get_str_param(request, "origen"),
        "destino": _get_str_param(request, "destino"),
        "fecha": _get_str_param(request, "fecha"),
        "observacion": _get_str_param(request, "observacion"),
    }


def _aplicar_filtros_traslados(traslados_qs, filtros: dict):
    traslado_id = _maybe_int(filtros.get("id"))
    recurso = filtros.get("recurso")
    serial = filtros.get("serial")
    origen = _maybe_int(filtros.get("origen"))
    destino = _maybe_int(filtros.get("destino"))
    fecha = filtros.get("fecha")
    observacion = filtros.get("observacion")

    if traslado_id is not None:
        traslados_qs = traslados_qs.filter(id_traslado=traslado_id)
    if recurso:
        traslados_qs = traslados_qs.filter(recurso__nombre_recurso__icontains=recurso)
    if serial:
        traslados_qs = traslados_qs.filter(recurso__serial_recurso__icontains=serial)
    if origen is not None:
        traslados_qs = traslados_qs.filter(ambiente_origen__num_ambiente=origen)
    if destino is not None:
        destinos_ids = Ambiente.objects.filter(num_ambiente=destino).values_list("id_ambiente", flat=True)
        traslados_qs = traslados_qs.filter(ambiente_destino__in=destinos_ids)
    if fecha:
        traslados_qs = traslados_qs.filter(fecha_traslado__date=fecha)
    if observacion:
        traslados_qs = traslados_qs.filter(observacion__icontains=observacion)

    return traslados_qs


def _liberar_minutas_vencidas():
    from django.utils import timezone
    from LoginApp.models import RegistroMinuta, Ambiente
    from django.db import transaction
    
    ahora = timezone.localtime()
    
    minutas_vencidas = RegistroMinuta.objects.filter(
        estado="Ocupado",
        fecha_hora_entrega__lt=ahora
    )
    
    if minutas_vencidas.exists():
        try:
            with transaction.atomic():
                for m in minutas_vencidas:
                    m.estado = "Disponible"
                    m.save(update_fields=['estado'])
                    
                    amb = m.ambiente
                    if amb.estado == "Ocupado":
                        otra_ocupada = RegistroMinuta.objects.filter(
                            ambiente=amb,
                            fecha_hora_recibo__lte=ahora,
                            fecha_hora_entrega__gte=ahora,
                            estado="Ocupado"
                        ).exclude(pk=m.pk).exists()
                        
                        if not otra_ocupada:
                            amb.estado = "Disponible"
                            amb.save(update_fields=['estado'])
        except Exception:
            pass



