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

-- llenar la tabla de usuarios

INSERT INTO usuarios (nombre_usuario, contraseña, rol, privilegios) VALUES
('santiago.cruzuwu25', 'Aprz#2025xy!', 'APRENDIZ', 'SELECT, UPDATE (solo su perfil)'),
('javier.pineda21', 'Instruc@321!', 'INSTRUCTOR', 'SELECT, INSERT, UPDATE'),
('jtavarez92', 'VgS3cuR@9!', 'VIGILANTE', 'SELECT, INSERT, UPDATE'),
('rositarpelea100', 'Adm!nR0!X@8', 'ADMINISTRADOR', 'SELECT, INSERT, UPDATE, DELETE');



-- Solo puede actualizar aprendiz (su perfil)
GRANT SELECT ON aprendiz TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON ambiente TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON equipouser_rol TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON horarios TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON modalidad TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON coordinacion TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON instructor TO 'santiago.cruzuwu25'@'localhost';
GRANT UPDATE ON jornada TO 'santiago.cruzuwu25'@'localhost';
GRANT UPDATE ON rol TO 'santiago.cruzuwu25'@'localhost';