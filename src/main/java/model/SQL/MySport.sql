-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: localhost    Database: mysport
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `member_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL,
  PRIMARY KEY (`member_id`),
  KEY `fk_account_member_idx` (`member_id`),
  CONSTRAINT `fk_account_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'dan@ms.se','5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5'),(2,'bob@mail.com','5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5'),(3,'peter.daniel@hkr.se','8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92'),(10,'daniel@yahoo.com','31da895dff55475c8f24cc504ea9b2ceeceb4daf8446b12a2c113930d82ebf6c'),(11,'emma@yahoo.com','52293754fdbea92ab6c69cd64e644deed1552f40ccd3c1cef9d4d63c754d13e3'),(12,'anna@yahoo.com','7f92063f461ca1991c0f734dca11d2c98d30e14f08c0e5a7f8e631eca3bc1b52');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (1,'Volleyball'),(2,'Football'),(3,'Boxing'),(4,'Chess');
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conversation`
--

DROP TABLE IF EXISTS `conversation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conversation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conversation`
--

LOCK TABLES `conversation` WRITE;
/*!40000 ALTER TABLE `conversation` DISABLE KEYS */;
/*!40000 ALTER TABLE `conversation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conversation_message`
--

DROP TABLE IF EXISTS `conversation_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conversation_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `conversation_id` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conversation_message`
--

LOCK TABLES `conversation_message` WRITE;
/*!40000 ALTER TABLE `conversation_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `conversation_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leader`
--

DROP TABLE IF EXISTS `leader`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leader` (
  `member_id` int(11) NOT NULL AUTO_INCREMENT,
  `key_number` varchar(25) NOT NULL,
  `board_position` varchar(45) NOT NULL,
  PRIMARY KEY (`member_id`),
  KEY `fk_leader_member_idx` (`member_id`),
  CONSTRAINT `fk_leader_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leader`
--

LOCK TABLES `leader` WRITE;
/*!40000 ALTER TABLE `leader` DISABLE KEYS */;
INSERT INTO `leader` VALUES (1,'DA006','Inventory manager');
/*!40000 ALTER TABLE `leader` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leader_has_activity`
--

DROP TABLE IF EXISTS `leader_has_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leader_has_activity` (
  `leader_member_id` int(11) NOT NULL,
  `activity_id` int(11) NOT NULL,
  PRIMARY KEY (`leader_member_id`,`activity_id`),
  KEY `fk_leader_has_activity_activity_idx` (`activity_id`),
  KEY `fk_leader_has_activity_leader_idx` (`leader_member_id`),
  CONSTRAINT `fk_leader_has_activity_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_leader_has_activity_leader` FOREIGN KEY (`leader_member_id`) REFERENCES `leader` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leader_has_activity`
--

LOCK TABLES `leader_has_activity` WRITE;
/*!40000 ALTER TABLE `leader_has_activity` DISABLE KEYS */;
INSERT INTO `leader_has_activity` VALUES (1,1),(1,4);
/*!40000 ALTER TABLE `leader_has_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `middlename` varchar(45) NOT NULL DEFAULT 'Null',
  `surname` varchar(45) NOT NULL,
  `ssn` char(13) NOT NULL,
  `mobile` varchar(25) NOT NULL,
  `birthday` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1,'Deniel','','Alekseev','12341212-1234','+123-123-12-12','1997-05-22'),(2,'Bob','Meloni','Sinclar','19801204-1234','+123-123-12-12','1980-12-04'),(3,'Peter','','Daniel','12340101-0123','+070-070-07-07','1930-01-01'),(10,'Daniel',' A.','Anderson','991010-3434','+070-000-22-22','1999-10-09'),(11,'Emma',' E.','Erikson','891010-3434','+071-110-88-22','1989-10-09'),(12,'Anna',' E.','Patrik','950702-0834','+076-240-80-22','1995-07-01');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fromMobile` varchar(45) NOT NULL,
  `toMobile` varchar(45) NOT NULL,
  `message` varchar(1000) NOT NULL,
  `timestamp` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resetpwtoken`
--

DROP TABLE IF EXISTS `resetpwtoken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resetpwtoken` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) DEFAULT NULL,
  `token` varchar(45) DEFAULT NULL,
  `futuredate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resetpwtoken`
--

LOCK TABLES `resetpwtoken` WRITE;
/*!40000 ALTER TABLE `resetpwtoken` DISABLE KEYS */;
/*!40000 ALTER TABLE `resetpwtoken` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `week` blob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (1,_binary '\n\0d09001245Chess\0\0\0\0\0S07000830\nVolleyball\0\0'),(2,_binary '}\0	\0\0S08301045\nVolleyball\0\0	\0\0S08301045\nVolleyball\0\0\r\0\0S08151130\nVolleyball\0\0	\0\0S09151045\nVolleyball\0\0\0\0S08001045\nVolleyball\0\0		\0\0S09151130\nVolleyball\0');
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_conversation`
--

DROP TABLE IF EXISTS `user_conversation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_conversation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `conversation_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_conversation`
--

LOCK TABLES `user_conversation` WRITE;
/*!40000 ALTER TABLE `user_conversation` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_conversation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userlogon`
--

DROP TABLE IF EXISTS `userlogon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userlogon` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `password` blob NOT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userlogon`
--

LOCK TABLES `userlogon` WRITE;
/*!40000 ALTER TABLE `userlogon` DISABLE KEYS */;
/*!40000 ALTER TABLE `userlogon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `username` varchar(15) NOT NULL,
  `password` varchar(12) NOT NULL,
  `name` varchar(25) NOT NULL,
  `dob` date NOT NULL,
  `gender` varchar(7) NOT NULL,
  `address` varchar(255) NOT NULL,
  `nic` varchar(10) NOT NULL,
  `contact` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-29  0:21:47
