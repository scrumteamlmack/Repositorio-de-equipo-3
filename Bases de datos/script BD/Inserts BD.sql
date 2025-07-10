-- Inserciones completas y corregidas para BD `mydb`

USE `mydb`;

-- ===========================
-- JORNADAS
-- ===========================
INSERT INTO `jornada` (`id_jornada`, `nombre_jornada`) VALUES
(1, 'Mañana'),
(2, 'Tarde'),
(3, 'Noche');

-- ===========================
-- MODALIDADES
-- ===========================
INSERT INTO `modalidad` (`id_modalidad`, `nombre_modalidad`) VALUES
(1, 'Presencial'),
(2, 'Virtual'),
(3, 'Mixta');

-- ===========================
-- ROLES
-- ===========================
INSERT INTO `rol` (`id_rol`, `nombre_rol`) VALUES
(1, 'Administrador'),
(2, 'Instructor'),
(3, 'Aprendiz'),
(4, 'Guarda de Seguridad');

-- ===========================
-- COORDINACIONES
-- ===========================
INSERT INTO `coordinacion` (`nombre_coordinacion`, `correo_coordinacion`) VALUES
('Coordinación de Sistemas', 'sistemas@sena.edu.co'),
('Coordinación de Diseño', 'diseno@sena.edu.co'),
('Coordinación de Logística', 'logistica@sena.edu.co'),
('Coordinación de Salud', 'salud@sena.edu.co'),
('Coordinación de Industria', 'industria@sena.edu.co');

-- ===========================
-- TIPOS DE INCIDENTE
-- ===========================
INSERT INTO `tipo_incidente` (`id_tipo_inc`, `tipo_incidente`, `observacion_inc`) VALUES
(1, 'Falla Eléctrica', 'Problemas con el suministro de energía'),
(2, 'Daño de Equipo', 'Equipo de cómputo defectuoso'),
(3, 'Problema de Conectividad', 'Fallo en la red de internet'),
(4, 'Falla de Mobiliario', 'Silla o mesa rota'),
(5, 'Emergencia Médica', 'Situación de salud de un aprendiz o instructor'),
(6, 'Problema de Seguridad', 'Incidentes relacionados con la seguridad del centro');

-- ===========================
-- TIPOS DE RECURSO
-- ===========================
INSERT INTO `tipo_recurso` (`id_tipo_recurso`, `recurso_tipo`, `descripcion_tipo`) VALUES
(1, 'Computador', 'Equipo informático de escritorio'),
(2, 'Proyector', 'Equipo para proyección de imágenes'),
(3, 'Impresora', 'Dispositivo para imprimir documentos'),
(4, 'Mobiliario', 'Sillas, mesas, armarios'),
(5, 'Herramienta', 'Herramientas manuales o eléctricas'),
(6, 'Audio/Video', 'Equipos de sonido y video'),
(7, 'Red', 'Equipos de networking y comunicaciones');

-- ===========================
-- USUARIOS
-- ===========================
INSERT INTO `usuario` (`p_nombre`, `s_nombre`, `p_apellido`, `s_apellido`, `tipo_documento`, `num_documento`, `correo`, `contraseña`) VALUES
('Carlos', 'Alberto', 'Rodriguez', 'Peña', 'CC', 1012345001, 'carlos.rodriguez@sena.edu.co', MD5('admin123')),
('Ana', 'Maria', 'Gonzalez', 'Lopez', 'CC', 1012345002, 'ana.gonzalez@sena.edu.co', MD5('admin456')),
('Juan', 'Carlos', 'Perez', 'Gomez', 'CC', 1012345003, 'juan.perez@sena.edu.co', MD5('inst123')),
('Maria', 'Alejandra', 'Lopez', 'Diaz', 'CC', 1012345004, 'maria.lopez@sena.edu.co', MD5('inst456')),
('Pedro', 'Luis', 'Martinez', 'Silva', 'CC', 1012345005, 'pedro.martinez@sena.edu.co', MD5('inst789')),
('Laura', 'Sofia', 'Torres', 'Vargas', 'CC', 1012345006, 'laura.torres@sena.edu.co', MD5('inst012')),
('Diego', 'Andres', 'Ramirez', 'Castro', 'CC', 1012345007, 'diego.ramirez@sena.edu.co', MD5('inst345')),
('Roberto', 'Antonio', 'Morales', 'Ruiz', 'CC', 1012345008, 'roberto.morales@sena.edu.co', MD5('guard123')),
('Carmen', 'Isabel', 'Jimenez', 'Herrera', 'CC', 1012345009, 'carmen.jimenez@sena.edu.co', MD5('guard456')),
('Fernando', 'Jose', 'Gutierrez', 'Sanchez', 'CC', 1012345010, 'fernando.gutierrez@sena.edu.co', MD5('guard789')),
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

-- ===========================
-- USER ROLES
-- ===========================
INSERT INTO `user_rol` (`id_usuario`, `id_rol`) VALUES
(1, 1), (2, 1),
(3, 2), (4, 2), (5, 2), (6, 2), (7, 2),
(8, 4), (9, 4), (10, 4),
(11, 3), (12, 3), (13, 3), (14, 3), (15, 3),
(16, 3), (17, 3), (18, 3), (19, 3), (20, 3);

-- ===========================
-- PROGRAMAS FORMATIVOS
-- ===========================
INSERT INTO `programas` (`id_programas`, `nombre_programa`, `nivel_formacion`, `duracion`, `jornada_id`, `modalidad_id`, `coordinacion_id`) VALUES
(2271021, 'Análisis y Desarrollo de Software', 'Tecnólogo', '24 meses', 1, 2, 1),
(2271022, 'Diseño Gráfico Publicitario', 'Técnico', '18 meses', 2, 1, 2),
(2271023, 'Gestión Logística', 'Tecnólogo', '24 meses', 3, 3, 3),
(2271024, 'Contabilidad y Finanzas', 'Tecnólogo', '24 meses', 1, 1, 1),
(2271025, 'Multimedia', 'Técnico', '18 meses', 2, 2, 2),
(2271026, 'Auxiliar en Enfermería', 'Técnico', '18 meses', 1, 1, 4),
(2271027, 'Mecánica Industrial', 'Tecnólogo', '24 meses', 2, 1, 5),
(2271028, 'Desarrollo Web', 'Especialización', '6 meses', 3, 2, 1);

-- ===========================
-- INSTRUCTORES (CORREGIDOS)
-- ===========================
INSERT INTO `instructor` (`Usuario_id_usuario`, `email`, `telefono`, `coordinacion`, `estado`) VALUES
(3, 'juan.perez@sena.edu.co', '3001112233', 'Coordinación de Sistemas', 'Activo'),
(4, 'maria.lopez@sena.edu.co', '3012223344', 'Coordinación de Diseño', 'Activo'),
(5, 'pedro.martinez@sena.edu.co', '3023334455', 'Coordinación de Logística', 'Activo'),
(6, 'laura.torres@sena.edu.co', '3034445566', 'Coordinación de Salud', 'Activo'),
(7, 'diego.ramirez@sena.edu.co', '3045556677', 'Coordinación de Industria', 'Activo');
