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
def listar_traslados(request):
    q = _q_param(request)
    traslados = (
        TrasladoRecurso.objects.select_related("recurso", "ambiente_origen")
        .all()
        .order_by("-fecha_traslado")
    )
    traslados = _filtrar_traslados(traslados, q)
    return _render_guarda(request, "guarda/traslados_list.html", {"traslados": traslados, "q": q})


@never_cache
def crear_traslado(request):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    recursos = Recursos.objects.select_related("ambiente").all().order_by("nombre_recurso")
    ambientes = Ambiente.objects.all().order_by("num_ambiente")
    if request.method == "POST":
        try:
            fecha_traslado = datetime.strptime(request.POST.get("fecha_traslado"), "%Y-%m-%dT%H:%M")
            TrasladoRecurso.objects.create(
                recurso_id=int(request.POST.get("recurso_id")),
                ambiente_origen_id=int(request.POST.get("ambiente_origen_id")),
                ambiente_destino=int(request.POST.get("ambiente_destino")),
                fecha_traslado=fecha_traslado,
                observacion=(request.POST.get("observacion") or "").strip() or None,
            )
            messages.success(request, "Traslado creado correctamente.")
            return _replace_redirect('guarda:guarda_traslados')
        except (TypeError, ValueError, IntegrityError):
            messages.error(request, "No se pudo crear el traslado.")
    return _render_guarda(request, "guarda/traslado_form.html", {"recursos": recursos, "ambientes": ambientes, "modo": "crear"})


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
            traslado.recurso_id = int(request.POST.get("recurso_id"))
            traslado.ambiente_origen_id = int(request.POST.get("ambiente_origen_id"))
            traslado.ambiente_destino = int(request.POST.get("ambiente_destino"))
            traslado.fecha_traslado = datetime.strptime(request.POST.get("fecha_traslado"), "%Y-%m-%dT%H:%M")
            traslado.observacion = (request.POST.get("observacion") or "").strip() or None
            traslado.save()
            messages.success(request, "Traslado actualizado correctamente.")
            return _replace_redirect('guarda:guarda_traslados')
        except (TypeError, ValueError, IntegrityError):
            messages.error(request, "No se pudo actualizar el traslado.")
    return _render_guarda(request, "guarda/traslado_form.html", {"traslado": traslado, "recursos": recursos, "ambientes": ambientes, "modo": "editar"})


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


