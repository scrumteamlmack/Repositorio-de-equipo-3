import hashlib
import re

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


def _redirect_despues_login(request):
    raw = (request.POST.get("next") or request.GET.get("next") or "").strip()
    if raw.startswith("/") and not raw.startswith("//"):
        return redirect(raw)
    return None


def login_view(request):

    if request.method == 'POST':
        cedula = request.POST.get('Cedula')
        password = request.POST.get('contraseña')

        try:
            user = Usuario.objects.get(num_documento=int(cedula))

            if not _contrasena_coincide(user.contrasena, password):
                messages.error(request, "Contraseña incorrecta")
                return render(
                    request,
                    "login.html",
                    {"next": request.POST.get("next") or ""},
                )

            request.session['usuario_id'] = user.id_usuario

            user_rol = UserRol.objects.get(id_usuario=user)
            rol = user_rol.id_rol.nombre_rol.lower()

            destino = _redirect_despues_login(request)
            if destino:
                return destino

            if rol in ("admin", "administrador"):
                return redirect('admin_index')

            if rol == "instructor":
                return redirect('instructor_index')

            if rol == "aprendiz":
                return redirect('aprendiz_index')

            if rol == "guarda de seguridad":
                return redirect('guarda_index')

            return redirect('index')

        except Usuario.DoesNotExist:
            messages.error(request, "Usuario no encontrado")
        except UserRol.DoesNotExist:
            messages.error(request, "Usuario sin rol")

        return render(
            request,
            "login.html",
            {"next": request.POST.get("next") or request.GET.get("next") or ""},
        )

    return render(
        request,
        "login.html",
        {"next": request.GET.get("next") or ""},
    )