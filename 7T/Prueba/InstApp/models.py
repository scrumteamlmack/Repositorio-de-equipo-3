from django.db import models
from UsuarioApp.models import Usuario
from CoordinacionApp.models import Coordinacion

class Instructor(models.Model):
    usuario = models.OneToOneField(
        Usuario,
        on_delete=models.DO_NOTHING,
        db_column='Usuario_id_usuario',
        primary_key=True
    )
    email = models.CharField(max_length=100)
    telefono = models.CharField(max_length=20)
    coordinacion = models.ForeignKey(
        Coordinacion,
        on_delete=models.DO_NOTHING,
        db_column='coordinacion_id_coordinacion'
    )
    estado = models.CharField(max_length=8)

    class Meta:
        managed = False
        db_table = 'instructor'