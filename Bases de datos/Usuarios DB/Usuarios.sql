CREATE TABLE usuarios (
    nombre_usuario VARCHAR(50) PRIMARY KEY,
    contrasena VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    privilegios TEXT NOT NULL
);

-- Aqui es para mirar informacion sobre el instructor, ejm: el aprtendiz tiene derecho a mirar con que instructor le toca, cual es su correo en q se especializa etc

CREATE TABLE instructor (
  id_instructor INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL,
  apellido VARCHAR(50) NOT NULL,
  tipo_documento ENUM('CC', 'TI', 'CE', 'Pasaporte') NOT NULL,
  numero_documento VARCHAR(20) UNIQUE NOT NULL,
  email VARCHAR(100) NOT NULL,
  telefono VARCHAR(20),
  coordinacion VARCHAR(100) NOT NULL,
  fichas_asignadas TEXT NOT NULL,
  especialidad VARCHAR(100),
  fecha_ingreso DATE,
  estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo'
);


-- info instructor
INSERT INTO instructor (
  nombre,
  apellido,
  tipo_documento,
  numero_documento,
  email,
  telefono,
  coordinacion,
  fichas_asignadas,
  especialidad,
  fecha_ingreso,
  estado
) VALUES (
  'Javier Leonardo',
  'Pineda Uribe',
  'CC',
  '1089347652',
  'jpinedau@sena.edu.co',
  '3124567890',
  'Tecnología e Innovación',
  '3197815',
  'Sistemas',
  '2012-03-15',
  'Activo'
);

-- llenar la tabla de usuarios

INSERT INTO usuarios (nombre_usuario, contraseña, rol, privilegios) VALUES
('santiago.cruzuwu25', 'Aprz#2025xy!', 'APRENDIZ', 'SELECT, UPDATE (solo su perfil)'),
('javier.pineda21', 'Instruc@321!', 'INSTRUCTOR', 'SELECT, INSERT, UPDATE'),
('jtavarez92', 'VgS3cuR@9!', 'VIGILANTE', 'SELECT, INSERT, UPDATE'),
('rositarpelea100', 'Adm!nR0!X@8', 'ADMINISTRADOR', 'SELECT, INSERT, UPDATE, DELETE');



-- Solo puede actualizar aprendiz (su perfil)
GRANT SELECT ON aprendiz TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON ambiente TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON user_rol TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON modalidad TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON coordinacion TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON instructor TO 'santiago.cruzuwu25'@'localhost';
GRANT UPDATE ON jornada TO 'santiago.cruzuwu25'@'localhost';
GRANT UPDATE ON rol TO 'santiago.cruzuwu25'@'localhost';


--Acceso a tablas instructor
GRANT SELECT, INSERT, UPDATE ON aprendiz TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON ambiente TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON instructor TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON jornada TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON registro_asistencia TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON modalidad TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON coordinacion TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON programas TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON recursos TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON rol TO 'javier.pineda21'@'localhost';

--Acceso a tablas vigilante 
GRANT SELECT, INSERT, UPDATE ON registro_minuta TO 'jtavarez92'@'localhost';
GRANT SELECT, INSERT, UPDATE ON recursos TO 'jtavarez92'@'localhost';
GRANT SELECT, INSERT, UPDATE ON ambiente TO 'jtavarez92'@'localhost';
GRANT SELECT, INSERT, UPDATE ON registro_incidente TO 'jtavarez92'@'localhost';
GRANT SELECT, INSERT, UPDATE ON tipo_recurso TO 'jtavarez92'@'localhost';
GRANT SELECT, INSERT, UPDATE ON instructor TO 'jtavarez92'@'localhost';
GRANT SELECT, INSERT, UPDATE ON rol TO 'jtavarez92'@'localhost';


--Acceso a tablas administrador
GRANT SELECT, INSERT, UPDATE, DELETE ON aprendiz TO 'rositarelepea100'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON ambiente TO 'rositarelepea100'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON instructor TO 'rositarelepea100'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON rol TO 'rositarelepea100'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON registro_asistencia TO 'rositarelepea100'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON modalidad TO 'rositarelepea100'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON coordinacion TO 'rositarelepea100'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON programas TO 'rositarelepea100'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON tipo_incidente TO 'rositarelepea100'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON registro_incidente TO 'rositarelepea100'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON recursos TO 'rositarelepea100'@'localhost'; 
GRANT SELECT, INSERT, UPDATE, DELETE ON jornada TO 'rositarelepea100'@'localhost';


-- Cambiar el rol 'VIGILANTE' a 'GUARDA DE SEGURIDAD'
UPDATE usuarios
SET rol = 'GUARDA DE SEGURIDAD'
WHERE rol = 'VIGILANTE';