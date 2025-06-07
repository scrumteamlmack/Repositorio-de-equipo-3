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

 CREATE TABLE turno (
  turno_id INT PRIMARY KEY auto_increment,
  turno_guarda ENUM('mañana', 'tarde', 'noche')
 );


CREATE TABLE modalidad (
  id_modalidad INT PRIMARY KEY auto_increment,
  nombre_modalidad ENUM('presencial', 'sincronica')
);

CREATE TABLE horarios (
  id_horario INT PRIMARY KEY auto_increment,
  dia_semana VARCHAR(10),
  hora_inicio TIME,
  hora_fin TIME
);

CREATE TABLE jornada (
  id_jornada INT PRIMARY KEY auto_increment,
  nombre_jornada VARCHAR(10),
  id_horario INT,
  FOREIGN KEY (id_horario) REFERENCES horarios(id_horario)
);


CREATE TABLE programas (
  id_programas INT PRIMARY KEY auto_increment,
  nombre_programa VARCHAR(50),
  nivel_formacion VARCHAR(30),
  duracion VARCHAR(50),
  jornada_id INT,
  modalidad_id INT,
  FOREIGN KEY (jornada_id) REFERENCES jornada(id_jornada),
  FOREIGN KEY (modalidad_id) REFERENCES modalidad(id_modalidad)
);

CREATE TABLE aprendiz (
  id_aprendiz INT PRIMARY KEY auto_increment,
  p_nombre VARCHAR(30),
  s_nombre VARCHAR(30),
  p_apellido VARCHAR(30),
  s_apellido VARCHAR(30),
  Num_ficha MEDIUMINT,
  control_accesos INT,
  programa_id INT,
  FOREIGN KEY (control_accesos) REFERENCES control_accesos(id_accesos),
  FOREIGN KEY (programa_id) REFERENCES programas(id_programas)
);


CREATE TABLE registro_equipo (
  id_equipo INT PRIMARY KEY auto_increment,
  tipo_equipo VARCHAR(30),
  marca VARCHAR(20),
  modelo VARCHAR(20),
  descripcion TEXT,
  estado VARCHAR(20),
  observacion TEXT,
  placa_equipo VARCHAR(20),
  usuario_docu int,
  FOREIGN KEY (usuario_docu) REFERENCES Usuario(documento)
);

CREATE TABLE coordinacion (
  id_coordinacion  INT PRIMARY KEY auto_increment,
  nombre_coordinacion VARCHAR(50),
  correo_cordinacion VARCHAR(30)
);

CREATE TABLE visitante (
  id_visita INT PRIMARY KEY auto_increment,
  pv_nombre VARCHAR(50),
  sv_nombre VARCHAR(50),
  pv_apellido VARCHAR(50),
  sv_apellido VARCHAR(50),
  correo_vis VARCHAR(50),
  tel_vis INT(20),
  motivo_visita TEXT,
  coordinacion_id INT,
  guarda_id INT,
  foreign key (coordinacion_id) references coordinacion(id_coordinacion),
  foreign key (guarda_id) references guarda_seguridad(id_guarda)
);

CREATE TABLE registro_visitante (
  id_visitas INT PRIMARY KEY auto_increment,
  fecha_hora_ingreso DATETIME,
  fecha_hora_salida DATETIME,
  obs_visita TEXT,
  visita_id INT,
  FOREIGN KEY (visita_id) REFERENCES visitante(id_visita)
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
  id_recurso INT PRIMARY KEY auto_increment,
  nombre_recurso VARCHAR(50),
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
  ambiente_id INT,
  descripcion_amb TEXT,
  docu_id int,
  FOREIGN KEY (ambiente_id) REFERENCES ambiente(id_ambiente),
  FOREIGN KEY (docu_id) REFERENCES Usuario(documento)
);

CREATE TABLE tipo_incidente(
  id_tipo_inc int primary key auto_increment,
  tipo_inc VARCHAR(30),
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
 

