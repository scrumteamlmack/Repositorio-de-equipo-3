from django.db import models

class Usuario(models.Model):
    id_usuario = models.AutoField(primary_key=True)
    p_nombre = models.CharField(max_length=50)
    s_nombre = models.CharField(max_length=50, blank=True, null=True)
    p_apellido = models.CharField(max_length=45)
    s_apellido = models.CharField(max_length=45, blank=True, null=True)
    tipo_documento = models.CharField(max_length=4)
    num_documento = models.IntegerField(unique=True)
    correo = models.EmailField(unique=True)
    contrasena = models.CharField(max_length=100)

    class Meta:
        db_table = 'usuario'
        managed = False  # 🔥 porque ya tienes BD

    def __str__(self):
        return f"{self.p_nombre} {self.p_apellido}"