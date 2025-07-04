drop database if exists `mydb`;
CREATE DATABASE IF NOT EXISTS `mydb` ;
USE `mydb` ;

CREATE TABLE  Usuario(
  `id_usuario` INT AUTO_INCREMENT NOT NULL COMMENT 'Clave primaria. Identificador del usuario.',
  `p_nombre` VARCHAR(50) NOT NULL COMMENT 'Primer nombre.',
  `s_nombre` VARCHAR(50) COMMENT 'Segundo nombre.',
  `p_apellido` VARCHAR(45) NOT NULL COMMENT 'Primer apellido.',
  `s_apellido` VARCHAR(45) COMMENT 'Segundo apellido.',
  `tipo_documento` ENUM('CC', 'TI', 'CE', 'OTRO') NOT NULL COMMENT 'Tipo de documento.',
  `num_documento` INT NOT NULL COMMENT'Número de documento.',
  `correo` VARCHAR(100) NOT NULL COMMENT'Correo institucional.',
  `contraseña` VARCHAR(100) NOT NULL COMMENT'Contraseña cifrada.',
  PRIMARY KEY (`id_usuario`));

CREATE TABLE coordinacion(
  `id_coordinacion` INT  AUTO_INCREMENT NOT NULL COMMENT 'Clave primaria. Identificador de la coordinación.',
  `nombre_coordinacion` VARCHAR(45) NOT NULL COMMENT 'Nombre de la coordinación (ej. tecnologia e innovacion).',
  `correo_coordinacion` VARCHAR(30) NOT NULL COMMENT 'Correo electrónico institucional de la coordinación.',
  PRIMARY KEY (`id_coordinacion`));

CREATE TABLE Coordinador(
  `Usuario_id_usuario` INT NOT NULL COMMENT 'Llave primaria y foránea. Usuario que tiene el rol de coordinador.',
  `coordinacion_id_coordinacion` INT NOT NULL COMMENT 'Llave foránea. Relaciona con la coordinación que lidera.',
  PRIMARY KEY (`Usuario_id_usuario`),
    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (`coordinacion_id_coordinacion`) REFERENCES coordinacion(`id_coordinacion`) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE jornada(
  `id_jornada` INT NOT NULL COMMENT 'Clave primaria. Identificador de la jornada.',
  `nombre_jornada` ENUM('Mañana', 'Tarde', 'Noche', 'Madrugada') NOT NULL COMMENT 'Nombre de la jornada (mañana, tarde, noche).',
  PRIMARY KEY (`id_jornada`));

CREATE TABLE modalidad(
  `id_modalidad` INT NOT NULL COMMENT 'Clave primaria. Identificador de la modalidad.',
  `nombre_modalidad` ENUM('Presencial', 'sincronica') NOT NULL COMMENT 'Nombre de la modalidad (presencial, sincronica).',
  PRIMARY KEY (`id_modalidad`));

CREATE TABLE programas(
  `id_programas` INT NOT NULL COMMENT 'Clave primaria. Identificador del programa académico.',
  `nombre_programa` VARCHAR(50) NOT NULL COMMENT 'Nombre del programa.',
  `nivel_formacion` VARCHAR(30) NOT NULL COMMENT 'Nivel de formación (tecnólogo, técnico, etc.).',
  `duracion` VARCHAR(50) NOT NULL COMMENT 'Duración estimada del programa.',
  `jornada_id` INT NOT NULL COMMENT 'Jornada asignada.',
  `modalidad_id` INT NOT NULL COMMENT 'Modalidad del programa.',
  `coordinacion_id` INT NOT NULL COMMENT 'Coordinación responsable del programa.',
  PRIMARY KEY (`id_programas`),
    FOREIGN KEY (`jornada_id`) REFERENCES jornada(`id_jornada`),
    FOREIGN KEY (`modalidad_id`) REFERENCES modalidad(`id_modalidad`),
    FOREIGN KEY (`coordinacion_id`) REFERENCES coordinacion(`id_coordinacion`));


CREATE TABLE instructor(
  `Usuario_id_usuario` INT NOT NULL COMMENT 'Llave primaria y foránea. Usuario que actúa como instructor.',
  `email` VARCHAR(100) NOT NULL  COMMENT 'Correo electrónico institucional.',
  `telefono` VARCHAR(20) NOT NULL COMMENT 'Teléfono de contacto.',
  `coordinacion` VARCHAR(100) NOT NULL COMMENT 'Coordinación a la que pertenece.',
  `fichas_asignadas` TEXT NOT NULL COMMENT 'Fecha en que fue asignado al ambiente o grupo.',
  `estado` ENUM('Activo', 'Inactivo') NOT NULL COMMENT 'Estado laboral (activo, inactivo).',
  `jornada_id_jornada` INT NOT NULL COMMENT 'Jornada asignada al instructor.',
  `programas_id_programas` INT NOT NULL COMMENT 'Programa que orienta.',
  PRIMARY KEY (`Usuario_id_usuario`),
    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`),
    FOREIGN KEY (`jornada_id_jornada`) REFERENCES jornada(`id_jornada`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (`programas_id_programas`) REFERENCES programas(`id_programas`) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE guarda_seguridad(
  `Usuario_id_usuario` INT NOT NULL COMMENT 'Clave primaria y foránea. Usuario que cumple funciones de guarda de seguridad.',
  `turno` ENUM('Mañana', 'Tarde', 'Noche') NOT NULL COMMENT 'Turno del guarda(ej. mañana, tarde, etc...)',
  `fecha_ingreso` DATE NOT NULL COMMENT 'Fecha de ingreso laboral del guarda.',
  `estado` ENUM('Activo', 'Inactivo') NOT NULL,
  PRIMARY KEY (`Usuario_id_usuario`),
    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`));

CREATE TABLE aprendiz(
  `Usuario_id_usuario` INT NOT NULL COMMENT 'Llave primaria y foránea. Identificador del aprendiz.',
  `Num_ficha` MEDIUMINT NOT NULL COMMENT 'Número de ficha de formación.',
  `programas_id_programas` INT NOT NULL COMMENT 'Llave foránea. Programa de formación del aprendiz.',
  `jornada_id_jornada` INT NOT NULL COMMENT 'Jornada en la que está inscrito el aprendiz.',
  PRIMARY KEY (`Usuario_id_usuario`),
    FOREIGN KEY (`programas_id_programas`) REFERENCES programas(`id_programas`) ON DELETE NO ACTION ON UPDATE NO ACTION,

    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,

    FOREIGN KEY (`jornada_id_jornada`) REFERENCES jornada(`id_jornada`) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE rol(
  `id_rol` INT NOT NULL COMMENT 'Clave primaria. Identificador del rol.', 
  `nombre_rol` VARCHAR(45) NOT NULL COMMENT 'Nombre del rol (aprendiz, instructor, etc.).',
  PRIMARY KEY (`id_rol`));

CREATE TABLE user_rol(
  `id_user_rol` INT NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del registro.',
  `id_usuario` INT NOT NULL COMMENT 'Usuario asociado.',
  `id_rol` INT NOT NULL COMMENT 'Rol asignado al usuario.',
  PRIMARY KEY (`id_user_rol`),
    FOREIGN KEY (`id_usuario`) REFERENCES Usuario(`id_usuario`),
    FOREIGN KEY (`id_rol`) REFERENCES  rol(`id_rol`));
    
    

CREATE TABLE ambiente(
  `id_ambiente` INT NOT NULL COMMENT 'Clave primaria. Identificador único del ambiente.',
  `num_ambiente` SMALLINT NOT NULL COMMENT 'Número o código del ambiente físico.',
  `capacidad` SMALLINT NOT NULL COMMENT 'Cantidad de personas que pueden estar en un ambiente',
  `tipo_ambiente` VARCHAR(45) NOT NULL COMMENT 'Tipo del ambiente (auditorio, sala, aula, etc.).',
  `estado` VARCHAR(30) NOT NULL COMMENT 'Estado actual del ambiente (disponible, ocupado, mantenimiento, etc.).',
  `coordinacion_id` INT NOT NULL COMMENT 'Llave foránea. Relaciona el ambiente con una coordinación responsable.',
  PRIMARY KEY (`id_ambiente`),
    FOREIGN KEY (`coordinacion_id`) REFERENCES coordinacion(`id_coordinacion`));

CREATE TABLE tipo_incidente(
  `id_tipo_inc` INT NOT NULL COMMENT 'Clave primaria. Identificador del tipo de incidente.',
  `tipo_incidente` VARCHAR(45) NOT NULL COMMENT 'Nombre del tipo.',
  `observacion_inc` TEXT NOT NULL COMMENT 'Observación adicional.',
  PRIMARY KEY (`id_tipo_inc`));

CREATE TABLE registro_incidente(
  `id_incidente` INT NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del incidente.',
  `descripcion` TEXT NULL DEFAULT NULL COMMENT 'Descripción general del incidente.',
  `fecha_incidente` DATE NOT NULL COMMENT 'Fecha en que ocurrió.',
  `hora_incidente` TIME NOT NULL COMMENT 'Hora en que ocurrio.',
  `ambiente_id` INT NOT NULL COMMENT 'Ambiente donde sucedió.',
  `tipo_inc_id` INT NOT NULL COMMENT 'Tipo de incidente.',
  `id_usuario_registra` INT NOT NULL COMMENT 'Usuario que reportó.',
  PRIMARY KEY (`id_incidente`),
    FOREIGN KEY (`ambiente_id`) REFERENCES ambiente(`id_ambiente`),

    FOREIGN KEY (`tipo_inc_id`) REFERENCES tipo_incidente(`id_tipo_inc`),

    FOREIGN KEY (`id_usuario_registra`) REFERENCES Usuario(`id_usuario`)
    );

CREATE TABLE tipo_recurso (
  `id_tipo_recurso` INT NOT NULL COMMENT 'Clave primaria. Identificador del tipo de recurso.',
  `recurso_tipo` VARCHAR(45) NOT NULL COMMENT 'Nombre del tipo (ej. PC, proyector, aire, etc.).',
  `descripcion_tipo` VARCHAR(60) NULL COMMENT 'Descripción adicional.',
  PRIMARY KEY (`id_tipo_recurso`));

CREATE TABLE recursos (
  `id_recurso` INT NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del recurso.',
  `serial_recurso` VARCHAR(100) NOT NULL COMMENT 'Serial físico o interno del recurso.',
  `num_recurso` TINYINT NOT NULL COMMENT 'Nombre del recurso en el ambiente.',
  `nombre_recurso` VARCHAR(60) NOT NULL COMMENT 'Nombre del recurso.',
  `tipo_recurso` INT NOT NULL COMMENT 'Llave foránea. Tipo de recurso.',
  `estado` enum('Disponible', 'En mantenimiento' , 'Dañado') COMMENT 'Estado del recurso (operativo, dañado, en mantenimiento).',
  `observacion` TEXT NULL COMMENT 'Observacion hacia algun recurso.',
  `ambiente_id` INT NOT NULL COMMENT 'Llave foránea. Ambiente donde se encuentra el recurso.',
    PRIMARY KEY (`id_recurso`),
    FOREIGN KEY (`tipo_recurso`) REFERENCES tipo_recurso(`id_tipo_recurso`),
    FOREIGN KEY (`ambiente_id`) REFERENCES ambiente(`id_ambiente`)
    );
    
  CREATE TABLE traslado_recurso (
    id_traslado INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Clave primaria. Identificador del traslado.',
    recurso_id INT NOT NULL COMMENT 'Recurso trasladado.',
    ambiente_origen_id INT NOT NULL COMMENT 'Ambiente de origen.',
    ambiente_destino_id INT NOT NULL COMMENT 'Ambiente de destino.',
    fecha_traslado DATETIME NOT NULL COMMENT 'Fecha del traslado.',
    observacion TEXT COMMENT 'Observaciones del traslado.',
    FOREIGN KEY (recurso_id) REFERENCES recursos(id_recurso),
    FOREIGN KEY (ambiente_origen_id) REFERENCES ambiente(id_ambiente),
    FOREIGN KEY (ambiente_destino_id) REFERENCES ambiente(id_ambiente)
    );


CREATE TABLE registro_minuta(
  `id_minuta` INT NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del registro de minuta.',
  `fecha_hora_recibo` DATETIME NOT NULL COMMENT 'Fecha y hora de recibo del ambiente.',
  `fecha_hora_entrega` DATETIME NOT NULL COMMENT 'Fecha y hora de entrega.',
  `novedad` TEXT NULL COMMENT 'Novedad o eventualidad ocurrida.',
  `responsable` VARCHAR(250) NOT NULL COMMENT 'Persona responsable.',
  `descripcion_min` TEXT NULL DEFAULT NULL COMMENT 'Observaciones generales.',
  `estado` TEXT not NULL COMMENT 'Estado general del ambiente al momento.',
  `ambiente_id` INT NOT NULL COMMENT 'Ambiente relacionado.',
  `Usuario_id_usuario` INT NOT NULL COMMENT 'Usuario que registró.',
  `guarda_seguridad_Usuario_id_usuario` INT NOT NULL COMMENT 'Guarda que recibió o entregó.',
  PRIMARY KEY (`id_minuta`),
    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`),
    FOREIGN KEY (`ambiente_id`) REFERENCES ambiente(`id_ambiente`),
    FOREIGN KEY (`guarda_seguridad_Usuario_id_usuario`) REFERENCES guarda_seguridad(`Usuario_id_usuario`)ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE registro_asistencia (
  `id_asistencia` INT NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del registro.',
  `fecha_asistencia` DATE NOT NULL COMMENT 'Fecha del registro de asistencia.',
  `estado_asistencia` ENUM('S', 'R', 'N') NOT NULL COMMENT 'Estado: A (asistió), R (retraso), N (no aplica).',
  `jornada_id` INT NOT NULL COMMENT 'Jornada del aprendiz.',
  `aprendiz_Usuario_id_usuario` INT NOT NULL COMMENT 'Llave foránea al aprendiz.',
  PRIMARY KEY (`id_asistencia`),
    FOREIGN KEY (`jornada_id`) REFERENCES jornada(`id_jornada`),
    FOREIGN KEY (`aprendiz_Usuario_id_usuario`) REFERENCES aprendiz(`Usuario_id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE alertas_inasistencia (
  `id_alerta` INT NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador de la alerta.',
  `aprendiz_id` INT NOT NULL COMMENT 'Llave foránea. Relaciona la alerta con el aprendiz.',
  `cantidad_fallas` INT NOT NULL COMMENT 'Número acumulado de inasistencias.',
  `fecha_alerta` DATETIME NOT NULL COMMENT 'Fecha de generación de la alerta.',
  `mensaje` TEXT NOT NULL COMMENT 'Descripción o detalle de la alerta.',
  `coordinacion_id` INT NOT NULL COMMENT 'Coordinación que recibe o emite la alerta.',
  PRIMARY KEY (`id_alerta`),
    FOREIGN KEY (`coordinacion_id`) REFERENCES coordinacion(`id_coordinacion`));

CREATE TABLE historico_incidentes (
  id_historico INT AUTO_INCREMENT PRIMARY KEY COMMENT 'Clave primaria. Identificador del historial del incidente.',
  incidente_id INT NOT NULL COMMENT 'Llave foránea. Incidente asociado.',
  ambiente_id INT NOT NULL COMMENT 'Llave foránea. Ambiente en donde ocurrió.',
  tipo_incidente_id INT NOT NULL COMMENT 'Llave foránea. Tipo de incidente registrado.',
  descripcion TEXT COMMENT 'Descripción de los hechos o seguimiento.',
  usuario_registra_id INT NOT NULL COMMENT 'Usuario que hizo el seguimiento o modificación.',
  fecha_registro DATETIME NOT NULL COMMENT 'Fecha del registro en el historial.',
  FOREIGN KEY (incidente_id) REFERENCES registro_incidente(id_incidente),
  FOREIGN KEY (usuario_registra_id) REFERENCES Usuario(id_usuario),
  FOREIGN KEY (ambiente_id) REFERENCES ambiente(id_ambiente),
  FOREIGN KEY (tipo_incidente_id) REFERENCES tipo_incidente(id_tipo_inc)
);
