-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-03-2026 a las 22:02:14
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `mydb`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alertas_inasistencia`
--

CREATE TABLE `alertas_inasistencia` (
  `id_alerta` int(11) NOT NULL COMMENT 'Clave primaria. Identificador de la alerta.\n',
  `aprendiz_id` int(11) NOT NULL COMMENT 'Llave foránea. Relaciona la alerta con el aprendiz.\n',
  `cantidad_fallas` int(11) NOT NULL COMMENT 'Número acumulado de inasistencias.\n',
  `fecha_alerta` datetime NOT NULL COMMENT 'Fecha de generación de la alerta.\n',
  `mensaje` text NOT NULL COMMENT 'Descripción o detalle de la alerta.\n',
  `coordinacion_id` int(11) NOT NULL COMMENT 'Coordinación que recibe o emite la alerta.\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `alertas_inasistencia`
--

INSERT INTO `alertas_inasistencia` (`id_alerta`, `aprendiz_id`, `cantidad_fallas`, `fecha_alerta`, `mensaje`, `coordinacion_id`) VALUES
(1, 3, 1, '2024-06-11 08:00:00', 'El aprendiz del programa de Diseño Gráfico ha acumulado 1 falla. Se requiere seguimiento.', 2),
(2, 2, 1, '2024-06-12 08:00:00', 'El aprendiz del programa de Análisis y Desarrollo de Software ha acumulado 1 falla.', 1),
(3, 5, 1, '2024-06-12 08:30:00', 'El aprendiz del programa de Gestión Logística presentó inasistencia injustificada.', 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ambiente`
--

CREATE TABLE `ambiente` (
  `id_ambiente` int(11) NOT NULL COMMENT 'Clave primaria. Identificador único del ambiente.\n',
  `num_ambiente` smallint(6) NOT NULL COMMENT 'Número o código del ambiente físico.\n',
  `capacidad` smallint(6) NOT NULL COMMENT 'Cantidad de personas que pueden estar en un ambiente',
  `tipo_ambiente` varchar(45) NOT NULL COMMENT 'Tipo del ambiente (auditorio, sala, aula, etc.).\n',
  `estado` varchar(30) NOT NULL COMMENT 'Estado actual del ambiente (disponible, ocupado, mantenimiento, etc.).\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `ambiente`
--

INSERT INTO `ambiente` (`id_ambiente`, `num_ambiente`, `capacidad`, `tipo_ambiente`, `estado`) VALUES
(1, 101, 30, 'Aula de Clases', 'Disponible'),
(2, 102, 25, 'Laboratorio de Cómputo', 'En Uso'),
(3, 103, 30, 'Aula de Clases', 'Disponible'),
(4, 201, 20, 'Taller de Diseño', 'Disponible'),
(5, 202, 18, 'Laboratorio de Multimedia', 'En Uso'),
(6, 301, 40, 'Salón Multipropósito', 'Mantenimiento'),
(7, 401, 15, 'Laboratorio de Enfermería', 'Disponible'),
(8, 402, 12, 'Consultorio de Práctica', 'Disponible'),
(9, 501, 35, 'Taller de Mecánica', 'En Uso'),
(10, 502, 25, 'Laboratorio de Soldadura De maquinas', 'Ocupado');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `aprendiz`
--

CREATE TABLE `aprendiz` (
  `Usuario_id_usuario` int(11) NOT NULL COMMENT 'Llave primaria y foránea. Identificador del aprendiz (usuario base).\n',
  `programas_id_programas` int(11) NOT NULL COMMENT 'Llave foránea. Programa de formación del aprendiz.\n',
  `ficha_idficha` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `aprendiz`
--

INSERT INTO `aprendiz` (`Usuario_id_usuario`, `programas_id_programas`, `ficha_idficha`) VALUES
(11, 1, 1),
(12, 1, 1),
(13, 2, 2),
(14, 2, 2),
(15, 3, 3),
(16, 5, 5),
(17, 5, 5),
(18, 6, 6),
(19, 7, 7),
(20, 4, 4),
(42, 1, 7),
(45, 1, 8),
(52, 1, 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `coordinacion`
--

CREATE TABLE `coordinacion` (
  `id_coordinacion` int(11) NOT NULL COMMENT 'Clave primaria. Identificador de la coordinación.\n',
  `nombre_coordinacion` varchar(45) NOT NULL COMMENT 'Nombre de la coordinación (ej. tecnologia e innovacion).\n',
  `correo_coordinacion` varchar(30) NOT NULL COMMENT 'Correo electrónico institucional de la coordinación.\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `coordinacion`
--

INSERT INTO `coordinacion` (`id_coordinacion`, `nombre_coordinacion`, `correo_coordinacion`) VALUES
(1, 'Coordinación de Sistemas', 'sistemas@sena.edu.co'),
(2, 'Coordinación de Diseño', 'diseno@sena.edu.co'),
(3, 'Coordinación de Logística', 'logistica@sena.edu.co'),
(4, 'Coordinación de Salud', 'salud@sena.edu.co'),
(5, 'Coordinación de Industria', 'industria@sena.edu.co');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `coordinador`
--

CREATE TABLE `coordinador` (
  `Usuario_id_usuario` int(11) NOT NULL COMMENT 'Llave primaria y foránea. Usuario que tiene el rol de coordinador.\n',
  `coordinacion_id_coordinacion` int(11) NOT NULL COMMENT 'Llave foránea. Relaciona con la coordinación que lidera.\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `coordinador`
--

INSERT INTO `coordinador` (`Usuario_id_usuario`, `coordinacion_id_coordinacion`) VALUES
(43, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ficha`
--

CREATE TABLE `ficha` (
  `idficha` int(11) NOT NULL,
  `Num_ficha` mediumint(9) NOT NULL,
  `instructor_Usuario_id_usuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `ficha`
--

INSERT INTO `ficha` (`idficha`, `Num_ficha`, `instructor_Usuario_id_usuario`) VALUES
(1, 2271021, 3),
(2, 2271022, 4),
(3, 2271023, 5),
(4, 2271025, 4),
(5, 2271026, 6),
(6, 2271027, 7),
(7, 2271028, 3),
(8, 3197815, 44),
(9, 3197833, 44),
(10, 3197814, 44);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `guarda_seguridad`
--

CREATE TABLE `guarda_seguridad` (
  `Usuario_id_usuario` int(11) NOT NULL COMMENT 'Clave primaria y foránea. Usuario que cumple funciones de guarda de seguridad.\n',
  `turno` enum('Mañana','Tarde','Noche') NOT NULL COMMENT 'Turno del guarda(ej. mañana, tarde, etc...)',
  `fecha_ingreso` date NOT NULL COMMENT 'Fecha de ingreso laboral del guarda.\n',
  `estado` enum('Activo','Inactivo') NOT NULL COMMENT 'Estado laboral del guarda (activo/inactivo).\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `guarda_seguridad`
--

INSERT INTO `guarda_seguridad` (`Usuario_id_usuario`, `turno`, `fecha_ingreso`, `estado`) VALUES
(8, 'Mañana', '2020-07-01', 'Activo'),
(9, 'Tarde', '2019-11-01', 'Activo'),
(10, 'Noche', '2021-02-15', 'Activo'),
(46, 'Mañana', '2025-12-10', 'Activo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historico_incidentes`
--

CREATE TABLE `historico_incidentes` (
  `id_historico` int(11) NOT NULL COMMENT 'Clave primaria. Identificador del historial del incidente.\n',
  `incidente_id` int(11) NOT NULL COMMENT 'Llave foránea. Incidente asociado.\n',
  `ambiente_id` int(11) NOT NULL COMMENT 'Llave foránea. Ambiente en donde ocurrió.\n',
  `tipo_incidente_id` int(11) NOT NULL COMMENT 'Llave foránea. Tipo de incidente registrado.\n',
  `descripcion` text DEFAULT NULL COMMENT 'Descripción de los hechos o seguimiento.\n',
  `fecha_registro` datetime NOT NULL COMMENT 'Fecha del registro en el historial.\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `instructor`
--

CREATE TABLE `instructor` (
  `Usuario_id_usuario` int(11) NOT NULL COMMENT 'Llave primaria y foránea. Usuario que actúa como instructor.\n',
  `email` varchar(100) NOT NULL COMMENT 'Correo electrónico institucional.\n',
  `telefono` varchar(20) NOT NULL COMMENT 'Teléfono de contacto.\n',
  `coordinacion_id_coordinacion` int(11) NOT NULL COMMENT 'Coordinación a la que pertenece.\n',
  `estado` enum('Activo','Inactivo') NOT NULL COMMENT 'Estado laboral (activo, inactivo).\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `instructor`
--

INSERT INTO `instructor` (`Usuario_id_usuario`, `email`, `telefono`, `coordinacion_id_coordinacion`, `estado`) VALUES
(3, 'juan.perez@sena.edu.co', '3001112233', 1, 'Activo'),
(4, 'maria.lopez@sena.edu.co', '3012223344', 2, 'Activo'),
(5, 'pedro.martinez@sena.edu.co', '3023334455', 3, 'Activo'),
(6, 'laura.torres@sena.edu.co', '3034445566', 4, 'Activo'),
(7, 'diego.ramirez@sena.edu.co', '3045556677', 5, 'Activo'),
(44, 'instructor@gmail.com', '3232302010', 1, 'Activo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jornada`
--

CREATE TABLE `jornada` (
  `id_jornada` int(11) NOT NULL COMMENT 'Clave primaria. Identificador de la jornada.\n',
  `nombre_jornada` enum('Mañana','Tarde','Noche','Madrugada') NOT NULL COMMENT 'Nombre de la jornada (mañana, tarde, noche).\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `jornada`
--

INSERT INTO `jornada` (`id_jornada`, `nombre_jornada`) VALUES
(1, 'Mañana'),
(2, 'Tarde'),
(3, 'Noche');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `modalidad`
--

CREATE TABLE `modalidad` (
  `id_modalidad` int(11) NOT NULL COMMENT 'Clave primaria. Identificador de la modalidad.\n',
  `nombre_modalidad` enum('Presencial','sincronica') NOT NULL COMMENT 'Nombre de la modalidad (presencial, sincronica).\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `modalidad`
--

INSERT INTO `modalidad` (`id_modalidad`, `nombre_modalidad`) VALUES
(1, 'Presencial'),
(2, 'sincronica');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `programas`
--

CREATE TABLE `programas` (
  `id_programas` int(11) NOT NULL COMMENT 'Clave primaria. Identificador del programa académico.\n',
  `nombre_programa` varchar(50) NOT NULL COMMENT 'Nombre del programa.\n',
  `nivel_formacion` varchar(30) NOT NULL COMMENT 'Nivel de formación (tecnólogo, técnico, etc.).\n',
  `duracion` varchar(50) NOT NULL COMMENT 'Duración estimada del programa.\n',
  `jornada_id` int(11) NOT NULL COMMENT 'Jornada asignada.\n',
  `modalidad_id` int(11) NOT NULL COMMENT 'Modalidad del programa.\n',
  `coordinacion_id` int(11) NOT NULL COMMENT 'Coordinación responsable del programa.\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `programas`
--

INSERT INTO `programas` (`id_programas`, `nombre_programa`, `nivel_formacion`, `duracion`, `jornada_id`, `modalidad_id`, `coordinacion_id`) VALUES
(1, 'Análisis y Desarrollo de Software', 'Tecnólogo', '24 meses', 1, 2, 1),
(2, 'Diseño Gráfico Publicitario', 'Técnico', '18 meses', 2, 1, 2),
(3, 'Gestión Logística', 'Tecnólogo', '24 meses', 3, 2, 3),
(4, 'Contabilidad y Finanzas', 'Tecnólogo', '24 meses', 1, 1, 1),
(5, 'Multimedia', 'Técnico', '18 meses', 2, 2, 2),
(6, 'Auxiliar en Enfermería', 'Técnico', '18 meses', 1, 1, 4),
(7, 'Mecánica Industrial', 'Tecnólogo', '24 meses', 2, 1, 5),
(8, 'Desarrollo Web', 'Especialización', '6 meses', 3, 2, 1),
(9, 'Gastronomia', 'Tecnico', '12 meses', 1, 1, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recursos`
--

CREATE TABLE `recursos` (
  `id_recurso` int(11) NOT NULL COMMENT 'Clave primaria. Identificador del recurso.\n',
  `serial_recurso` varchar(100) NOT NULL COMMENT 'Serial físico o interno del recurso.\n',
  `num_recurso` tinyint(4) NOT NULL COMMENT 'Nombre del recurso en el ambiente.\n',
  `nombre_recurso` varchar(60) NOT NULL COMMENT 'Nombre del recurso.\n',
  `tipo_recurso` int(11) NOT NULL COMMENT 'Llave foránea. Tipo de recurso.\n',
  `estado` enum('Disponible','En mantenimiento','Dañado') DEFAULT NULL COMMENT 'Estado del recurso (operativo, dañado, en mantenimiento).\n',
  `observacion` text DEFAULT NULL COMMENT 'Observacion hacia algun recurso.',
  `ambiente_id` int(11) NOT NULL COMMENT 'Llave foranea, Ambiente al que pertenece.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `recursos`
--

INSERT INTO `recursos` (`id_recurso`, `serial_recurso`, `num_recurso`, `nombre_recurso`, `tipo_recurso`, `estado`, `observacion`, `ambiente_id`) VALUES
(1, 'PC-LAB-001', 1, 'Computador Dell OptiPlex 1', 1, 'Disponible', 'Funcionando correctamente', 2),
(2, 'PC-LAB-002', 2, 'Computador Dell OptiPlex 2', 1, 'Disponible', 'Funcionando correctamente', 3),
(3, 'PC-LAB-003', 3, 'Computador Dell OptiPlex 3', 1, 'Dañado', 'Necesita mantenimiento', 3),
(4, 'PROY-AUL-001', 1, 'Proyector Epson PowerLite', 2, 'Disponible', 'Funcionando correctamente', 3),
(5, 'PROY-AUL-002', 2, 'Proyector BenL MS524', 2, 'En mantenimiento', 'Toma por cambiar', 5),
(6, 'IMPR-ADM-001', 1, 'Impresora HP LaserJet Pro', 3, 'Disponible', 'Funcionando correctamente', 7),
(7, 'IMPR-ADM-002', 2, 'Impresora Canon Pixma', 3, 'Dañado', 'Necesita tinta', 7),
(8, 'SILLA-101-01', 1, 'Silla Ergonómica Azul', 4, 'Disponible', 'Buen estado', 8),
(9, 'MESA-101-01', 1, 'Mesa de Trabajo Madera', 4, 'Disponible', 'Rayones menores', 9),
(10, 'MART-501-01', 1, 'Martillo de Carpintero', 5, 'Disponible', 'Buen estado', 10),
(11, 'SOLD-502-01', 1, 'Soldador Eléctrico 110V', 5, 'Disponible', 'Funcionando correctamente', 1),
(12, 'SPEAK-201-01', 1, 'Parlantes Logitech Z313', 6, 'Disponible', 'Funcionando correctamente', 2),
(13, 'MIC-201-01', 1, 'Micrófono Audio-Technica', 6, 'Dañado', 'Cable defectuoso', 1),
(14, 'SWITCH-102-01', 1, 'Switch Cisco 24 puertos', 7, 'Disponible', 'Funcionando correctamente', 2),
(15, 'ROUTER-ADM-01', 1, 'Router TP-Link AC1750', 7, 'Disponible', 'Funcionando correctamente', 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registro_inasistencia`
--

CREATE TABLE `registro_inasistencia` (
  `id_inasistencia` int(11) NOT NULL COMMENT 'Clave primaria. Identificador del registro.\n',
  `fecha_inasistencia` date NOT NULL COMMENT 'Fecha del registro de asistencia.\n',
  `estado_inasistencia` enum('S','R','N') NOT NULL COMMENT 'Estado: S (asistió), R (retraso), N (no asistio).\n',
  `jornada_id` int(11) NOT NULL COMMENT 'Jornada del aprendiz.\n',
  `aprendiz_Usuario_id_usuario` int(11) NOT NULL COMMENT 'Llave foránea al aprendiz.\n\n',
  `instructor_Usuario_id_usuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `registro_inasistencia`
--

INSERT INTO `registro_inasistencia` (`id_inasistencia`, `fecha_inasistencia`, `estado_inasistencia`, `jornada_id`, `aprendiz_Usuario_id_usuario`, `instructor_Usuario_id_usuario`) VALUES
(1, '2024-06-10', 'S', 1, 11, 3),
(2, '2024-06-10', 'S', 1, 12, 3),
(3, '2024-06-10', 'R', 2, 13, 4),
(4, '2024-06-10', 'S', 2, 14, 4),
(5, '2024-06-10', 'S', 3, 15, 5),
(6, '2024-06-11', 'S', 1, 11, 3),
(7, '2024-06-11', 'R', 1, 12, 3),
(8, '2024-06-11', 'S', 2, 13, 4),
(9, '2024-06-11', 'S', 2, 14, 4),
(10, '2024-06-11', 'N', 3, 15, 5),
(12, '2025-12-10', 'N', 2, 45, 44);

--
-- Disparadores `registro_inasistencia`
--
DELIMITER $$
CREATE TRIGGER `contar_inasistencias_aprendiz` AFTER INSERT ON `registro_inasistencia` FOR EACH ROW BEGIN
    DECLARE total_fallas INT;
    DECLARE coordinacion_aprendiz INT;

    -- Contar inasistencias ('N')
    SELECT COUNT(*) INTO total_fallas
    FROM registro_inasistencia
    WHERE aprendiz_Usuario_id_usuario = NEW.aprendiz_Usuario_id_usuario
      AND estado_inasistencia = 'N';

    -- Obtener la coordinación del aprendiz
    SELECT p.coordinacion_id INTO coordinacion_aprendiz
    FROM aprendiz a
    INNER JOIN programas p 
        ON a.programas_id_programas = p.id_programas
    WHERE a.Usuario_id_usuario = NEW.aprendiz_Usuario_id_usuario;

    -- Si tiene 5 o más fallas, generar o actualizar alerta
    IF total_fallas >= 5 THEN
        
        IF EXISTS (
            SELECT 1 FROM alertas_inasistencia
            WHERE aprendiz_id = NEW.aprendiz_Usuario_id_usuario
        ) THEN
            UPDATE alertas_inasistencia
            SET cantidad_fallas = total_fallas,
                fecha_alerta = NOW(),
                mensaje = CONCAT(
                    '⚠️ ALERTA CRÍTICA: El aprendiz con ID ',
                    NEW.aprendiz_Usuario_id_usuario,
                    ' tiene ',
                    total_fallas,
                    ' inasistencias. Se debe iniciar proceso de deserción académica.'
                )
            WHERE aprendiz_id = NEW.aprendiz_Usuario_id_usuario;

        ELSE
            INSERT INTO alertas_inasistencia (
                aprendiz_id,
                cantidad_fallas,
                fecha_alerta,
                mensaje,
                coordinacion_id
            )
            VALUES (
                NEW.aprendiz_Usuario_id_usuario,
                total_fallas,
                NOW(),
                CONCAT(
                    '⚠️ ALERTA CRÍTICA: El aprendiz con ID ',
                    NEW.aprendiz_Usuario_id_usuario,
                    ' tiene ',
                    total_fallas,
                    ' inasistencias. Se debe iniciar proceso de deserción académica.'
                ),
                IFNULL(coordinacion_aprendiz, 1)
            );
        END IF;

    END IF;

END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registro_incidente`
--

CREATE TABLE `registro_incidente` (
  `id_incidente` int(11) NOT NULL COMMENT 'Clave primaria. Identificador del incidente.\n\n',
  `descripcion` text DEFAULT NULL COMMENT 'Descripción general del incidente.\n',
  `fecha_incidente` date NOT NULL COMMENT 'Fecha en que ocurrió.\n',
  `hora_incidente` time NOT NULL COMMENT 'Hora en que ocurrio.',
  `ambiente_id` int(11) NOT NULL COMMENT 'Ambiente donde sucedió.\n',
  `tipo_inc_id` int(11) NOT NULL COMMENT 'Tipo de incidente.\n',
  `usuario_id_usuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `registro_incidente`
--

INSERT INTO `registro_incidente` (`id_incidente`, `descripcion`, `fecha_incidente`, `hora_incidente`, `ambiente_id`, `tipo_inc_id`, `usuario_id_usuario`) VALUES
(1, 'Cortocircuito en tomacorriente principal del laboratorio', '2024-06-10', '09:30:00', 2, 1, 3),
(2, 'Monitor del computador PC-LAB-003 no enciende', '2024-06-11', '14:00:00', 2, 2, 3),
(3, 'Fallo total de internet en el taller de diseño', '2024-06-12', '10:15:00', 4, 3, 4),
(4, 'Pata de silla SILLA-101-01 presenta grieta', '2024-06-13', '11:45:00', 1, 4, 4),
(5, 'Estudiante presentó desmayo durante práctica', '2024-06-14', '08:00:00', 7, 5, 5),
(6, 'Acceso no autorizado detectado en laboratorio', '2024-06-15', '22:30:00', 2, 6, 6),
(7, 'Proyector PROY-AUL-002 se apaga automáticamente', '2024-06-16', '15:20:00', 4, 2, 7),
(8, 'Fuga de agua en el taller de soldadura', '2024-06-17', '07:45:00', 10, 1, 8);

--
-- Disparadores `registro_incidente`
--
DELIMITER $$
CREATE TRIGGER `responsable_registro_incidente` AFTER INSERT ON `registro_incidente` FOR EACH ROW BEGIN
    INSERT INTO historico_incidentes (
        incidente_id,
        ambiente_id,
        tipo_incidente_id,
        descripcion,
        fecha_registro
    )
    VALUES (
        NEW.id_incidente,
        NEW.ambiente_id,
        NEW.tipo_inc_id,
        NEW.descripcion,
        NOW()
    );
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registro_minuta`
--

CREATE TABLE `registro_minuta` (
  `id_minuta` int(11) NOT NULL COMMENT 'Clave primaria. Identificador del registro de minuta.\n',
  `fecha_hora_recibo` datetime NOT NULL COMMENT 'Fecha y hora de recibo del ambiente.\n',
  `fecha_hora_entrega` datetime NOT NULL COMMENT 'Fecha y hora de entrega.\n',
  `novedad` text DEFAULT NULL COMMENT 'Novedad o eventualidad ocurrida.\n',
  `descripcion_min` text DEFAULT NULL COMMENT 'Observaciones generales.\n',
  `estado` text NOT NULL COMMENT 'Estado general del ambiente al momento.\n',
  `ambiente_id` int(11) NOT NULL COMMENT 'Ambiente relacionado.\n',
  `guarda_seguridad_Usuario_id_usuario` int(11) NOT NULL COMMENT 'Guarda que recibió o entregó.\n\n',
  `responsable_id` int(11) NOT NULL,
  `registro_minutacol` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `registro_minuta`
--

INSERT INTO `registro_minuta` (`id_minuta`, `fecha_hora_recibo`, `fecha_hora_entrega`, `novedad`, `descripcion_min`, `estado`, `ambiente_id`, `guarda_seguridad_Usuario_id_usuario`, `responsable_id`, `registro_minutacol`) VALUES
(1, '2024-06-15 07:00:00', '2024-06-15 13:00:00', 'Entrega normal del ambiente, sin novedades', 'Recibo y entrega de ambiente 101 sin incidentes', 'Disponible', 1, 8, 3, NULL),
(2, '2024-06-15 13:00:00', '2024-06-15 19:00:00', 'Se reporta problema de conectividad en diseño', 'Ambiente 201 presenta fallas de red intermitentes', 'Disponible', 4, 9, 4, NULL),
(3, '2024-06-15 19:00:00', '2024-06-16 07:00:00', 'Ronda nocturna sin incidentes', 'Verificación de seguridad en todos los ambientes', 'Disponible', 2, 10, 5, NULL),
(4, '2024-06-16 07:00:00', '2024-06-16 13:00:00', 'Reporte de equipo dañado en laboratorio', 'PC-LAB-003 presenta falla en monitor', 'Disponible', 2, 8, 6, NULL),
(5, '2024-06-16 13:00:00', '2024-06-16 19:00:00', 'Mantenimiento preventivo realizado', 'Limpieza y verificación de equipos en taller mecánica', 'Disponible', 9, 9, 7, NULL),
(6, '2024-06-17 07:00:00', '2024-06-17 13:00:00', 'Emergencia médica atendida satisfactoriamente', 'Estudiante atendido por desmayo, trasladado a enfermería', 'Disponible', 7, 8, 3, NULL),
(8, '2025-12-11 21:44:00', '2025-12-11 21:46:00', 'Ninguna', 'Todo correcto', 'Disponible', 1, 46, 44, NULL),
(9, '2025-12-11 21:48:00', '2025-12-11 21:50:00', 'Todo bien', 'Normal', 'Disponible', 1, 46, 44, NULL),
(10, '2025-12-12 02:43:00', '2025-12-12 02:45:00', 'Nada nuevo\r\n', 'Todo correcto', 'Disponible', 1, 46, 44, NULL),
(13, '2025-12-12 11:51:00', '2025-12-12 11:54:00', 'Nuevo ambiente ', 'apertura de ambiente', 'Disponible', 3, 46, 7, NULL);

--
-- Disparadores `registro_minuta`
--
DELIMITER $$
CREATE TRIGGER `tr_asignar_estado_minuta` BEFORE INSERT ON `registro_minuta` FOR EACH ROW BEGIN
    IF NEW.fecha_hora_entrega > NOW() THEN
        SET NEW.estado = 'Ocupado';
    ELSE
        SET NEW.estado = 'Disponible';
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `id_rol` int(11) NOT NULL COMMENT 'Clave primaria. Identificador del rol.\n',
  `nombre_rol` varchar(45) NOT NULL COMMENT 'Nombre del rol (aprendiz, instructor, etc.).\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`id_rol`, `nombre_rol`) VALUES
(1, 'Administrador'),
(2, 'Instructor'),
(3, 'Aprendiz'),
(4, 'Guarda de Seguridad');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_incidente`
--

CREATE TABLE `tipo_incidente` (
  `id_tipo_inc` int(11) NOT NULL COMMENT 'Clave primaria. Identificador del tipo de incidente.\n',
  `tipo_incidente` varchar(45) NOT NULL COMMENT 'Nombre del tipo.\n',
  `observacion_inc` text NOT NULL COMMENT 'Observación adicional.\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipo_incidente`
--

INSERT INTO `tipo_incidente` (`id_tipo_inc`, `tipo_incidente`, `observacion_inc`) VALUES
(1, 'Falla Eléctrica', 'Problemas con el suministro de energía'),
(2, 'Daño de Equipo', 'Equipo de cómputo defectuoso'),
(3, 'Problema de Conectividad', 'Fallo en la red de internet'),
(4, 'Falla de Mobiliario', 'Silla o mesa rota'),
(5, 'Emergencia Médica', 'Situación de salud de un aprendiz o instructor'),
(6, 'Problema de Seguridad', 'Incidentes relacionados con la seguridad del centro');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_recurso`
--

CREATE TABLE `tipo_recurso` (
  `id_tipo_recurso` int(11) NOT NULL COMMENT 'Clave primaria. Identificador del tipo de recurso.\n',
  `recurso_tipo` varchar(45) NOT NULL COMMENT 'Nombre del tipo (ej. PC, proyector, aire, etc.).\n',
  `descripcion_tipo` varchar(60) DEFAULT NULL COMMENT 'Descripción adicional.\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipo_recurso`
--

INSERT INTO `tipo_recurso` (`id_tipo_recurso`, `recurso_tipo`, `descripcion_tipo`) VALUES
(1, 'Computador', 'Equipo informático de escritorio'),
(2, 'Proyector', 'Equipo para proyección de imágenes'),
(3, 'Impresora', 'Dispositivo para imprimir documentos'),
(4, 'Mobiliario', 'Sillas, mesas, armarios'),
(5, 'Herramienta', 'Herramientas manuales o eléctricas'),
(6, 'Audio/Video', 'Equipos de sonido y video'),
(7, 'Red', 'Equipos de networking y comunicaciones');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `traslado_recurso`
--

CREATE TABLE `traslado_recurso` (
  `id_traslado` int(11) NOT NULL COMMENT 'Clave primaria. Identificador del traslado.\n',
  `recurso_id` int(11) NOT NULL COMMENT 'Recurso trasladado.\n',
  `ambiente_origen` int(11) NOT NULL COMMENT 'Ambiente de origen.\n',
  `ambiente_destino` int(11) NOT NULL COMMENT 'Ambiente de destino.\n',
  `fecha_traslado` datetime NOT NULL COMMENT 'Fecha del traslado.\n',
  `observacion` text DEFAULT NULL COMMENT 'Observaciones del traslado.\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `traslado_recurso`
--

INSERT INTO `traslado_recurso` (`id_traslado`, `recurso_id`, `ambiente_origen`, `ambiente_destino`, `fecha_traslado`, `observacion`) VALUES
(2, 1, 1, 2, '2025-12-12 03:06:00', 'Se presta temporalmente'),
(6, 4, 2, 3, '2025-12-12 03:32:00', 'Se presta el proyector dos'),
(8, 6, 4, 7, '2025-12-12 03:39:00', 'Se devuelve impresora');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user_rol`
--

CREATE TABLE `user_rol` (
  `id_user_rol` int(11) NOT NULL COMMENT 'Clave primaria. Identificador del registro.\n',
  `id_usuario` int(11) NOT NULL COMMENT 'Usuario asociado.\n',
  `id_rol` int(11) NOT NULL COMMENT 'Rol asignado al usuario.\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `user_rol`
--

INSERT INTO `user_rol` (`id_user_rol`, `id_usuario`, `id_rol`) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 2),
(4, 4, 2),
(5, 5, 2),
(6, 6, 2),
(7, 7, 2),
(8, 8, 4),
(9, 9, 4),
(10, 10, 4),
(11, 11, 3),
(12, 12, 3),
(13, 13, 3),
(14, 14, 3),
(15, 15, 3),
(16, 16, 3),
(17, 17, 3),
(18, 18, 3),
(19, 19, 3),
(20, 20, 3),
(21, 21, 1),
(42, 42, 3),
(43, 43, 1),
(44, 44, 2),
(45, 45, 3),
(46, 46, 4),
(52, 52, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` int(11) NOT NULL COMMENT 'Clave primaria. Identificador del usuario.\n',
  `p_nombre` varchar(50) NOT NULL COMMENT 'Primer nombre.\n',
  `s_nombre` varchar(50) DEFAULT NULL COMMENT 'Segundo nombre.\n',
  `p_apellido` varchar(45) NOT NULL COMMENT 'Primer apellido.\n',
  `s_apellido` varchar(45) DEFAULT NULL COMMENT 'Segundo apellido.\n',
  `tipo_documento` enum('CC','TI','CE','OTRO') NOT NULL COMMENT 'Tipo de documento.\n',
  `num_documento` int(11) NOT NULL COMMENT 'Número de documento.\n',
  `correo` varchar(100) NOT NULL COMMENT 'Correo institucional.\n',
  `contraseña` varchar(100) NOT NULL COMMENT 'Contraseña cifrada.\n\n'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `p_nombre`, `s_nombre`, `p_apellido`, `s_apellido`, `tipo_documento`, `num_documento`, `correo`, `contraseña`) VALUES
(1, 'Carlos', 'Alberto', 'Rodriguez', 'Peña', 'CC', 1012345001, 'carlos.rodriguez@sena.edu.co', '0192023a7bbd73250516f069df18b500'),
(2, 'Ana', 'Maria', 'Gonzalez', 'Lopez', 'CC', 1012345002, 'ana.gonzalez@sena.edu.co', '1a145a23d6e47aadfe2063f1f951e691'),
(3, 'Juan', 'Carlos', 'Perez', 'Gomez', 'CC', 1012345003, 'juan.perez@sena.edu.co', '04b918c442e65393a51bbc5053182e94'),
(4, 'Maria', 'Alejandra', 'Lopez', 'Diaz', 'CC', 1012345004, 'maria.lopez@sena.edu.co', '9564628d490f9ad17f45191e01cd7aae'),
(5, 'Pedro', 'Luis', 'Martinez', 'Silva', 'CC', 1012345005, 'pedro.martinez@sena.edu.co', 'b6de54b24ff4234c4f8226aaee5279a7'),
(6, 'Laura', 'Sofia', 'Torres', 'Vargas', 'CC', 1012345006, 'laura.torres@sena.edu.co', 'd217ffa64094a64f26dae45343d576d5'),
(7, 'Diego', 'Andres', 'Ramirez', 'Castro', 'CC', 1012345007, 'diego.ramirez@sena.edu.co', '22d90ef7d0e27f194ced2add1ede92d9'),
(8, 'Roberto', 'Antonio', 'Morales', 'Ruiz', 'CC', 1012345008, 'roberto.morales@sena.edu.co', '8dc137e098b97d39ba9915f9e7a45e57'),
(9, 'Carmen', 'Isabel', 'Jimenez', 'Herrera', 'CC', 1012345009, 'carmen.jimenez@sena.edu.co', 'b676728cb9ca01df59767ec700ef2e34'),
(10, 'Fernando', 'Jose', 'Gutierrez', 'Sanchez', 'CC', 1012345010, 'fernando.gutierrez@sena.edu.co', 'c32bc6d9da419ec0efafac778b2d29f9'),
(11, 'Alejandro', 'David', 'Moreno', 'Ortega', 'TI', 1012345011, 'alejandro.moreno@misena.edu.co', '0ad573be49c0c011c304fdb2970c4459'),
(12, 'Valentina', 'Camila', 'Diaz', 'Suarez', 'TI', 1012345012, 'valentina.diaz@misena.edu.co', '7a4e1484b9ad8b2f796b6db886f180a5'),
(13, 'Sebastian', 'Nicolas', 'Vargas', 'Mendez', 'TI', 1012345013, 'sebastian.vargas@misena.edu.co', 'b33cee39b81423da28a67ded123e181b'),
(14, 'Isabella', 'Andrea', 'Rojas', 'Pineda', 'TI', 1012345014, 'isabella.rojas@misena.edu.co', '37adf989e70a814429b0495773f87f26'),
(15, 'Mateo', 'Alejandro', 'Castillo', 'Vega', 'TI', 1012345015, 'mateo.castillo@misena.edu.co', 'd94095750f31d48813e82913e8209511'),
(16, 'Sophia', 'Valentina', 'Hernandez', 'Molina', 'TI', 1012345016, 'sophia.hernandez@misena.edu.co', 'a1c75410f4497cc90e499aa1640a6111'),
(17, 'Daniel', 'Santiago', 'Florez', 'Rios', 'TI', 1012345017, 'daniel.florez@misena.edu.co', '15122e8adf18b1a3ea40838034fb7718'),
(18, 'Camila', 'Alejandra', 'Acosta', 'Guerrero', 'TI', 1012345018, 'camila.acosta@misena.edu.co', '2803413a0bec8c4c8a70831be6c0b94c'),
(19, 'Andres', 'Felipe', 'Ospina', 'Cardenas', 'TI', 1012345019, 'andres.ospina@misena.edu.co', 'ab53cd4138c6cc21c856de4360c2bd1f'),
(20, 'Mariana', 'Juliana', 'Parra', 'Aguilar', 'TI', 1012345020, 'mariana.parra@misena.edu.co', '1153693d12f4d097c1b02d9ad5d787ec'),
(21, 'David', 'Santiago', 'Cruz', 'Amado', 'CC', 1013123184, 'david.cruz@sena.edu.co', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5'),
(42, 'Luis', 'Javier', 'Mariño', 'Beltran', 'CC', 1021672531, 'soylucho2006@gmail.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
(43, 'Elkin ', 'Gustavo', 'Olarte', 'Olarte', 'CC', 1140914957, 'elkingustavo15@gmail.com', '8bb0cf6eb9b17d0f7d22b456f121257dc1254e1f01665370476383ea776df414'),
(44, 'Kennen', 'Stwd', 'Cortes', 'Chaparro', 'CC', 1234567890, 'instructor@gmail.com', '47e91eba0cc1d77bcd14812ab877ef64962ba8be7aedf495bbdc1914c0780378'),
(45, 'Camilo', 'Andrey', 'Diaz', 'Sarmiento', 'CC', 1010101010, 'camilo@gmail.com', '5aea47df7713fa92d0abe9cfeea34261396541a0c432c8a45a68acecc9f65ec2'),
(46, 'Mahily', 'Kathalina', 'Gutierrez', 'Ramirez', 'TI', 1028487888, 'guarda@gmail.com', '13791440eec56d20730b2c42e51e36f51d793d2f0e81483bbf6130f316661510'),
(52, 'Sergio', 'Alejandro', 'Espinoza', 'Muñoz', 'CC', 1000726773, 'sergio@gmail.com', 'e77cb994418f5bc0ec1007b6c137918d9aed4e7b0bde45d74a50d09272c2d602');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alertas_inasistencia`
--
ALTER TABLE `alertas_inasistencia`
  ADD PRIMARY KEY (`id_alerta`),
  ADD KEY `coordinacion_id` (`coordinacion_id`);

--
-- Indices de la tabla `ambiente`
--
ALTER TABLE `ambiente`
  ADD PRIMARY KEY (`id_ambiente`);

--
-- Indices de la tabla `aprendiz`
--
ALTER TABLE `aprendiz`
  ADD PRIMARY KEY (`Usuario_id_usuario`),
  ADD KEY `programas_id_programas` (`programas_id_programas`),
  ADD KEY `ficha_idficha` (`ficha_idficha`);

--
-- Indices de la tabla `coordinacion`
--
ALTER TABLE `coordinacion`
  ADD PRIMARY KEY (`id_coordinacion`);

--
-- Indices de la tabla `coordinador`
--
ALTER TABLE `coordinador`
  ADD PRIMARY KEY (`Usuario_id_usuario`),
  ADD KEY `coordinacion_id_coordinacion` (`coordinacion_id_coordinacion`);

--
-- Indices de la tabla `ficha`
--
ALTER TABLE `ficha`
  ADD PRIMARY KEY (`idficha`),
  ADD KEY `instructor_Usuario_id_usuario` (`instructor_Usuario_id_usuario`);

--
-- Indices de la tabla `guarda_seguridad`
--
ALTER TABLE `guarda_seguridad`
  ADD PRIMARY KEY (`Usuario_id_usuario`);

--
-- Indices de la tabla `historico_incidentes`
--
ALTER TABLE `historico_incidentes`
  ADD PRIMARY KEY (`id_historico`),
  ADD KEY `incidente_id` (`incidente_id`),
  ADD KEY `ambiente_id` (`ambiente_id`),
  ADD KEY `tipo_incidente_id` (`tipo_incidente_id`);

--
-- Indices de la tabla `instructor`
--
ALTER TABLE `instructor`
  ADD PRIMARY KEY (`Usuario_id_usuario`),
  ADD KEY `coordinacion_id_coordinacion` (`coordinacion_id_coordinacion`);

--
-- Indices de la tabla `jornada`
--
ALTER TABLE `jornada`
  ADD PRIMARY KEY (`id_jornada`);

--
-- Indices de la tabla `modalidad`
--
ALTER TABLE `modalidad`
  ADD PRIMARY KEY (`id_modalidad`);

--
-- Indices de la tabla `programas`
--
ALTER TABLE `programas`
  ADD PRIMARY KEY (`id_programas`),
  ADD KEY `jornada_id` (`jornada_id`),
  ADD KEY `modalidad_id` (`modalidad_id`),
  ADD KEY `coordinacion_id` (`coordinacion_id`);

--
-- Indices de la tabla `recursos`
--
ALTER TABLE `recursos`
  ADD PRIMARY KEY (`id_recurso`),
  ADD KEY `tipo_recurso` (`tipo_recurso`),
  ADD KEY `ambiente_id` (`ambiente_id`);

--
-- Indices de la tabla `registro_inasistencia`
--
ALTER TABLE `registro_inasistencia`
  ADD PRIMARY KEY (`id_inasistencia`),
  ADD KEY `jornada_id` (`jornada_id`),
  ADD KEY `aprendiz_Usuario_id_usuario` (`aprendiz_Usuario_id_usuario`),
  ADD KEY `instructor_Usuario_id_usuario` (`instructor_Usuario_id_usuario`);

--
-- Indices de la tabla `registro_incidente`
--
ALTER TABLE `registro_incidente`
  ADD PRIMARY KEY (`id_incidente`),
  ADD KEY `ambiente_id` (`ambiente_id`),
  ADD KEY `tipo_inc_id` (`tipo_inc_id`),
  ADD KEY `usuario_id_usuario` (`usuario_id_usuario`);

--
-- Indices de la tabla `registro_minuta`
--
ALTER TABLE `registro_minuta`
  ADD PRIMARY KEY (`id_minuta`),
  ADD KEY `ambiente_id` (`ambiente_id`),
  ADD KEY `guarda_seguridad_Usuario_id_usuario` (`guarda_seguridad_Usuario_id_usuario`),
  ADD KEY `responsable_id` (`responsable_id`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`id_rol`);

--
-- Indices de la tabla `tipo_incidente`
--
ALTER TABLE `tipo_incidente`
  ADD PRIMARY KEY (`id_tipo_inc`);

--
-- Indices de la tabla `tipo_recurso`
--
ALTER TABLE `tipo_recurso`
  ADD PRIMARY KEY (`id_tipo_recurso`);

--
-- Indices de la tabla `traslado_recurso`
--
ALTER TABLE `traslado_recurso`
  ADD PRIMARY KEY (`id_traslado`),
  ADD KEY `recurso_id` (`recurso_id`),
  ADD KEY `ambiente_origen` (`ambiente_origen`);

--
-- Indices de la tabla `user_rol`
--
ALTER TABLE `user_rol`
  ADD PRIMARY KEY (`id_user_rol`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_rol` (`id_rol`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id_usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `alertas_inasistencia`
--
ALTER TABLE `alertas_inasistencia`
  MODIFY `id_alerta` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador de la alerta.\n', AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `coordinacion`
--
ALTER TABLE `coordinacion`
  MODIFY `id_coordinacion` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador de la coordinación.\n', AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `historico_incidentes`
--
ALTER TABLE `historico_incidentes`
  MODIFY `id_historico` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del historial del incidente.\n', AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `recursos`
--
ALTER TABLE `recursos`
  MODIFY `id_recurso` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del recurso.\n', AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `registro_inasistencia`
--
ALTER TABLE `registro_inasistencia`
  MODIFY `id_inasistencia` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del registro.\n', AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `registro_incidente`
--
ALTER TABLE `registro_incidente`
  MODIFY `id_incidente` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del incidente.\n\n', AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `registro_minuta`
--
ALTER TABLE `registro_minuta`
  MODIFY `id_minuta` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del registro de minuta.\n', AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `traslado_recurso`
--
ALTER TABLE `traslado_recurso`
  MODIFY `id_traslado` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del traslado.\n', AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `user_rol`
--
ALTER TABLE `user_rol`
  MODIFY `id_user_rol` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del registro.\n', AUTO_INCREMENT=53;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del usuario.\n', AUTO_INCREMENT=53;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `alertas_inasistencia`
--
ALTER TABLE `alertas_inasistencia`
  ADD CONSTRAINT `alertas_inasistencia_ibfk_1` FOREIGN KEY (`coordinacion_id`) REFERENCES `coordinacion` (`id_coordinacion`);

--
-- Filtros para la tabla `aprendiz`
--
ALTER TABLE `aprendiz`
  ADD CONSTRAINT `aprendiz_ibfk_1` FOREIGN KEY (`programas_id_programas`) REFERENCES `programas` (`id_programas`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `aprendiz_ibfk_2` FOREIGN KEY (`Usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `aprendiz_ibfk_3` FOREIGN KEY (`ficha_idficha`) REFERENCES `ficha` (`idficha`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `coordinador`
--
ALTER TABLE `coordinador`
  ADD CONSTRAINT `coordinador_ibfk_1` FOREIGN KEY (`Usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `coordinador_ibfk_2` FOREIGN KEY (`coordinacion_id_coordinacion`) REFERENCES `coordinacion` (`id_coordinacion`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `ficha`
--
ALTER TABLE `ficha`
  ADD CONSTRAINT `ficha_ibfk_1` FOREIGN KEY (`instructor_Usuario_id_usuario`) REFERENCES `instructor` (`Usuario_id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `guarda_seguridad`
--
ALTER TABLE `guarda_seguridad`
  ADD CONSTRAINT `guarda_seguridad_ibfk_1` FOREIGN KEY (`Usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`);

--
-- Filtros para la tabla `historico_incidentes`
--
ALTER TABLE `historico_incidentes`
  ADD CONSTRAINT `historico_incidentes_ibfk_1` FOREIGN KEY (`incidente_id`) REFERENCES `registro_incidente` (`id_incidente`),
  ADD CONSTRAINT `historico_incidentes_ibfk_2` FOREIGN KEY (`ambiente_id`) REFERENCES `ambiente` (`id_ambiente`),
  ADD CONSTRAINT `historico_incidentes_ibfk_3` FOREIGN KEY (`tipo_incidente_id`) REFERENCES `tipo_incidente` (`id_tipo_inc`);

--
-- Filtros para la tabla `instructor`
--
ALTER TABLE `instructor`
  ADD CONSTRAINT `instructor_ibfk_1` FOREIGN KEY (`Usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`),
  ADD CONSTRAINT `instructor_ibfk_2` FOREIGN KEY (`coordinacion_id_coordinacion`) REFERENCES `coordinacion` (`id_coordinacion`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `programas`
--
ALTER TABLE `programas`
  ADD CONSTRAINT `programas_ibfk_1` FOREIGN KEY (`jornada_id`) REFERENCES `jornada` (`id_jornada`),
  ADD CONSTRAINT `programas_ibfk_2` FOREIGN KEY (`modalidad_id`) REFERENCES `modalidad` (`id_modalidad`),
  ADD CONSTRAINT `programas_ibfk_3` FOREIGN KEY (`coordinacion_id`) REFERENCES `coordinacion` (`id_coordinacion`);

--
-- Filtros para la tabla `recursos`
--
ALTER TABLE `recursos`
  ADD CONSTRAINT `recursos_ibfk_1` FOREIGN KEY (`tipo_recurso`) REFERENCES `tipo_recurso` (`id_tipo_recurso`),
  ADD CONSTRAINT `recursos_ibfk_2` FOREIGN KEY (`ambiente_id`) REFERENCES `ambiente` (`id_ambiente`);

--
-- Filtros para la tabla `registro_inasistencia`
--
ALTER TABLE `registro_inasistencia`
  ADD CONSTRAINT `registro_inasistencia_ibfk_1` FOREIGN KEY (`jornada_id`) REFERENCES `jornada` (`id_jornada`),
  ADD CONSTRAINT `registro_inasistencia_ibfk_2` FOREIGN KEY (`aprendiz_Usuario_id_usuario`) REFERENCES `aprendiz` (`Usuario_id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `registro_inasistencia_ibfk_3` FOREIGN KEY (`instructor_Usuario_id_usuario`) REFERENCES `instructor` (`Usuario_id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `registro_incidente`
--
ALTER TABLE `registro_incidente`
  ADD CONSTRAINT `registro_incidente_ibfk_1` FOREIGN KEY (`ambiente_id`) REFERENCES `ambiente` (`id_ambiente`),
  ADD CONSTRAINT `registro_incidente_ibfk_2` FOREIGN KEY (`tipo_inc_id`) REFERENCES `tipo_incidente` (`id_tipo_inc`),
  ADD CONSTRAINT `registro_incidente_ibfk_3` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `registro_minuta`
--
ALTER TABLE `registro_minuta`
  ADD CONSTRAINT `registro_minuta_ibfk_1` FOREIGN KEY (`ambiente_id`) REFERENCES `ambiente` (`id_ambiente`),
  ADD CONSTRAINT `registro_minuta_ibfk_2` FOREIGN KEY (`guarda_seguridad_Usuario_id_usuario`) REFERENCES `guarda_seguridad` (`Usuario_id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `registro_minuta_ibfk_3` FOREIGN KEY (`responsable_id`) REFERENCES `instructor` (`Usuario_id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `traslado_recurso`
--
ALTER TABLE `traslado_recurso`
  ADD CONSTRAINT `traslado_recurso_ibfk_1` FOREIGN KEY (`recurso_id`) REFERENCES `recursos` (`id_recurso`),
  ADD CONSTRAINT `traslado_recurso_ibfk_2` FOREIGN KEY (`ambiente_origen`) REFERENCES `ambiente` (`id_ambiente`);

--
-- Filtros para la tabla `user_rol`
--
ALTER TABLE `user_rol`
  ADD CONSTRAINT `user_rol_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`),
  ADD CONSTRAINT `user_rol_ibfk_2` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`);

DELIMITER $$
--
-- Eventos
--
CREATE DEFINER=`root`@`localhost` EVENT `actualizar_minutas_a_disponible` ON SCHEDULE EVERY 1 MINUTE STARTS '2025-12-11 21:43:51' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    UPDATE registro_minuta
    SET estado = 'Disponible'
    WHERE estado = 'Ocupado'
      AND NOW() > fecha_hora_entrega;
END$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
