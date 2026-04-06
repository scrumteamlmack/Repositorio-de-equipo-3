# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Make sure each ForeignKey and OneToOneField has `on_delete` set to the desired behavior
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from django.db import models


class AlertasInasistencia(models.Model):
    id_alerta = models.AutoField(primary_key=True, db_comment='Clave primaria. Identificador de la alerta.\n')
    aprendiz_id = models.IntegerField(db_comment='Llave forßnea. Relaciona la alerta con el aprendiz.\n')
    cantidad_fallas = models.IntegerField(db_comment='N·mero acumulado de inasistencias.\n')
    fecha_alerta = models.DateTimeField(db_comment='Fecha de generaci¾n de la alerta.\n')
    mensaje = models.TextField(db_comment='Descripci¾n o detalle de la alerta.\n')
    coordinacion = models.ForeignKey('Coordinacion', models.DO_NOTHING, db_comment='Coordinaci¾n que recibe o emite la alerta.\n\n')

    class Meta:
        managed = False
        db_table = 'alertas_inasistencia'


class Ambiente(models.Model):
    id_ambiente = models.IntegerField(primary_key=True, db_comment='Clave primaria. Identificador ·nico del ambiente.\n')
    num_ambiente = models.SmallIntegerField(db_comment='N·mero o c¾digo del ambiente fÝsico.\n')
    capacidad = models.SmallIntegerField(db_comment='Cantidad de personas que pueden estar en un ambiente')
    tipo_ambiente = models.CharField(max_length=45, db_comment='Tipo del ambiente (auditorio, sala, aula, etc.).\n')
    estado = models.CharField(max_length=30, db_comment='Estado actual del ambiente (disponible, ocupado, mantenimiento, etc.).\n')

    class Meta:
        managed = False
        db_table = 'ambiente'

    def __str__(self):
        return f"Ambiente {self.num_ambiente}"


class Aprendiz(models.Model):
    usuario_id_usuario = models.OneToOneField('Usuario', models.DO_NOTHING, db_column='Usuario_id_usuario', primary_key=True, db_comment='Llave primaria y forßnea. Identificador del aprendiz (usuario base).\n')  # Field name made lowercase.
    programas_id_programas = models.ForeignKey('Programas', models.DO_NOTHING, db_column='programas_id_programas', db_comment='Llave forßnea. Programa de formaci¾n del aprendiz.\n', null=True, blank=True)
    ficha_idficha = models.ForeignKey('Ficha', models.DO_NOTHING, db_column='ficha_idficha', null=True, blank=True)

    class Meta:
        managed = True
        db_table = 'aprendiz'


class AuthGroup(models.Model):
    name = models.CharField(unique=True, max_length=150)

    class Meta:
        managed = False
        db_table = 'auth_group'


class AuthGroupPermissions(models.Model):
    id = models.BigAutoField(primary_key=True)
    group = models.ForeignKey(AuthGroup, models.DO_NOTHING)
    permission = models.ForeignKey('AuthPermission', models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_group_permissions'
        unique_together = (('group', 'permission'),)


class AuthPermission(models.Model):
    name = models.CharField(max_length=255)
    content_type = models.ForeignKey('DjangoContentType', models.DO_NOTHING)
    codename = models.CharField(max_length=100)

    class Meta:
        managed = False
        db_table = 'auth_permission'
        unique_together = (('content_type', 'codename'),)


class AuthUser(models.Model):
    password = models.CharField(max_length=128)
    last_login = models.DateTimeField(blank=True, null=True)
    is_superuser = models.IntegerField()
    username = models.CharField(unique=True, max_length=150)
    first_name = models.CharField(max_length=150)
    last_name = models.CharField(max_length=150)
    email = models.CharField(max_length=254)
    is_staff = models.IntegerField()
    is_active = models.IntegerField()
    date_joined = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'auth_user'


class AuthUserGroups(models.Model):
    id = models.BigAutoField(primary_key=True)
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)
    group = models.ForeignKey(AuthGroup, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_user_groups'
        unique_together = (('user', 'group'),)


class AuthUserUserPermissions(models.Model):
    id = models.BigAutoField(primary_key=True)
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)
    permission = models.ForeignKey(AuthPermission, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_user_user_permissions'
        unique_together = (('user', 'permission'),)


class Coordinacion(models.Model):
    id_coordinacion = models.AutoField(primary_key=True, db_comment='Clave primaria. Identificador de la coordinaci¾n.\n')
    nombre_coordinacion = models.CharField(max_length=45, db_comment='Nombre de la coordinaci¾n (ej. tecnologia e innovacion).\n')
    correo_coordinacion = models.CharField(max_length=30, db_comment='Correo electr¾nico institucional de la coordinaci¾n.\n\n')

    class Meta:
        managed = False
        db_table = 'coordinacion'

    def __str__(self):
        return self.nombre_coordinacion


class Coordinador(models.Model):
    usuario_id_usuario = models.OneToOneField('Usuario', models.DO_NOTHING, db_column='Usuario_id_usuario', primary_key=True, db_comment='Llave primaria y forßnea. Usuario que tiene el rol de coordinador.\n')  # Field name made lowercase.
    coordinacion_id_coordinacion = models.ForeignKey(Coordinacion, models.DO_NOTHING, db_column='coordinacion_id_coordinacion', db_comment='Llave forßnea. Relaciona con la coordinaci¾n que lidera.\n\n', null=True, blank=True)

    class Meta:
        managed = True
        db_table = 'coordinador'


class DataWizardIdentifier(models.Model):
    serializer = models.CharField(max_length=255)
    name = models.CharField(max_length=255)
    field = models.CharField(max_length=255, blank=True, null=True)
    value = models.CharField(max_length=255, blank=True, null=True)
    attr_id = models.PositiveIntegerField(blank=True, null=True)
    resolved = models.IntegerField()
    attr_field = models.CharField(max_length=255, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'data_wizard_identifier'


class DataWizardRange(models.Model):
    type = models.CharField(max_length=10)
    header_col = models.IntegerField()
    start_col = models.IntegerField()
    end_col = models.IntegerField(blank=True, null=True)
    header_row = models.IntegerField()
    start_row = models.IntegerField()
    end_row = models.IntegerField(blank=True, null=True)
    count = models.IntegerField(blank=True, null=True)
    identifier = models.ForeignKey(DataWizardIdentifier, models.DO_NOTHING)
    run = models.ForeignKey('DataWizardRun', models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'data_wizard_range'


class DataWizardRecord(models.Model):
    object_id = models.PositiveIntegerField(blank=True, null=True)
    row = models.PositiveIntegerField()
    success = models.IntegerField()
    fail_reason = models.TextField(blank=True, null=True)
    content_type = models.ForeignKey('DjangoContentType', models.DO_NOTHING, blank=True, null=True)
    run = models.ForeignKey('DataWizardRun', models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'data_wizard_record'


class DataWizardRun(models.Model):
    record_count = models.IntegerField(blank=True, null=True)
    loader = models.CharField(max_length=255, blank=True, null=True)
    serializer = models.CharField(max_length=255, blank=True, null=True)
    object_id = models.PositiveIntegerField(blank=True, null=True)
    content_type = models.ForeignKey('DjangoContentType', models.DO_NOTHING, blank=True, null=True)
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'data_wizard_run'


class DataWizardRunlog(models.Model):
    event = models.CharField(max_length=100)
    date = models.DateTimeField()
    run = models.ForeignKey(DataWizardRun, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'data_wizard_runlog'


class DjangoAdminLog(models.Model):
    action_time = models.DateTimeField()
    object_id = models.TextField(blank=True, null=True)
    object_repr = models.CharField(max_length=200)
    action_flag = models.PositiveSmallIntegerField()
    change_message = models.TextField()
    content_type = models.ForeignKey('DjangoContentType', models.DO_NOTHING, blank=True, null=True)
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'django_admin_log'


class DjangoContentType(models.Model):
    app_label = models.CharField(max_length=100)
    model = models.CharField(max_length=100)

    class Meta:
        managed = False
        db_table = 'django_content_type'
        unique_together = (('app_label', 'model'),)


class DjangoMigrations(models.Model):
    id = models.BigAutoField(primary_key=True)
    app = models.CharField(max_length=255)
    name = models.CharField(max_length=255)
    applied = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'django_migrations'


class DjangoSession(models.Model):
    session_key = models.CharField(primary_key=True, max_length=40)
    session_data = models.TextField()
    expire_date = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'django_session'


class Ficha(models.Model):
    idficha = models.AutoField(primary_key=True)
    num_ficha = models.IntegerField(db_column='Num_ficha', unique=True)
    instructor_usuario_id_usuario = models.ForeignKey(
        'Instructor',
        models.DO_NOTHING,
        db_column='instructor_Usuario_id_usuario',
        null=True,
        blank=True,
        related_name='fichas'
    )

    class Meta:
        managed = True
        db_table = 'ficha'

    def __str__(self):
        return str(self.num_ficha)


class GuardaSeguridad(models.Model):
    usuario_id_usuario = models.OneToOneField('Usuario', models.DO_NOTHING, db_column='Usuario_id_usuario', primary_key=True, db_comment='Clave primaria y forßnea. Usuario que cumple funciones de guarda de seguridad.\n')  # Field name made lowercase.
    turno = models.CharField(max_length=6, db_comment='Turno del guarda(ej. ma±ana, tarde, etc...)')
    fecha_ingreso = models.DateField(db_comment='Fecha de ingreso laboral del guarda.\n')
    estado = models.CharField(max_length=8, db_comment='Estado laboral del guarda (activo/inactivo).\n\n')

    class Meta:
        managed = True
        db_table = 'guarda_seguridad'


class HistoricoIncidentes(models.Model):
    id_historico = models.AutoField(primary_key=True, db_comment='Clave primaria. Identificador del historial del incidente.\n')
    incidente = models.ForeignKey('RegistroIncidente', models.DO_NOTHING, db_comment='Llave forßnea. Incidente asociado.\n')
    ambiente = models.ForeignKey(Ambiente, models.DO_NOTHING, db_comment='Llave forßnea. Ambiente en donde ocurri¾.\n')
    tipo_incidente = models.ForeignKey('TipoIncidente', models.DO_NOTHING, db_comment='Llave forßnea. Tipo de incidente registrado.\n')
    descripcion = models.TextField(blank=True, null=True, db_comment='Descripci¾n de los hechos o seguimiento.\n')
    fecha_registro = models.DateTimeField(db_comment='Fecha del registro en el historial.\n\n')

    class Meta:
        managed = False
        db_table = 'historico_incidentes'


class Instructor(models.Model):
    usuario_id_usuario = models.OneToOneField('Usuario', models.DO_NOTHING, db_column='Usuario_id_usuario', primary_key=True, db_comment='Llave primaria y forßnea. Usuario que act·a como instructor.\n')  # Field name made lowercase.
    email = models.CharField(max_length=100, db_comment='Correo electr¾nico institucional.\n')
    telefono = models.CharField(max_length=20, db_comment='TelÚfono de contacto.\n')
    coordinacion_id_coordinacion = models.ForeignKey(Coordinacion, models.DO_NOTHING, db_column='coordinacion_id_coordinacion', db_comment='Coordinaci¾n a la que pertenece.\n', null=True, blank=True)
    estado = models.CharField(max_length=8, db_comment='Estado laboral (activo, inactivo).\n')

    class Meta:
        managed = True
        db_table = 'instructor'

    def __str__(self):
        u = self.usuario_id_usuario
        return f"{u.p_nombre} {u.p_apellido}".strip() or f"Instructor ID {self.pk}"


class Jornada(models.Model):
    id_jornada = models.IntegerField(primary_key=True, db_comment='Clave primaria. Identificador de la jornada.\n')
    nombre_jornada = models.CharField(max_length=9, db_comment='Nombre de la jornada (ma±ana, tarde, noche).\n\n')

    class Meta:
        managed = False
        db_table = 'jornada'

    def __str__(self):
        return self.nombre_jornada


class Modalidad(models.Model):
    id_modalidad = models.IntegerField(primary_key=True, db_comment='Clave primaria. Identificador de la modalidad.\n')
    nombre_modalidad = models.CharField(max_length=10, db_comment='Nombre de la modalidad (presencial, sincronica).\n\n')

    class Meta:
        managed = False
        db_table = 'modalidad'

    def __str__(self):
        return self.nombre_modalidad


class Programas(models.Model):
    id_programas = models.IntegerField(primary_key=True, db_comment='Clave primaria. Identificador del programa acadÚmico.\n')
    nombre_programa = models.CharField(max_length=50, db_comment='Nombre del programa.\n')
    nivel_formacion = models.CharField(max_length=30, db_comment='Nivel de formaci¾n (tecn¾logo, tÚcnico, etc.).\n')
    duracion = models.CharField(max_length=50, db_comment='Duraci¾n estimada del programa.\n')
    jornada = models.ForeignKey(Jornada, models.DO_NOTHING, db_comment='Jornada asignada.\n')
    modalidad = models.ForeignKey(Modalidad, models.DO_NOTHING, db_comment='Modalidad del programa.\n')
    coordinacion = models.ForeignKey(Coordinacion, models.DO_NOTHING, db_comment='Coordinaci¾n responsable del programa.\n\n')

    class Meta:
        managed = False
        db_table = 'programas'

    def __str__(self):
        return self.nombre_programa


class Recursos(models.Model):
    id_recurso = models.AutoField(primary_key=True, db_comment='Clave primaria. Identificador del recurso.\n')
    serial_recurso = models.CharField(max_length=100, db_comment='Serial fÝsico o interno del recurso.\n')
    num_recurso = models.IntegerField(db_comment='Nombre del recurso en el ambiente.\n')
    nombre_recurso = models.CharField(max_length=60, db_comment='Nombre del recurso.\n')
    tipo_recurso = models.ForeignKey('TipoRecurso', models.DO_NOTHING, db_column='tipo_recurso', db_comment='Llave forßnea. Tipo de recurso.\n')
    estado = models.CharField(max_length=16, blank=True, null=True, db_comment='Estado del recurso (operativo, da±ado, en mantenimiento).\n')
    observacion = models.TextField(blank=True, null=True, db_comment='Observacion hacia algun recurso.')
    ambiente = models.ForeignKey(Ambiente, models.DO_NOTHING, db_comment='Llave foranea, Ambiente al que pertenece.')

    class Meta:
        managed = False
        db_table = 'recursos'


class RegistroInasistencia(models.Model):
    id_inasistencia = models.AutoField(primary_key=True, db_comment='Clave primaria. Identificador del registro.\n')
    fecha_inasistencia = models.DateField(db_comment='Fecha del registro de asistencia.\n')
    estado_inasistencia = models.CharField(max_length=1, db_comment='Estado: S (asisti¾), R (retraso), N (no asistio).\n')
    jornada = models.ForeignKey(Jornada, models.DO_NOTHING, db_comment='Jornada del aprendiz.\n')
    aprendiz_usuario_id_usuario = models.ForeignKey(Aprendiz, models.DO_NOTHING, db_column='aprendiz_Usuario_id_usuario', db_comment='Llave forßnea al aprendiz.\n\n')  # Field name made lowercase.
    instructor_usuario_id_usuario = models.ForeignKey(Instructor, models.DO_NOTHING, db_column='instructor_Usuario_id_usuario')  # Field name made lowercase.

    class Meta:
        managed = False
        db_table = 'registro_inasistencia'


class RegistroIncidente(models.Model):
    id_incidente = models.AutoField(primary_key=True, db_comment='Clave primaria. Identificador del incidente.\n\n')
    descripcion = models.TextField(blank=True, null=True, db_comment='Descripci¾n general del incidente.\n')
    fecha_incidente = models.DateField(db_comment='Fecha en que ocurri¾.\n')
    hora_incidente = models.TimeField(db_comment='Hora en que ocurrio.')
    ambiente = models.ForeignKey(Ambiente, models.DO_NOTHING, db_comment='Ambiente donde sucedi¾.\n')
    tipo_inc = models.ForeignKey('TipoIncidente', models.DO_NOTHING, db_comment='Tipo de incidente.\n')
    usuario_id_usuario = models.ForeignKey('Usuario', models.DO_NOTHING, db_column='usuario_id_usuario')

    class Meta:
        managed = False
        db_table = 'registro_incidente'


class RegistroMinuta(models.Model):
    id_minuta = models.AutoField(primary_key=True, db_comment='Clave primaria. Identificador del registro de minuta.\n')
    fecha_hora_recibo = models.DateTimeField(db_comment='Fecha y hora de recibo del ambiente.\n')
    fecha_hora_entrega = models.DateTimeField(db_comment='Fecha y hora de entrega.\n')
    novedad = models.TextField(blank=True, null=True, db_comment='Novedad o eventualidad ocurrida.\n')
    descripcion_min = models.TextField(blank=True, null=True, db_comment='Observaciones generales.\n')
    estado = models.TextField(db_comment='Estado general del ambiente al momento.\n')
    ambiente = models.ForeignKey(Ambiente, models.DO_NOTHING, db_comment='Ambiente relacionado.\n')
    guarda_seguridad_usuario_id_usuario = models.ForeignKey(GuardaSeguridad, models.DO_NOTHING, db_column='guarda_seguridad_Usuario_id_usuario', db_comment='Guarda que recibi¾ o entreg¾.\n\n')  # Field name made lowercase.
    responsable = models.ForeignKey(Instructor, models.DO_NOTHING)
    registro_minutacol = models.CharField(max_length=45, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'registro_minuta'


class Rol(models.Model):
    id_rol = models.IntegerField(primary_key=True, db_comment='Clave primaria. Identificador del rol.\n')
    nombre_rol = models.CharField(max_length=45, db_comment='Nombre del rol (aprendiz, instructor, etc.).\n\n')

    class Meta:
        managed = False
        db_table = 'rol'

    def __str__(self):
        return self.nombre_rol


class SourcesFilesource(models.Model):
    name = models.CharField(max_length=255, blank=True, null=True)
    file = models.CharField(max_length=100)
    date = models.DateTimeField()
    user = models.ForeignKey(AuthUser, models.DO_NOTHING, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'sources_filesource'


class SourcesUrlsource(models.Model):
    name = models.CharField(max_length=255, blank=True, null=True)
    url = models.CharField(max_length=200)
    date = models.DateTimeField()
    user = models.ForeignKey(AuthUser, models.DO_NOTHING, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'sources_urlsource'


class TipoIncidente(models.Model):
    id_tipo_inc = models.IntegerField(primary_key=True, db_comment='Clave primaria. Identificador del tipo de incidente.\n')
    tipo_incidente = models.CharField(max_length=45, db_comment='Nombre del tipo.\n')
    observacion_inc = models.TextField(db_comment='Observaci¾n adicional.\n\n')

    class Meta:
        managed = False
        db_table = 'tipo_incidente'


class TipoRecurso(models.Model):
    id_tipo_recurso = models.IntegerField(primary_key=True, db_comment='Clave primaria. Identificador del tipo de recurso.\n')
    recurso_tipo = models.CharField(max_length=45, db_comment='Nombre del tipo (ej. PC, proyector, aire, etc.).\n')
    descripcion_tipo = models.CharField(max_length=60, blank=True, null=True, db_comment='Descripci¾n adicional.\n\n')

    class Meta:
        managed = False
        db_table = 'tipo_recurso'


class TrasladoRecurso(models.Model):
    id_traslado = models.AutoField(primary_key=True, db_comment='Clave primaria. Identificador del traslado.\n')
    recurso = models.ForeignKey(Recursos, models.DO_NOTHING, db_comment='Recurso trasladado.\n')
    ambiente_origen = models.ForeignKey(Ambiente, models.DO_NOTHING, db_column='ambiente_origen', db_comment='Ambiente de origen.\n')
    ambiente_destino = models.IntegerField(db_comment='Ambiente de destino.\n')
    fecha_traslado = models.DateTimeField(db_comment='Fecha del traslado.\n')
    observacion = models.TextField(blank=True, null=True, db_comment='Observaciones del traslado.\n\n')

    class Meta:
        managed = False
        db_table = 'traslado_recurso'


class UserRol(models.Model):
    id_user_rol = models.AutoField(primary_key=True, db_comment='Clave primaria. Identificador del registro.\n')
    id_usuario = models.ForeignKey('Usuario', models.DO_NOTHING, db_column='id_usuario', db_comment='Usuario asociado.\n', null=True, blank=True)
    id_rol = models.ForeignKey(Rol, models.DO_NOTHING, db_column='id_rol', db_comment='Rol asignado al usuario.\n\n', null=True, blank=True)

    class Meta:
        managed = True
        db_table = 'user_rol'


class Usuario(models.Model):
    id_usuario = models.AutoField(primary_key=True, db_comment='Clave primaria. Identificador del usuario.\n')
    p_nombre = models.CharField(max_length=50, db_comment='Primer nombre.\n')
    s_nombre = models.CharField(max_length=50, blank=True, null=True, db_comment='Segundo nombre.\n')
    p_apellido = models.CharField(max_length=45, db_comment='Primer apellido.\n')
    s_apellido = models.CharField(max_length=45, blank=True, null=True, db_comment='Segundo apellido.\n')
    tipo_documento = models.CharField(max_length=4, db_comment='Tipo de documento.\n')
    num_documento = models.IntegerField(db_comment='N·mero de documento.\n')
    correo = models.CharField(max_length=100, db_comment='Correo institucional.\n')
    contrasena = models.CharField(max_length=100, db_column='Contraseña')

    class Meta:
        managed = True
        db_table = 'usuario'
