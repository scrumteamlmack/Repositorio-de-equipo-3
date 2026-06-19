from .models import Usuario

def user_context(request):
    """
    Makes the logged-in user object available globally in all templates.
    Use {{ user_logged_in.p_nombre }} to access the user's first name.
    """
    usuario_id = request.session.get('usuario_id')
    if usuario_id:
        try:
            user = Usuario.objects.get(pk=usuario_id)
            return {'user_logged_in': user}
        except Usuario.DoesNotExist:
            pass
    return {'user_logged_in': None}
