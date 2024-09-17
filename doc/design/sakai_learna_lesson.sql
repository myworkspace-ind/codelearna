-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sakai
-- ------------------------------------------------------
-- Server version	8.0.39

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
-- Table structure for table `learna_lesson`
--

DROP TABLE IF EXISTS `learna_lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `learna_lesson` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_dte` datetime DEFAULT NULL,
  `modified_dte` datetime DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `video_url` varchar(255) DEFAULT NULL,
  `course_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKohvapyxwki3e21upbt7xwwtax` (`course_id`),
  CONSTRAINT `FKohvapyxwki3e21upbt7xwwtax` FOREIGN KEY (`course_id`) REFERENCES `learna_course` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learna_lesson`
--

LOCK TABLES `learna_lesson` WRITE;
/*!40000 ALTER TABLE `learna_lesson` DISABLE KEYS */;
INSERT INTO `learna_lesson` VALUES (1,'2024-09-13 15:47:53','2024-09-13 15:47:53','Introduction to Java','http://example.com/java-intro.mp4',1),(2,'2024-09-13 15:47:53','2024-09-13 15:47:53','Advanced Java','http://example.com/java-advanced.mp4',1);
/*!40000 ALTER TABLE `learna_lesson` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-14 21:04:41
