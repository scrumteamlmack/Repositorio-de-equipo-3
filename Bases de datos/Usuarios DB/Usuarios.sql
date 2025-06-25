CREATE TABLE usuarios (
    nombre_usuario VARCHAR(50) PRIMARY KEY,
    contrasena VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    privilegios TEXT NOT NULL
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

-- Insertar nuevo usuario con rol de COORDINADOR
INSERT INTO usuarios (nombre_usuario, contrasena, rol, privilegios)
VALUES ('coordinador.gomez90', 'Coordimez#0039v', 'COORDINADOR', 'SELECT, INSERT, UPDATE');

--Cambiar nombre de usuario coordinador
UPDATE usuarios
SET nombre_usuario = 'PedritoGomez64'
WHERE nombre_usuario = 'coordinador.gomez90';

--crear usuario de coordinador 
CREATE USER 'PedritoGomez64'@'localhost' IDENTIFIED BY 'Coordimez#0039';

-- Asignar privilegios a coordinador
GRANT SELECT, INSERT, UPDATE, DELETE ON ambiente TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON aprendiz TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON instructor TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON coordinacion TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON jornada TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON programas TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON recursos TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON registro_asistencia TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON registro_incidente TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON tipo_incidente TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON rol TO 'PedritoGomez64'@'localhost';

-- Aplicar cambios
FLUSH PRIVILEGES;

-- correccion usuarios nueva scrit
-- Insertar roles
INSERT INTO rol (id_rol, nombre_rol) VALUES
(1, 'APRENDIZ'),
(2, 'INSTRUCTOR'),
(3, 'GUARDA DE SEGURIDAD'),
(4, 'ADMINISTRADOR'),
(5, 'COORDINADOR');

-- Insertar usuarios en la tabla principal 'Usuario'
INSERT INTO Usuario (p_nombre, s_nombre, p_apellido, s_apellido, tipo_documento, num_documento, correo, contraseña)
VALUES 
('Santiago', NULL, 'Cruz', NULL, 'CC', 109874321, 'santiago.cruz@sena.edu.co', 'Aprz#2025xy!'),
('Javier', NULL, 'Pineda', 'Uribe', 'CC', 1089347652, 'jpinedau@sena.edu.co', 'Instruc@321!'),
('Juan', NULL, 'Tavarez', NULL, 'CC', 1000100010, 'jtavarez@sena.edu.co', 'VgS3cuR@9!'),
('Rosita', NULL, 'Ropelea', NULL, 'CC', 123456789, 'rositar@sena.edu.co', 'Adm!nR0!X@8'),
('Pedro', NULL, 'Gomez', NULL, 'CC', 1122334455, 'pedrogomez@sena.edu.co', 'Coordimez#0039');

-- Asignar roles (relacion entre Usuario y Rol)
INSERT INTO user_rol (id_usuario, id_rol) VALUES
(1, 1), -- Santiago -> Aprendiz
(2, 2), -- Javier -> Instructor
(3, 3), -- Juan -> Guarda de seguridad
(4, 4), -- Rosita -> Administrador
(5, 5); -- Pedro -> Coordinador

-- Privilegios de Santiago
GRANT SELECT ON mydb.aprendiz TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON mydb.ambiente TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON mydb.user_rol TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON mydb.modalidad TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON mydb.coordinacion TO 'santiago.cruzuwu25'@'localhost';
GRANT SELECT ON mydb.instructor TO 'santiago.cruzuwu25'@'localhost';
GRANT UPDATE ON mydb.aprendiz TO 'santiago.cruzuwu25'@'localhost';

-- Privilegios de Javier (INSTRUCTOR)
GRANT SELECT, INSERT, UPDATE ON mydb.aprendiz TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.ambiente TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.instructor TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.jornada TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.registro_asistencia TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.modalidad TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.coordinacion TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.programas TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.recursos TO 'javier.pineda21'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.rol TO 'javier.pineda21'@'localhost';

-- Privilegios de Juan (GUARDA DE SEGURIDAD)
GRANT SELECT, INSERT, UPDATE ON mydb.registro_minuta TO 'jtavarez92'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.recursos TO 'jtavarez92'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.ambiente TO 'jtavarez92'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.registro_incidente TO 'jtavarez92'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.tipo_recurso TO 'jtavarez92'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.instructor TO 'jtavarez92'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.rol TO 'jtavarez92'@'localhost';

-- Privilegios de Rosita (ADMINISTRADORA)
GRANT ALL PRIVILEGES ON mydb.* TO 'rositarpelea100'@'localhost';

-- Privilegios de Pedro (COORDINADOR)
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.ambiente TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.aprendiz TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.instructor TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.coordinacion TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.jornada TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.programas TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.recursos TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.registro_asistencia TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.registro_incidente TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.tipo_incidente TO 'PedritoGomez64'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.rol TO 'PedritoGomez64'@'localhost';



-- CORRECCIÓN 2 :/
-- COORDINADORES 
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.* TO 'carlos.garcia'@'localhost';
CREATE USER 'maria.rodriguez'@'localhost' IDENTIFIED BY 'Mariarodriguez#7230';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.* TO 'maria.rodriguez'@'localhost';
CREATE USER 'juan.hernandez'@'localhost' IDENTIFIED BY 'Juanhernandez#365f';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.* TO 'juan.hernandez'@'localhost';
CREATE USER 'ana.jimenez'@'localhost' IDENTIFIED BY 'Anajimenez#ab65';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.* TO 'ana.jimenez'@'localhost';
CREATE USER 'luis.vargas'@'localhost' IDENTIFIED BY 'Luisvargas#56a6';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.* TO 'luis.vargas'@'localhost';

-- Aprendiz patricia morales
GRANT SELECT ON mydb.aprendiz TO 'patricia.morales'@'localhost';
GRANT SELECT ON mydb.programas TO 'patricia.morales'@'localhost';
GRANT SELECT ON mydb.jornada TO 'patricia.morales'@'localhost';
GRANT SELECT ON mydb.modalidad TO 'patricia.morales'@'localhost';