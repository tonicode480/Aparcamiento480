-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 11, 2023 at 04:41 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `parking480`
--

-- --------------------------------------------------------

--
-- Table structure for table `estancias`
--

CREATE TABLE `estancias` (
  `id` int(11) NOT NULL,
  `matricula` varchar(10) DEFAULT NULL,
  `horaEntrada` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `horaSalida` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `estancias`
--

INSERT INTO `estancias` (`id`, `matricula`, `horaEntrada`, `horaSalida`) VALUES
(1, NULL, '2023-07-10 13:48:57', NULL),
(2, NULL, '2023-07-10 13:49:04', NULL),
(3, NULL, '2023-07-10 13:49:07', NULL),
(4, NULL, '2023-07-10 13:49:19', NULL),
(5, NULL, '2023-07-10 13:49:27', NULL),
(10, 'AAB', '2023-07-11 07:21:32', '2023-07-11 07:21:32'),
(12, 'ABA', '2023-07-11 07:25:59', '2023-07-11 07:25:59'),
(17, 'asd', '2023-07-11 08:24:28', NULL),
(18, 'aa1', '2023-07-11 08:46:16', '2023-07-11 08:46:16'),
(20, 'aa1', '2023-07-11 08:46:16', '2023-07-11 08:46:16'),
(21, 'aa1', '2023-07-11 08:46:16', '2023-07-11 08:46:16'),
(23, 'test1', '2023-07-11 09:33:57', '2023-07-11 09:33:57'),
(24, 'test2', '2023-07-11 09:32:49', '2023-07-11 09:32:49'),
(26, '4646', '2023-07-11 13:57:33', NULL);

--
-- Triggers `estancias`
--
DELIMITER $$
CREATE TRIGGER `actualizarTiempoEstacionado` AFTER UPDATE ON `estancias` FOR EACH ROW BEGIN
    IF NEW.horaSalida IS NOT NULL THEN
        UPDATE vehiculos 
        SET tiempoEstacionado = tiempoEstacionado + TIMESTAMPDIFF(MINUTE, OLD.horaEntrada, NEW.horaSalida)
        WHERE matricula = NEW.matricula;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `precios`
--

CREATE TABLE `precios` (
  `tipo` varchar(20) NOT NULL,
  `precioPorMinuto` decimal(10,5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `precios`
--

INSERT INTO `precios` (`tipo`, `precioPorMinuto`) VALUES
('no residente', 0.02000),
('oficial', 0.00000),
('residente', 0.00200);

-- --------------------------------------------------------

--
-- Table structure for table `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `user` varchar(50) DEFAULT NULL,
  `pass` varchar(50) DEFAULT NULL,
  `isBlocked` tinyint(1) DEFAULT NULL,
  `telefono` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `usuarios`
--

INSERT INTO `usuarios` (`id`, `user`, `pass`, `isBlocked`, `telefono`) VALUES
(0, 'jano', '0613040', 1, '34601522053');

-- --------------------------------------------------------

--
-- Table structure for table `vehiculos`
--

CREATE TABLE `vehiculos` (
  `matricula` varchar(10) NOT NULL,
  `tipo` enum('oficial','residente','no residente') DEFAULT NULL,
  `tiempoEstacionado` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `vehiculos`
--

INSERT INTO `vehiculos` (`matricula`, `tipo`, `tiempoEstacionado`) VALUES
('232344AAA', 'residente', 0),
('4545', 'oficial', 0),
('4545ABC', 'oficial', 0),
('4646', 'residente', 0),
('aa1', 'residente', 0),
('aa2', 'oficial', 134),
('AAB', 'residente', 0),
('ABA', 'residente', 0),
('abb', 'oficial', 0),
('abc', 'residente', 0),
('asd', 'residente', 0),
('ASDSDADAS', 'oficial', 0),
('ass', 'oficial', 0),
('BAB', 'oficial', 4),
('test1', 'no residente', 46),
('test2', 'residente', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `estancias`
--
ALTER TABLE `estancias`
  ADD PRIMARY KEY (`id`),
  ADD KEY `matricula` (`matricula`);

--
-- Indexes for table `precios`
--
ALTER TABLE `precios`
  ADD PRIMARY KEY (`tipo`);

--
-- Indexes for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `vehiculos`
--
ALTER TABLE `vehiculos`
  ADD PRIMARY KEY (`matricula`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `estancias`
--
ALTER TABLE `estancias`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `estancias`
--
ALTER TABLE `estancias`
  ADD CONSTRAINT `estancias_ibfk_1` FOREIGN KEY (`matricula`) REFERENCES `vehiculos` (`matricula`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
