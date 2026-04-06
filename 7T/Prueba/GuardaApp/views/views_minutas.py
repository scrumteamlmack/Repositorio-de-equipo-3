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



from .utils import *
from LoginApp.decorators import login_requerido, rol_requerido

@never_cache
def listar_minutas(request):
    q = _q_param(request)
    minutas = (
        RegistroMinuta.objects.select_related(
            "ambiente",
            "guarda_seguridad_usuario_id_usuario__usuario_id_usuario",
            "responsable__usuario_id_usuario",
        )
        .all()
        .order_by("-fecha_hora_recibo")
    )
    minutas = _filtrar_minutas(minutas, q)
    return _render_guarda(request, "guarda/minutas_list.html", {"minutas": minutas, "q": q})


@never_cache
def crear_minuta(request):
    ambientes = Ambiente.objects.all().order_by("num_ambiente")
    responsables = Instructor.objects.select_related("usuario_id_usuario").all()
    usuario, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied

    if request.method == "POST":
        try:
            ambiente_id = int(request.POST.get("ambiente_id"))
            responsable_id = int(request.POST.get("responsable_id"))
            fecha_recibo = datetime.strptime(request.POST.get("fecha_hora_recibo"), "%Y-%m-%dT%H:%M")
            fecha_entrega = datetime.strptime(request.POST.get("fecha_hora_entrega"), "%Y-%m-%dT%H:%M")
            estado = (request.POST.get("estado") or "").strip()
            novedad = (request.POST.get("novedad") or "").strip() or None
            descripcion = (request.POST.get("descripcion_min") or "").strip() or None
        except (TypeError, ValueError):
            messages.error(request, "Datos inválidos para crear la minuta.")
            return _render_guarda(
                request,
                "guarda/minuta_form.html",
                {"ambientes": ambientes, "responsables": responsables, "modo": "crear"},
            )

        # Validación de fecha (Solo hoy)
        hoy = datetime.now().date()
        if fecha_recibo.date() != hoy:
            messages.error(request, f"Error: Solo se permite registrar minutas con la fecha del día actual ({hoy}).")
            return _render_guarda(request, "guarda/minuta_form.html", {"ambientes": ambientes, "responsables": responsables, "modo": "crear"})

        guarda = GuardaSeguridad.objects.filter(usuario_id_usuario=usuario).first()
        if not guarda:
            messages.error(request, "Tu usuario no tiene registro activo como guarda.")
            return _render_guarda(request, "guarda/minuta_form.html", {"ambientes": ambientes, "responsables": responsables, "modo": "crear"})

        RegistroMinuta.objects.create(
            fecha_hora_recibo=fecha_recibo,
            fecha_hora_entrega=fecha_entrega,
            novedad=novedad,
            descripcion_min=descripcion,
            estado=estado,
            ambiente_id=ambiente_id,
            guarda_seguridad_usuario_id_usuario=guarda,
            responsable_id=responsable_id,
        )
        messages.success(request, "Minuta creada correctamente.")
        return _replace_redirect('guarda:guarda_minutas')

    return _render_guarda(
        request,
        "guarda/minuta_form.html",
        {"ambientes": ambientes, "responsables": responsables, "modo": "crear"},
    )


@never_cache
def editar_minuta(request, minuta_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    minuta = get_object_or_404(RegistroMinuta, pk=minuta_id)
    ambientes = Ambiente.objects.all().order_by("num_ambiente")
    responsables = Instructor.objects.select_related("usuario_id_usuario").all()
    if request.method == "POST":
        try:
            minuta.ambiente_id = int(request.POST.get("ambiente_id"))
            minuta.responsable_id = int(request.POST.get("responsable_id"))
            minuta.fecha_hora_recibo = datetime.strptime(request.POST.get("fecha_hora_recibo"), "%Y-%m-%dT%H:%M")
            minuta.fecha_hora_entrega = datetime.strptime(request.POST.get("fecha_hora_entrega"), "%Y-%m-%dT%H:%M")
            minuta.estado = (request.POST.get("estado") or "").strip()
            minuta.novedad = (request.POST.get("novedad") or "").strip() or None
            minuta.descripcion_min = (request.POST.get("descripcion_min") or "").strip() or None
            minuta.save()
            messages.success(request, "Minuta actualizada correctamente.")
            return _replace_redirect('guarda:guarda_minutas')
        except (TypeError, ValueError):
            messages.error(request, "No se pudo actualizar la minuta, revisa los campos.")

    return _render_guarda(
        request,
        "guarda/minuta_form.html",
        {"minuta": minuta, "ambientes": ambientes, "responsables": responsables, "modo": "editar"},
    )


@require_POST
@never_cache
def eliminar_minuta(request, minuta_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    minuta = get_object_or_404(RegistroMinuta, pk=minuta_id)
    minuta.delete()
    messages.success(request, "Minuta eliminada correctamente.")
    return _replace_redirect('guarda:guarda_minutas')


