drop database if exists `mydb`;
create database `mydb`;
CREATE SCHEMA IF NOT EXISTS `mydb`;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`coordinacion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`coordinacion` (
  `id_coordinacion` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador de la coordinación.\n',
  `nombre_coordinacion` VARCHAR(45) NOT NULL COMMENT 'Nombre de la coordinación (ej. tecnologia e innovacion).\n',
  `correo_coordinacion` VARCHAR(30) NOT NULL COMMENT 'Correo electrónico institucional de la coordinación.\n\n',
  PRIMARY KEY (`id_coordinacion`));



-- -----------------------------------------------------
-- Table `mydb`.`alertas_inasistencia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`alertas_inasistencia` (
  `id_alerta` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador de la alerta.\n',
  `aprendiz_id` INT(11) NOT NULL COMMENT 'Llave foránea. Relaciona la alerta con el aprendiz.\n',
  `cantidad_fallas` INT(11) NOT NULL COMMENT 'Número acumulado de inasistencias.\n',
  `fecha_alerta` DATETIME NOT NULL COMMENT 'Fecha de generación de la alerta.\n',
  `mensaje` TEXT NOT NULL COMMENT 'Descripción o detalle de la alerta.\n',
  `coordinacion_id` INT(11) NOT NULL COMMENT 'Coordinación que recibe o emite la alerta.\n\n',
  PRIMARY KEY (`id_alerta`),
 
    FOREIGN KEY (`coordinacion_id`)
    REFERENCES `mydb`.`coordinacion` (`id_coordinacion`))
;


-- -----------------------------------------------------
-- Table `mydb`.`ambiente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`ambiente` (
  `id_ambiente` INT(11) NOT NULL COMMENT 'Clave primaria. Identificador único del ambiente.\n',
  `num_ambiente` SMALLINT(6) NOT NULL COMMENT 'Número o código del ambiente físico.\n',
  `capacidad` SMALLINT(6) NOT NULL COMMENT 'Cantidad de personas que pueden estar en un ambiente',
  `tipo_ambiente` VARCHAR(45) NOT NULL COMMENT 'Tipo del ambiente (auditorio, sala, aula, etc.).\n',
  `estado` VARCHAR(30) NOT NULL COMMENT 'Estado actual del ambiente (disponible, ocupado, mantenimiento, etc.).\n',
  PRIMARY KEY (`id_ambiente`))
;


-- -----------------------------------------------------
-- Table `mydb`.`jornada`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`jornada` (
  `id_jornada` INT(11) NOT NULL COMMENT 'Clave primaria. Identificador de la jornada.\n',
  `nombre_jornada` ENUM('Mañana', 'Tarde', 'Noche', 'Madrugada') NOT NULL COMMENT 'Nombre de la jornada (mañana, tarde, noche).\n\n',
  PRIMARY KEY (`id_jornada`))
;


-- -----------------------------------------------------
-- Table `mydb`.`modalidad`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`modalidad` (
  `id_modalidad` INT(11) NOT NULL COMMENT 'Clave primaria. Identificador de la modalidad.\n',
  `nombre_modalidad` ENUM('Presencial', 'sincronica') NOT NULL COMMENT 'Nombre de la modalidad (presencial, sincronica).\n\n',
  PRIMARY KEY (`id_modalidad`))
;


-- -----------------------------------------------------
-- Table `mydb`.`programas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`programas` (
  `id_programas` INT(11) NOT NULL COMMENT 'Clave primaria. Identificador del programa académico.\n',
  `nombre_programa` VARCHAR(50) NOT NULL COMMENT 'Nombre del programa.\n',
  `nivel_formacion` VARCHAR(30) NOT NULL COMMENT 'Nivel de formación (tecnólogo, técnico, etc.).\n',
  `duracion` VARCHAR(50) NOT NULL COMMENT 'Duración estimada del programa.\n',
  `jornada_id` INT(11) NOT NULL COMMENT 'Jornada asignada.\n',
  `modalidad_id` INT(11) NOT NULL COMMENT 'Modalidad del programa.\n',
  `coordinacion_id` INT(11) NOT NULL COMMENT 'Coordinación responsable del programa.\n\n',
  PRIMARY KEY (`id_programas`),
 
    FOREIGN KEY (`jornada_id`)
    REFERENCES `mydb`.`jornada` (`id_jornada`),
 
    FOREIGN KEY (`modalidad_id`)
    REFERENCES `mydb`.`modalidad` (`id_modalidad`),

    FOREIGN KEY (`coordinacion_id`)
    REFERENCES `mydb`.`coordinacion` (`id_coordinacion`))
;


-- -----------------------------------------------------
-- Table `mydb`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`usuario` (
  `id_usuario` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del usuario.\n',
  `p_nombre` VARCHAR(50) NOT NULL COMMENT 'Primer nombre.\n',
  `s_nombre` VARCHAR(50) NULL DEFAULT NULL COMMENT 'Segundo nombre.\n',
  `p_apellido` VARCHAR(45) NOT NULL COMMENT 'Primer apellido.\n',
  `s_apellido` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Segundo apellido.\n',
  `tipo_documento` ENUM('CC', 'TI', 'CE', 'OTRO') NOT NULL COMMENT 'Tipo de documento.\n',
  `num_documento` INT(11) NOT NULL COMMENT 'Número de documento.\n',
  `correo` VARCHAR(100) NOT NULL COMMENT 'Correo institucional.\n',
  `contraseña` VARCHAR(100) NOT NULL COMMENT 'Contraseña cifrada.\n\n',
  PRIMARY KEY (`id_usuario`))
;


-- -----------------------------------------------------
-- Table `mydb`.`instructor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`instructor` (
  `Usuario_id_usuario` INT(11) NOT NULL COMMENT 'Llave primaria y foránea. Usuario que actúa como instructor.\n',
  `email` VARCHAR(100) NOT NULL COMMENT 'Correo electrónico institucional.\n',
  `telefono` VARCHAR(20) NOT NULL COMMENT 'Teléfono de contacto.\n',
  `coordinacion` VARCHAR(100) NOT NULL COMMENT 'Coordinación a la que pertenece.\n',
  `estado` ENUM('Activo', 'Inactivo') NOT NULL COMMENT 'Estado laboral (activo, inactivo).\n',
  PRIMARY KEY (`Usuario_id_usuario`),

    FOREIGN KEY (`Usuario_id_usuario`)
    REFERENCES `mydb`.`usuario` (`id_usuario`))
;


-- -----------------------------------------------------
-- Table `mydb`.`ficha`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`ficha` (
  `idficha` INT NOT NULL,
  `Num_ficha` MEDIUMINT NOT NULL,
  `instructor_Usuario_id_usuario` INT(11) NOT NULL,
  PRIMARY KEY (`idficha`),
  
 
    FOREIGN KEY (`instructor_Usuario_id_usuario`)
    REFERENCES `mydb`.`instructor` (`Usuario_id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
;


-- -----------------------------------------------------
-- Table `mydb`.`aprendiz`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`aprendiz` (
  `Usuario_id_usuario` INT(11) NOT NULL COMMENT 'Llave primaria y foránea. Identificador del aprendiz (usuario base).\n',
  `programas_id_programas` INT(11) NOT NULL COMMENT 'Llave foránea. Programa de formación del aprendiz.\n',
  `ficha_idficha` INT NOT NULL,
  PRIMARY KEY (`Usuario_id_usuario`),
 
    FOREIGN KEY (`programas_id_programas`)
    REFERENCES `mydb`.`programas` (`id_programas`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
 
    FOREIGN KEY (`Usuario_id_usuario`)
    REFERENCES `mydb`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
 
  
    FOREIGN KEY (`ficha_idficha`)
    REFERENCES `mydb`.`ficha` (`idficha`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
;


-- -----------------------------------------------------
-- Table `mydb`.`coordinador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`coordinador` (
  `Usuario_id_usuario` INT(11) NOT NULL COMMENT 'Llave primaria y foránea. Usuario que tiene el rol de coordinador.\n',
  `coordinacion_id_coordinacion` INT(11) NOT NULL COMMENT 'Llave foránea. Relaciona con la coordinación que lidera.\n\n',
  PRIMARY KEY (`Usuario_id_usuario`),
  
    FOREIGN KEY (`Usuario_id_usuario`)
    REFERENCES `mydb`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  
    FOREIGN KEY (`coordinacion_id_coordinacion`)
    REFERENCES `mydb`.`coordinacion` (`id_coordinacion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
;


-- -----------------------------------------------------
-- Table `mydb`.`guarda_seguridad`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`guarda_seguridad` (
  `Usuario_id_usuario` INT(11) NOT NULL COMMENT 'Clave primaria y foránea. Usuario que cumple funciones de guarda de seguridad.\n',
  `turno` ENUM('Mañana', 'Tarde', 'Noche') NOT NULL COMMENT 'Turno del guarda(ej. mañana, tarde, etc...)',
  `fecha_ingreso` DATE NOT NULL COMMENT 'Fecha de ingreso laboral del guarda.\n',
  `estado` ENUM('Activo', 'Inactivo') NOT NULL COMMENT 'Estado laboral del guarda (activo/inactivo).\n\n',
  PRIMARY KEY (`Usuario_id_usuario`),
 
    FOREIGN KEY (`Usuario_id_usuario`)
    REFERENCES `mydb`.`usuario` (`id_usuario`))
;


-- -----------------------------------------------------
-- Table `mydb`.`tipo_incidente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tipo_incidente` (
  `id_tipo_inc` INT(11) NOT NULL COMMENT 'Clave primaria. Identificador del tipo de incidente.\n',
  `tipo_incidente` VARCHAR(45) NOT NULL COMMENT 'Nombre del tipo.\n',
  `observacion_inc` TEXT NOT NULL COMMENT 'Observación adicional.\n\n',
  PRIMARY KEY (`id_tipo_inc`))
;


-- -----------------------------------------------------
-- Table `mydb`.`registro_incidente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`registro_incidente` (
  `id_incidente` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del incidente.\n\n',
  `descripcion` TEXT NULL DEFAULT NULL COMMENT 'Descripción general del incidente.\n',
  `fecha_incidente` DATE NOT NULL COMMENT 'Fecha en que ocurrió.\n',
  `hora_incidente` TIME NOT NULL COMMENT 'Hora en que ocurrio.',
  `ambiente_id` INT(11) NOT NULL COMMENT 'Ambiente donde sucedió.\n',
  `tipo_inc_id` INT(11) NOT NULL COMMENT 'Tipo de incidente.\n',
  `usuario_id_usuario` INT(11) NOT NULL,
  PRIMARY KEY (`id_incidente`),
 
    FOREIGN KEY (`ambiente_id`)
    REFERENCES `mydb`.`ambiente` (`id_ambiente`),
 
    FOREIGN KEY (`tipo_inc_id`)
    REFERENCES `mydb`.`tipo_incidente` (`id_tipo_inc`),
  
    FOREIGN KEY (`usuario_id_usuario`)
    REFERENCES `mydb`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
;


-- -----------------------------------------------------
-- Table `mydb`.`historico_incidentes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`historico_incidentes` (
  `id_historico` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del historial del incidente.\n',
  `incidente_id` INT(11) NOT NULL COMMENT 'Llave foránea. Incidente asociado.\n',
  `ambiente_id` INT(11) NOT NULL COMMENT 'Llave foránea. Ambiente en donde ocurrió.\n',
  `tipo_incidente_id` INT(11) NOT NULL COMMENT 'Llave foránea. Tipo de incidente registrado.\n',
  `descripcion` TEXT NULL DEFAULT NULL COMMENT 'Descripción de los hechos o seguimiento.\n',
  `fecha_registro` DATETIME NOT NULL COMMENT 'Fecha del registro en el historial.\n\n',
  PRIMARY KEY (`id_historico`),
 
    FOREIGN KEY (`incidente_id`)
    REFERENCES `mydb`.`registro_incidente` (`id_incidente`),
 
    FOREIGN KEY (`ambiente_id`)
    REFERENCES `mydb`.`ambiente` (`id_ambiente`),
  
    FOREIGN KEY (`tipo_incidente_id`)
    REFERENCES `mydb`.`tipo_incidente` (`id_tipo_inc`))
;


-- -----------------------------------------------------
-- Table `mydb`.`tipo_recurso`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tipo_recurso` (
  `id_tipo_recurso` INT(11) NOT NULL COMMENT 'Clave primaria. Identificador del tipo de recurso.\n',
  `recurso_tipo` VARCHAR(45) NOT NULL COMMENT 'Nombre del tipo (ej. PC, proyector, aire, etc.).\n',
  `descripcion_tipo` VARCHAR(60) NULL DEFAULT NULL COMMENT 'Descripción adicional.\n\n',
  PRIMARY KEY (`id_tipo_recurso`))
;


-- -----------------------------------------------------
-- Table `mydb`.`recursos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`recursos` (
  `id_recurso` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del recurso.\n',
  `serial_recurso` VARCHAR(100) NOT NULL COMMENT 'Serial físico o interno del recurso.\n',
  `num_recurso` TINYINT(4) NOT NULL COMMENT 'Nombre del recurso en el ambiente.\n',
  `nombre_recurso` VARCHAR(60) NOT NULL COMMENT 'Nombre del recurso.\n',
  `tipo_recurso` INT(11) NOT NULL COMMENT 'Llave foránea. Tipo de recurso.\n',
  `estado` ENUM('Disponible', 'En mantenimiento', 'Dañado') NULL DEFAULT NULL COMMENT 'Estado del recurso (operativo, dañado, en mantenimiento).\n',
  `observacion` TEXT NULL DEFAULT NULL COMMENT 'Observacion hacia algun recurso.',
  `ambiente_id` INT NOT NULL COMMENT 'Llave foranea, Ambiente al que pertenece.',
  PRIMARY KEY (`id_recurso`),
  
    FOREIGN KEY (`tipo_recurso`)
    REFERENCES `mydb`.`tipo_recurso` (`id_tipo_recurso`),
    
    FOREIGN KEY (`ambiente_id`)
    REFERENCES `mydb`.`ambiente` (`id_ambiente`))
;



-- -----------------------------------------------------
-- Table `mydb`.`registro_inasistencia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`registro_inasistencia` (
  `id_inasistencia` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del registro.\n',
  `fecha_inasistencia` DATE NOT NULL COMMENT 'Fecha del registro de asistencia.\n',
  `estado_inasistencia` ENUM('S', 'R', 'N') NOT NULL COMMENT 'Estado: S (asistió), R (retraso), N (no asistio).\n',
  `jornada_id` INT(11) NOT NULL COMMENT 'Jornada del aprendiz.\n',
  `aprendiz_Usuario_id_usuario` INT(11) NOT NULL COMMENT 'Llave foránea al aprendiz.\n\n',
  `instructor_Usuario_id_usuario` INT(11) NOT NULL,
  PRIMARY KEY (`id_inasistencia`),
  
    FOREIGN KEY (`jornada_id`)
    REFERENCES `mydb`.`jornada` (`id_jornada`),
  
    FOREIGN KEY (`aprendiz_Usuario_id_usuario`)
    REFERENCES `mydb`.`aprendiz` (`Usuario_id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  
    FOREIGN KEY (`instructor_Usuario_id_usuario`)
    REFERENCES `mydb`.`instructor` (`Usuario_id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
;


-- -----------------------------------------------------
-- Table `mydb`.`registro_minuta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`registro_minuta` (
  `id_minuta` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del registro de minuta.\n',
  `fecha_hora_recibo` DATETIME NOT NULL COMMENT 'Fecha y hora de recibo del ambiente.\n',
  `fecha_hora_entrega` DATETIME NOT NULL COMMENT 'Fecha y hora de entrega.\n',
  `novedad` TEXT NULL DEFAULT NULL COMMENT 'Novedad o eventualidad ocurrida.\n',
  `descripcion_min` TEXT NULL DEFAULT NULL COMMENT 'Observaciones generales.\n',
  `estado` TEXT NOT NULL COMMENT 'Estado general del ambiente al momento.\n',
  `ambiente_id` INT(11) NOT NULL COMMENT 'Ambiente relacionado.\n',
  `guarda_seguridad_Usuario_id_usuario` INT(11) NOT NULL COMMENT 'Guarda que recibió o entregó.\n\n',
  `responsable_id` INT(11) NOT NULL,
  `registro_minutacol` VARCHAR(45) NULL,
  PRIMARY KEY (`id_minuta`),
  
    FOREIGN KEY (`ambiente_id`)
    REFERENCES `mydb`.`ambiente` (`id_ambiente`),
 
    FOREIGN KEY (`guarda_seguridad_Usuario_id_usuario`)
    REFERENCES `mydb`.`guarda_seguridad` (`Usuario_id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,

    FOREIGN KEY (`responsable_id`)
    REFERENCES `mydb`.`instructor` (`Usuario_id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
;


-- -----------------------------------------------------
-- Table `mydb`.`rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`rol` (
  `id_rol` INT(11) NOT NULL COMMENT 'Clave primaria. Identificador del rol.\n',
  `nombre_rol` VARCHAR(45) NOT NULL COMMENT 'Nombre del rol (aprendiz, instructor, etc.).\n\n',
  PRIMARY KEY (`id_rol`))
;


-- -----------------------------------------------------
-- Table `mydb`.`traslado_recurso`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`traslado_recurso` (
  `id_traslado` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del traslado.\n',
  `recurso_id` INT(11) NOT NULL COMMENT 'Recurso trasladado.\n',
  `ambiente_origen_id` INT(11) NOT NULL COMMENT 'Ambiente de origen.\n',
  `ambiente_destino` INT(11) NOT NULL COMMENT 'Ambiente de destino.\n',
  `fecha_traslado` DATETIME NOT NULL COMMENT 'Fecha del traslado.\n',
  `observacion` TEXT NULL COMMENT 'Observaciones del traslado.\n\n',
  PRIMARY KEY (`id_traslado`),
 
    FOREIGN KEY (`recurso_id`)
    REFERENCES `mydb`.`recursos` (`id_recurso`),
 
    FOREIGN KEY (`ambiente_origen_id`)
    REFERENCES `mydb`.`ambiente` (`id_ambiente`))
;


-- -----------------------------------------------------
-- Table `mydb`.`user_rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user_rol` (
  `id_user_rol` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del registro.\n',
  `id_usuario` INT(11) NOT NULL COMMENT 'Usuario asociado.\n',
  `id_rol` INT(11) NOT NULL COMMENT 'Rol asignado al usuario.\n\n',
  PRIMARY KEY (`id_user_rol`),
  
    FOREIGN KEY (`id_usuario`)
    REFERENCES `mydb`.`usuario` (`id_usuario`),
  
    FOREIGN KEY (`id_rol`)
    REFERENCES `mydb`.`rol` (`id_rol`))
;

USE `mydb` ;
