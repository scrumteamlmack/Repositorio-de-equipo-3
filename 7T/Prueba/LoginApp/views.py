import hashlib
import re

from django.http import HttpResponse
from django.shortcuts import render, redirect
from django.contrib import messages
from .models import Usuario, UserRol


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
    # IMPORTANTE:
    # En este proyecto el login se usa también como "pantalla de entrada".
    # No auto-redirigimos en GET aunque exista sesión, porque rompe la navegabilidad
    # (al clickear "Iniciar sesión" se debe ver el formulario).

    if request.method == 'POST':
        cedula = request.POST.get('Cedula')
        password = request.POST.get('contraseña')

        try:
            user = Usuario.objects.get(num_documento=int(cedula))

            if not _contrasena_coincide(user.contrasena, password):
                messages.error(request, "Contraseña incorrecta")
                return (render(request, "login.html"))

            # GUARDAR SESIÓN
            request.session['usuario_id'] = user.id_usuario

            # SACAR ROL
            user_rol = UserRol.objects.get(id_usuario=user)
            rol = (user_rol.id_rol.nombre_rol or "").strip().lower()

            # REDIRIGIR A OTRA APP
            if rol in ("admin", "administrador"):
                return redirect('admin_panel:admin_index')

            if rol == "instructor":
                return redirect('instructor:instructor_index')

            if rol == "aprendiz":
                return redirect('aprendiz:aprendiz_index')

            if rol == "guarda de seguridad":
                return redirect('guarda:guarda_index')

            return redirect('mackapp:index')


        except Usuario.DoesNotExist:
            messages.error(request, "Usuario no encontrado")
        except UserRol.DoesNotExist:
            messages.error(request, "Usuario sin rol")

    return (render(request, "login.html"))