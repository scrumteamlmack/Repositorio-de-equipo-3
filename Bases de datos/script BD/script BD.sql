drop database if exists bdproyectoPrueba;
create database bdproyectoPrueba;
use bdproyectoPrueba;

CREATE TABLE rol (
  id_rol INT PRIMARY KEY auto_increment NOT NULL,
  nombre_rol VARCHAR(50) NOT NULL 
);

CREATE TABLE Usuario (
  documento int PRIMARY KEY NOT NULL,
  nombre VARCHAR(50),
  apellido VARCHAR(50),
  tipo_documento VARCHAR(20),
  telefono INT(10),
  correo VARCHAR(20),
  contrasena VARCHAR(15)
);

CREATE TABLE user_rol (
  id_user_rol INT PRIMARY KEY auto_increment,
  doc_id INT,
  rol_id INT,
  FOREIGN KEY (doc_id) REFERENCES Usuario(documento),
  FOREIGN KEY (rol_id) REFERENCES rol(id_rol)
);

CREATE TABLE modalidad (
  id_modalidad INT PRIMARY KEY auto_increment,
  nombre_modalidad ENUM('presencial', 'sincronica')
);

CREATE TABLE jornada (
  id_jornada INT PRIMARY KEY auto_increment,
  nombre_jornada ENUM('mañana', 'tarde', 'noche', 'madrugada')
);


CREATE TABLE programas (
  id_programas INT PRIMARY KEY auto_increment,
  nombre_programa VARCHAR(50),
  nivel_formacion VARCHAR(30),
  duracion VARCHAR(50),
  jornada_id INT,
  modalidad_id INT,
  coordinacion_id INT,
  FOREIGN KEY (jornada_id) REFERENCES jornada(id_jornada),
  FOREIGN KEY (modalidad_id) REFERENCES modalidad(id_modalidad)
  FOREING KEY (coordinacion_id) REFERENCES coordinacion(id_coordinacion)
);

CREATE TABLE aprendiz (
  id_aprendiz INT PRIMARY KEY auto_increment,
  p_nombre VARCHAR(30),
  s_nombre VARCHAR(30),
  p_apellido VARCHAR(30),
  s_apellido VARCHAR(30),
  Num_ficha MEDIUMINT,
  programa_id INT,
  usua_id INT,
  FOREIGN KEY (programa_id) REFERENCES programas(id_programas),
  FOREIGN KEY (usua_id) REFERENCES Usuario(documento)
);

CREATE TABLE registro_asistencia (
  id_asistencia int PRIMARY KEY auto_increment,
  fecha_asistencia DATE,
  estado_asistencia ENUM('N', 'E', 'S')
)

CREATE TABLE coordinacion (
  id_coordinacion  INT PRIMARY KEY auto_increment,
  nombre_coordinacion VARCHAR(50),
  correo_cordinacion VARCHAR(30)
);

CREATE TABLE ambiente (
  id_ambiente INT PRIMARY KEY auto_increment,
  num_ambiente SMALLINT,
  capacidad SMALLINT ,
  tipo_ambiente VARCHAR(50),
  estado VARCHAR(30),
  coordinacion_id INT,
  FOREIGN KEY (coordinacion_id) REFERENCES coordinacion(id_coordinacion)
);

CREATE TABLE recursos (
  serial_recurso VARCHAR PRIMARY KEY,
  nombre_recurso VARCHAR(50),
  num_recurso TINYINT,
  tipo_recurso VARCHAR(30),
  estado VARCHAR(20),
  ambiente_id INT,
  FOREIGN KEY (ambiente_id) REFERENCES ambiente(id_ambiente)
);

CREATE TABLE registro_minuta (
  id_minuta INT PRIMARY KEY auto_increment,
  fecha_hora_recibido DATETIME,
  fecha_hora_entrega DATETIME,
  novedad TEXT,
  responsable VARCHAR(30),
  descripcion_amb TEXT,
  ambiente_id INT,
  docu_id int,
  FOREIGN KEY (ambiente_id) REFERENCES ambiente(id_ambiente),
  FOREIGN KEY (docu_id) REFERENCES Usuario(documento)
);

CREATE TABLE tipo_incidente(
  id_tipo_inc int primary key auto_increment,
  tipo_incidente VARCHAR(30),
  observacion_inc TEXT
);

CREATE TABLE registro_incidente (
  id_incidente INT PRIMARY KEY auto_increment,
  descripcion TEXT,
  fecha_incidente DATE,
  hora_incidente TIME,
  ambiente_id int,
  tipo_inc_id int,
  FOREIGN KEY (ambiente_id) REFERENCES ambiente(id_ambiente),
  FOREIGN KEY (tipo_inc_id) REFERENCES tipo_incidente(id_tipo_inc)
);
 

