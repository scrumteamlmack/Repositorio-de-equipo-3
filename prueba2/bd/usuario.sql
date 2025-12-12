-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 17-09-2025 a las 13:52:12
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
(21, 'David', 'Santiago', 'Cruz', 'Amado', 'TI', 1013123184, 'dscruzamado111@gmail.com', '$2y$12$mm/ZSAZQm3/RyYOU59/Rr.OnhfRdrhAHG49B7.EDuuFLKB6v/BBpK');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id_usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Clave primaria. Identificador del usuario.\n', AUTO_INCREMENT=22;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
