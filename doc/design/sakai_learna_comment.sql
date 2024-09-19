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
-- Table structure for table `learna_comment`
--

DROP TABLE IF EXISTS `learna_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `learna_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(1000) NOT NULL,
  `created_dte` datetime DEFAULT NULL,
  `modified_dte` datetime DEFAULT NULL,
  `lesson_id` bigint NOT NULL,
  `parent_comment_id` bigint DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgmncqwk1a5y6rajd6egmwcqjj` (`lesson_id`),
  KEY `FKjilgogem1vrubwado7kuuubio` (`parent_comment_id`),
  KEY `FKrnquv1e2p17tiftiqclftr2po` (`user_id`),
  CONSTRAINT `FKgmncqwk1a5y6rajd6egmwcqjj` FOREIGN KEY (`lesson_id`) REFERENCES `learna_lesson` (`id`),
  CONSTRAINT `FKjilgogem1vrubwado7kuuubio` FOREIGN KEY (`parent_comment_id`) REFERENCES `learna_comment` (`id`),
  CONSTRAINT `FKrnquv1e2p17tiftiqclftr2po` FOREIGN KEY (`user_id`) REFERENCES `learna_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learna_comment`
--

LOCK TABLES `learna_comment` WRITE;
/*!40000 ALTER TABLE `learna_comment` DISABLE KEYS */;
INSERT INTO `learna_comment` VALUES (1,'Great introduction to Java, very helpful!','2024-09-13 16:08:11','2024-09-13 16:08:11',1,NULL,1),(2,'I found this really useful as well. Thanks!','2024-09-13 16:08:11','2024-09-13 16:08:11',1,1,2),(3,'The advanced concepts were challenging but rewarding.','2024-09-13 16:08:11','2024-09-13 16:08:11',2,NULL,3),(4,'I agree, the topics were intense but valuable.','2024-09-13 16:08:11','2024-09-13 16:08:11',2,3,1),(30,'Great introduction to Java, very helpful!','2024-09-14 17:24:38','2024-09-14 17:24:38',1,NULL,1),(31,'I found the explanation of loops really clear.','2024-09-14 17:24:38','2024-09-14 17:24:38',1,NULL,1),(32,'Can you explain more about OOP concepts?','2024-09-14 17:24:38','2024-09-14 17:24:38',1,NULL,1),(33,'I really enjoyed the explanation of data structures.','2024-09-14 17:24:38','2024-09-14 17:24:38',1,NULL,3),(34,'Can you provide more examples?','2024-09-14 17:24:38','2024-09-14 17:24:38',1,NULL,3),(35,'This is exactly what I needed to understand the basics of Java.','2024-09-14 17:24:38','2024-09-14 17:24:38',1,NULL,4),(36,'The pace of the course is perfect for beginners like me.','2024-09-14 17:24:38','2024-09-14 17:24:38',1,NULL,5),(37,'Can you go over polymorphism again? It’s a bit confusing.','2024-09-14 17:24:38','2024-09-14 17:24:38',1,NULL,6),(38,'The quizzes really helped solidify my understanding.','2024-09-14 17:24:38','2024-09-14 17:24:38',1,NULL,7),(39,'Thank you for such a detailed explanation on methods.','2024-09-14 17:24:38','2024-09-14 17:24:38',1,NULL,8),(55,'I agree, the quizzes were really helpful.','2024-09-14 17:39:40','2024-09-14 17:39:40',1,38,2),(56,'Polymorphism can be tricky at first, but it’s essential!','2024-09-14 17:39:40','2024-09-14 17:39:40',1,37,4),(57,'Great job explaining OOP, looking forward to more!','2024-09-14 17:39:40','2024-09-14 17:39:40',1,32,5),(58,'I struggled with the methods part too. Thanks for the help!','2024-09-14 17:39:40','2024-09-14 17:39:40',1,39,6),(59,'The data structures explanation was excellent!','2024-09-14 17:39:40','2024-09-14 17:39:40',1,34,7);
/*!40000 ALTER TABLE `learna_comment` ENABLE KEYS */;
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
