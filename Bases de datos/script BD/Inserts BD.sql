-- Script para poblar la base de datos mydb con datos de prueba
USE `mydb`;

-- Poblar tabla jornada (debe ir primero por las dependencias)
INSERT INTO jornada (id_jornada, nombre_jornada) VALUES
(1, 'Mañana'),
(2, 'Tarde'),
(3, 'Noche');

-- Poblar tabla modalidad
INSERT INTO modalidad (id_modalidad, nombre_modalidad) VALUES
(1, 'Presencial'),
(2, 'Virtual'),
(3, 'Mixta');

-- Poblar tabla coordinacion
INSERT INTO coordinacion (nombre_coordinacion, correo_coordinacion) VALUES
('Sistemas de Información', 'sistemas@sena.edu.co'),
('Electrónica y Telecomunicaciones', 'electronica@sena.edu.co'),
('Mecánica Industrial', 'mecanica@sena.edu.co'),
('Administración de Empresas', 'administracion@sena.edu.co'),
('Salud y Bienestar', 'salud@sena.edu.co'),
('Construcción', 'construccion@sena.edu.co'),
('Agricultura', 'agricultura@sena.edu.co'),
('Turismo y Hotelería', 'turismo@sena.edu.co');

-- Poblar tabla programas
INSERT INTO programas (id_programas, nombre_programa, nivel_formacion, duracion, jornada_id, modalidad_id, coordinacion_id) VALUES
(1001, 'Análisis y Desarrollo de Software', 'Tecnólogo', '24 meses', 1, 1, 1),
(1002, 'Técnico en Sistemas', 'Técnico', '18 meses', 2, 1, 1),
(1003, 'Redes y Telecomunicaciones', 'Tecnólogo', '24 meses', 1, 2, 2),
(1004, 'Electrónica Industrial', 'Técnico', '18 meses', 3, 1, 2),
(1005, 'Mantenimiento Industrial', 'Técnico', '18 meses', 1, 1, 3),
(1006, 'Soldadura Industrial', 'Técnico', '12 meses', 2, 1, 3),
(1007, 'Administración de Empresas', 'Tecnólogo', '24 meses', 2, 3, 4),
(1008, 'Contabilidad y Finanzas', 'Técnico', '18 meses', 1, 2, 4),
(1009, 'Enfermería', 'Técnico', '18 meses', 1, 1, 5),
(1010, 'Auxiliar en Salud Oral', 'Técnico', '12 meses', 2, 1, 5);

-- Poblar tabla Usuario (30 usuarios)
INSERT INTO Usuario (p_nombre, s_nombre, p_apellido, s_apellido, tipo_documento, num_documento, correo, contraseña) VALUES
('Carlos', 'Andrés', 'García', 'López', 'CC', 1234567890, 'carlos.garcia@sena.edu.co', 'password123'),
('María', 'Elena', 'Rodríguez', 'Martínez', 'CC', 1234567891, 'maria.rodriguez@sena.edu.co', 'password123'),
('Juan', 'Pablo', 'Hernández', 'González', 'CC', 1234567892, 'juan.hernandez@sena.edu.co', 'password123'),
('Ana', 'Sofía', 'Jiménez', 'Torres', 'CC', 1234567893, 'ana.jimenez@sena.edu.co', 'password123'),
('Luis', 'Fernando', 'Vargas', 'Ruiz', 'CC', 1234567894, 'luis.vargas@sena.edu.co', 'password123'),
('Patricia', 'Isabel', 'Morales', 'Castro', 'CC', 1234567895, 'patricia.morales@sena.edu.co', 'password123'),
('Roberto', 'José', 'Sánchez', 'Díaz', 'CC', 1234567896, 'roberto.sanchez@sena.edu.co', 'password123'),
('Carmen', 'Lucía', 'Ramírez', 'Peña', 'CC', 1234567897, 'carmen.ramirez@sena.edu.co', 'password123'),
('Miguel', 'Ángel', 'Flores', 'Vega', 'CC', 1234567898, 'miguel.flores@sena.edu.co', 'password123'),
('Laura', 'Cristina', 'Mendoza', 'Silva', 'CC', 1234567899, 'laura.mendoza@sena.edu.co', 'password123'),
('Diego', 'Alejandro', 'Ortega', 'Ramos', 'CC', 1234567800, 'diego.ortega@sena.edu.co', 'password123'),
('Sandra', 'Milena', 'Guerrero', 'Herrera', 'CC', 1234567801, 'sandra.guerrero@sena.edu.co', 'password123'),
('Andrés', 'Felipe', 'Rojas', 'Medina', 'CC', 1234567802, 'andres.rojas@sena.edu.co', 'password123'),
('Claudia', 'Patricia', 'Aguilar', 'Cortés', 'CC', 1234567803, 'claudia.aguilar@sena.edu.co', 'password123'),
('Fernando', 'Antonio', 'Delgado', 'Paredes', 'CC', 1234567804, 'fernando.delgado@sena.edu.co', 'password123'),
('Gloria', 'Esperanza', 'Parra', 'Bautista', 'CC', 1234567805, 'gloria.parra@sena.edu.co', 'password123'),
('Javier', 'Enrique', 'Cárdenas', 'Molina', 'CC', 1234567806, 'javier.cardenas@sena.edu.co', 'password123'),
('Mónica', 'Alejandra', 'Ospina', 'Restrepo', 'CC', 1234567807, 'monica.ospina@sena.edu.co', 'password123'),
('Ricardo', 'Javier', 'Muñoz', 'Salazar', 'CC', 1234567808, 'ricardo.munoz@sena.edu.co', 'password123'),
('Beatriz', 'Elena', 'Castaño', 'Giraldo', 'CC', 1234567809, 'beatriz.castano@sena.edu.co', 'password123'),
('Sergio', 'Iván', 'Valencia', 'Quintero', 'CC', 1234567810, 'sergio.valencia@sena.edu.co', 'password123'),
('Liliana', 'María', 'Bedoya', 'Alzate', 'CC', 1234567811, 'liliana.bedoya@sena.edu.co', 'password123'),
('Óscar', 'David', 'Henao', 'Cardona', 'CC', 1234567812, 'oscar.henao@sena.edu.co', 'password123'),
('Paola', 'Andrea', 'Vélez', 'Jaramillo', 'CC', 1234567813, 'paola.velez@sena.edu.co', 'password123'),
('Hernán', 'Darío', 'Mesa', 'Londoño', 'CC', 1234567814, 'hernan.mesa@sena.edu.co', 'password123'),
('Yolanda', 'Rocío', 'Arbeláez', 'Gómez', 'CC', 1234567815, 'yolanda.arbelaez@sena.edu.co', 'password123'),
('Germán', 'Alberto', 'Escobar', 'Ríos', 'CC', 1234567816, 'german.escobar@sena.edu.co', 'password123'),
('Rosa', 'María', 'Arango', 'Franco', 'CC', 1234567817, 'rosa.arango@sena.edu.co', 'password123'),
('Álvaro', 'Hernando', 'Zapata', 'Mejía', 'CC', 1234567818, 'alvaro.zapata@sena.edu.co', 'password123'),
('Martha', 'Cecilia', 'Correa', 'Montoya', 'CC', 1234567819, 'martha.correa@sena.edu.co', 'password123');

-- Poblar tabla rol
INSERT INTO rol (id_rol, nombre_rol) VALUES
(1, 'Coordinador'),
(2, 'Instructor'),
(3, 'Aprendiz'),
(4, 'Guarda de Seguridad'),
(5, 'Administrador');

-- Poblar tabla user_rol
INSERT INTO user_rol (id_usuario, id_rol) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1),
(6, 2), (7, 2), (8, 2), (9, 2), (10, 2),
(11, 2), (12, 2), (13, 2), (14, 2), (15, 2),
(16, 3), (17, 3), (18, 3), (19, 3), (20, 3),
(21, 3), (22, 3), (23, 3), (24, 3), (25, 3),
(26, 3), (27, 3), (28, 4), (29, 4), (30, 5);

-- Poblar tabla Coordinador
INSERT INTO Coordinador (Usuario_id_usuario, coordinacion_id_coordinacion) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Poblar tabla instructor
INSERT INTO instructor (Usuario_id_usuario, email, telefono, coordinacion, fichas_asignadas, estado, jornada_id_jornada, programas_id_programas) VALUES
(6, 'instructor1@sena.edu.co', '3001234567', 'Sistemas de Información', '2567890,2567891', 'Activo', 1, 1001),
(7, 'instructor2@sena.edu.co', '3001234568', 'Sistemas de Información', '2567892', 'Activo', 2, 1002),
(8, 'instructor3@sena.edu.co', '3001234569', 'Electrónica y Telecomunicaciones', '2567893,2567894', 'Activo', 1, 1003),
(9, 'instructor4@sena.edu.co', '3001234570', 'Electrónica y Telecomunicaciones', '2567895', 'Activo', 3, 1004),
(10, 'instructor5@sena.edu.co', '3001234571', 'Mecánica Industrial', '2567896,2567897', 'Activo', 1, 1005),
(11, 'instructor6@sena.edu.co', '3001234572', 'Mecánica Industrial', '2567898', 'Activo', 2, 1006),
(12, 'instructor7@sena.edu.co', '3001234573', 'Administración de Empresas', '2567899', 'Activo', 2, 1007),
(13, 'instructor8@sena.edu.co', '3001234574', 'Administración de Empresas', '2567900', 'Activo', 1, 1008),
(14, 'instructor9@sena.edu.co', '3001234575', 'Salud y Bienestar', '2567901', 'Activo', 1, 1009),
(15, 'instructor10@sena.edu.co', '3001234576', 'Salud y Bienestar', '2567902', 'Activo', 2, 1010);

-- Poblar tabla guarda_seguridad
INSERT INTO guarda_seguridad (Usuario_id_usuario, turno, fecha_ingreso, estado) VALUES
(28, 'Mañana', '2024-01-15', 'Activo'),
(29, 'Tarde', '2024-02-20', 'Activo');

-- Poblar tabla aprendiz
INSERT INTO aprendiz (Usuario_id_usuario, Num_ficha, programas_id_programas, jornada_id_jornada) VALUES
(16, 2567890, 1001, 1),
(17, 2567891, 1001, 1),
(18, 2567892, 1002, 2),
(19, 2567893, 1003, 1),
(20, 2567894, 1003, 1),
(21, 2567895, 1004, 3),
(22, 2567896, 1005, 1),
(23, 2567897, 1005, 1),
(24, 2567898, 1006, 2),
(25, 2567899, 1007, 2),
(26, 2567900, 1008, 1),
(27, 2567901, 1009, 1);

-- Poblar tabla ambiente
INSERT INTO ambiente (id_ambiente, num_ambiente, capacidad, tipo_ambiente, estado, coordinacion_id) VALUES
(101, 101, 30, 'Aula de Sistemas', 'Disponible', 1),
(102, 102, 25, 'Laboratorio de Programación', 'Disponible', 1),
(103, 103, 35, 'Aula Teórica', 'Disponible', 1),
(201, 201, 20, 'Laboratorio de Electrónica', 'Disponible', 2),
(202, 202, 25, 'Taller de Telecomunicaciones', 'Disponible', 2),
(301, 301, 30, 'Taller de Mecánica', 'Disponible', 3),
(302, 302, 15, 'Laboratorio de Soldadura', 'Disponible', 3),
(401, 401, 40, 'Aula de Administración', 'Disponible', 4),
(501, 501, 25, 'Laboratorio de Enfermería', 'Disponible', 5),
(502, 502, 20, 'Consultorio Odontológico', 'Disponible', 5);

-- Poblar tabla tipo_incidente
INSERT INTO tipo_incidente (id_tipo_inc, tipo_incidente, observacion_inc) VALUES
(1, 'Daño de Equipo', 'Equipos dañados o averiados'),
(2, 'Accidente Laboral', 'Accidentes durante las prácticas'),
(3, 'Hurto', 'Pérdida o robo de elementos'),
(4, 'Indisciplina', 'Comportamientos inadecuados'),
(5, 'Emergencia Médica', 'Situaciones médicas de emergencia');

-- Poblar tabla registro_incidente
INSERT INTO registro_incidente (descripcion, fecha_incidente, hora_incidente, ambiente_id, tipo_inc_id, guarda_seguridad_Usuario_id_usuario) VALUES
('Computador presenta fallas en el arranque', '2024-03-15', '10:30:00', 101, 1, 28),
('Estudiante se cortó con herramienta en el taller', '2024-03-20', '14:15:00', 301, 2, 29),
('Falta un mouse del laboratorio', '2024-03-25', '08:45:00', 102, 3, 28),
('Discusión entre estudiantes', '2024-04-01', '16:20:00', 401, 4, 29),
('Estudiante se sintió mal durante la clase', '2024-04-05', '11:30:00', 501, 5, 28);

-- Poblar tabla tipo_recurso
INSERT INTO tipo_recurso (id_tipo_recurso, recurso_tipo, descripcion_tipo) VALUES
(1, 'Computador', 'Equipos de cómputo'),
(2, 'Proyector', 'Equipos de proyección'),
(3, 'Herramienta', 'Herramientas de taller'),
(4, 'Mobiliario', 'Mesas, sillas, escritorios'),
(5, 'Equipo Médico', 'Equipos para salud');

-- Poblar tabla recursos
INSERT INTO recursos (serial_recurso, num_recurso, nombre_recurso, tipo_recurso, observacion, ambiente_id) VALUES
('PC001', 1, 'Computador Dell Optiplex', 1, 'Estado: Bueno', 101),
('PC002', 2, 'Computador HP Pavilion', 1, 'Estado: Regular', 101),
('PC003', 3, 'Computador Lenovo ThinkCentre', 1, 'Estado: Bueno', 102),
('PROY001', 1, 'Proyector Epson', 2, 'Estado: Excelente', 103),
('PROY002', 2, 'Proyector Sony', 2, 'Estado: Bueno', 401),
('HERR001', 1, 'Taladro Industrial', 3, 'Estado: Bueno', 301),
('HERR002', 2, 'Soldador Eléctrico', 3, 'Estado: Regular', 302),
('MESA001', 1, 'Mesa de Trabajo', 4, 'Estado: Bueno', 301),
('SILLA001', 1, 'Silla Ergonómica', 4, 'Estado: Excelente', 101),
('MED001', 1, 'Tensiómetro Digital', 5, 'Estado: Bueno', 501);

-- Poblar tabla registro_minuta
INSERT INTO registro_minuta (fecha_hora_recibo, fecha_hora_entrega, novedad, responsable, descripcion_min, ambiente_id, Usuario_id_usuario, guarda_seguridad_Usuario_id_usuario) VALUES
('2024-06-01 07:00:00', '2024-06-01 18:00:00', 'Todo en orden', 'Carlos García', 'Entrega normal del ambiente', 101, 1, 28),
('2024-06-02 07:00:00', '2024-06-02 18:00:00', 'Proyector con fallas', 'María Rodríguez', 'Proyector presenta intermitencias', 103, 2, 28),
('2024-06-03 07:00:00', '2024-06-03 18:00:00', 'Sin novedades', 'Juan Hernández', 'Ambiente en perfecto estado', 201, 3, 29),
('2024-06-04 07:00:00', '2024-06-04 18:00:00', 'Herramienta faltante', 'Ana Jiménez', 'Falta un destornillador', 301, 4, 29),
('2024-06-05 07:00:00', '2024-06-05 18:00:00', 'Todo normal', 'Luis Vargas', 'Sin novedades que reportar', 501, 5, 28);

-- Poblar tabla registro_asistencia (múltiples registros por aprendiz)
INSERT INTO registro_asistencia (fecha_asistencia, estado_asistencia, jornada_id, aprendiz_Usuario_id_usuario) VALUES
-- Aprendiz 16
('2024-06-01', 'S', 1, 16),
('2024-06-02', 'S', 1, 16),
('2024-06-03', 'R', 1, 16),
('2024-06-04', 'S', 1, 16),
('2024-06-05', 'N', 1, 16),
-- Aprendiz 17
('2024-06-01', 'S', 1, 17),
('2024-06-02', 'S', 1, 17),
('2024-06-03', 'S', 1, 17),
('2024-06-04', 'S', 1, 17),
('2024-06-05', 'S', 1, 17),
-- Aprendiz 18
('2024-06-01', 'S', 2, 18),
('2024-06-02', 'N', 2, 18),
('2024-06-03', 'N', 2, 18),
('2024-06-04', 'R', 2, 18),
('2024-06-05', 'S', 2, 18),
-- Aprendiz 19
('2024-06-01', 'S', 1, 19),
('2024-06-02', 'S', 1, 19),
('2024-06-03', 'S', 1, 19),
('2024-06-04', 'N', 1, 19),
('2024-06-05', 'S', 1, 19),
-- Aprendiz 20
('2024-06-01', 'S', 1, 20),
('2024-06-02', 'S', 1, 20),
('2024-06-03', 'R', 1, 20),
('2024-06-04', 'S', 1, 20),
('2024-06-05', 'S', 1, 20),
-- Aprendiz 21
('2024-06-01', 'S', 3, 21),
('2024-06-02', 'S', 3, 21),
('2024-06-03', 'S', 3, 21),
('2024-06-04', 'S', 3, 21),
('2024-06-05', 'N', 3, 21),
-- Más registros para otros aprendices
('2024-06-01', 'S', 1, 22),
('2024-06-02', 'N', 1, 22),
('2024-06-03', 'N', 1, 22),
('2024-06-01', 'S', 1, 23),
('2024-06-02', 'S', 1, 23),
('2024-06-01', 'S', 2, 24),
('2024-06-02', 'R', 2, 24),
('2024-06-01', 'S', 2, 25),
('2024-06-02', 'S', 2, 25),
('2024-06-01', 'S', 1, 26),
('2024-06-02', 'N', 1, 26),
('2024-06-01', 'S', 1, 27),
('2024-06-02', 'S', 1, 27);

-- Poblar tabla alertas_inasistencia
INSERT INTO alertas_inasistencia (id_alerta, aprendiz_id, cantidad_fallas, fecha_alerta, mensaje, coordinacion_id) VALUES
(1, 16, 1, '2024-06-05 16:00:00', 'El aprendiz ha faltado 1 día esta semana', 1),
(2, 18, 2, '2024-06-04 16:00:00', 'El aprendiz ha acumulado 2 faltas consecutivas', 1),
(3, 22, 2, '2024-06-03 16:00:00', 'Alerta por inasistencias frecuentes', 3),
(4, 26, 1, '2024-06-02 16:00:00', 'Primera alerta por inasistencia', 4);

-- Verificar el conteo total de registros
SELECT 'Total de registros insertados:' as Info;
SELECT 'Usuario' as Tabla, COUNT(*) as Registros FROM Usuario
UNION ALL
SELECT 'coordinacion', COUNT(*) FROM coordinacion
UNION ALL
SELECT 'Coordinador', COUNT(*) FROM Coordinador
UNION ALL
SELECT 'jornada', COUNT(*) FROM jornada
UNION ALL
SELECT 'modalidad', COUNT(*) FROM modalidad
UNION ALL
SELECT 'programas', COUNT(*) FROM programas
UNION ALL
SELECT 'instructor', COUNT(*) FROM instructor
UNION ALL
SELECT 'guarda_seguridad', COUNT(*) FROM guarda_seguridad
UNION ALL
SELECT 'aprendiz', COUNT(*) FROM aprendiz
UNION ALL
SELECT 'rol', COUNT(*) FROM rol
UNION ALL
SELECT 'user_rol', COUNT(*) FROM user_rol
UNION ALL
SELECT 'ambiente', COUNT(*) FROM ambiente
UNION ALL
SELECT 'tipo_incidente', COUNT(*) FROM tipo_incidente
UNION ALL
SELECT 'registro_incidente', COUNT(*) FROM registro_incidente
UNION ALL
SELECT 'tipo_recurso', COUNT(*) FROM tipo_recurso
UNION ALL
SELECT 'recursos', COUNT(*) FROM recursos
UNION ALL
SELECT 'registro_minuta', COUNT(*) FROM registro_minuta
UNION ALL
SELECT 'registro_asistencia', COUNT(*) FROM registro_asistencia
UNION ALL
SELECT 'alertas_inasistencia', COUNT(*) FROM alertas_inasistencia;