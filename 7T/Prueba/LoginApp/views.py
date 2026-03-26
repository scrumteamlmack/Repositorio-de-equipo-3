import hashlib
import re

from django.http import HttpResponse
from django.shortcuts import render, redirect
from django.contrib import messages
from .models import Usuario, UserRol


def _no_cache(response: HttpResponse) -> HttpResponse:
    response["Cache-Control"] = "no-cache, no-store, must-revalidate, max-age=0"
    response["Pragma"] = "no-cache"
    response["Expires"] = "0"
    return response


def _contrasena_coincide(almacenada: str, ingresada: str) -> bool:
    """La BD guarda MD5 (32 hex), SHA-256 (64 hex) o texto plano según el registro."""
    almacenada = (almacenada or "").strip()
    ingresada = (ingresada or "").strip()
    if not almacenada:
        return False
    baja = almacenada.lower()
    if re.fullmatch(r"[0-9a-f]{64}", baja):
        return hashlib.sha256(ingresada.encode("utf-8")).hexdigest() == baja
    if re.fullmatch(r"[0-9a-f]{32}", baja):
        return hashlib.md5(ingresada.encode("utf-8")).hexdigest() == baja
    return almacenada == ingresada


def login_view(request):
    # Si ya hay sesión, evita que el botón "Atrás" vuelva al login.
    if request.method == "GET":
        usuario_id = request.session.get("usuario_id")
        if usuario_id:
            user = Usuario.objects.filter(pk=usuario_id).first()
            if user:
                user_rol = UserRol.objects.filter(id_usuario=user).select_related("id_rol").first()
                rol = (user_rol.id_rol.nombre_rol or "").strip().lower() if user_rol else ""

                if rol in ("admin", "administrador"):
                    return _no_cache(redirect("admin_index"))
                if rol == "instructor":
                    return _no_cache(redirect("instructor_index"))
                if rol == "aprendiz":
                    return _no_cache(redirect("Aprendiz_index"))
                if rol == "guarda de seguridad":
                    return _no_cache(redirect("guarda_index"))
                return _no_cache(redirect("index"))

    if request.method == 'POST':
        cedula = request.POST.get('Cedula')
        password = request.POST.get('contraseña')

        try:
            user = Usuario.objects.get(num_documento=int(cedula))

            if not _contrasena_coincide(user.contrasena, password):
                messages.error(request, "Contraseña incorrecta")
                return _no_cache(render(request, "login.html"))

            # GUARDAR SESIÓN
            request.session['usuario_id'] = user.id_usuario

            # SACAR ROL
            user_rol = UserRol.objects.get(id_usuario=user)
            rol = (user_rol.id_rol.nombre_rol or "").strip().lower()

            # REDIRIGIR A OTRA APP
            if rol in ("admin", "administrador"):
                return _no_cache(redirect('admin_index'))
            
            elif rol in ("instructor",):
                return _no_cache(redirect('instructor_index'))
            
            elif rol in ("aprendiz",):
                return _no_cache(redirect('Aprendiz_index'))
            
            elif rol in ("guarda de seguridad",):
                return _no_cache(redirect('guarda_index'))
            
            else:
                return _no_cache(redirect('index'))

        except Usuario.DoesNotExist:
            messages.error(request, "Usuario no encontrado")
        except UserRol.DoesNotExist:
            messages.error(request, "Usuario sin rol")

    return _no_cache(render(request, "login.html"))