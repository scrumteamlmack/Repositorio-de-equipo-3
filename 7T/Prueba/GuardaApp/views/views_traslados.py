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
from django.utils import timezone
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



from .utils import *
from LoginApp.decorators import login_requerido, rol_requerido


def _fecha_traslado_desde_post(valor):
    if not valor:
        raise ValueError("Fecha de traslado vacia.")

    fecha = datetime.strptime(valor, "%Y-%m-%dT%H:%M")
    fecha_actual = timezone.localdate()

    if fecha.date() != fecha_actual:
        raise ValueError("La fecha del traslado debe ser la fecha actual.")

    fecha = timezone.make_aware(fecha, timezone.get_current_timezone())

    return fecha


def _contexto_form_traslado(modo, recursos, ambientes, traslado=None):
    ahora = timezone.localtime()
    from LoginApp.models import Instructor
    instructores = Instructor.objects.select_related("usuario_id_usuario").filter(estado="Activo")
    if not instructores.exists():
        instructores = Instructor.objects.select_related("usuario_id_usuario").all()
    return {
        "traslado": traslado,
        "recursos": recursos,
        "ambientes": ambientes,
        "instructores": instructores,
        "modo": modo,
        "fecha_actual": ahora.strftime("%Y-%m-%d"),
        "fecha_minima": ahora.strftime("%Y-%m-%dT00:00"),
        "fecha_maxima": ahora.strftime("%Y-%m-%dT23:59"),
        "fecha_hora_actual": ahora.strftime("%Y-%m-%dT%H:%M"),
    }


@never_cache
def listar_traslados(request):
    filtros = _get_traslados_filters(request)
    traslados_qs = (
        TrasladoRecurso.objects.select_related("recurso", "ambiente_origen", "instructor_origen__usuario_id_usuario", "instructor_destino__usuario_id_usuario")
        .all()
        .order_by("-fecha_traslado")
    )
    traslados_qs = _aplicar_filtros_traslados(traslados_qs, filtros)

    destinos_ids = {t.ambiente_destino for t in traslados_qs if t.ambiente_destino}
    destinos_map = {
        ambiente.id_ambiente: ambiente.num_ambiente
        for ambiente in Ambiente.objects.filter(id_ambiente__in=destinos_ids)
    }

    traslados = []
    for traslado in traslados_qs:
        traslado.destino_num_ambiente = destinos_map.get(
            traslado.ambiente_destino,
            traslado.ambiente_destino,
        )
        traslados.append(traslado)

    return _render_guarda(
        request,
        "guarda/traslados_list.html",
        {"traslados": traslados, "filtros": filtros, "hay_filtros": any(filtros.values())},
    )


@never_cache
def crear_traslado(request):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    recursos = Recursos.objects.select_related("ambiente").all().order_by("nombre_recurso")
    ambientes = Ambiente.objects.all().order_by("num_ambiente")
    if request.method == "POST":
        try:
            recurso = get_object_or_404(
                Recursos.objects.select_related("ambiente"),
                pk=int(request.POST.get("recurso_id")),
            )
            fecha_traslado = _fecha_traslado_desde_post(request.POST.get("fecha_traslado"))
            inst_origen_id = request.POST.get("instructor_origen_id")
            inst_destino_id = request.POST.get("instructor_destino_id")
            
            TrasladoRecurso.objects.create(
                recurso=recurso,
                ambiente_origen=recurso.ambiente,
                ambiente_destino=int(request.POST.get("ambiente_destino")),
                fecha_traslado=fecha_traslado,
                observacion=(request.POST.get("observacion") or "").strip() or None,
                instructor_origen_id=int(inst_origen_id) if inst_origen_id else None,
                instructor_destino_id=int(inst_destino_id) if inst_destino_id else None,
                tiempo_prestamo=(request.POST.get("tiempo_prestamo") or "").strip() or None,
            )
            messages.success(request, "Traslado creado correctamente.")
            return _replace_redirect('guarda:guarda_traslados')
        except (TypeError, ValueError, IntegrityError) as exc:
            messages.error(request, str(exc) if isinstance(exc, ValueError) else "No se pudo crear el traslado.")
    return _render_guarda(
        request,
        "guarda/traslado_form.html",
        _contexto_form_traslado("crear", recursos, ambientes),
    )


@never_cache
def editar_traslado(request, traslado_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    traslado = get_object_or_404(TrasladoRecurso, pk=traslado_id)
    recursos = Recursos.objects.select_related("ambiente").all().order_by("nombre_recurso")
    ambientes = Ambiente.objects.all().order_by("num_ambiente")
    if request.method == "POST":
        try:
            recurso = get_object_or_404(
                Recursos.objects.select_related("ambiente"),
                pk=int(request.POST.get("recurso_id")),
            )
            inst_origen_id = request.POST.get("instructor_origen_id")
            inst_destino_id = request.POST.get("instructor_destino_id")
            
            traslado.recurso = recurso
            traslado.ambiente_origen = recurso.ambiente
            traslado.ambiente_destino = int(request.POST.get("ambiente_destino"))
            traslado.fecha_traslado = _fecha_traslado_desde_post(request.POST.get("fecha_traslado"))
            traslado.observacion = (request.POST.get("observacion") or "").strip() or None
            traslado.instructor_origen_id = int(inst_origen_id) if inst_origen_id else None
            traslado.instructor_destino_id = int(inst_destino_id) if inst_destino_id else None
            traslado.tiempo_prestamo = (request.POST.get("tiempo_prestamo") or "").strip() or None
            traslado.save()
            messages.success(request, "Traslado actualizado correctamente.")
            return _replace_redirect('guarda:guarda_traslados')
        except (TypeError, ValueError, IntegrityError) as exc:
            messages.error(request, str(exc) if isinstance(exc, ValueError) else "No se pudo actualizar el traslado.")
    return _render_guarda(
        request,
        "guarda/traslado_form.html",
        _contexto_form_traslado("editar", recursos, ambientes, traslado=traslado),
    )


@require_POST
@never_cache
def eliminar_traslado(request, traslado_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    traslado = get_object_or_404(TrasladoRecurso, pk=traslado_id)
    traslado.delete()
    messages.success(request, "Traslado eliminado correctamente.")
    return _replace_redirect('guarda:guarda_traslados')


@never_cache
def resolver_instructor_origen_ajax(request):
    """
    Endpoint AJAX para obtener el instructor asignado a un ambiente el día de hoy.
    """
    from django.http import JsonResponse
    from LoginApp.models import AsignacionAmbiente
    from django.utils import timezone

    ambiente_id = request.GET.get("ambiente_id")
    if not ambiente_id:
        return JsonResponse({"success": False, "error": "No se proporcionó el ID del ambiente."})

    hoy = timezone.localdate()
    # Buscar una asignación activa en el ambiente seleccionado para hoy
    asig = AsignacionAmbiente.objects.filter(
        ambiente_id=ambiente_id,
        fecha_inicio__lte=hoy,
        fecha_fin__gte=hoy,
        estado='ACTIVO'
    ).select_related("instructor__usuario_id_usuario").first()

    if asig:
        return JsonResponse({
            "success": True,
            "instructor_id": asig.instructor.usuario_id_usuario_id,
            "nombre": f"{asig.instructor.usuario_id_usuario.p_nombre} {asig.instructor.usuario_id_usuario.p_apellido}"
        })

    return JsonResponse({"success": False, "error": "No hay instructores asignados a este ambiente hoy."})
