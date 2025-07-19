-- Script para poblar la base de datos con datos de prueba consistentes
-- Total de inserts: 50+ registros distribuidos en todas las tablas
SET SQL_SAFE_UPDATES = 0;

-- Limpiar datos existentes para evitar conflictos
DELETE FROM `mydb`.`alertas_inasistencia`;
DELETE FROM `mydb`.`registro_inasistencia`;
DELETE FROM `mydb`.`registro_minuta`;
DELETE FROM `mydb`.`recursos`;
DELETE FROM `mydb`.`registro_incidente`;
DELETE FROM `mydb`.`aprendiz`;
DELETE FROM `mydb`.`user_rol`;
DELETE FROM `mydb`.`guarda_seguridad`;
DELETE FROM `mydb`.`instructor`;
DELETE FROM `mydb`.`programas`;
DELETE FROM `mydb`.`ambiente`;
DELETE FROM `mydb`.`Usuario`;
DELETE FROM `mydb`.`coordinacion`;
DELETE FROM `mydb`.`tipo_recurso`;
DELETE FROM `mydb`.`tipo_incidente`;
DELETE FROM `mydb`.`rol`;
DELETE FROM `mydb`.`modalidad`;
DELETE FROM `mydb`.`jornada`;

-- Resetear AUTO_INCREMENT
ALTER TABLE `mydb`.`Usuario` AUTO_INCREMENT = 1;
ALTER TABLE `mydb`.`coordinacion` AUTO_INCREMENT = 1;
ALTER TABLE `mydb`.`aprendiz` AUTO_INCREMENT = 1;
ALTER TABLE `mydb`.`user_rol` AUTO_INCREMENT = 1;
ALTER TABLE `mydb`.`registro_incidente` AUTO_INCREMENT = 1;
ALTER TABLE `mydb`.`recursos` AUTO_INCREMENT = 1;
ALTER TABLE `mydb`.`registro_minuta` AUTO_INCREMENT = 1;
ALTER TABLE `mydb`.`registro_inasistencia` AUTO_INCREMENT = 1;
ALTER TABLE `mydb`.`alertas_inasistencia` AUTO_INCREMENT = 1;

-- =============================================
-- DATOS MAESTROS (Catálogos)
-- =============================================

-- Jornadas (3 registros)
INSERT INTO `mydb`.`jornada` (`id_jornada`, `nombre_jornada`) VALUES
(1, 'Mañana'),
(2, 'Tarde'),
(3, 'Noche');

-- Modalidades (3 registros)
INSERT INTO `mydb`.`modalidad` (`id_modalidad`, `nombre_modalidad`) VALUES
(1, 'Presencial'),
(2, 'Sincronica');

-- Roles (4 registros)
INSERT INTO `mydb`.`rol` (`id_rol`, `nombre_rol`) VALUES
(1, 'Administrador'),
(2, 'Instructor'),
(3, 'Aprendiz'),
(4, 'Guarda de Seguridad');

-- Coordinaciones (5 registros)
INSERT INTO `mydb`.`coordinacion` (`nombre_coordinacion`, `correo_coordinacion`) VALUES
('Coordinación de Sistemas', 'sistemas@sena.edu.co'),
('Coordinación de Diseño', 'diseno@sena.edu.co'),
('Coordinación de Logística', 'logistica@sena.edu.co'),
('Coordinación de Salud', 'salud@sena.edu.co'),
('Coordinación de Industria', 'industria@sena.edu.co');

-- Tipos de Incidente (6 registros)
INSERT INTO `mydb`.`tipo_incidente` (`id_tipo_inc`, `tipo_incidente`, `observacion_inc`) VALUES
(1, 'Falla Eléctrica', 'Problemas con el suministro de energía'),
(2, 'Daño de Equipo', 'Equipo de cómputo defectuoso'),
(3, 'Problema de Conectividad', 'Fallo en la red de internet'),
(4, 'Falla de Mobiliario', 'Silla o mesa rota'),
(5, 'Emergencia Médica', 'Situación de salud de un aprendiz o instructor'),
(6, 'Problema de Seguridad', 'Incidentes relacionados con la seguridad del centro');

-- Tipos de Recurso (7 registros)
INSERT INTO `mydb`.`tipo_recurso` (`id_tipo_recurso`, `recurso_tipo`, `descripcion_tipo`) VALUES
(1, 'Computador', 'Equipo informático de escritorio'),
(2, 'Proyector', 'Equipo para proyección de imágenes'),
(3, 'Impresora', 'Dispositivo para imprimir documentos'),
(4, 'Mobiliario', 'Sillas, mesas, armarios'),
(5, 'Herramienta', 'Herramientas manuales o eléctricas'),
(6, 'Audio/Video', 'Equipos de sonido y video'),
(7, 'Red', 'Equipos de networking y comunicaciones');

-- =============================================
-- USUARIOS (20 registros)
-- =============================================

INSERT INTO `mydb`.`Usuario` (`p_nombre`, `s_nombre`, `p_apellido`, `s_apellido`, `tipo_documento`, `num_documento`, `correo`, `contraseña`) VALUES
-- Administradores
('Carlos', 'Alberto', 'Rodriguez', 'Peña', 'CC', 1012345001, 'carlos.rodriguez@sena.edu.co', MD5('admin123')),
('Ana', 'Maria', 'Gonzalez', 'Lopez', 'CC', 1012345002, 'ana.gonzalez@sena.edu.co', MD5('admin456')),

-- Instructores
('Juan', 'Carlos', 'Perez', 'Gomez', 'CC', 1012345003, 'juan.perez@sena.edu.co', MD5('inst123')),
('Maria', 'Alejandra', 'Lopez', 'Diaz', 'CC', 1012345004, 'maria.lopez@sena.edu.co', MD5('inst456')),
('Pedro', 'Luis', 'Martinez', 'Silva', 'CC', 1012345005, 'pedro.martinez@sena.edu.co', MD5('inst789')),
('Laura', 'Sofia', 'Torres', 'Vargas', 'CC', 1012345006, 'laura.torres@sena.edu.co', MD5('inst012')),
('Diego', 'Andres', 'Ramirez', 'Castro', 'CC', 1012345007, 'diego.ramirez@sena.edu.co', MD5('inst345')),

-- Guardas de Seguridad
('Roberto', 'Antonio', 'Morales', 'Ruiz', 'CC', 1012345008, 'roberto.morales@sena.edu.co', MD5('guard123')),
('Carmen', 'Isabel', 'Jimenez', 'Herrera', 'CC', 1012345009, 'carmen.jimenez@sena.edu.co', MD5('guard456')),
('Fernando', 'Jose', 'Gutierrez', 'Sanchez', 'CC', 1012345010, 'fernando.gutierrez@sena.edu.co', MD5('guard789')),

-- Aprendices
('Alejandro', 'David', 'Moreno', 'Ortega', 'TI', 1012345011, 'alejandro.moreno@misena.edu.co', MD5('apren123')),
('Valentina', 'Camila', 'Diaz', 'Suarez', 'TI', 1012345012, 'valentina.diaz@misena.edu.co', MD5('apren456')),
('Sebastian', 'Nicolas', 'Vargas', 'Mendez', 'TI', 1012345013, 'sebastian.vargas@misena.edu.co', MD5('apren789')),
('Isabella', 'Andrea', 'Rojas', 'Pineda', 'TI', 1012345014, 'isabella.rojas@misena.edu.co', MD5('apren012')),
('Mateo', 'Alejandro', 'Castillo', 'Vega', 'TI', 1012345015, 'mateo.castillo@misena.edu.co', MD5('apren345')),
('Sophia', 'Valentina', 'Hernandez', 'Molina', 'TI', 1012345016, 'sophia.hernandez@misena.edu.co', MD5('apren678')),
('Daniel', 'Santiago', 'Florez', 'Rios', 'TI', 1012345017, 'daniel.florez@misena.edu.co', MD5('apren901')),
('Camila', 'Alejandra', 'Acosta', 'Guerrero', 'TI', 1012345018, 'camila.acosta@misena.edu.co', MD5('apren234')),
('Andres', 'Felipe', 'Ospina', 'Cardenas', 'TI', 1012345019, 'andres.ospina@misena.edu.co', MD5('apren567')),
('Mariana', 'Juliana', 'Parra', 'Aguilar', 'TI', 1012345020, 'mariana.parra@misena.edu.co', MD5('apren890'));

-- =============================================
-- ROLES DE USUARIO (20 registros)
-- =============================================

INSERT INTO `mydb`.`user_rol` (`id_usuario`, `id_rol`) VALUES
-- Administradores
(1, 1), (2, 1),
-- Instructores
(3, 2), (4, 2), (5, 2), (6, 2), (7, 2),
-- Guardas
(8, 4), (9, 4), (10, 4),
-- Aprendices
(11, 3), (12, 3), (13, 3), (14, 3), (15, 3),
(16, 3), (17, 3), (18, 3), (19, 3), (20, 3);

-- =============================================
-- PROGRAMAS FORMATIVOS (8 registros)
-- =============================================

INSERT INTO `mydb`.`programas` (`id_programas`, `nombre_programa`, `nivel_formacion`, `duracion`, `jornada_id`, `modalidad_id`, `coordinacion_id`) VALUES
(1, 'Análisis y Desarrollo de Software', 'Tecnólogo', '24 meses', 1, 2, 1),
(2, 'Diseño Gráfico Publicitario', 'Técnico', '18 meses', 2, 1, 2),
(3, 'Gestión Logística', 'Tecnólogo', '24 meses', 3, 2, 3),
(4, 'Contabilidad y Finanzas', 'Tecnólogo', '24 meses', 1, 1, 1),
(5, 'Multimedia', 'Técnico', '18 meses', 2, 2, 2),
(6, 'Auxiliar en Enfermería', 'Técnico', '18 meses', 1, 1, 4),
(7, 'Mecánica Industrial', 'Tecnólogo', '24 meses', 2, 1, 5),
(8, 'Desarrollo Web', 'Especialización', '6 meses', 3, 2, 1);

-- =============================================
-- INSTRUCTORES (5 registros corregidos)
-- =============================================
INSERT INTO `mydb`.`instructor` 
(`Usuario_id_usuario`, `email`, `telefono`, `coordinacion`, `estado`) VALUES
(3, 'juan.perez@sena.edu.co', '3001112233', 'Coordinación de Sistemas', 'Activo'),
(4, 'maria.lopez@sena.edu.co', '3012223344', 'Coordinación de Diseño', 'Activo'),
(5, 'pedro.martinez@sena.edu.co', '3023334455', 'Coordinación de Logística', 'Activo'),
(6, 'laura.torres@sena.edu.co', '3034445566', 'Coordinación de Salud', 'Activo'),
(7, 'diego.ramirez@sena.edu.co', '3045556677', 'Coordinación de Industria', 'Activo');

-- =============================================
-- GUARDAS DE SEGURIDAD (3 registros)
-- =============================================

INSERT INTO `mydb`.`guarda_seguridad` (`Usuario_id_usuario`, `turno`, `fecha_ingreso`, `estado`) VALUES
(8, 'Mañana', '2020-07-01', 'Activo'),
(9, 'Tarde', '2019-11-01', 'Activo'),
(10, 'Noche', '2021-02-15', 'Activo');

-- =============================================
-- AMBIENTES (10 registros corregidos)
-- =============================================
INSERT INTO `mydb`.`ambiente` 
(`id_ambiente`, `num_ambiente`, `capacidad`, `tipo_ambiente`, `estado`) VALUES
(101, 101, 30, 'Aula de Clases', 'Disponible'),
(102, 102, 25, 'Laboratorio de Cómputo', 'En Uso'),
(103, 103, 30, 'Aula de Clases', 'Disponible'),
(201, 201, 20, 'Taller de Diseño', 'Disponible'),
(202, 202, 18, 'Laboratorio de Multimedia', 'En Uso'),
(301, 301, 40, 'Salón Multipropósito', 'Mantenimiento'),
(401, 401, 15, 'Laboratorio de Enfermería', 'Disponible'),
(402, 402, 12, 'Consultorio de Práctica', 'Disponible'),
(501, 501, 35, 'Taller de Mecánica', 'En Uso'),
(502, 502, 25, 'Laboratorio de Soldadura', 'Disponible');

-- =============================================
-- FICHAS (7 registros)
-- =============================================
INSERT INTO `mydb`.`ficha` (`idficha`, `Num_ficha`, `instructor_Usuario_id_usuario`) VALUES
(1, 2271021, 3), -- Juan Perez
(2, 2271022, 4), -- Maria Lopez
(3, 2271023, 5), -- Pedro Martinez
(4, 2271025, 4), -- Maria Lopez
(5, 2271026, 6), -- Laura Torres
(6, 2271027, 7), -- Diego Ramirez
(7, 2271028, 3); -- Juan Perez

-- Ahora sí puedes insertar en aprendiz
INSERT INTO `mydb`.`aprendiz` (`Usuario_id_usuario`, `ficha_idficha`, `programas_id_programas`) VALUES
(11, 1, 1),
(12, 1, 1),
(13, 2, 2),
(14, 2, 2),
(15, 3, 3),
(16, 5, 5),
(17, 5, 5),
(18, 6, 6),
(19, 7, 7),
(20, 4, 4);

INSERT INTO `mydb`.`recursos` 
(`serial_recurso`, `num_recurso`, `nombre_recurso`, `tipo_recurso`, `estado`, `observacion`,`ambiente_id` )
VALUES
('PC-LAB-001', 1, 'Computador Dell OptiPlex 1', 1, 'Disponible', 'Funcionando correctamente',101),
('PC-LAB-002', 2, 'Computador Dell OptiPlex 2', 1, 'Disponible', 'Funcionando correctamente',102),
('PC-LAB-003', 3, 'Computador Dell OptiPlex 3', 1, 'Dañado', 'Necesita mantenimiento',103),
('PROY-AUL-001', 1, 'Proyector Epson PowerLite', 2, 'Disponible', 'Funcionando correctamente',201),
('PROY-AUL-002', 2, 'Proyector BenQ MS524', 2, 'En mantenimiento', 'Lámpara por cambiar',202),
('IMPR-ADM-001', 1, 'Impresora HP LaserJet Pro', 3, 'Disponible', 'Funcionando correctamente',301),
('IMPR-ADM-002', 2, 'Impresora Canon Pixma', 3, 'Dañado', 'Necesita tinta',401),
('SILLA-101-01', 1, 'Silla Ergonómica Azul', 4, 'Disponible', 'Buen estado',402),
('MESA-101-01', 1, 'Mesa de Trabajo Madera', 4, 'Disponible', 'Rayones menores',501),
('MART-501-01', 1, 'Martillo de Carpintero', 5, 'Disponible', 'Buen estado',502),
('SOLD-502-01', 1, 'Soldador Eléctrico 110V', 5, 'Disponible', 'Funcionando correctamente',101),
('SPEAK-201-01', 1, 'Parlantes Logitech Z313', 6, 'Disponible', 'Funcionando correctamente',102),
('MIC-201-01', 1, 'Micrófono Audio-Technica', 6, 'Dañado', 'Cable defectuoso',101),
('SWITCH-102-01', 1, 'Switch Cisco 24 puertos', 7, 'Disponible', 'Funcionando correctamente',102),
('ROUTER-ADM-01', 1, 'Router TP-Link AC1750', 7, 'Disponible', 'Funcionando correctamente',103);

INSERT INTO `mydb`.`registro_incidente`
(`descripcion`, `fecha_incidente`, `hora_incidente`, `ambiente_id`, `tipo_inc_id`, `usuario_id_usuario`)
VALUES
('Cortocircuito en tomacorriente principal del laboratorio', '2024-06-10', '09:30:00', 102, 1, 3),
('Monitor del computador PC-LAB-003 no enciende', '2024-06-11', '14:00:00', 102, 2, 3),
('Fallo total de internet en el taller de diseño', '2024-06-12', '10:15:00', 201, 3, 4),
('Pata de silla SILLA-101-01 presenta grieta', '2024-06-13', '11:45:00', 101, 4, 4),
('Estudiante presentó desmayo durante práctica', '2024-06-14', '08:00:00', 401, 5, 5),
('Acceso no autorizado detectado en laboratorio', '2024-06-15', '22:30:00', 102, 6, 6),
('Proyector PROY-AUL-002 se apaga automáticamente', '2024-06-16', '15:20:00', 201, 2, 7),
('Fuga de agua en el taller de soldadura', '2024-06-17', '07:45:00', 502, 1, 8);


INSERT INTO `mydb`.`registro_minuta`
(`fecha_hora_recibo`, `fecha_hora_entrega`, `novedad`, `descripcion_min`, `estado`, `ambiente_id`, `guarda_seguridad_Usuario_id_usuario`, `responsable_id`, `registro_minutacol`)
VALUES
('2024-06-15 07:00:00', '2024-06-15 13:00:00', 'Entrega normal del ambiente, sin novedades', 'Recibo y entrega de ambiente 101 sin incidentes', 'Ocupado', 101, 8, 3, NULL),
('2024-06-15 13:00:00', '2024-06-15 19:00:00', 'Se reporta problema de conectividad en diseño', 'Ambiente 201 presenta fallas de red intermitentes', 'Ocupado', 201, 9, 4, NULL),
('2024-06-15 19:00:00', '2024-06-16 07:00:00', 'Ronda nocturna sin incidentes', 'Verificación de seguridad en todos los ambientes', 'Ocupado', 102, 10, 5, NULL),
('2024-06-16 07:00:00', '2024-06-16 13:00:00', 'Reporte de equipo dañado en laboratorio', 'PC-LAB-003 presenta falla en monitor', 'Ocupado', 102, 8, 6, NULL),
('2024-06-16 13:00:00', '2024-06-16 19:00:00', 'Mantenimiento preventivo realizado', 'Limpieza y verificación de equipos en taller mecánica', 'Ocupado', 501, 9, 7, NULL),
('2024-06-17 07:00:00', '2024-06-17 13:00:00', 'Emergencia médica atendida satisfactoriamente', 'Estudiante atendido por desmayo, trasladado a enfermería', 'Ocupado', 401, 8, 3, NULL);

-- =============================================
-- REGISTRO DE INASISTENCIA (10 registros)
-- =============================================
INSERT INTO `mydb`.`registro_inasistencia` (`fecha_inasistencia`, `estado_inasistencia`, `jornada_id`, `aprendiz_Usuario_id_usuario`, `instructor_Usuario_id_usuario`) VALUES
('2024-06-10', 'S', 1, 11, 3),
('2024-06-10', 'S', 1, 12, 3),
('2024-06-10', 'R', 2, 13, 4),
('2024-06-10', 'S', 2, 14, 4),
('2024-06-10', 'S', 3, 15, 5),
('2024-06-11', 'S', 1, 11, 3),
('2024-06-11', 'R', 1, 12, 3),
('2024-06-11', 'S', 2, 13, 4),
('2024-06-11', 'S', 2, 14, 4),
('2024-06-11', 'N', 3, 15, 5);


-- =============================================
-- ALERTAS DE INASISTENCIA (3 registros)
-- =============================================

INSERT INTO `mydb`.`alertas_inasistencia` (`aprendiz_id`, `cantidad_fallas`, `fecha_alerta`, `mensaje`, `coordinacion_id`) VALUES
(3, 1, '2024-06-11 08:00:00', 'El aprendiz del programa de Diseño Gráfico ha acumulado 1 falla. Se requiere seguimiento.', 2),
(2, 1, '2024-06-12 08:00:00', 'El aprendiz del programa de Análisis y Desarrollo de Software ha acumulado 1 falla.', 1),
(5, 1, '2024-06-12 08:30:00', 'El aprendiz del programa de Gestión Logística presentó inasistencia injustificada.', 3);

-- =============================================
-- VERIFICACIÓN DE DATOS INSERTADOS
-- =============================================

-- Mostrar resumen de registros por tabla
SELECT 'Usuario' as Tabla, COUNT(*) as Total_Registros FROM `mydb`.`Usuario`
UNION ALL
SELECT 'instructor' as Tabla, COUNT(*) as Total_Registros FROM `mydb`.`instructor`
UNION ALL
SELECT 'guarda_seguridad' as Tabla, COUNT(*) as Total_Registros FROM `mydb`.`guarda_seguridad`
UNION ALL
SELECT 'programas' as Tabla, COUNT(*) as Total_Registros FROM `mydb`.`programas`
UNION ALL
SELECT 'aprendiz' as Tabla, COUNT(*) as Total_Registros FROM `mydb`.`aprendiz`
UNION ALL
SELECT 'ambiente' as Tabla, COUNT(*) as Total_Registros FROM `mydb`.`ambiente`
UNION ALL
SELECT 'recursos' as Tabla, COUNT(*) as Total_Registros FROM `mydb`.`recursos`
UNION ALL
SELECT 'registro_incidente' as Tabla, COUNT(*) as Total_Registros FROM `mydb`.`registro_incidente`
UNION ALL
SELECT 'registro_minuta' as Tabla, COUNT(*) as Total_Registros FROM `mydb`.`registro_minuta`
UNION ALL
SELECT 'registro_asistencia' as Tabla, COUNT(*) as Total_Registros FROM `mydb`.`registro_inasistencia`
UNION ALL
SELECT 'alertas_inasistencia' as Tabla, COUNT(*) as Total_Registros FROM `mydb`.`alertas_inasistencia`
UNION ALL
SELECT 'user_rol' as Tabla, COUNT(*) as Total_Registros FROM `mydb`.`user_rol`;