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
        try:
            RegistroIncidente.objects.create(
                descripcion=(request.POST.get("descripcion") or "").strip() or None,
                fecha_incidente=request.POST.get("fecha_incidente"),
                hora_incidente=request.POST.get("hora_incidente"),
                ambiente_id=int(request.POST.get("ambiente_id")),
                tipo_inc_id=int(request.POST.get("tipo_inc_id")),
                usuario_id_usuario=usuario,
            )
            messages.success(request, "Incidente creado correctamente.")
            return _replace_redirect('guarda:guarda_incidentes')
        except (TypeError, ValueError, IntegrityError):
            messages.error(request, "No se pudo crear el incidente.")
    return _render_guarda(request, "guarda/incidente_form.html", {"ambientes": ambientes, "tipos": tipos, "modo": "crear"})


@never_cache
def editar_incidente(request, incidente_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    incidente = get_object_or_404(RegistroIncidente, pk=incidente_id)
    ambientes = Ambiente.objects.all().order_by("num_ambiente")
    tipos = TipoIncidente.objects.all().order_by("tipo_incidente")

    if request.method == "POST":
        try:
            incidente.descripcion = (request.POST.get("descripcion") or "").strip() or None
            incidente.fecha_incidente = request.POST.get("fecha_incidente")
            incidente.hora_incidente = request.POST.get("hora_incidente")
            incidente.ambiente_id = int(request.POST.get("ambiente_id"))
            incidente.tipo_inc_id = int(request.POST.get("tipo_inc_id"))
            incidente.save()
            messages.success(request, "Incidente actualizado correctamente.")
            return _replace_redirect('guarda:guarda_incidentes')
        except (TypeError, ValueError, IntegrityError):
            messages.error(request, "No se pudo actualizar el incidente.")
    return _render_guarda(request, "guarda/incidente_form.html", {"incidente": incidente, "ambientes": ambientes, "tipos": tipos, "modo": "editar"})


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
    return _render_guarda(request, "guarda/incidente_detalle.html", {"incidente": incidente})


