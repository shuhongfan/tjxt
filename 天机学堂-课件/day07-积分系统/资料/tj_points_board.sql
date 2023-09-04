-- --------------------------------------------------------
-- 主机:                           192.168.150.101
-- 服务器版本:                        8.0.29 - MySQL Community Server - GPL
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  12.2.0.6576
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 tj_learning 的数据库结构
CREATE DATABASE IF NOT EXISTS `tj_learning` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `tj_learning`;

-- 导出  表 tj_learning.points_board 结构
CREATE TABLE IF NOT EXISTS `points_board` (
  `id` bigint NOT NULL COMMENT '榜单id',
  `user_id` bigint NOT NULL COMMENT '学生id',
  `points` int NOT NULL COMMENT '积分值',
  `rank` tinyint NOT NULL COMMENT '名次，只记录赛季前100',
  `season` smallint NOT NULL COMMENT '赛季，例如 1,就是第一赛季，2-就是第二赛季',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_season_user` (`season`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='学霸天梯榜';

-- 正在导出表  tj_learning.points_board 的数据：~0 rows (大约)
DELETE FROM `points_board`;

-- 导出  表 tj_learning.points_board_season 结构
CREATE TABLE IF NOT EXISTS `points_board_season` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增长id，season标示',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '赛季名称，例如：第1赛季',
  `begin_time` date NOT NULL COMMENT '赛季开始时间',
  `end_time` date NOT NULL COMMENT '赛季结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- 正在导出表  tj_learning.points_board_season 的数据：~12 rows (大约)
DELETE FROM `points_board_season`;
INSERT INTO `points_board_season` (`id`, `name`, `begin_time`, `end_time`) VALUES
	(1, '第1赛季', '2022-10-01', '2022-10-31'),
	(2, '第2赛季', '2022-11-01', '2022-11-30'),
	(3, '第3赛季', '2022-12-01', '2022-12-31'),
	(4, '第4赛季', '2023-01-01', '2023-01-31'),
	(5, '第5赛季', '2023-02-01', '2023-02-28'),
	(6, '第6赛季', '2023-03-01', '2023-03-31'),
	(7, '第7赛季', '2023-04-01', '2023-04-30'),
	(8, '第8赛季', '2023-05-01', '2023-05-31'),
	(9, '第9赛季', '2023-06-01', '2023-06-30'),
	(10, '第10赛季', '2023-07-01', '2023-07-31'),
	(11, '第11赛季', '2023-08-01', '2023-08-31'),
	(12, '第12赛季', '2023-09-01', '2023-09-30');

-- 导出  表 tj_learning.points_record 结构
CREATE TABLE IF NOT EXISTS `points_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '积分记录表id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `type` tinyint NOT NULL COMMENT '积分方式：1-课程学习，2-每日签到，3-课程问答， 4-课程笔记，5-课程评价',
  `points` tinyint NOT NULL COMMENT '积分值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`,`type`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='学习积分记录，每个月底清零';

-- 正在导出表  tj_learning.points_record 的数据：~0 rows (大约)
DELETE FROM `points_record`;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
