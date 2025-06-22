
CREATE DATABASE IF NOT EXISTS `mydb` ;
USE `mydb` ;

CREATE TABLE  Usuario(
  `id_usuario` INT AUTO_INCREMENT NOT NULL,
  `p_nombre` VARCHAR(50) NOT NULL,
  `s_nombre` VARCHAR(50) ,
  `p_apellido` VARCHAR(45) NOT NULL,
  `s_apellido` VARCHAR(45) ,
  `tipo_documento` ENUM('CC', 'TI', 'CE', 'OTRO') NOT NULL,
  `num_documento` INT NOT NULL,
  `correo` VARCHAR(100) NOT NULL,
  `contraseña` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id_usuario`));

CREATE TABLE coordinacion(
  `id_coordinacion` INT  AUTO_INCREMENT NOT NULL,
  `nombre_coordinacion` VARCHAR(45) NOT NULL,
  `correo_coordinacion` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id_coordinacion`));

CREATE TABLE Coordinador(
  `Usuario_id_usuario` INT NOT NULL,
  `coordinacion_id_coordinacion` INT NOT NULL,
  PRIMARY KEY (`Usuario_id_usuario`),
    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (`coordinacion_id_coordinacion`) REFERENCES coordinacion(`id_coordinacion`) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE jornada(
  `id_jornada` INT NOT NULL,
  `nombre_jornada` ENUM('Mañana', 'Tarde', 'Noche', 'Madrugada') NOT NULL,
  PRIMARY KEY (`id_jornada`));

CREATE TABLE modalidad(
  `id_modalidad` INT NOT NULL,
  `nombre_modalidad` ENUM('Presencial', 'sincronica') NOT NULL,
  PRIMARY KEY (`id_modalidad`));

CREATE TABLE programas(
  `id_programas` INT NOT NULL,
  `nombre_programa` VARCHAR(50) NOT NULL,
  `nivel_formacion` VARCHAR(30) NOT NULL,
  `duracion` VARCHAR(50) NOT NULL,
  `jornada_id` INT NOT NULL,
  `modalidad_id` INT NOT NULL,
  `coordinacion_id` INT NOT NULL,
  PRIMARY KEY (`id_programas`),
    FOREIGN KEY (`jornada_id`) REFERENCES jornada(`id_jornada`),
    FOREIGN KEY (`modalidad_id`) REFERENCES modalidad(`id_modalidad`),
    FOREIGN KEY (`coordinacion_id`) REFERENCES coordinacion(`id_coordinacion`));


CREATE TABLE instructor(
  `Usuario_id_usuario` INT NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `telefono` VARCHAR(20) NOT NULL,
  `coordinacion` VARCHAR(100) NOT NULL,
  `fichas_asignadas` TEXT NOT NULL,
  `estado` ENUM('Activo', 'Inactivo') NOT NULL,
  `jornada_id_jornada` INT NOT NULL,
  `programas_id_programas` INT NOT NULL,
  PRIMARY KEY (`Usuario_id_usuario`),
    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`),
    FOREIGN KEY (`jornada_id_jornada`) REFERENCES jornada(`id_jornada`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (`programas_id_programas`) REFERENCES programas(`id_programas`) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE guarda_seguridad(
  `Usuario_id_usuario` INT NOT NULL,
  `turno` ENUM('Mañana', 'Tarde', 'Noche') NOT NULL,
  `fecha_ingreso` DATE NOT NULL,
  `estado` ENUM('Activo', 'Inactivo') NOT NULL,
  PRIMARY KEY (`Usuario_id_usuario`),
    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`));

CREATE TABLE aprendiz(
  `Usuario_id_usuario` INT NOT NULL,
  `Num_ficha` MEDIUMINT NOT NULL,
  `programas_id_programas` INT NOT NULL,
  `jornada_id_jornada` INT NOT NULL,
  PRIMARY KEY (`Usuario_id_usuario`),
    FOREIGN KEY (`programas_id_programas`) REFERENCES programas(`id_programas`) ON DELETE NO ACTION ON UPDATE NO ACTION,

    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,

    FOREIGN KEY (`jornada_id_jornada`) REFERENCES jornada(`id_jornada`) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE rol(
  `id_rol` INT NOT NULL,
  `nombre_rol` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_rol`));

CREATE TABLE user_rol(
  `id_user_rol` INT NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `id_rol` INT NOT NULL,
  PRIMARY KEY (`id_user_rol`),
    FOREIGN KEY (`id_usuario`) REFERENCES Usuario(`id_usuario`),
    FOREIGN KEY (`id_rol`) REFERENCES  rol(`id_rol`));

CREATE TABLE ambiente(
  `id_ambiente` INT NOT NULL,
  `num_ambiente` SMALLINT NOT NULL,
  `capacidad` SMALLINT NOT NULL,
  `tipo_ambiente` VARCHAR(45) NOT NULL,
  `estado` VARCHAR(30) NOT NULL,
  `coordinacion_id` INT NOT NULL,
  PRIMARY KEY (`id_ambiente`),
    FOREIGN KEY (`coordinacion_id`) REFERENCES coordinacion(`id_coordinacion`));

CREATE TABLE tipo_incidente(
  `id_tipo_inc` INT NOT NULL,
  `tipo_incidente` VARCHAR(45) NOT NULL,
  `observacion_inc` TEXT NOT NULL,
  PRIMARY KEY (`id_tipo_inc`));

CREATE TABLE registro_incidente(
  `id_incidente` INT NOT NULL AUTO_INCREMENT,
  `descripcion` TEXT NULL DEFAULT NULL,
  `fecha_incidente` DATE NOT NULL,
  `hora_incidente` TIME NOT NULL,
  `ambiente_id` INT NOT NULL,
  `tipo_inc_id` INT NOT NULL,
  `guarda_seguridad_Usuario_id_usuario` INT NOT NULL,
  PRIMARY KEY (`id_incidente`),
    FOREIGN KEY (`ambiente_id`) REFERENCES ambiente(`id_ambiente`),

    FOREIGN KEY (`tipo_inc_id`) REFERENCES tipo_incidente(`id_tipo_inc`),

    FOREIGN KEY (`guarda_seguridad_Usuario_id_usuario`) REFERENCES guarda_seguridad(`Usuario_id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE tipo_recurso (
  `id_tipo_recurso` INT NOT NULL,
  `recurso_tipo` VARCHAR(45) NOT NULL,
  `descripcion_tipo` VARCHAR(60) NULL,
  PRIMARY KEY (`id_tipo_recurso`));

CREATE TABLE recursos(
  `id_recurso` INT NOT NULL AUTO_INCREMENT,
  `serial_recurso` VARCHAR(100) NOT NULL,
  `num_recurso` TINYINT NOT NULL,
  `nombre_recurso` VARCHAR(60) NOT NULL,
  `tipo_recurso` INT NOT NULL,
  `observacion` TEXT NULL,
  `ambiente_id` INT NOT NULL,
  PRIMARY KEY (`id_recurso`),
    FOREIGN KEY (`tipo_recurso`) REFERENCES tipo_recurso(`id_tipo_recurso`),
    FOREIGN KEY (`ambiente_id`) REFERENCES ambiente(`id_ambiente`));

CREATE TABLE registro_minuta(
  `id_minuta` INT NOT NULL AUTO_INCREMENT,
  `fecha_hora_recibo` DATETIME NOT NULL,
  `fecha_hora_entrega` DATETIME NOT NULL,
  `novedad` TEXT NULL,
  `responsable` VARCHAR(250) NOT NULL,
  `descripcion_min` TEXT NULL DEFAULT NULL,
  `ambiente_id` INT NOT NULL,
  `Usuario_id_usuario` INT NOT NULL,
  `guarda_seguridad_Usuario_id_usuario` INT NOT NULL,
  PRIMARY KEY (`id_minuta`),
    FOREIGN KEY (`Usuario_id_usuario`) REFERENCES Usuario(`id_usuario`),
    FOREIGN KEY (`ambiente_id`) REFERENCES ambiente(`id_ambiente`),
    FOREIGN KEY (`guarda_seguridad_Usuario_id_usuario`) REFERENCES guarda_seguridad(`Usuario_id_usuario`)ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE registro_asistencia (
  `id_asistencia` INT NOT NULL AUTO_INCREMENT,
  `fecha_asistencia` DATE NOT NULL,
  `estado_asistencia` ENUM('S', 'R', 'N') NOT NULL,
  `jornada_id` INT NOT NULL,
  `aprendiz_Usuario_id_usuario` INT NOT NULL,
  PRIMARY KEY (`id_asistencia`),
    FOREIGN KEY (`jornada_id`) REFERENCES jornada(`id_jornada`),
    FOREIGN KEY (`aprendiz_Usuario_id_usuario`) REFERENCES aprendiz(`Usuario_id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION);

CREATE TABLE alertas_inasistencia (
  `id_alerta` INT NULL DEFAULT NULL,
  `aprendiz_id` INT NOT NULL,
  `cantidad_fallas` INT NOT NULL,
  `fecha_alerta` DATETIME NOT NULL,
  `mensaje` TEXT NOT NULL,
  `coordinacion_id` INT NOT NULL,
  PRIMARY KEY (`id_alerta`),
    FOREIGN KEY (`coordinacion_id`) REFERENCES coordinacion(`id_coordinacion`));

