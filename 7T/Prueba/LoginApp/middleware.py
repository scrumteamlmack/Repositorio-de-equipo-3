from django.utils.cache import add_never_cache_headers

class NoCacheMiddleware:
    """
    Middleware que añade cabeceras 'Cache-Control: no-cache, no-store, must-revalidate'
    a todas las respuestas para usuarios con sesión activa.
    Esto previene que el botón 'Atrás' del navegador muestre contenido sensible después del logout.
    """
    def __init__(self, get_response):
        self.get_response = get_response

    def __call__(self, request):
        response = self.get_response(request)
        # Si el usuario tiene una sesión iniciada o acaba de cerrar sesión (y queremos limpiar caché)
        if request.session.get('usuario_id'):
            add_never_cache_headers(response)
        return response
