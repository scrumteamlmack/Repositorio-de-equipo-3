from django import forms
from .models import Usuario, Rol, Programas, Ficha, Coordinacion, Instructor, Aprendiz, GuardaSeguridad, Coordinador
from .validators import (
    validate_solo_letras, validate_solo_numeros, 
    validate_documento_longitud, validate_telefono_longitud, 
    validate_password_strength
)

class BaseUserForm(forms.ModelForm):
    p_nombre = forms.CharField(label="Primer Nombre", validators=[validate_solo_letras])
    s_nombre = forms.CharField(label="Segundo Nombre", required=False, validators=[validate_solo_letras])
    p_apellido = forms.CharField(label="Primer Apellido", validators=[validate_solo_letras])
    s_apellido = forms.CharField(label="Segundo Apellido", required=False, validators=[validate_solo_letras])
    tipo_documento = forms.ChoiceField(
        choices=[
            ('SE', 'Seleccione el tipo de documento'),
            ('CC', 'Cédula de Ciudadanía'),
            ('TI', 'Tarjeta de Identidad'),
            ('CE', 'Cédula de Extranjería'),
            ('PEP', 'Permiso Especial de Permanencia'),
        ],
        label="Tipo de Documento"
    )
    num_documento = forms.CharField(
        label="Número de Documento", 
        validators=[validate_solo_numeros, validate_documento_longitud],
        widget=forms.TextInput(attrs={
            'oninput': "this.value = this.value.replace(/[^0-9]/g, '')",
            'placeholder': 'Ej: 123456789',
            'pattern': '[0-9]*',
            'title': 'Solo se permiten números'
        })
    )
    correo = forms.EmailField(label="Correo Institucional")
    contrasena = forms.CharField(label="Contraseña", widget=forms.PasswordInput, validators=[validate_password_strength])
    rol = forms.ModelChoiceField(queryset=Rol.objects.all(), label="Rol", required=True)

    class Meta:
        model = Usuario
        fields = ['p_nombre', 's_nombre', 'p_apellido', 's_apellido', 'tipo_documento', 'num_documento', 'correo']

class AprendizForm(forms.ModelForm):
    programas_id_programas = forms.ModelChoiceField(queryset=Programas.objects.all(), label="Programa de Formación")
    ficha_idficha = forms.ModelChoiceField(queryset=Ficha.objects.all(), label="Número de Ficha")

    class Meta:
        model = Aprendiz
        fields = ['programas_id_programas', 'ficha_idficha']

class InstructorForm(forms.ModelForm):
    email = forms.EmailField(label="Correo Institucional")
    telefono = forms.CharField(label="Teléfono", validators=[validate_solo_numeros, validate_telefono_longitud])
    coordinacion_id_coordinacion = forms.ModelChoiceField(queryset=Coordinacion.objects.all(), label="Coordinación")
    estado = forms.ChoiceField(choices=[('Activo', 'Activo'), ('Inactivo', 'Inactivo')], label="Estado")

    class Meta:
        model = Instructor
        fields = ['email', 'telefono', 'coordinacion_id_coordinacion', 'estado']

class GuardaForm(forms.ModelForm):
    turno = forms.ChoiceField(choices=[('Mañana', 'Mañana'), ('Tarde', 'Tarde'), ('Noche', 'Noche')], label="Turno")
    fecha_ingreso = forms.DateField(label="Fecha de Ingreso", widget=forms.DateInput(attrs={'type': 'date'}))
    estado = forms.ChoiceField(choices=[('Activo', 'Activo'), ('Inactivo', 'Inactivo')], label="Estado")

    class Meta:
        model = GuardaSeguridad
        fields = ['turno', 'fecha_ingreso', 'estado']

class CoordinadorForm(forms.ModelForm):
    coordinacion_id_coordinacion = forms.ModelChoiceField(queryset=Coordinacion.objects.all(), label="Coordinación")

    class Meta:
        model = Coordinador
        fields = ['coordinacion_id_coordinacion']

class FichaForm(forms.ModelForm):
    instructores = forms.ModelMultipleChoiceField(
        queryset=Instructor.objects.all(),
        label="Instructores Responsables",
        required=True,
        error_messages={
            'required': 'Debes seleccionar al menos un instructor.'
        },
        widget=forms.SelectMultiple(attrs={'class': 'form-select', 'size': '6'})
    )

    class Meta:
        model = Ficha
        fields = ['num_ficha', 'instructores']
        labels = {
            'num_ficha': 'Número de ficha',
        }
        widgets = {
            'num_ficha': forms.TextInput(attrs={
                'class': 'form-control',
                'placeholder': 'Ej: 2271021',
                'oninput': "this.value = this.value.replace(/[^0-9]/g, '')",
                'minlength': '7',
                'pattern': '[0-9]*',
            }),
        }

    def clean_num_ficha(self):
        num_ficha = self.cleaned_data.get('num_ficha')
        if num_ficha is not None:
            # 1. No permitir números negativos
            if num_ficha < 0:
                raise forms.ValidationError("El número de ficha no puede ser negativo.")
            
            val_str = str(num_ficha)
            
            # 2. No iniciar por 0
            if val_str.startswith('0'):
                raise forms.ValidationError("El número de ficha no puede empezar por cero.")
                
            # 3. Mínimo 7 dígitos
            if len(val_str) < 7:
                raise forms.ValidationError("El número de ficha debe tener al menos 7 dígitos.")
                
            # 4. Validar unicidad (excluyendo la misma ficha si estamos editando)
            qs = Ficha.objects.filter(num_ficha=num_ficha)
            if self.instance.pk:
                qs = qs.exclude(pk=self.instance.pk)
            if qs.exists():
                raise forms.ValidationError("Este número de ficha ya está registrado en el sistema.")
                
        return num_ficha