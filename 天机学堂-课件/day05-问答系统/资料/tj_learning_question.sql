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

-- 导出  表 tj_learning.interaction_question 结构
CREATE TABLE IF NOT EXISTS `interaction_question` (
  `id` bigint NOT NULL COMMENT '主键，互动问题的id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '互动问题的标题',
  `description` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '问题描述信息',
  `course_id` bigint NOT NULL COMMENT '所属课程id',
  `chapter_id` bigint NOT NULL COMMENT '所属课程章id',
  `section_id` bigint NOT NULL COMMENT '所属课程节id',
  `user_id` bigint NOT NULL COMMENT '提问学员id',
  `latest_answer_id` bigint DEFAULT NULL COMMENT '最新的一个回答的id',
  `answer_times` int unsigned NOT NULL DEFAULT '0' COMMENT '问题下的回答数量',
  `anonymity` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否匿名，默认false',
  `hidden` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否被隐藏，默认false',
  `status` tinyint DEFAULT '0' COMMENT '管理端问题状态：0-未查看，1-已查看',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提问时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_course_id` (`course_id`) USING BTREE,
  KEY `section_id` (`section_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='互动提问的问题表';

-- 正在导出表  tj_learning.interaction_question 的数据：~3 rows (大约)
DELETE FROM `interaction_question`;
INSERT INTO `interaction_question` (`id`, `title`, `description`, `course_id`, `chapter_id`, `section_id`, `user_id`, `latest_answer_id`, `answer_times`, `anonymity`, `hidden`, `status`, `create_time`, `update_time`) VALUES
	(1552212554946768897, 'redis安装的时候有问题，一直报错是怎么回事？', 'redis安装的时候有问题，总是报错是怎么回事？redis安装的时候有问题，总是报错是怎么回事？redis安装的时候有问题，总是报错是怎么回事？redis安装的时候有问题，总是报错是怎么回事？', 2, 15, 17, 5, 1548889371405492225, 0, b'1', b'0', 1, '2022-07-27 16:41:27', '2023-01-31 21:46:47'),
	(1585089140469317634, 'JDK安装后为什么没有效果？', '我按照老师的方式安装了JDK，但是控制台输入 java -version没有效果啊', 2, 15, 16, 2, 1585178277083951106, 0, b'0', b'0', 1, '2022-10-26 10:01:16', '2023-01-31 21:46:47'),
	(1585589766919852033, 'Java的IO是阻塞IO吗？', '比如IO流中的数据读写', 2, 15, 17, 2, 1588103282121805825, 0, b'0', b'0', 1, '2022-10-27 12:31:44', '2023-01-31 21:46:47');

-- 导出  表 tj_learning.interaction_reply 结构
CREATE TABLE IF NOT EXISTS `interaction_reply` (
  `id` bigint NOT NULL COMMENT '互动问题的回答id',
  `question_id` bigint NOT NULL COMMENT '互动问题问题id',
  `answer_id` bigint DEFAULT '0' COMMENT '回复的上级回答id',
  `user_id` bigint NOT NULL COMMENT '回答者id',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回答内容',
  `target_user_id` bigint DEFAULT '0' COMMENT '回复的目标用户id',
  `target_reply_id` bigint DEFAULT '0' COMMENT '回复的目标回复id',
  `reply_times` int NOT NULL DEFAULT '0' COMMENT '评论数量',
  `liked_times` int NOT NULL DEFAULT '0' COMMENT '点赞数量',
  `hidden` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否被隐藏，默认false',
  `anonymity` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否匿名，默认false',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_question_id` (`question_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='互动问题的回答或评论';

-- 正在导出表  tj_learning.interaction_reply 的数据：~12 rows (大约)
DELETE FROM `interaction_reply`;
INSERT INTO `interaction_reply` (`id`, `question_id`, `answer_id`, `user_id`, `content`, `target_user_id`, `target_reply_id`, `reply_times`, `liked_times`, `hidden`, `anonymity`, `create_time`, `update_time`) VALUES
	(1548889371405492225, 1552212554946768897, 0, 1548889371405492225, '是不是Redis的依赖没有安装呢？', 0, 0, 0, 0, b'0', b'0', '2022-07-27 16:44:37', '2022-07-28 10:36:08'),
	(1585177426969833473, 1585089140469317634, 0, 129, '同问啊，我也碰到这个问题了，老师，救救孩子吧', 2, 0, 0, 0, b'0', b'1', '2022-10-26 15:52:04', '2022-11-29 15:25:06'),
	(1585178277083951106, 1585089140469317634, 0, 1548889371405492225, '安装完成后有没有配置环境变量呢？', 2, 0, 2, 0, b'0', b'0', '2022-10-26 15:55:27', '2022-10-27 10:10:56'),
	(1585179315912388610, 1585089140469317634, 1585178277083951106, 2, '配置了，在Path中配置了JAVA_HOME', 1548889371405492225, 1585178277083951106, 0, 0, b'0', b'0', '2022-10-26 15:59:34', '2022-10-26 16:02:09'),
	(1585180460118519809, 1585089140469317634, 1585178277083951106, 1548889371405492225, '如果确定环境变量没有配置错误的话，试试看关闭CMD窗口，再次打开', 2, 1585179315912388610, 2, 0, b'0', b'0', '2022-10-26 16:04:07', '2022-10-27 10:11:18'),
	(1585183997506433026, 1585089140469317634, 1585178277083951106, 2, '老师牛啊，确实是这个问题。不过为什么要关闭呢？', 1548889371405492225, 1585178277083951106, 1, 0, b'0', b'0', '2022-10-26 16:18:10', '2022-10-27 10:27:55'),
	(1585184256685060098, 1585089140469317634, 1585178277083951106, 129, '666，老师还是厉害啊', 1548889371405492225, 1585178277083951106, 0, 0, b'0', b'1', '2022-10-26 16:19:12', '2022-11-29 15:25:09'),
	(1585184666292400129, 1585089140469317634, 1585178277083951106, 1548889371405492225, '因为CMD默认加载的还是你配置Path之前的环境变量，关闭重新打开后才会读取到最新的Path', 2, 1585183997506433026, 1, 0, b'0', b'0', '2022-10-26 16:20:50', '2022-10-27 10:26:50'),
	(1585191151894343681, 1585089140469317634, 1585178277083951106, 2, '你个老6，为什么老匿名', 129, 1585184256685060098, 0, 0, b'0', b'0', '2022-10-26 16:46:36', '2022-11-29 15:25:13'),
	(1588103282121805825, 1585589766919852033, 0, 1, '阻塞IO和非阻塞IO都有。java.io包下的都是阻塞IO，java.nio下的是非阻塞IO', 0, 0, 0, 0, b'0', b'0', '2022-11-03 16:35:32', '2022-11-03 18:13:24'),
	(1588105119793188865, 1585589766919852033, 1588103282121805825, 2, '感谢老师的回复，我们课堂中没有讲过NIO，有没有资料啊', 0, 0, 0, 0, b'0', b'0', '2022-11-03 16:43:27', '2022-11-03 18:13:24'),
	(1588110148121956353, 1585589766919852033, 1588103282121805825, 1, 'NIO资料可以联系客服MM获取哦', 2, 1588105119793188865, 0, 0, b'0', b'0', '2022-11-03 17:05:06', '2022-11-03 18:13:24');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
