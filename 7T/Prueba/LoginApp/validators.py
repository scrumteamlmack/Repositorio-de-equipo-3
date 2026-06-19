import re
from django.core.exceptions import ValidationError
from django.utils.translation import gettext_lazy as _

def validate_solo_letras(value):
    """Permite solo letras, espacios, tildes y ñ."""
    if not re.match(r'^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$', value):
        raise ValidationError(
            _('%(value)s contiene caracteres no permitidos. Solo se admiten letras.'),
            params={'value': value},
        )

def validate_solo_numeros(value):
    """Permite solo números."""
    if not str(value).isdigit():
        raise ValidationError(
            _('Este campo solo debe contener números.'),
        )

def validate_documento_longitud(value):
    """Valida que el documento sea positivo, no empiece con cero y tenga entre 6 y 10 dígitos."""
    val_str = str(value)
    
    # Validar que solo sean números antes de otras comprobaciones
    if not val_str.isdigit():
        raise ValidationError(_('El número de documento debe contener solo dígitos.'))
    
    # No permitir que empiece con cero
    if val_str.startswith('0'):
        raise ValidationError(_('El número de documento no puede empezar por cero.'))
    
    # Rango de 6 a 10 dígitos
    if len(val_str) < 6 or len(val_str) > 10:
        raise ValidationError(_('El documento debe tener entre 6 y 10 dígitos.'))

def validate_telefono_longitud(value):
    """Valida longitud de teléfono (7 a 15 dígitos)."""
    val_str = str(value)
    if len(val_str) < 7 or len(val_str) > 15:
        raise ValidationError(
            _('El teléfono debe tener entre 7 y 15 dígitos.'),
        )

def validate_password_strength(value):
    """Valida longitud mínima y (opcionalmente) complejidad."""
    if len(value) < 8:
        raise ValidationError(
            _('La contraseña debe tener al menos 8 caracteres.'),
        )
    # Opcional: Requerir al menos una mayúscula y un número
    if not any(c.isupper() for c in value) or not any(c.isdigit() for c in value):
        raise ValidationError(
            _('La contraseña debe incluir al menos una letra mayúscula y un número.'),
        )
