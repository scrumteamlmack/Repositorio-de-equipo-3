from django.db import models


class AlertasInasistencia(models.Model):
    id_alerta = models.AutoField(primary_key=True)
    aprendiz_id = models.IntegerField()
    cantidad_fallas = models.IntegerField()
    fecha_alerta = models.DateTimeField()
    mensaje = models.TextField()
    coordinacion = models.ForeignKey('Coordinacion', models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'alertas_inasistencia'


class Ambiente(models.Model):
    id_ambiente = models.IntegerField(primary_key=True)
    num_ambiente = models.SmallIntegerField()
    capacidad = models.SmallIntegerField()
    tipo_ambiente = models.CharField(max_length=45)
    estado = models.CharField(max_length=30)

    class Meta:
        managed = False
        db_table = 'ambiente'

    def __str__(self):
        return f"Ambiente {self.num_ambiente}"


class Aprendiz(models.Model):
    usuario_id_usuario = models.OneToOneField(
        'Usuario', models.DO_NOTHING,
        db_column='Usuario_id_usuario', primary_key=True
    )
    programas_id_programas = models.ForeignKey(
        'Programas', models.DO_NOTHING,
        db_column='programas_id_programas', null=True, blank=True
    )
    ficha_idficha = models.ForeignKey(
        'Ficha', models.DO_NOTHING,
        db_column='ficha_idficha', null=True, blank=True
    )

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
    id_coordinacion = models.AutoField(primary_key=True)
    nombre_coordinacion = models.CharField(max_length=45)
    correo_coordinacion = models.CharField(max_length=100)

    class Meta:
        managed = False
        db_table = 'coordinacion'

    def __str__(self):
        return self.nombre_coordinacion


class Coordinador(models.Model):
    usuario_id_usuario = models.OneToOneField(
        'Usuario', models.DO_NOTHING,
        db_column='Usuario_id_usuario', primary_key=True
    )
    coordinacion_id_coordinacion = models.ForeignKey(
        Coordinacion, models.DO_NOTHING,
        db_column='coordinacion_id_coordinacion', null=True, blank=True
    )

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
    instructores = models.ManyToManyField(
        'Instructor',
        related_name='fichas_asignadas',
        db_table='ficha_instructor',
        blank=True
    )

    class Meta:
        managed = True
        db_table = 'ficha'

    def __str__(self):
        return str(self.num_ficha)


class GuardaSeguridad(models.Model):
    usuario_id_usuario = models.OneToOneField(
        'Usuario', models.DO_NOTHING,
        db_column='Usuario_id_usuario', primary_key=True
    )
    turno = models.CharField(max_length=6)
    fecha_ingreso = models.DateField()
    estado = models.CharField(max_length=8)

    class Meta:
        managed = True
        db_table = 'guarda_seguridad'


class HistoricoIncidentes(models.Model):
    id_historico = models.AutoField(primary_key=True)
    incidente = models.ForeignKey('RegistroIncidente', models.DO_NOTHING)
    ambiente = models.ForeignKey(Ambiente, models.DO_NOTHING)
    tipo_incidente = models.ForeignKey('TipoIncidente', models.DO_NOTHING)
    descripcion = models.TextField(blank=True, null=True)
    fecha_registro = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'historico_incidentes'


class Instructor(models.Model):
    usuario_id_usuario = models.OneToOneField(
        'Usuario', models.DO_NOTHING,
        db_column='Usuario_id_usuario', primary_key=True
    )
    email = models.CharField(max_length=100)
    telefono = models.CharField(max_length=20)
    coordinacion_id_coordinacion = models.ForeignKey(
        Coordinacion, models.DO_NOTHING,
        db_column='coordinacion_id_coordinacion', null=True, blank=True
    )
    estado = models.CharField(max_length=8)

    class Meta:
        managed = True
        db_table = 'instructor'

    def __str__(self):
        u = self.usuario_id_usuario
        return f"{u.p_nombre} {u.p_apellido}".strip() or f"Instructor ID {self.pk}"


class Jornada(models.Model):
    id_jornada = models.IntegerField(primary_key=True)
    nombre_jornada = models.CharField(max_length=9)

    class Meta:
        managed = False
        db_table = 'jornada'

    def __str__(self):
        return self.nombre_jornada


class Modalidad(models.Model):
    id_modalidad = models.IntegerField(primary_key=True)
    nombre_modalidad = models.CharField(max_length=10)

    class Meta:
        managed = False
        db_table = 'modalidad'

    def __str__(self):
        return self.nombre_modalidad


class Programas(models.Model):
    id_programas = models.IntegerField(primary_key=True)
    nombre_programa = models.CharField(max_length=50)
    nivel_formacion = models.CharField(max_length=30)
    duracion = models.CharField(max_length=50)
    jornada = models.ForeignKey(Jornada, models.DO_NOTHING)
    modalidad = models.ForeignKey(Modalidad, models.DO_NOTHING)
    coordinacion = models.ForeignKey(Coordinacion, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'programas'

    def __str__(self):
        return self.nombre_programa


class Recursos(models.Model):
    id_recurso = models.AutoField(primary_key=True)
    serial_recurso = models.CharField(max_length=100)
    num_recurso = models.IntegerField()
    nombre_recurso = models.CharField(max_length=60)
    tipo_recurso = models.ForeignKey('TipoRecurso', models.DO_NOTHING, db_column='tipo_recurso')
    estado = models.CharField(max_length=16, blank=True, null=True)
    observacion = models.TextField(blank=True, null=True)
    ambiente = models.ForeignKey(Ambiente, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'recursos'


class RegistroInasistencia(models.Model):
    TIPO_CHOICES = (
        ('Justificada', 'Justificada'),
        ('No justificada', 'No justificada'),
    )

    id_inasistencia = models.AutoField(primary_key=True)
    fecha_inasistencia = models.DateField()
    estado_inasistencia = models.CharField(max_length=1)
    jornada = models.ForeignKey(Jornada, models.DO_NOTHING)
    aprendiz_usuario_id_usuario = models.ForeignKey(
        Aprendiz, models.DO_NOTHING, db_column='aprendiz_Usuario_id_usuario'
    )
    instructor_usuario_id_usuario = models.ForeignKey(
        Instructor, models.DO_NOTHING, db_column='instructor_Usuario_id_usuario'
    )
    tipo_inasistencia = models.CharField(
        max_length=20, choices=TIPO_CHOICES, blank=True, null=True
    )

    def save(self, *args, **kwargs):
        # Auto-asignar tipo según el estado
        if self.estado_inasistencia == 'N' and not self.tipo_inasistencia:
            self.tipo_inasistencia = 'No justificada'
        elif self.estado_inasistencia in ('S', 'R'):
            self.tipo_inasistencia = None
        super().save(*args, **kwargs)

    class Meta:
        managed = True
        db_table = 'registro_inasistencia'


class RegistroIncidente(models.Model):
    NIVELES_GRAVEDAD = (
        ('Bajo', 'Bajo'),
        ('Medio', 'Medio'),
        ('Alto', 'Alto'),
    )
    ESTADOS_INCIDENTE = (
        ('Abierto', 'Abierto'),
        ('En Proceso', 'En Proceso'),
        ('Cerrado', 'Cerrado'),
    )

    id_incidente = models.AutoField(primary_key=True)
    descripcion = models.TextField(blank=True, null=True)
    fecha_incidente = models.DateField()
    hora_incidente = models.TimeField()
    ambiente = models.ForeignKey(Ambiente, models.DO_NOTHING)
    tipo_inc = models.ForeignKey('TipoIncidente', models.DO_NOTHING)
    nivel_gravedad = models.CharField(max_length=10, choices=NIVELES_GRAVEDAD, blank=True, null=True)
    usuario_id_usuario = models.ForeignKey('Usuario', models.DO_NOTHING, db_column='usuario_id_usuario')
    estado = models.CharField(max_length=20, choices=ESTADOS_INCIDENTE, default='Abierto')

    def save(self, *args, **kwargs):
        """Registrar en historial cuando el estado cambia."""
        old_estado = None
        is_update = self.pk is not None
        if is_update:
            try:
                old_estado = RegistroIncidente.objects.values_list('estado', flat=True).get(pk=self.pk)
            except RegistroIncidente.DoesNotExist:
                pass

        super().save(*args, **kwargs)

        if is_update and old_estado and old_estado != self.estado:
            from django.utils import timezone
            HistoricoIncidentes.objects.create(
                incidente=self,
                ambiente=self.ambiente,
                tipo_incidente=self.tipo_inc,
                descripcion=f"Cambio de estado: '{old_estado}' → '{self.estado}'",
                fecha_registro=timezone.now(),
            )

    class Meta:
        managed = True
        db_table = 'registro_incidente'


class RegistroMinuta(models.Model):
    id_minuta = models.AutoField(primary_key=True)
    fecha_hora_recibo = models.DateTimeField()
    fecha_hora_entrega = models.DateTimeField()
    novedad = models.TextField(blank=True, null=True)
    descripcion_min = models.TextField(blank=True, null=True)
    estado = models.TextField()
    ambiente = models.ForeignKey(Ambiente, models.DO_NOTHING)
    guarda_seguridad_usuario_id_usuario = models.ForeignKey(
        GuardaSeguridad, models.DO_NOTHING,
        db_column='guarda_seguridad_Usuario_id_usuario'
    )
    responsable = models.ForeignKey(Instructor, models.DO_NOTHING)
    registro_minutacol = models.CharField(max_length=45, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'registro_minuta'


class Rol(models.Model):
    id_rol = models.IntegerField(primary_key=True)
    nombre_rol = models.CharField(max_length=45)

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
    id_tipo_inc = models.IntegerField(primary_key=True)
    tipo_incidente = models.CharField(max_length=45)
    observacion_inc = models.TextField()

    class Meta:
        managed = False
        db_table = 'tipo_incidente'

    def __str__(self):
        return self.tipo_incidente


class TipoRecurso(models.Model):
    id_tipo_recurso = models.IntegerField(primary_key=True)
    recurso_tipo = models.CharField(max_length=45)
    descripcion_tipo = models.CharField(max_length=60, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'tipo_recurso'


class TrasladoRecurso(models.Model):
    id_traslado = models.AutoField(primary_key=True)
    recurso = models.ForeignKey(Recursos, models.DO_NOTHING)
    ambiente_origen = models.ForeignKey(Ambiente, models.DO_NOTHING, db_column='ambiente_origen')
    ambiente_destino = models.IntegerField()
    fecha_traslado = models.DateTimeField()
    observacion = models.TextField(blank=True, null=True)
    instructor_origen = models.ForeignKey(
        'Instructor', models.SET_NULL,
        db_column='instructor_origen',
        null=True, blank=True,
        related_name='traslados_autorizados'
    )
    instructor_destino = models.ForeignKey(
        'Instructor', models.SET_NULL,
        db_column='instructor_destino',
        null=True, blank=True,
        related_name='traslados_recibidos'
    )
    tiempo_prestamo = models.CharField(max_length=100, blank=True, null=True)

    class Meta:
        managed = True
        db_table = 'traslado_recurso'


class UserRol(models.Model):
    id_user_rol = models.AutoField(primary_key=True)
    id_usuario = models.ForeignKey('Usuario', models.DO_NOTHING, db_column='id_usuario', null=True, blank=True)
    id_rol = models.ForeignKey(Rol, models.DO_NOTHING, db_column='id_rol', null=True, blank=True)

    class Meta:
        managed = True
        db_table = 'user_rol'


class Usuario(models.Model):
    id_usuario = models.AutoField(primary_key=True)
    p_nombre = models.CharField(max_length=50)
    s_nombre = models.CharField(max_length=50, blank=True, null=True)
    p_apellido = models.CharField(max_length=45)
    s_apellido = models.CharField(max_length=45, blank=True, null=True)
    tipo_documento = models.CharField(max_length=4)
    num_documento = models.IntegerField()
    correo = models.CharField(max_length=100)
    contrasena = models.CharField(max_length=100, db_column='Contraseña')

    # Propiedades requeridas para compatibilidad con Django auth
    @property
    def is_anonymous(self):
        return False

    @property
    def is_authenticated(self):
        return True

    @property
    def is_active(self):
        return True

    def get_username(self):
        return str(self.num_documento)

    def get_full_name(self):
        return f"{self.p_nombre} {self.p_apellido}".strip()

    def __str__(self):
        return self.get_full_name()

    class Meta:
        managed = True
        db_table = 'usuario'


# ─── NUEVOS MODELOS ──────────────────────────────────────────────────────────

class AsignacionAmbiente(models.Model):
    """Relaciona instructores con ambientes físicos por trimestre."""
    id_asignacion = models.AutoField(primary_key=True)
    instructor = models.ForeignKey(
        Instructor, models.CASCADE,
        db_column='instructor_Usuario_id_usuario',
        related_name='asignaciones'
    )
    ambiente = models.ForeignKey(
        Ambiente, models.CASCADE,
        db_column='ambiente_id',
        related_name='asignaciones'
    )
    jornada = models.ForeignKey(
        'Jornada', models.DO_NOTHING,
        db_column='jornada_id',
        related_name='asignaciones',
        null=True, blank=True
    )
    fecha_inicio = models.DateField()
    fecha_fin = models.DateField()
    trimestre = models.CharField(max_length=50)
    estado = models.CharField(max_length=20, default='ACTIVO')

    class Meta:
        managed = True
        db_table = 'asignacion_ambiente'

    def clean(self):
        from django.core.exceptions import ValidationError
        super().clean()
        if self.fecha_inicio and self.fecha_fin:
            if self.fecha_inicio > self.fecha_fin:
                raise ValidationError({
                    'fecha_inicio': 'La fecha de inicio no puede ser posterior a la fecha de fin.'
                })
            
            # Chequear traslapes si jornada está definida
            if self.jornada:
                # El conflicto se da solo si el MISMO instructor intenta ser asignado
                # al mismo ambiente en la misma jornada en fechas traslapadas.
                conflictos = AsignacionAmbiente.objects.filter(
                    instructor=self.instructor,
                    ambiente=self.ambiente,
                    jornada=self.jornada,
                    estado='ACTIVO',
                    fecha_inicio__lte=self.fecha_fin,
                    fecha_fin__gte=self.fecha_inicio
                )
                if self.pk:
                    conflictos = conflictos.exclude(pk=self.pk)
                if conflictos.exists():
                    raise ValidationError(
                        f"Este instructor ya tiene asignado el Ambiente {self.ambiente.num_ambiente} "
                        f"en la jornada '{self.jornada}' en el rango de fechas seleccionado."
                    )

    def save(self, *args, **kwargs):
        self.full_clean()
        super().save(*args, **kwargs)

    @property
    def esta_activa_hoy(self):
        from django.utils import timezone
        hoy = timezone.localdate()
        return self.estado == 'ACTIVO' and self.fecha_inicio <= hoy <= self.fecha_fin

    def __str__(self):
        jornada_str = self.jornada.nombre_jornada if self.jornada else "Sin jornada"
        return f"{self.instructor} → Ambiente {self.ambiente} ({jornada_str}) | {self.trimestre}"


class Justificacion(models.Model):
    """Justificación en PDF subida por un aprendiz para una inasistencia."""
    ESTADOS = (
        ('Pendiente', 'Pendiente'),
        ('Aprobada', 'Aprobada'),
        ('Rechazada', 'Rechazada'),
    )

    id_justificacion = models.AutoField(primary_key=True)
    inasistencia = models.OneToOneField(
        RegistroInasistencia, models.CASCADE,
        db_column='registro_inasistencia_id',
        related_name='justificacion'
    )
    pdf_file = models.FileField(db_column='archivo_pdf', upload_to='justificaciones/')
    estado = models.CharField(db_column='estado', max_length=20, choices=ESTADOS, default='Pendiente')
    observacion = models.TextField(db_column='descripcion', blank=True, null=True)
    fecha_creacion = models.DateTimeField(db_column='created_at', auto_now_add=True)

    class Meta:
        managed = True
        db_table = 'justificacion'

    def __str__(self):
        return f"Justificación #{self.id_justificacion} – Inasistencia #{self.inasistencia_id}"


