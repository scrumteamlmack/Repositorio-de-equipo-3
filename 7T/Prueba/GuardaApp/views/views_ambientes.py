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
def listar_ambientes(request):
    _liberar_minutas_vencidas()
    filtros = _get_ambientes_filters(request)
    ambientes = Ambiente.objects.all().order_by("id_ambiente")
    ambientes = _aplicar_filtros_ambientes(ambientes, filtros)
    return _render_guarda(
        request,
        "guarda/ambientes_list.html",
        {"ambientes": ambientes, "filtros": filtros, "hay_filtros": any(filtros.values())},
    )


@never_cache
def crear_ambiente(request):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    if request.method == "POST":
        try:
            Ambiente.objects.create(
                id_ambiente=_next_id(Ambiente, "id_ambiente"),
                num_ambiente=int(request.POST.get("num_ambiente")),
                capacidad=int(request.POST.get("capacidad")),
                tipo_ambiente=(request.POST.get("tipo_ambiente") or "").strip(),
                estado=(request.POST.get("estado") or "").strip(),
            )
            messages.success(request, "Ambiente creado correctamente.")
            return _replace_redirect('guarda:guarda_ambientes')
        except (TypeError, ValueError, IntegrityError):
            messages.error(request, "No se pudo crear el ambiente.")
    return _render_guarda(request, "guarda/ambiente_form.html", {"modo": "crear"})


@never_cache
def editar_ambiente(request, ambiente_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    ambiente = get_object_or_404(Ambiente, pk=ambiente_id)
    if request.method == "POST":
        try:
            ambiente.num_ambiente = int(request.POST.get("num_ambiente"))
            ambiente.capacidad = int(request.POST.get("capacidad"))
            ambiente.tipo_ambiente = (request.POST.get("tipo_ambiente") or "").strip()
            ambiente.estado = (request.POST.get("estado") or "").strip()
            ambiente.save()
            messages.success(request, "Ambiente actualizado correctamente.")
            return _replace_redirect('guarda:guarda_ambientes')
        except (TypeError, ValueError, IntegrityError):
            messages.error(request, "No se pudo actualizar el ambiente.")
    return _render_guarda(request, "guarda/ambiente_form.html", {"ambiente": ambiente, "modo": "editar"})


@require_POST
@never_cache
def eliminar_ambiente(request, ambiente_id):
    _, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    ambiente = get_object_or_404(Ambiente, pk=ambiente_id)
    ambiente.delete()
    messages.success(request, "Ambiente eliminado correctamente.")
    return _replace_redirect('guarda:guarda_ambientes')


