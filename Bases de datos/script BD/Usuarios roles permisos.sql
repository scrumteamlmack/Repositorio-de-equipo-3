
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