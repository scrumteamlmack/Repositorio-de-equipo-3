import hashlib

from django.shortcuts import render, redirect
from django.contrib import messages
from django.contrib.auth import authenticate, login as auth_login
from .models import Usuario, UserRol


def login_view(request):
    if request.method == 'POST':
        cedula   = request.POST.get('Cedula', '').strip()
        password = request.POST.get('contraseña', '').strip()

        try:
            # Autenticar usando el backend personalizado
            usuario = authenticate(request, num_documento=cedula, password=password)

            if usuario is None:
                messages.error(request, "Cédula o contraseña incorrectos.")
                return render(request, "login.html")

            # Mantener sesión personalizada (compatibilidad con decoradores)
            request.session['usuario_id'] = usuario.id_usuario

            # Determinar rol y redirigir
            user_rol = UserRol.objects.select_related('id_rol').get(id_usuario=usuario)
            rol = (user_rol.id_rol.nombre_rol or "").strip().lower()

            if rol in ("admin", "administrador"):
                return redirect('admin_panel:admin_index')
            if rol == "instructor":
                return redirect('instructor:instructor_index')
            if rol == "aprendiz":
                return redirect('aprendiz:aprendiz_index')
            if rol == "guarda de seguridad":
                return redirect('guarda:guarda_index')

            return redirect('mackapp:index')

        except UserRol.DoesNotExist:
            messages.error(request, "El usuario no tiene un rol asignado.")
        except Exception as e:
            messages.error(request, f"Error inesperado: {e}")

    return render(request, "login.html")