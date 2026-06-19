from django.db import models
from UsuarioApp.models import Usuario

class Admin(models.Model):
    usuario = models.OneToOneField(
        Usuario,
        on_delete=models.DO_NOTHING,
        primary_key=True
    )

    class Meta:
        db_table = 'admin'
        managed = False

    def __str__(self):
        return f"Admin: {self.usuario}"