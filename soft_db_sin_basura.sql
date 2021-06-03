-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 25-05-2021 a las 10:47:01
-- Versión del servidor: 10.4.18-MariaDB
-- Versión de PHP: 8.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `soft_db`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `administrador`
--

CREATE TABLE `administrador` (
  `id` varchar(18) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `administrador`
--

INSERT INTO `administrador` (`id`) VALUES
('admin');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `agencia`
--

CREATE TABLE `agencia` (
  `codigo` varchar(40) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `contraseña` varchar(45) NOT NULL,
  `web` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `agencia`
--

INSERT INTO `agencia` (`codigo`, `nombre`, `contraseña`, `web`) VALUES
('5', 'Inmobiliarias Ibañez', 'calle5', 'Ibañez.net'),
('ninguna', 'ninguno', '123', 'ninguna');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `agente`
--

CREATE TABLE `agente` (
  `username` varchar(40) NOT NULL,
  `agencia` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `agente`
--

INSERT INTO `agente` (`username`, `agencia`) VALUES
('adminA', '5');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ciudad`
--

CREATE TABLE `ciudad` (
  `nombre` varchar(18) NOT NULL,
  `codigo_postal` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `ciudad`
--

INSERT INTO `ciudad` (`nombre`, `codigo_postal`) VALUES
('Cataluña', '08000'),
('Galicia', '27000'),
('Madrid', '28000'),
('Valencia', '46000');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `id` varchar(18) NOT NULL,
  `nombreCompleto` varchar(45) NOT NULL,
  `fechaNacimiento` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`id`, `nombreCompleto`, `fechaNacimiento`) VALUES
('1', 'PedroFernandezGomez', '1969-01-01 00:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `favoritos`
--

CREATE TABLE `favoritos` (
  `id` varchar(18) NOT NULL,
  `id_cliente` varchar(45) NOT NULL,
  `valoracion` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `favoritos`
--

INSERT INTO `favoritos` (`id`, `id_cliente`, `valoracion`) VALUES
('vivienda1', 'Daviles', 8),
('vivienda1', 'gabri', NULL),
('vivienda2', 'Daviles', 7),
('vivienda2', 'gabri', 8),
('vivienda4', 'admin', NULL),
('vivienda5', 'admin', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `filtros`
--

CREATE TABLE `filtros` (
  `id` varchar(18) NOT NULL,
  `fecha_entrada` datetime DEFAULT NULL,
  `fecha_salida` datetime DEFAULT NULL,
  `ciudad` varchar(45) NOT NULL,
  `tipo` int(11) NOT NULL,
  `p_min` int(11) DEFAULT NULL,
  `p_max` int(11) DEFAULT NULL,
  `habitaciones` int(11) DEFAULT NULL,
  `baños` int(11) DEFAULT NULL,
  `ventaAlquiler` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `fotografia`
--

CREATE TABLE `fotografia` (
  `id` varchar(200) NOT NULL,
  `id_vivienda` varchar(18) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `fotografia`
--

INSERT INTO `fotografia` (`id`, `id_vivienda`) VALUES
('src\\trobify\\images\\casa1.jpg', 'vivienda3'),
('src\\trobify\\images\\casa2.jpg', 'vivienda4'),
('src\\trobify\\images\\casa3.jpg', 'vivienda7'),
('src\\trobify\\images\\casa4.jpg', 'vivienda8'),
('src\\trobify\\images\\comedor1.jpg', 'vivienda3'),
('src\\trobify\\images\\comedor2.jpg', 'vivienda1'),
('src\\trobify\\images\\comedor3.jpg', 'vivienda2'),
('src\\trobify\\images\\comedor4.jpg', 'vivienda5'),
('src\\trobify\\images\\comedor5.jpg', 'vivienda6'),
('src\\trobify\\images\\dormitorio1.jpg', 'vivienda3'),
('src\\trobify\\images\\dormitorio2.jpg', 'vivienda3'),
('src\\trobify\\images\\piso1.jpg', 'vivienda1'),
('src\\trobify\\images\\piso2.jpg', 'vivienda2'),
('src\\trobify\\images\\piso3.jpg', 'vivienda5'),
('src\\trobify\\images\\piso4.jpg', 'vivienda6');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial`
--

CREATE TABLE `historial` (
  `id` int(11) NOT NULL,
  `id_vivienda` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mensajes`
--

CREATE TABLE `mensajes` (
  `id` int(11) NOT NULL,
  `cuerpo` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notificaciones`
--

CREATE TABLE `notificaciones` (
  `id` int(11) NOT NULL,
  `id_vivi` varchar(45) NOT NULL,
  `id_usuario` varchar(45) DEFAULT NULL,
  `descripción` varchar(45) NOT NULL,
  `tipo` int(11) NOT NULL,
  `estado` int(11) NOT NULL,
  `last_mod` datetime NOT NULL,
  `id_usuario_destino` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `propietario`
--

CREATE TABLE `propietario` (
  `id` varchar(18) NOT NULL,
  `nombreCompleto` varchar(45) NOT NULL,
  `fechaNacimiento` datetime NOT NULL,
  `id_agencia` varchar(18) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `servicios`
--

CREATE TABLE `servicios` (
  `id` varchar(18) NOT NULL,
  `supermercado` int(11) DEFAULT 0,
  `transporte_publico` int(11) DEFAULT 0,
  `banco` int(11) DEFAULT 0,
  `estanco` int(11) DEFAULT 0,
  `centro_comercial` int(11) DEFAULT 0,
  `gimnasio` int(11) DEFAULT 0,
  `farmacia` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `servicios`
--

INSERT INTO `servicios` (`id`, `supermercado`, `transporte_publico`, `banco`, `estanco`, `centro_comercial`, `gimnasio`, `farmacia`) VALUES
('vivienda1', 1, 0, 0, 1, 0, 0, 1),
('vivienda2', 0, 1, 1, 0, 0, 1, 1),
('vivienda3', 0, 0, 0, 1, 1, 1, 1),
('vivienda4', 1, 1, 1, 1, 0, 0, 0),
('vivienda5', 1, 0, 1, 0, 1, 0, 1),
('vivienda6', 1, 0, 0, 1, 1, 0, 0),
('vivienda7', 0, 1, 1, 0, 0, 1, 1),
('vivienda8', 1, 1, 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` varchar(18) NOT NULL,
  `dni` varchar(40) NOT NULL,
  `password` varchar(40) NOT NULL,
  `nombre` varchar(40) NOT NULL,
  `apellidos` varchar(40) NOT NULL,
  `email` varchar(45) NOT NULL,
  `foto` varchar(200) DEFAULT NULL,
  `preferencia` varchar(24) DEFAULT ' '
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `dni`, `password`, `nombre`, `apellidos`, `email`, `foto`, `preferencia`) VALUES
('admin', '12341234A', 'admin', 'admin', 'admin', 'admin', NULL, 'Valencia'),
('adminA', '12312312A', 'admin', 'adminA', 'adminA', 'adminA', NULL, ' '),
('Daviles', '48714195B', 'Kanshou', 'David', 'Montesinos', 'mondavidmat@outlook.es', NULL, 'Barcelona'),
('gabri', '12345678S', '1234', 'gabriela', 'corbalan', 'gabri@gmail.com', NULL, ' ');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `vivienda`
--

CREATE TABLE `vivienda` (
  `id` varchar(45) NOT NULL,
  `calle` varchar(45) NOT NULL,
  `ciudad` varchar(15) NOT NULL,
  `ventaAlquiler` int(11) NOT NULL,
  `id_agencia` varchar(18) DEFAULT NULL,
  `precio` int(11) NOT NULL,
  `id_propietario` varchar(18) DEFAULT NULL,
  `tipo` int(11) NOT NULL,
  `baños` int(11) NOT NULL,
  `habitaciones` int(11) NOT NULL,
  `descripcion` varchar(528) DEFAULT NULL,
  `piso` int(11) DEFAULT NULL,
  `puerta` varchar(45) DEFAULT NULL,
  `codigo_postal` int(11) DEFAULT NULL,
  `activo` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `vivienda`
--

INSERT INTO `vivienda` (`id`, `calle`, `ciudad`, `ventaAlquiler`, `id_agencia`, `precio`, `id_propietario`, `tipo`, `baños`, `habitaciones`, `descripcion`, `piso`, `puerta`, `codigo_postal`, `activo`) VALUES
('vivienda1', 'Calle Arzobispo Mayoral', 'Valencia', 1, '5', 1000, 'admin', 1, 2, 2, NULL, 2, '2', 46002, 0),
('vivienda2', 'Calle de Sorní', 'Valencia', 1, 'ninguna', 1200, 'admin', 1, 2, 3, NULL, 4, '5', 46004, 0),
('vivienda3', 'Carrer de l\'Angelicot', 'Valencia', 1, '5', 600, 'admin', 2, 1, 1, NULL, 5, '12', 46001, 0),
('vivienda4', 'Calle de Pelayo', 'Valencia', 1, '5', 700, 'gabri', 2, 1, 4, NULL, 3, '4', 46007, 1),
('vivienda5', 'Calle de Béjar', 'Madrid', 1, '5', 1500, 'gabri', 1, 2, 0, NULL, 5, '14', 28028, 0),
('vivienda6', 'Calle Ardemans', 'Madrid', 1, '5', 2000, 'gabri', 1, 3, 0, NULL, 8, '25', 28028, 0),
('vivienda7', 'Calle Mantuano', 'Madrid', 1, '5', 500, 'admin', 2, 1, 0, NULL, 1, '1', 28002, 0),
('vivienda8', 'Calle de Edgar Neville', 'Madrid', 2, '5', 800, 'admin', 2, 1, 0, NULL, 3, '6', 28020, 0);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `administrador`
--
ALTER TABLE `administrador`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `agencia`
--
ALTER TABLE `agencia`
  ADD PRIMARY KEY (`codigo`);

--
-- Indices de la tabla `agente`
--
ALTER TABLE `agente`
  ADD PRIMARY KEY (`username`),
  ADD KEY `fk_agencia_agente_idx` (`agencia`);

--
-- Indices de la tabla `ciudad`
--
ALTER TABLE `ciudad`
  ADD PRIMARY KEY (`nombre`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`id`,`nombreCompleto`);

--
-- Indices de la tabla `favoritos`
--
ALTER TABLE `favoritos`
  ADD PRIMARY KEY (`id`,`id_cliente`),
  ADD KEY `fk_favoritos_cliente_idx` (`id_cliente`);

--
-- Indices de la tabla `filtros`
--
ALTER TABLE `filtros`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `fotografia`
--
ALTER TABLE `fotografia`
  ADD PRIMARY KEY (`id`,`id_vivienda`),
  ADD KEY `fk_fotografia_vivienda_idx` (`id_vivienda`);

--
-- Indices de la tabla `historial`
--
ALTER TABLE `historial`
  ADD PRIMARY KEY (`id`),
  ADD KEY `vivienda_idx` (`id_vivienda`);

--
-- Indices de la tabla `mensajes`
--
ALTER TABLE `mensajes`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `notificaciones`
--
ALTER TABLE `notificaciones`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_notificaciones_vivienda_idx` (`id_vivi`),
  ADD KEY `fk_notificaciones_usuario_idx` (`id_usuario`);

--
-- Indices de la tabla `propietario`
--
ALTER TABLE `propietario`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_propietario_agencia_idx` (`id_agencia`);

--
-- Indices de la tabla `servicios`
--
ALTER TABLE `servicios`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `vivienda`
--
ALTER TABLE `vivienda`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_vivienda_agencia_idx` (`id_agencia`),
  ADD KEY `fk_vivienda_propietario_idx` (`id_propietario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `historial`
--
ALTER TABLE `historial`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=92;

--
-- AUTO_INCREMENT de la tabla `mensajes`
--
ALTER TABLE `mensajes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `notificaciones`
--
ALTER TABLE `notificaciones`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `administrador`
--
ALTER TABLE `administrador`
  ADD CONSTRAINT `usuario` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `agente`
--
ALTER TABLE `agente`
  ADD CONSTRAINT `especializacion` FOREIGN KEY (`username`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_agencia_agente` FOREIGN KEY (`agencia`) REFERENCES `agencia` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `favoritos`
--
ALTER TABLE `favoritos`
  ADD CONSTRAINT `fk_favoritos_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_favoritos_vivienda` FOREIGN KEY (`id`) REFERENCES `vivienda` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `filtros`
--
ALTER TABLE `filtros`
  ADD CONSTRAINT `fk_filtro_usuario` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `fotografia`
--
ALTER TABLE `fotografia`
  ADD CONSTRAINT `fk_fotografia_vivienda` FOREIGN KEY (`id_vivienda`) REFERENCES `vivienda` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `propietario`
--
ALTER TABLE `propietario`
  ADD CONSTRAINT `fk_propietarioUsuario` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_propietario_agencia` FOREIGN KEY (`id_agencia`) REFERENCES `agencia` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `servicios`
--
ALTER TABLE `servicios`
  ADD CONSTRAINT `fk_servicios_vivienda` FOREIGN KEY (`id`) REFERENCES `vivienda` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `vivienda`
--
ALTER TABLE `vivienda`
  ADD CONSTRAINT `fk_vivienda_agencia` FOREIGN KEY (`id_agencia`) REFERENCES `agencia` (`codigo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_vivienda_propietario` FOREIGN KEY (`id_propietario`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
