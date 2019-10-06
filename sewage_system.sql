-- MySQL dump 10.13  Distrib 8.0.16, for macos10.14 (x86_64)
--
-- Host: cdb-8zkw6uw6.bj.tencentcdb.com    Database: sewage_system
-- ------------------------------------------------------
-- Server version	5.7.18-20170830-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'b1a00d79-d08d-11e9-92c9-5254002ec3e5:1-375';

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `title` varchar(200) NOT NULL,
  `content` text NOT NULL,
  `job_type_id` int(11) NOT NULL DEFAULT '0',
  `telephone` char(12) DEFAULT NULL,
  `email` varchar(320) DEFAULT NULL,
  `is_valid` bit(1) NOT NULL DEFAULT b'1',
  `creator_uid` int(11) DEFAULT NULL,
  `processor_uid` int(11) DEFAULT NULL,
  `inspector_uid` int(11) DEFAULT NULL,
  `status` char(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (51,'2019-09-29 11:36:02','水管炸裂','设备故障问题，详情见附件',2,'13131548484','123@1.com',_binary '',1,3,NULL,'2'),(52,'2019-09-29 12:01:58','测试','内容',1,'13145465445','21@qq.com',_binary '',1,1,1,'6'),(53,'2019-09-29 13:12:47','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(54,'2019-09-29 13:12:47','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(55,'2019-09-29 13:12:47','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(56,'2019-09-29 13:12:47','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(57,'2019-09-29 13:12:47','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(58,'2019-09-29 13:17:05','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(59,'2019-09-29 13:17:11','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(60,'2019-09-29 13:17:11','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(61,'2019-09-29 13:17:11','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(62,'2019-09-29 13:17:11','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(63,'2019-09-29 13:17:11','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(64,'2019-09-29 13:17:11','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(65,'2019-09-29 13:17:11','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(66,'2019-09-29 13:17:11','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,NULL,NULL,'1'),(67,'2019-09-29 13:17:12','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,0,NULL,'2'),(68,'2019-09-29 13:17:12','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,1,1,'6'),(69,'2019-09-29 13:17:12','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,0,NULL,'2'),(70,'2019-09-29 13:17:12','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,3,NULL,'2'),(71,'2019-09-29 13:17:12','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,0,NULL,'2'),(72,'2019-09-29 13:17:12','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,2,NULL,'2'),(73,'2019-09-29 13:17:12','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,2,NULL,'2'),(74,'2019-09-29 13:17:12','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,1,NULL,'4'),(75,'2019-09-29 13:17:12','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,1,NULL,'2'),(76,'2019-09-29 13:17:12','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,1,NULL,'2'),(77,'2019-09-29 13:17:12','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,1,NULL,'2'),(78,'2019-09-29 13:17:12','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,1,NULL,'2'),(79,'2019-09-29 13:17:13','测试','测试内容，不要动',2,'18111111111','test@test.com',_binary '',1,5,NULL,'2'),(80,'2019-09-29 13:18:11','设备故障','设备损坏',2,'13115454545','111@1.com',_binary '',1,1,1,'6'),(81,'2019-09-29 14:38:51','水管炸裂','见附件',1,'13131545455','1@1.com',_binary '',1,1,1,'6'),(82,'2019-09-29 14:39:24','投诉客服','无',1,'13131545455','13131545455@qq.com',_binary '',1,1,1,'5'),(83,'2019-09-29 14:40:05','电路故障','供电异常',2,'13131545455','13131545455@q.com',_binary '',1,1,1,'6'),(84,'2019-09-29 17:27:45','测试断电','断电了',2,'13154545452','1@1.com',_binary '',1,4,NULL,'2'),(85,'2019-10-02 12:52:27','3','32',1,'13123213112','2@qq.com',_binary '',1,1,1,'5'),(86,'2019-10-02 07:43:25','测试','哈哈哈',1,'13212131212','12@qq.com',_binary '',1,1,1,'5'),(87,'2019-10-02 09:24:49','11111','111',1,'13123213131','1@1.com',_binary '',1,NULL,NULL,'1');
/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_process`
--

DROP TABLE IF EXISTS `job_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `job_process` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `type` char(3) NOT NULL,
  `is_finished` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime DEFAULT NULL,
  `is_valid` bit(1) NOT NULL DEFAULT b'1',
  `serial_number` int(11) DEFAULT '0',
  `remark` text,
  `file` varchar(320) DEFAULT NULL,
  `job_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_process`
--

LOCK TABLES `job_process` WRITE;
/*!40000 ALTER TABLE `job_process` DISABLE KEYS */;
INSERT INTO `job_process` VALUES (103,1,'1',_binary '\0','2019-09-29 11:36:02',_binary '',0,NULL,NULL,51),(104,1,'2',_binary '\0','2019-09-29 11:43:44',_binary '',0,NULL,NULL,51),(105,1,'1',_binary '\0','2019-09-29 12:01:58',_binary '',0,NULL,NULL,52),(106,1,'2',_binary '\0','2019-09-29 12:02:10',_binary '',0,NULL,NULL,52),(107,1,'4',_binary '\0','2019-09-29 12:02:46',_binary '',0,'完成',NULL,52),(108,1,'6',_binary '\0','2019-09-29 12:03:19',_binary '',0,'不通过',NULL,52),(109,1,'3',_binary '\0','2019-09-29 12:19:27',_binary '',0,NULL,NULL,51),(110,3,'2',_binary '\0','2019-09-29 12:19:27',_binary '',0,NULL,NULL,51),(111,1,'1',_binary '\0','2019-09-29 13:12:47',_binary '',0,NULL,NULL,53),(112,1,'1',_binary '\0','2019-09-29 13:18:11',_binary '',0,NULL,NULL,80),(113,1,'2',_binary '\0','2019-09-29 13:18:29',_binary '',0,NULL,NULL,80),(114,1,'2',_binary '\0','2019-09-29 13:18:29',_binary '',0,NULL,NULL,79),(115,1,'2',_binary '\0','2019-09-29 13:18:29',_binary '',0,NULL,NULL,67),(116,1,'2',_binary '\0','2019-09-29 13:18:29',_binary '',0,NULL,NULL,68),(117,1,'2',_binary '\0','2019-09-29 13:18:29',_binary '',0,NULL,NULL,69),(118,1,'2',_binary '\0','2019-09-29 13:18:29',_binary '',0,NULL,NULL,70),(119,1,'2',_binary '\0','2019-09-29 13:18:29',_binary '',0,NULL,NULL,71),(120,1,'2',_binary '\0','2019-09-29 13:18:29',_binary '',0,NULL,NULL,72),(121,1,'2',_binary '\0','2019-09-29 13:18:29',_binary '',0,NULL,NULL,73),(122,1,'2',_binary '\0','2019-09-29 13:18:29',_binary '',0,NULL,NULL,74),(123,1,'2',_binary '\0','2019-09-29 13:18:52',_binary '',0,NULL,NULL,76),(124,1,'3',_binary '\0','2019-09-29 13:19:14',_binary '',0,NULL,NULL,72),(125,2,'2',_binary '\0','2019-09-29 13:19:14',_binary '',0,NULL,NULL,72),(126,1,'3',_binary '\0','2019-09-29 13:19:14',_binary '',0,NULL,NULL,73),(127,2,'2',_binary '\0','2019-09-29 13:19:14',_binary '',0,NULL,NULL,73),(128,1,'4',_binary '\0','2019-09-29 13:20:15',_binary '',0,'处理完了',NULL,80),(129,1,'6',_binary '\0','2019-09-29 13:20:45',_binary '',0,'不行',NULL,80),(130,1,'1',_binary '\0','2019-09-29 14:38:52',_binary '',0,NULL,NULL,81),(131,1,'1',_binary '\0','2019-09-29 14:39:24',_binary '',0,NULL,NULL,82),(132,1,'1',_binary '\0','2019-09-29 14:40:05',_binary '',0,NULL,NULL,83),(133,1,'2',_binary '\0','2019-09-29 14:40:12',_binary '',0,NULL,NULL,83),(134,1,'2',_binary '\0','2019-09-29 14:40:12',_binary '',0,NULL,NULL,82),(135,1,'2',_binary '\0','2019-09-29 14:40:12',_binary '',0,NULL,NULL,81),(136,1,'4',_binary '\0','2019-09-29 14:41:01',_binary '',0,'我处理完了，你们审核下',NULL,82),(137,1,'4',_binary '\0','2019-09-29 14:41:55',_binary '',0,'处理不了，谢谢',NULL,83),(138,1,'4',_binary '\0','2019-09-29 14:42:23',_binary '',0,'故障已排除',NULL,81),(139,1,'5',_binary '\0','2019-09-29 14:43:11',_binary '',0,'确认完成',NULL,82),(140,1,'1',_binary '\0','2019-09-29 17:27:45',_binary '',0,NULL,NULL,84),(141,1,'6',_binary '\0','2019-09-29 17:29:17',_binary '',0,'1',NULL,83),(142,1,'1',_binary '\0','2019-10-02 12:52:27',_binary '',0,NULL,NULL,85),(143,1,'2',_binary '\0','2019-10-02 12:52:36',_binary '',0,NULL,NULL,85),(144,1,'3',_binary '\0','2019-10-02 12:52:54',_binary '',0,NULL,NULL,79),(145,5,'2',_binary '\0','2019-10-02 12:52:54',_binary '',0,NULL,NULL,79),(146,1,'4',_binary '\0','2019-10-02 12:53:24',_binary '',0,NULL,NULL,85),(147,1,'5',_binary '\0','2019-10-02 12:53:36',_binary '',0,NULL,NULL,85),(148,1,'6',_binary '\0','2019-10-02 12:53:42',_binary '',0,NULL,NULL,81),(149,1,'1',_binary '\0','2019-10-02 07:43:25',_binary '',0,NULL,NULL,86),(150,1,'2',_binary '\0','2019-10-02 07:54:59',_binary '',0,NULL,NULL,86),(151,1,'3',_binary '\0','2019-10-02 07:56:12',_binary '',0,NULL,NULL,67),(152,0,'2',_binary '\0','2019-10-02 07:56:12',_binary '',0,NULL,NULL,67),(153,1,'4',_binary '\0','2019-10-02 08:22:05',_binary '',0,NULL,NULL,86),(154,1,'5',_binary '\0','2019-10-02 08:22:09',_binary '',0,NULL,NULL,86),(155,1,'4',_binary '\0','2019-10-02 08:22:14',_binary '',0,NULL,NULL,68),(156,1,'6',_binary '\0','2019-10-02 08:22:16',_binary '',0,NULL,NULL,68),(157,1,'3',_binary '\0','2019-10-02 08:22:31',_binary '',0,NULL,NULL,69),(158,0,'2',_binary '\0','2019-10-02 08:22:31',_binary '',0,NULL,NULL,69),(159,1,'3',_binary '\0','2019-10-02 08:25:13',_binary '',0,NULL,NULL,71),(160,0,'2',_binary '\0','2019-10-02 08:25:13',_binary '',0,NULL,NULL,71),(161,1,'4',_binary '\0','2019-10-02 09:01:23',_binary '',0,NULL,NULL,74),(162,1,'2',_binary '\0','2019-10-02 09:01:35',_binary '',0,NULL,NULL,75),(163,1,'2',_binary '\0','2019-10-02 09:01:49',_binary '',0,NULL,NULL,84),(164,1,'3',_binary '\0','2019-10-02 09:02:15',_binary '',0,NULL,NULL,70),(165,3,'2',_binary '\0','2019-10-02 09:02:15',_binary '',0,NULL,NULL,70),(166,1,'3',_binary '\0','2019-10-02 09:11:28',_binary '',0,NULL,NULL,84),(167,4,'2',_binary '\0','2019-10-02 09:11:28',_binary '',0,NULL,NULL,84),(168,1,'2',_binary '\0','2019-10-02 09:24:00',_binary '',0,NULL,NULL,77),(169,1,'2',_binary '\0','2019-10-02 09:24:05',_binary '',0,NULL,NULL,78),(170,1,'1',_binary '\0','2019-10-02 09:24:49',_binary '',0,NULL,NULL,87);
/*!40000 ALTER TABLE `job_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_type`
--

DROP TABLE IF EXISTS `job_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `job_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_type_name` varchar(200) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_valid` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_type`
--

LOCK TABLES `job_type` WRITE;
/*!40000 ALTER TABLE `job_type` DISABLE KEYS */;
INSERT INTO `job_type` VALUES (1,'投诉/建议','2019-09-22 21:13:17','2019-09-22 21:13:19',_binary ''),(2,'设备故障','2019-09-22 21:13:37','2019-09-22 21:13:40',_binary '');
/*!40000 ALTER TABLE `job_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `permission` (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `identity` varchar(255) NOT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (1,'admin'),(2,'user');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `delete_status` int(11) NOT NULL,
  `password` varchar(255) NOT NULL,
  `pid` int(11) NOT NULL,
  `salt` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `area` varchar(255) NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,0,'51d2e335d32a961373248d6a9cc6502f',1,'f381ab6af95dffc7d7c277f9bc3a8d69','admin','临沧市;玉溪市;昆明市;未标记城市;三门峡市;'),(4,0,'98e89551afebca746d93fbdfb82a58da',2,'6ef8d6acdccdfd9bea3dc345e5f59fb8','test','玉溪市;昆明市;三门峡市;'),(5,0,'7742be1c9334c62f056d41ac716acb07',1,'f381ab6af95dffc7d7c277f9bc3a8d69','shuchao','临沧市;玉溪市;昆明市;未标记城市;三门峡市;');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-06 14:57:24
