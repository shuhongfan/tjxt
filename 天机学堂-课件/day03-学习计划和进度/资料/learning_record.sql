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

-- 导出  表 tj_learning.learning_record 结构
CREATE TABLE IF NOT EXISTS `learning_record` (
  `id` bigint NOT NULL COMMENT '学习记录的id',
  `lesson_id` bigint NOT NULL COMMENT '对应课表的id',
  `section_id` bigint NOT NULL COMMENT '对应小节的id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `moment` int DEFAULT '0' COMMENT '视频的当前观看时间点，单位秒',
  `finished` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否完成学习，默认false',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '第一次观看时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成学习的时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（最近一次观看时间）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_update_time` (`update_time`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_lesson_id` (`lesson_id`,`section_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='学习记录表';

-- 正在导出表  tj_learning.learning_record 的数据：~14 rows (大约)
DELETE FROM `learning_record`;
INSERT INTO `learning_record` (`id`, `lesson_id`, `section_id`, `user_id`, `moment`, `finished`, `create_time`, `finish_time`, `update_time`) VALUES
	(1582555272977592322, 1, 16, 2, 239, b'1', '2022-10-19 10:12:34', '2022-10-19 10:45:55', '2022-10-19 15:50:12'),
	(1582565729037791233, 1, 17, 2, 148, b'1', '2022-10-19 10:54:07', '2022-10-19 10:57:47', '2022-10-19 15:50:05'),
	(1582572518466760706, 1, 18, 2, 14, b'1', '2022-10-19 11:21:06', '2022-10-19 14:52:50', '2022-10-27 12:05:14'),
	(1582572534866489346, 1, 19, 2, 250, b'1', '2022-10-19 11:21:10', '2022-10-19 17:53:55', '2022-10-19 17:56:01'),
	(1582572535164284930, 1, 20, 2, 135, b'1', '2022-10-19 11:21:10', '2022-10-19 17:58:09', '2022-10-27 12:07:12'),
	(1582572537429209089, 1, 21, 2, 253, b'1', '2022-10-19 11:21:10', '2022-10-19 18:02:34', '2022-10-19 18:04:54'),
	(1582674101338656770, 1, 22, 2, 257, b'1', '2022-10-19 18:04:45', '2022-10-19 18:07:15', '2022-10-19 18:09:35'),
	(1582675262523326466, 1, 23, 2, 254, b'1', '2022-10-19 18:09:22', '2022-10-19 18:11:37', '2022-10-19 18:13:57'),
	(1582676374013886465, 1, 24, 2, 250, b'1', '2022-10-19 18:13:47', '2022-10-19 18:16:02', '2022-10-20 11:36:50'),
	(1582938844335001602, 1, 25, 2, 80, b'1', '2022-10-20 11:36:44', '2022-10-20 11:38:59', '2022-10-20 11:58:00'),
	(1583012729738776577, 1, 26, 2, 262, b'1', '2022-10-20 16:30:20', '2022-10-20 16:30:21', '2022-10-20 16:38:39'),
	(1586757474101342209, 1, 27, 2, 0, b'1', '2022-10-31 00:30:37', '2022-10-31 00:30:36', '2022-10-31 00:30:37'),
	(1586757474101342309, 2, 29, 2, 10, b'0', '2022-10-31 00:30:37', NULL, '2022-10-31 00:30:37'),
	(1599780755855228929, 1585170299127607297, 16, 129, 37, b'0', '2022-12-05 23:00:29', NULL, '2022-12-05 23:01:34');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
