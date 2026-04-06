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
def guarda_index(request):
    usuario, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied
    return _render_guarda(request, "guarda/dashboard.html")


@never_cache
def cerrar_sesion_guarda(request):
    request.session.flush()
    response = _no_cache(render(request, "guarda/siza.html", {}))
    response["Clear-Site-Data"] = '"cache", "storage"'
    return response


@never_cache
def mi_perfil(request):
    usuario, denied = _acceso_guarda_o_login(request)
    if denied:
        return denied

    user_rol = UserRol.objects.filter(id_usuario=usuario).select_related("id_rol").first()
    rol_nombre = (user_rol.id_rol.nombre_rol or "").strip() if user_rol else "Guarda de Seguridad"

    if request.method == "POST":
        usuario.p_nombre = (request.POST.get("p_nombre") or "").strip()
        usuario.s_nombre = (request.POST.get("s_nombre") or "").strip() or None
        usuario.p_apellido = (request.POST.get("p_apellido") or "").strip()
        usuario.s_apellido = (request.POST.get("s_apellido") or "").strip() or None
        usuario.correo = (request.POST.get("correo") or "").strip()
        try:
            with transaction.atomic():
                usuario.save()
            messages.success(request, "Perfil actualizado correctamente.")
            return _replace_redirect('guarda:guarda_perfil')
        except IntegrityError:
            messages.error(request, "No se pudo actualizar el perfil.")

    return _render_guarda(
        request,
        "guarda/perfil.html",
        {"perfil": usuario, "rol_nombre": rol_nombre},
    )


