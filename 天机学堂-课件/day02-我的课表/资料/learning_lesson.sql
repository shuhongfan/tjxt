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

-- 导出  表 tj_learning.learning_lesson 结构
DROP TABLE IF EXISTS `learning_lesson`;
CREATE TABLE IF NOT EXISTS `learning_lesson` (
  `id` bigint NOT NULL COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '学员id',
  `course_id` bigint NOT NULL COMMENT '课程id',
  `status` tinyint DEFAULT '0' COMMENT '课程状态，0-未学习，1-学习中，2-已学完，3-已失效',
  `week_freq` tinyint DEFAULT NULL COMMENT '每周学习频率，例如每周学习6小节，则频率为6',
  `plan_status` tinyint NOT NULL DEFAULT '0' COMMENT '学习计划状态，0-没有计划，1-计划进行中',
  `learned_sections` int NOT NULL DEFAULT '0' COMMENT '已学习小节数量',
  `latest_section_id` bigint DEFAULT NULL COMMENT '最近一次学习的小节id',
  `latest_learn_time` datetime DEFAULT NULL COMMENT '最近一次学习的时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_user_id` (`user_id`,`course_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='学生课程表';

-- 正在导出表  tj_learning.learning_lesson 的数据：~4 rows (大约)
DELETE FROM `learning_lesson`;
INSERT INTO `learning_lesson` (`id`, `user_id`, `course_id`, `status`, `week_freq`, `plan_status`, `learned_sections`, `latest_section_id`, `latest_learn_time`, `create_time`, `expire_time`, `update_time`) VALUES
	(1, 2, 2, 2, 6, 1, 12, 16, '2023-04-11 22:34:45', '2022-08-05 20:02:50', '2023-08-05 20:02:29', '2023-04-19 10:29:29'),
	(2, 2, 3, 1, 4, 1, 3, 31, '2023-04-19 11:42:50', '2022-08-06 15:16:48', '2023-08-06 15:16:37', '2023-04-19 11:42:50'),
	(1585170299127607297, 129, 2, 0, NULL, 0, 0, 16, '2023-04-11 22:37:05', '2022-12-05 23:00:29', '2023-10-26 15:14:54', '2023-04-11 22:37:05'),
	(1601061367207464961, 2, 1549025085494521857, 1, 3, 1, 4, 1550383240983875589, '2023-04-11 16:34:44', '2022-12-09 11:49:11', '2023-12-09 11:49:11', '2023-04-11 16:34:43');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
