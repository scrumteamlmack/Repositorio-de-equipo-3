
__all__ = ['_no_cache', '_usuario_sesion', '_es_guarda', '_acceso_guarda_o_login', '_render_guarda', '_replace_redirect', '_next_id', '_q_param', '_maybe_int', '_safe_q_part', '_filtrar_minutas', '_filtrar_incidentes', '_filtrar_traslados', '_filtrar_ambientes']
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


