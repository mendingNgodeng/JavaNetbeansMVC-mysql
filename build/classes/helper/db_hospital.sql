-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 23, 2024 at 01:40 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_hospital`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id_admin` int(11) NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `pass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `role` enum('Admin','Staff','Doctor') DEFAULT 'Admin',
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`id_admin`, `username`, `pass`, `name`, `email`, `role`, `created_at`) VALUES
(1, 'helmiAdmins', '123', 'Muhammad Helmi', 'helmi@gmail.com', 'Admin', '2024-12-21 13:52:25'),
(2, 'pai', 'pai', 'Paisal Ali Acin', 'PaiAcin@gmail.com', 'Staff', '2024-12-21 22:54:48'),
(9, 'faiz', '123', 'fais', '123', 'Staff', '2024-12-22 09:57:57'),
(34, 'helmi', '123', '211', 'helmiSbe@gmail.com', 'Admin', '2024-12-22 10:50:39'),
(37, 'Mamang', '5', 'Momonga', 'Momonga@gmail.com', 'Staff', '2024-12-23 19:37:09');

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

CREATE TABLE `appointments` (
  `id_appointment` int(11) NOT NULL,
  `id_patient` int(11) NOT NULL,
  `id_doctor` int(11) DEFAULT NULL,
  `appointment_date` date NOT NULL,
  `status` enum('Scheduled','completed','cancelled') NOT NULL,
  `notes` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `appointments`
--

INSERT INTO `appointments` (`id_appointment`, `id_patient`, `id_doctor`, `appointment_date`, `status`, `notes`) VALUES
(2, 5, 1, '2024-12-25', 'Scheduled', 'Pasien susah tidur dan sering muntah'),
(3, 7, 2, '2024-12-11', 'completed', 'adadadad');

-- --------------------------------------------------------

--
-- Table structure for table `doctors`
--

CREATE TABLE `doctors` (
  `id_doctor` int(11) NOT NULL,
  `name_doctor` varchar(100) NOT NULL,
  `specialization` varchar(30) NOT NULL,
  `phone` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `doctors`
--

INSERT INTO `doctors` (`id_doctor`, `name_doctor`, `specialization`, `phone`, `email`) VALUES
(1, 'Muhammad \"Helm Otaku\" Helmi', 'Gigi', '0823134', 'helm@gmail.com'),
(2, 'Daruh \"DRLChessBoy\" Mawahib', 'Mancing', '082133', 'darulM@gmail.com'),
(4, 'Bintang \"Koin Emas\" Rachman', 'Chess ', '08232311', 'Bintangt@gmail.com'),
(5, 'Syafiq \"Noob People\" Nuhaa', 'Prodigy in all', '0823211', 'SyafiqBisa@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `medicines`
--

CREATE TABLE `medicines` (
  `id_medicine` int(11) NOT NULL,
  `name_medicine` varchar(100) NOT NULL,
  `stok` int(11) NOT NULL,
  `description` text NOT NULL,
  `price` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `medicines`
--

INSERT INTO `medicines` (`id_medicine`, `name_medicine`, `stok`, `description`, `price`) VALUES
(1, 'Paracetamol', 20, 'Obat sakit pala dan hangat badan', 10000),
(2, 'Sangobion', 94, 'adaa', 15000),
(5, 'Paramex', 60, 'Obat Kepala', 50000);

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE `patients` (
  `id_patient` int(11) NOT NULL,
  `name_patient` varchar(100) NOT NULL,
  `gender` enum('Male','Female') NOT NULL,
  `birth_date` date NOT NULL,
  `phone` varchar(30) NOT NULL,
  `address` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `patients`
--

INSERT INTO `patients` (`id_patient`, `name_patient`, `gender`, `birth_date`, `phone`, `address`) VALUES
(5, 'Muhammad Rahma', 'Female', '2024-12-05', '12', 'karangnyar'),
(7, 'Muhammad Yahay', 'Male', '2024-12-12', '165', 'Jawa'),
(9, 'Gon', 'Male', '2024-12-20', '07422', 'Langam');

-- --------------------------------------------------------

--
-- Table structure for table `receipts`
--

CREATE TABLE `receipts` (
  `id_receipt` int(11) NOT NULL,
  `id_medicine` int(11) NOT NULL,
  `id_patient` int(11) DEFAULT NULL,
  `medicine_quantity` int(11) DEFAULT NULL,
  `medicine_price` decimal(10,2) DEFAULT NULL,
  `service_name` varchar(100) DEFAULT NULL,
  `service_price` decimal(10,2) DEFAULT NULL,
  `total_amount` decimal(12,2) GENERATED ALWAYS AS (coalesce(`medicine_quantity` * `medicine_price`,0) + coalesce(`service_price`,0)) STORED,
  `payment_date` datetime DEFAULT current_timestamp(),
  `payment_status` enum('Paid','Unpaid') DEFAULT 'Unpaid'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `receipts`
--

INSERT INTO `receipts` (`id_receipt`, `id_medicine`, `id_patient`, `medicine_quantity`, `medicine_price`, `service_name`, `service_price`, `payment_date`, `payment_status`) VALUES
(3, 1, 5, 2, NULL, 'Perawatan', 20000.00, '2024-12-22 23:04:39', 'Paid'),
(10, 1, 5, 10, NULL, 'adadad', 23000.00, '2024-12-23 13:57:52', 'Paid'),
(11, 2, 7, 10, NULL, 'Medis', 23000.00, '2024-12-23 18:37:39', 'Paid'),
(12, 5, 9, 10, NULL, 'Pijat Kepala', 30000.00, '2024-12-23 19:06:41', 'Unpaid'),
(13, 5, 9, 20, NULL, 'Pijat Kepala', 20000.00, '2024-12-23 19:07:57', 'Paid');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id_admin`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `appointments`
--
ALTER TABLE `appointments`
  ADD PRIMARY KEY (`id_appointment`),
  ADD KEY `appointments_ibfk_1` (`id_doctor`),
  ADD KEY `id_patient` (`id_patient`);

--
-- Indexes for table `doctors`
--
ALTER TABLE `doctors`
  ADD PRIMARY KEY (`id_doctor`);

--
-- Indexes for table `medicines`
--
ALTER TABLE `medicines`
  ADD PRIMARY KEY (`id_medicine`);

--
-- Indexes for table `patients`
--
ALTER TABLE `patients`
  ADD PRIMARY KEY (`id_patient`);

--
-- Indexes for table `receipts`
--
ALTER TABLE `receipts`
  ADD PRIMARY KEY (`id_receipt`),
  ADD KEY `receipts_ibfk_1` (`id_patient`),
  ADD KEY `id_medicine` (`id_medicine`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `id_admin` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `appointments`
--
ALTER TABLE `appointments`
  MODIFY `id_appointment` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `doctors`
--
ALTER TABLE `doctors`
  MODIFY `id_doctor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `medicines`
--
ALTER TABLE `medicines`
  MODIFY `id_medicine` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `patients`
--
ALTER TABLE `patients`
  MODIFY `id_patient` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `receipts`
--
ALTER TABLE `receipts`
  MODIFY `id_receipt` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `appointments`
--
ALTER TABLE `appointments`
  ADD CONSTRAINT `appointments_ibfk_1` FOREIGN KEY (`id_doctor`) REFERENCES `doctors` (`id_doctor`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `appointments_ibfk_2` FOREIGN KEY (`id_patient`) REFERENCES `patients` (`id_patient`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `receipts`
--
ALTER TABLE `receipts`
  ADD CONSTRAINT `receipts_ibfk_1` FOREIGN KEY (`id_patient`) REFERENCES `patients` (`id_patient`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `receipts_ibfk_2` FOREIGN KEY (`id_medicine`) REFERENCES `medicines` (`id_medicine`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
