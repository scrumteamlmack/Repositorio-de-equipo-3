import hashlib
from collections import defaultdict
from urllib.parse import urlencode
from django.contrib import messages
from django.contrib.auth import logout
from django.db import IntegrityError, transaction
from django.db.models import Max, Q
from django.shortcuts import get_object_or_404, redirect, render
from django.urls import reverse
from django.views.decorators.http import require_POST
from django.views.generic import ListView, CreateView, UpdateView, DeleteView, View

from LoginApp.models import (
    Usuario,
    UserRol,
    Ficha,
    Rol,
    Programas,
    Recursos,
    Ambiente,
    Instructor,
    Jornada,
    Modalidad,
    Coordinacion,
    TipoRecurso,
)


from .utils import *
from LoginApp.decorators import login_requerido, rol_requerido

from django.views.decorators.cache import never_cache

@never_cache
def cerrar_sesion(request):
    # Este proyecto maneja sesión propia con `request.session['usuario_id']`.
    # Siempre la limpiamos, incluso si no se usa `django.contrib.auth`.
    request.session.pop("usuario_id", None)
    try:
        request.session.flush()
    except Exception:
        # Si el backend de sesión no permite flush por algún motivo,
        # al menos ya eliminamos la clave principal.
        pass
    if request.user.is_authenticated:
        logout(request)
    messages.success(request, "Sesión cerrada correctamente.")
    return redirect('admin_panel:siza')


@never_cache
@rol_requerido(['admin', 'administrador'])
def admin_index(request):
    return _render_admin(request, "admin_index.html")


def uadmin(request):
    return redirect('admin_panel:admin_index')


@never_cache
@rol_requerido(['admin', 'administrador'])
def index_admin(request):
    return _render_admin(request, "indexAdmin.html")


@never_cache
def siza(request):
    return render(request, "siza.html")


@rol_requerido(['admin', 'administrador'])
def form_instructor(request):
    return _render_admin(request, "formInstructor.html")


@rol_requerido(['admin', 'administrador'])
def form_coordinador(request):
    return _render_admin(request, "formCoordinador.html")


@rol_requerido(['admin', 'administrador'])
def form_guarda(request):
    return _render_admin(request, "formGuarda.html")


