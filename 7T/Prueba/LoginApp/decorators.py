import functools
from django.shortcuts import redirect
from django.contrib import messages
from LoginApp.models import UserRol

def login_requerido(view_func):
    """
    Verifica que el usuario tenga una sesión activa.
    """
    @functools.wraps(view_func)
    def wrapper(request, *args, **kwargs):
        if not request.session.get('usuario_id'):
            messages.error(request, "Debes iniciar sesión para acceder a este módulo.")
            return redirect('login:login')
        return view_func(request, *args, **kwargs)
    return wrapper

def rol_requerido(roles_permitidos):
    """
    Verifica que el usuario en sesión tenga uno de los roles permitidos.
    roles_permitidos puede ser una lista de strings con nombres de roles en minúscula.
    """
    def decorator(view_func):
        @functools.wraps(view_func)
        def wrapper(request, *args, **kwargs):
            usuario_id = request.session.get('usuario_id')
            if not usuario_id:
                messages.error(request, "Debes iniciar sesión para acceder a este módulo.")
                return redirect('login:login')
            
            # Buscar el rol del usuario
            rol_usuario = UserRol.objects.select_related('id_rol').filter(id_usuario_id=usuario_id).first()
            if not rol_usuario:
                messages.error(request, "No tienes roles asignados.")
                return redirect('login:login')
            
            nombre_rol = (rol_usuario.id_rol.nombre_rol or "").strip().lower()
            
            if nombre_rol not in roles_permitidos:
                messages.error(request, "No tienes permisos para acceder a este módulo.")
                return redirect('login:login')
            
            return view_func(request, *args, **kwargs)
        return wrapper
    return decorator
