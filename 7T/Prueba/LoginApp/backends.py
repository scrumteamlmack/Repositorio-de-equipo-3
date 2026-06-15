import hashlib
import re

from django.contrib.auth.backends import BaseBackend
from .models import Usuario


class UsuarioBackend(BaseBackend):
    """
    Backend de autenticación personalizado para el modelo Usuario.
    Soporta contraseñas en MD5 (32 hex) o SHA-256 (64 hex).
    El campo de credenciales es `num_documento`.
    """

    def _verificar_password(self, almacenada: str, ingresada: str) -> bool:
        almacenada = (almacenada or "").strip()
        ingresada = (ingresada or "").strip()
        if not almacenada:
            return False
        baja = almacenada.lower()
        if re.fullmatch(r"[0-9a-f]{64}", baja):
            return hashlib.sha256(ingresada.encode("utf-8")).hexdigest() == baja
        if re.fullmatch(r"[0-9a-f]{32}", baja):
            return hashlib.md5(ingresada.encode("utf-8")).hexdigest() == baja
        # Texto plano (solo para desarrollo)
        return almacenada == ingresada

    def authenticate(self, request, num_documento=None, password=None, **kwargs):
        if num_documento is None or password is None:
            return None
        try:
            usuario = Usuario.objects.get(num_documento=int(num_documento))
        except (Usuario.DoesNotExist, ValueError):
            return None

        if self._verificar_password(usuario.contrasena, password):
            return usuario
        return None

    def get_user(self, user_id):
        try:
            return Usuario.objects.get(pk=user_id)
        except Usuario.DoesNotExist:
            return None
