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
-- Table structure for table `learna_course`
--

DROP TABLE IF EXISTS `learna_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `learna_course` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_dte` datetime DEFAULT NULL,
  `description` text,
  `difficulty_level` varchar(255) NOT NULL,
  `discounted_price` double NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `lesson_type` varchar(255) NOT NULL,
  `modified_dte` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `original_price` double NOT NULL,
  `category_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learna_course`
--

LOCK TABLES `learna_course` WRITE;
/*!40000 ALTER TABLE `learna_course` DISABLE KEYS */;
INSERT INTO `learna_course` VALUES (1,'2024-09-10 10:45:15','Learn Python like a Professional Start from the basics and go all the way to creating your own applications and games','BEGINNER',500000,'https://img-c.udemycdn.com/course/240x135/567828_67d0.jpg','VIDEO','2024-09-10 10:45:15','Complete Python Bootcamp',2000000,0),(2,'2024-09-10 10:45:15','The only course you need to learn web development - HTML, CSS, JS, Node, and More!','BEGINNER',600000,'https://img-c.udemycdn.com/course/240x135/625204_436a_2.jpg','VIDEO','2024-09-10 10:45:15','The Web Developer Bootcamp',2500000,0),(3,'2024-09-10 10:45:15','Learn to create Machine Learning Algorithms in Python and R from two Data Science experts. Code templates included.','INTERMEDIATE',700000,'https://img-c.udemycdn.com/course/240x135/950390_270f_3.jpg','VIDEO','2024-09-10 10:45:15','Machine Learning A-Z',3000000,0),(4,'2024-09-10 10:45:15','Learn Java In This Course And Become a Computer Programmer. Obtain valuable Core Java Skills And Java Certification','BEGINNER',550000,'https://img-c.udemycdn.com/course/240x135/533682_c10c_4.jpg','VIDEO','2024-09-10 10:45:15','Java Programming Masterclass',2200000,0),(5,'2024-09-10 10:45:15','Master JavaScript with the most complete course! Projects, challenges, quizzes, JavaScript ES6+, OOP, AJAX, Webpack','INTERMEDIATE',600000,'https://img-c.udemycdn.com/course/240x135/851712_fc61_6.jpg','VIDEO','2024-09-10 10:45:15','The Complete JavaScript Course 2021',2400000,0),(6,'2024-09-10 10:45:15','Dive in and learn React.js from scratch! Learn Reactjs, Hooks, Redux, React Routing, Animations, Next.js and way more!','INTERMEDIATE',650000,'https://img-c.udemycdn.com/course/240x135/1362070_b9a1_2.jpg','VIDEO','2024-09-10 10:45:15','React - The Complete Guide',2600000,0),(7,'2024-09-10 10:45:15','Master Angular (formerly \"Angular 2\") and build awesome, reactive web apps with the successor of Angular.js','ADVANCED',700000,'https://img-c.udemycdn.com/course/240x135/756150_c033_2.jpg','VIDEO','2024-09-10 10:45:15','Angular - The Complete Guide',2800000,0),(8,'2024-09-10 10:45:15','Build, test, and deploy Docker applications with Kubernetes while learning production-style development workflows','ADVANCED',800000,'https://img-c.udemycdn.com/course/240x135/1793828_7999.jpg','VIDEO','2024-09-10 10:45:15','Docker and Kubernetes: The Complete Guide',3200000,0),(9,'2024-09-10 10:45:15','Go from SQL Beginner to Expert! Learn SQL for Data Analysis, Business Intelligence, and Data Science','BEGINNER',500000,'https://img-c.udemycdn.com/course/240x135/1187016_51b3.jpg','VIDEO','2024-09-10 10:45:15','The Ultimate MySQL Bootcamp',2000000,0),(10,'2024-09-10 10:45:15','From Beginner to iOS App Developer with Just One Course! Fully Updated with a Comprehensive Module Dedicated to SwiftUI!','INTERMEDIATE',750000,'https://img-c.udemycdn.com/course/240x135/1778502_f4b9_12.jpg','VIDEO','2024-09-10 10:45:15','iOS 13 & Swift 5 - The Complete iOS App Development Bootcamp',3000000,0),(11,'2024-09-10 10:47:11','Learn Python like a Professional Start from the basics and go all the way to creating your own applications and games','BEGINNER',500000,'https://img-c.udemycdn.com/course/240x135/567828_67d0.jpg','VIDEO','2024-09-10 10:47:11','Complete Python Bootcamp',2000000,0),(12,'2024-09-10 10:47:11','The only course you need to learn web development - HTML, CSS, JS, Node, and More!','BEGINNER',600000,'https://img-c.udemycdn.com/course/240x135/625204_436a_2.jpg','VIDEO','2024-09-10 10:47:11','The Web Developer Bootcamp',2500000,0),(13,'2024-09-10 10:47:11','Learn to create Machine Learning Algorithms in Python and R from two Data Science experts. Code templates included.','INTERMEDIATE',700000,'https://img-c.udemycdn.com/course/240x135/950390_270f_3.jpg','VIDEO','2024-09-10 10:47:11','Machine Learning A-Z',3000000,0),(14,'2024-09-10 10:47:11','Learn Java In This Course And Become a Computer Programmer. Obtain valuable Core Java Skills And Java Certification','BEGINNER',550000,'https://img-c.udemycdn.com/course/240x135/533682_c10c_4.jpg','VIDEO','2024-09-10 10:47:11','Java Programming Masterclass',2200000,0),(15,'2024-09-10 10:47:11','Master JavaScript with the most complete course! Projects, challenges, quizzes, JavaScript ES6+, OOP, AJAX, Webpack','INTERMEDIATE',600000,'https://img-c.udemycdn.com/course/240x135/851712_fc61_6.jpg','VIDEO','2024-09-10 10:47:11','The Complete JavaScript Course 2021',2400000,0),(16,'2024-09-10 10:47:11','Dive in and learn React.js from scratch! Learn Reactjs, Hooks, Redux, React Routing, Animations, Next.js and way more!','INTERMEDIATE',650000,'https://img-c.udemycdn.com/course/240x135/1362070_b9a1_2.jpg','VIDEO','2024-09-10 10:47:11','React - The Complete Guide',2600000,0),(17,'2024-09-10 10:47:11','Master Angular (formerly \"Angular 2\") and build awesome, reactive web apps with the successor of Angular.js','ADVANCED',700000,'https://img-c.udemycdn.com/course/240x135/756150_c033_2.jpg','VIDEO','2024-09-10 10:47:11','Angular - The Complete Guide',2800000,0),(18,'2024-09-10 10:47:11','Build, test, and deploy Docker applications with Kubernetes while learning production-style development workflows','ADVANCED',800000,'https://img-c.udemycdn.com/course/240x135/1793828_7999.jpg','VIDEO','2024-09-10 10:47:11','Docker and Kubernetes: The Complete Guide',3200000,0),(19,'2024-09-10 10:47:11','Go from SQL Beginner to Expert! Learn SQL for Data Analysis, Business Intelligence, and Data Science','BEGINNER',500000,'https://img-c.udemycdn.com/course/240x135/1187016_51b3.jpg','VIDEO','2024-09-10 10:47:11','The Ultimate MySQL Bootcamp',2000000,0),(20,'2024-09-10 10:47:11','From Beginner to iOS App Developer with Just One Course! Fully Updated with a Comprehensive Module Dedicated to SwiftUI!','INTERMEDIATE',750000,'https://img-c.udemycdn.com/course/240x135/1778502_f4b9_12.jpg','VIDEO','2024-09-10 10:47:11','iOS 13 & Swift 5 - The Complete iOS App Development Bootcamp',3000000,0),(21,'2024-09-10 10:47:11','Learn how to use the R programming language for data science and machine learning and data visualization!','INTERMEDIATE',700000,'https://img-c.udemycdn.com/course/240x135/777890_0b61_2.jpg','VIDEO','2024-09-10 10:47:11','Data Science and Machine Learning Bootcamp with R',2800000,0),(22,'2024-09-10 10:47:11','Learn Node.js by building real-world applications with Node, Express, MongoDB, Jest, and more!','INTERMEDIATE',600000,'https://img-c.udemycdn.com/course/240x135/922484_52a1_8.jpg','VIDEO','2024-09-10 10:47:11','The Complete Node.js Developer Course',2500000,0),(23,'2024-09-10 10:47:11','Understand React Native with Hooks, Context, and React Navigation.','INTERMEDIATE',675000,'https://img-c.udemycdn.com/course/240x135/959700_8bd2_11.jpg','VIDEO','2024-09-10 10:47:11','The Complete React Native + Hooks Course',2700000,0),(24,'2024-09-10 10:47:11','The most advanced and modern CSS course on the internet: master flexbox, CSS Grid, responsive design, and so much more.','ADVANCED',650000,'https://img-c.udemycdn.com/course/240x135/1026604_790b_2.jpg','VIDEO','2024-09-10 10:47:11','Advanced CSS and Sass: Flexbox, Grid, Animations and More!',2600000,0),(25,'2024-09-10 10:47:11','Learn advanced React patterns, hooks, and techniques to create complex and scalable applications.','ADVANCED',750000,'https://img-c.udemycdn.com/course/240x135/1501104_967d_2.jpg','VIDEO','2024-09-10 10:47:11','The Complete Guide to Advanced React Patterns',3000000,0),(26,'2024-09-10 10:47:11','Become an expert at SQL! Learn how to create and manage databases, write complex queries, and more.','BEGINNER',550000,'https://img-c.udemycdn.com/course/240x135/762616_7693_3.jpg','VIDEO','2024-09-10 10:47:11','The Complete SQL Bootcamp 2021',2200000,0),(27,'2024-09-10 10:47:11','Learn to code and become a web developer in 2021 with HTML, CSS, Javascript, React, Node.js, Machine Learning & more!','BEGINNER',600000,'https://img-c.udemycdn.com/course/240x135/1430746_2f43_9.jpg','VIDEO','2024-09-10 10:47:11','The Complete Web Developer in 2021: Zero to Mastery',2500000,0),(28,'2024-09-10 10:47:11','Master Digital Marketing Strategy, Social Media Marketing, SEO, YouTube, Email, Facebook Marketing, Analytics & More!','BEGINNER',750000,'https://img-c.udemycdn.com/course/240x135/914296_3670_8.jpg','VIDEO','2024-09-10 10:47:11','The Complete Digital Marketing Course - 12 Courses in 1',3000000,0),(29,'2024-09-10 10:47:11','Excel, Accounting, Financial Statement Analysis, Business Analysis, Financial Math, PowerPoint: Everything is Included!','BEGINNER',700000,'https://img-c.udemycdn.com/course/240x135/648826_f0e5_4.jpg','VIDEO','2024-09-10 10:47:11','The Complete Financial Analyst Course 2021',2800000,0),(30,'2024-09-10 10:47:11','Learn everything you need to know about cryptocurrency and blockchain, including investing, mining, and more.','BEGINNER',800000,'https://img-c.udemycdn.com/course/240x135/1840738_2f4b_2.jpg','VIDEO','2024-09-10 10:47:11','The Complete Cryptocurrency Investment Course',3200000,0);
/*!40000 ALTER TABLE `learna_course` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-14 21:04:42
