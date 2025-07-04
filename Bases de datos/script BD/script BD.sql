drop database if exists `mydb`;
CREATE DATABASE IF NOT EXISTS `mydb` ;
USE `mydb` ;

CREATE TABLE  Usuario(
  `id_usuario` INT AUTO_INCREMENT NOT NULL COMMENT 'Clave primaria. Identificador del usuario.',
  `p_nombre` VARCHAR(50) NOT NULL COMMENT 'Primer nombre.\n',
  `s_nombre` VARCHAR(50) COMMENT 'Segundo nombre.\n',
  `p_apellido` VARCHAR(45) NOT NULL COMMENT 'Primer apellido.\n',
  `s_apellido` VARCHAR(45) COMMENT 'Segundo apellido.\n',
  `tipo_documento` ENUM('CC', 'TI', 'CE', 'OTRO') NOT NULL COMMENT 'Tipo de documento.\n',
  `num_documento` INT NOT NULL 'Número de documento.\n',
  `correo` VARCHAR(100) NOT NULL 'Correo institucional.\n',
  `contraseña` VARCHAR(100) NOT NULL 'Contraseña cifrada.',
  PRIMARY KEY (`id_usuario`));

CREATE TABLE coordinacion(
  `id_coordinacion` INT  AUTO_INCREMENT NOT NULL COMMENT 'Clave primaria. Identificador de la coordinación.\n',
  `nombre_coordinacion` VARCHAR(45) NOT NULL COMMENT 'Nombre de la coordinación (ej. tecnologia e innovacion).\n',
  `correo_coordinacion` VARCHAR(30) NOT NULL COMMENT 'Correo electrónico institucional de la coordinación.',
  PRIMARY KEY (`id_coordinacion`));

CREATE TABLE Coordinador(
  `Usuario_id_usuario` INT NOT NULL COMMENT 'Llave primaria y foránea. Usuario que tiene el rol de coordinador.\n',
  `coordinacion_id_coordinacion` INT NOT NULL COMMENT 'Llave foránea. Relaciona con la coordinación que lidera.',
  PRIMARY KEY (`Usuario_id_usuario`),
    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (`coordinacion_id_coordinacion`) REFERENCES coordinacion(`id_coordinacion`) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE jornada(
  `id_jornada` INT NOT NULL COMMENT 'Clave primaria. Identificador de la jornada.',
  `nombre_jornada` ENUM('Mañana', 'Tarde', 'Noche', 'Madrugada') NOT NULL COMMENT 'Nombre de la jornada (mañana, tarde, noche).',
  PRIMARY KEY (`id_jornada`));

CREATE TABLE modalidad(
  `id_modalidad` INT NOT NULL COMMENT 'Clave primaria. Identificador de la modalidad.\n',
  `nombre_modalidad` ENUM('Presencial', 'sincronica') NOT NULL COMMENT 'Nombre de la modalidad (presencial, sincronica).',
  PRIMARY KEY (`id_modalidad`));

CREATE TABLE programas(
  `id_programas` INT NOT NULL COMMENT 'Clave primaria. Identificador del programa académico.',
  `nombre_programa` VARCHAR(50) NOT NULL COMMENT 'Nombre del programa.',
  `nivel_formacion` VARCHAR(30) NOT NULL COMMENT 'Nivel de formación (tecnólogo, técnico, etc.).\n',
  `duracion` VARCHAR(50) NOT NULL COMMENT 'Duración estimada del programa.\n',
  `jornada_id` INT NOT NULL COMMENT 'Jornada asignada.\n',
  `modalidad_id` INT NOT NULL COMMENT 'Modalidad del programa.\n',
  `coordinacion_id` INT NOT NULL COMMENT 'Coordinación responsable del programa.',
  PRIMARY KEY (`id_programas`),
    FOREIGN KEY (`jornada_id`) REFERENCES jornada(`id_jornada`),
    FOREIGN KEY (`modalidad_id`) REFERENCES modalidad(`id_modalidad`),
    FOREIGN KEY (`coordinacion_id`) REFERENCES coordinacion(`id_coordinacion`));


CREATE TABLE instructor(
  `Usuario_id_usuario` INT NOT NULL COMMENT 'Llave primaria y foránea. Usuario que actúa como instructor.\n',
  `email` VARCHAR(100) NOT NULL  COMMENT 'Correo electrónico institucional.\n',
  `telefono` VARCHAR(20) NOT NULL COMMENT 'Teléfono de contacto.\n',
  `coordinacion` VARCHAR(100) NOT NULL COMMENT 'Coordinación a la que pertenece.\n',
  `fichas_asignadas` TEXT NOT NULL COMMENT 'Fecha en que fue asignado al ambiente o grupo.\n',
  `estado` ENUM('Activo', 'Inactivo') NOT NULL COMMENT 'Estado laboral (activo, inactivo).\n',
  `jornada_id_jornada` INT NOT NULL COMMENT 'Jornada asignada al instructor.',
  `programas_id_programas` INT NOT NULL COMMENT 'Programa que orienta.',
  PRIMARY KEY (`Usuario_id_usuario`),
    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`),
    FOREIGN KEY (`jornada_id_jornada`) REFERENCES jornada(`id_jornada`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (`programas_id_programas`) REFERENCES programas(`id_programas`) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE guarda_seguridad(
  `Usuario_id_usuario` INT NOT NULL COMMENT 'Clave primaria y foránea. Usuario que cumple funciones de guarda de seguridad.\n',
  `turno` ENUM('Mañana', 'Tarde', 'Noche') NOT NULL COMMENT 'Turno del guarda(ej. mañana, tarde, etc...)',
  `fecha_ingreso` DATE NOT NULL COMMENT 'Fecha de ingreso laboral del guarda.\n',
  `estado` ENUM('Activo', 'Inactivo') NOT NULL,
  PRIMARY KEY (`Usuario_id_usuario`),
    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`));

CREATE TABLE aprendiz(
  `Usuario_id_usuario` INT NOT NULL COMMENT 'Llave primaria y foránea. Identificador del aprendiz.',
  `Num_ficha` MEDIUMINT NOT NULL COMMENT 'Número de ficha de formación.\n',
  `programas_id_programas` INT NOT NULL COMMENT 'Llave foránea. Programa de formación del aprendiz.\n',
  `jornada_id_jornada` INT NOT NULL COMMENT 'Jornada en la que está inscrito el aprendiz.',
  PRIMARY KEY (`Usuario_id_usuario`),
    FOREIGN KEY (`programas_id_programas`) REFERENCES programas(`id_programas`) ON DELETE NO ACTION ON UPDATE NO ACTION,

    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,

    FOREIGN KEY (`jornada_id_jornada`) REFERENCES jornada(`id_jornada`) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE rol(
  `id_rol` INT NOT NULL COMMENT 'Clave primaria. Identificador del rol.\n', 
  `nombre_rol` VARCHAR(45) NOT NULL COMMENT 'Nombre del rol (aprendiz, instructor, etc.).',
  PRIMARY KEY (`id_rol`));

CREATE TABLE user_rol(
  `id_user_rol` INT NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del registro.\n',
  `id_usuario` INT NOT NULL COMMENT 'Usuario asociado.\n',
  `id_rol` INT NOT NULL COMMENT 'Rol asignado al usuario.',
  PRIMARY KEY (`id_user_rol`),
    FOREIGN KEY (`id_usuario`) REFERENCES Usuario(`id_usuario`),
    FOREIGN KEY (`id_rol`) REFERENCES  rol(`id_rol`));
    
    

CREATE TABLE ambiente(
  `id_ambiente` INT NOT NULL COMMENT 'Clave primaria. Identificador único del ambiente.\n',
  `num_ambiente` SMALLINT NOT NULL COMMENT 'Número o código del ambiente físico.\n',
  `capacidad` SMALLINT NOT NULL COMMENT 'Cantidad de personas que pueden estar en un ambiente',
  `tipo_ambiente` VARCHAR(45) NOT NULL COMMENT 'Tipo del ambiente (auditorio, sala, aula, etc.).\n',
  `estado` VARCHAR(30) NOT NULL COMMENT 'Estado actual del ambiente (disponible, ocupado, mantenimiento, etc.).\n',
  `coordinacion_id` INT NOT NULL COMMENT 'Llave foránea. Relaciona el ambiente con una coordinación responsable.',
  PRIMARY KEY (`id_ambiente`),
    FOREIGN KEY (`coordinacion_id`) REFERENCES coordinacion(`id_coordinacion`));

CREATE TABLE tipo_incidente(
  `id_tipo_inc` INT NOT NULL COMMENT 'Clave primaria. Identificador del tipo de incidente.\n',
  `tipo_incidente` VARCHAR(45) NOT NULL COMMENT 'Nombre del tipo.\n',
  `observacion_inc` TEXT NOT NULL COMMENT 'Observación adicional.',
  PRIMARY KEY (`id_tipo_inc`));

CREATE TABLE registro_incidente(
  `id_incidente` INT NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del incidente.',
  `descripcion` TEXT NULL DEFAULT NULL COMMENT 'Descripción general del incidente.\n',
  `fecha_incidente` DATE NOT NULL COMMENT 'Fecha en que ocurrió.\n',
  `hora_incidente` TIME NOT NULL COMMENT 'Hora en que ocurrio.',
  `ambiente_id` INT NOT NULL COMMENT 'Ambiente donde sucedió.\n',
  `tipo_inc_id` INT NOT NULL COMMENT 'Tipo de incidente.\n',
  `id_usuario_registra` INT NOT NULL COMMENT 'Usuario que reportó.',
  PRIMARY KEY (`id_incidente`),
    FOREIGN KEY (`ambiente_id`) REFERENCES ambiente(`id_ambiente`),

    FOREIGN KEY (`tipo_inc_id`) REFERENCES tipo_incidente(`id_tipo_inc`),

    FOREIGN KEY (`id_usuario_registra`) REFERENCES Usuario(`id_usuario`)
    );

CREATE TABLE tipo_recurso (
  `id_tipo_recurso` INT NOT NULL COMMENT 'Clave primaria. Identificador del tipo de recurso.\n',
  `recurso_tipo` VARCHAR(45) NOT NULL COMMENT 'Nombre del tipo (ej. PC, proyector, aire, etc.).\n',
  `descripcion_tipo` VARCHAR(60) NULL COMMENT 'Descripción adicional.',
  PRIMARY KEY (`id_tipo_recurso`));

CREATE TABLE recursos (
  `id_recurso` INT NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del recurso.\n',
  `serial_recurso` VARCHAR(100) NOT NULL COMMENT 'Serial físico o interno del recurso.\n',
  `num_recurso` TINYINT NOT NULL COMMENT 'Nombre del recurso en el ambiente.\n',
  `nombre_recurso` VARCHAR(60) NOT NULL COMMENT 'Nombre del recurso.\n',
  `tipo_recurso` INT NOT NULL COMMENT 'Llave foránea. Tipo de recurso.\n',
  `estado` enum('Disponible', 'En mantenimiento' , 'Dañado') COMMENT 'Estado del recurso (operativo, dañado, en mantenimiento).\n',
  `observacion` TEXT NULL COMMENT 'Observacion hacia algun recurso.',
  `ambiente_id` INT NOT NULL COMMENT 'Llave foránea. Ambiente donde se encuentra el recurso.',
    PRIMARY KEY (`id_recurso`),
    FOREIGN KEY (`tipo_recurso`) REFERENCES tipo_recurso(`id_tipo_recurso`),
    FOREIGN KEY (`ambiente_id`) REFERENCES ambiente(`id_ambiente`)
    );
    
  CREATE TABLE traslado_recurso (
    id_traslado INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Clave primaria. Identificador del traslado.\n',
    recurso_id INT NOT NULL COMMENT 'Recurso trasladado.\n',
    ambiente_origen_id INT NOT NULL COMMENT 'Ambiente de origen.\n',
    ambiente_destino_id INT NOT NULL COMMENT 'Ambiente de destino.\n',
    fecha_traslado DATETIME NOT NULL COMMENT 'Fecha del traslado.\n',
    observacion TEXT COMMENT 'Observaciones del traslado.',
    FOREIGN KEY (recurso_id) REFERENCES recursos(id_recurso),
    FOREIGN KEY (ambiente_origen_id) REFERENCES ambiente(id_ambiente),
    FOREIGN KEY (ambiente_destino_id) REFERENCES ambiente(id_ambiente)
    );


CREATE TABLE registro_minuta(
  `id_minuta` INT NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del registro de minuta.\n',
  `fecha_hora_recibo` DATETIME NOT NULL COMMENT 'Fecha y hora de recibo del ambiente.\n',
  `fecha_hora_entrega` DATETIME NOT NULL COMMENT 'Fecha y hora de entrega.\n',
  `novedad` TEXT NULL COMMENT 'Novedad o eventualidad ocurrida.\n',
  `responsable` VARCHAR(250) NOT NULL COMMENT 'Persona responsable.\n',
  `descripcion_min` TEXT NULL DEFAULT NULL COMMENT 'Observaciones generales.\n',
  `estado` TEXT not NULL COMMENT 'Estado general del ambiente al momento.\n',
  `ambiente_id` INT NOT NULL COMMENT 'Ambiente relacionado.\n',
  `Usuario_id_usuario` INT NOT NULL COMMENT 'Usuario que registró.\n',
  `guarda_seguridad_Usuario_id_usuario` INT NOT NULL COMMENT 'Guarda que recibió o entregó.',
  PRIMARY KEY (`id_minuta`),
    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`),
    FOREIGN KEY (`ambiente_id`) REFERENCES ambiente(`id_ambiente`),
    FOREIGN KEY (`guarda_seguridad_Usuario_id_usuario`) REFERENCES guarda_seguridad(`Usuario_id_usuario`)ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE registro_asistencia (
  `id_asistencia` INT NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del registro.\n',
  `fecha_asistencia` DATE NOT NULL COMMENT 'Fecha del registro de asistencia.\n',
  `estado_asistencia` ENUM('S', 'R', 'N') NOT NULL COMMENT 'Estado: A (asistió), R (retraso), N (no aplica).\n',
  `jornada_id` INT NOT NULL COMMENT 'Jornada del aprendiz.\n',
  `aprendiz_Usuario_id_usuario` INT NOT NULL COMMENT 'Llave foránea al aprendiz.',
  PRIMARY KEY (`id_asistencia`),
    FOREIGN KEY (`jornada_id`) REFERENCES jornada(`id_jornada`),
    FOREIGN KEY (`aprendiz_Usuario_id_usuario`) REFERENCES aprendiz(`Usuario_id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE alertas_inasistencia (
  `id_alerta` INT NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador de la alerta.\n',
  `aprendiz_id` INT NOT NULL COMMENT 'Llave foránea. Relaciona la alerta con el aprendiz.\n',
  `cantidad_fallas` INT NOT NULL COMMENT 'Número acumulado de inasistencias.\n',
  `fecha_alerta` DATETIME NOT NULL COMMENT 'Fecha de generación de la alerta.\n',
  `mensaje` TEXT NOT NULL COMMENT 'Descripción o detalle de la alerta.\n',
  `coordinacion_id` INT NOT NULL COMMENT 'Coordinación que recibe o emite la alerta.',
  PRIMARY KEY (`id_alerta`),
    FOREIGN KEY (`coordinacion_id`) REFERENCES coordinacion(`id_coordinacion`));

CREATE TABLE historico_incidentes (
  id_historico INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Clave primaria. Identificador del historial del incidente.\n',
  incidente_id INT NOT NULL COMMENT 'Llave foránea. Incidente asociado.\n',
  ambiente_id INT NOT NULL COMMENT 'Llave foránea. Ambiente en donde ocurrió.\n',
  tipo_incidente_id INT NOT NULL COMMENT 'Llave foránea. Tipo de incidente registrado.\n',
  descripcion TEXT COMMENT 'Descripción de los hechos o seguimiento.\n',
  usuario_registra_id INT NOT NULL COMMENT 'Usuario que hizo el seguimiento o modificación.\n',
  fecha_registro DATETIME NOT NULL COMMENT 'Fecha del registro en el historial.',
  FOREIGN KEY (incidente_id) REFERENCES registro_incidente(id_incidente),
  FOREIGN KEY (usuario_registra_id) REFERENCES Usuario(id_usuario),
  FOREIGN KEY (ambiente_id) REFERENCES ambiente(id_ambiente),
  FOREIGN KEY (tipo_incidente_id) REFERENCES tipo_incidente(id_tipo_inc)
);
