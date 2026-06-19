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

NIVELES_GRAVEDAD = [opcion for opcion, _ in RegistroIncidente.NIVELES_GRAVEDAD]

from .utils import *
from LoginApp.decorators import login_requerido, rol_requerido

@never_cache
def listar_incidentes(request):
    filtros = _get_incidentes_filters(request)
    incidentes = (
        RegistroIncidente.objects.select_related("ambiente", "tipo_inc", "usuario_id_usuario")
        .all()
        .order_by("-fecha_incidente", "-hora_incidente")
    )
    incidentes = _aplicar_filtros_incidentes(incidentes, filtros)
    return _render_guarda(
        request,
        "guarda/incidentes_list.html",
        {"incidentes": incidentes, "filtros": filtros, "hay_filtros": any(filtros.values())},
    )


@never_cache
def crear_incidente(request):
    ambientes = Ambiente.objects.all().order_by("num_ambiente")
    tipos = TipoIncidente.objects.all().order_by("tipo_incidente")
    usuario, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied

    if request.method == "POST":
        nivel_gravedad = (request.POST.get("nivel_gravedad") or "").strip().title()
        try:
            if nivel_gravedad not in NIVELES_GRAVEDAD:
                raise ValueError("nivel_gravedad")

            from django.utils import timezone
            ahora = timezone.localtime()
            ambiente_id = int(request.POST.get("ambiente_id"))

            incidente = RegistroIncidente.objects.create(
                descripcion=(request.POST.get("descripcion") or "").strip() or None,
                fecha_incidente=ahora.date(),
                hora_incidente=ahora.time(),
                ambiente_id=ambiente_id,
                tipo_inc_id=int(request.POST.get("tipo_inc_id")),
                nivel_gravedad=nivel_gravedad,
                usuario_id_usuario=usuario,
                estado='Abierto',
            )

            # Email a coordinación del instructor asignado al ambiente
            try:
                from LoginApp.models import AsignacionAmbiente
                from django.core.mail import send_mail
                asignacion = AsignacionAmbiente.objects.select_related(
                    'instructor__coordinacion_id_coordinacion',
                    'instructor__usuario_id_usuario',
                ).filter(ambiente_id=ambiente_id, estado='ACTIVO').first()

                if asignacion and asignacion.instructor.coordinacion_id_coordinacion:
                    coord = asignacion.instructor.coordinacion_id_coordinacion
                    instr_u = asignacion.instructor.usuario_id_usuario
                    send_mail(
                        f"[SENA] Incidente en Ambiente {incidente.ambiente.num_ambiente}",
                        f"Incidente #{incidente.id_incidente}\n"
                        f"Fecha: {ahora.strftime('%d/%m/%Y %H:%M')}\n"
                        f"Tipo: {incidente.tipo_inc.tipo_incidente}\n"
                        f"Gravedad: {incidente.nivel_gravedad}\n"
                        f"Registrado por (Guarda): {usuario.p_nombre} {usuario.p_apellido}\n"
                        f"Instructor asignado: {instr_u.p_nombre} {instr_u.p_apellido}\n"
                        f"Descripción: {incidente.descripcion or 'Sin descripción'}",
                        'no-reply@sena.edu.co',
                        [coord.correo_coordinacion],
                        fail_silently=True,
                    )
            except Exception:
                pass

            messages.success(request, "Incidente creado correctamente.")
            return _replace_redirect('guarda:guarda_incidentes')
        except (TypeError, ValueError, IntegrityError):
            messages.error(request, "No se pudo crear el incidente.")

    from django.utils import timezone as tz
    return _render_guarda(
        request,
        "guarda/incidente_form.html",
        {
            "ambientes": ambientes,
            "tipos": tipos,
            "modo": "crear",
            "niveles_gravedad": NIVELES_GRAVEDAD,
            "hoy": tz.localdate().isoformat(),
            "hora_ahora": tz.localtime().strftime("%H:%M"),
        },
    )



@never_cache
def editar_incidente(request, incidente_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    incidente = get_object_or_404(RegistroIncidente, pk=incidente_id)
    ambientes = Ambiente.objects.all().order_by("num_ambiente")
    tipos = TipoIncidente.objects.all().order_by("tipo_incidente")

    if request.method == "POST":
        nivel_gravedad = (request.POST.get("nivel_gravedad") or "").strip().title()
        try:
            if nivel_gravedad not in NIVELES_GRAVEDAD:
                raise ValueError("nivel_gravedad")
            incidente.descripcion = (request.POST.get("descripcion") or "").strip() or None
            incidente.fecha_incidente = request.POST.get("fecha_incidente")
            incidente.hora_incidente = request.POST.get("hora_incidente")
            incidente.ambiente_id = int(request.POST.get("ambiente_id"))
            incidente.tipo_inc_id = int(request.POST.get("tipo_inc_id"))
            incidente.nivel_gravedad = nivel_gravedad
            incidente.save()
            messages.success(request, "Incidente actualizado correctamente.")
            return _replace_redirect('guarda:guarda_incidentes')
        except (TypeError, ValueError, IntegrityError):
            messages.error(request, "No se pudo actualizar el incidente.")
    return _render_guarda(
        request,
        "guarda/incidente_form.html",
        {"incidente": incidente, "ambientes": ambientes, "tipos": tipos, "modo": "editar", "niveles_gravedad": NIVELES_GRAVEDAD},
    )


@require_POST
@never_cache
def eliminar_incidente(request, incidente_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied


@require_POST
@never_cache
def eliminar_incidente(request, incidente_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    incidente = get_object_or_404(RegistroIncidente, pk=incidente_id)
    incidente.delete()
    messages.success(request, "Incidente eliminado correctamente.")
    return _replace_redirect('guarda:guarda_incidentes')


@never_cache
def detalle_incidente(request, incidente_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    incidente = get_object_or_404(
        RegistroIncidente.objects.select_related("ambiente", "tipo_inc", "usuario_id_usuario"),
        pk=incidente_id
    )
    from LoginApp.models import HistoricoIncidentes
    historicos = HistoricoIncidentes.objects.filter(incidente=incidente).order_by('-fecha_registro')
    return _render_guarda(request, "guarda/incidente_detalle.html", {"incidente": incidente, "historicos": historicos})


@never_cache
def actualizar_estado_incidente(request, incidente_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    incidente = get_object_or_404(RegistroIncidente, pk=incidente_id)
    if request.method == "POST":
        nuevo_estado = request.POST.get("estado")
        comentario = request.POST.get("comentario", "").strip()

        if nuevo_estado in dict(RegistroIncidente.ESTADOS_INCIDENTE):
            old_estado = incidente.estado
            if old_estado != nuevo_estado:
                incidente.estado = nuevo_estado
                incidente.save()

                if comentario:
                    from LoginApp.models import HistoricoIncidentes
                    latest_hist = HistoricoIncidentes.objects.filter(incidente=incidente).order_by('-id_historico').first()
                    if latest_hist:
                        latest_hist.descripcion = f"Cambio de estado: '{old_estado}' → '{nuevo_estado}'. Nota: {comentario}"
                        latest_hist.save(update_fields=['descripcion'])

                messages.success(request, f"Estado del incidente actualizado a {nuevo_estado}.")
            else:
                if comentario:
                    from LoginApp.models import HistoricoIncidentes
                    from django.utils import timezone
                    HistoricoIncidentes.objects.create(
                        incidente=incidente,
                        ambiente=incidente.ambiente,
                        tipo_incidente=incidente.tipo_inc,
                        descripcion=f"Nota de seguimiento: {comentario}",
                        fecha_registro=timezone.now()
                    )
                    messages.success(request, "Nota de seguimiento agregada correctamente.")
                else:
                    messages.info(request, "No se realizaron cambios.")
        else:
            messages.error(request, "Estado no válido.")

    return redirect('guarda:guarda_incidente_detalle', incidente_id=incidente.id_incidente)
