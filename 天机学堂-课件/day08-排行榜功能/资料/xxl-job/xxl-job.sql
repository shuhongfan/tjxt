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


-- 导出 xxl_job 的数据库结构
CREATE DATABASE IF NOT EXISTS `xxl_job` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `xxl_job`;

-- 导出  表 xxl_job.xxl_job_group 结构
CREATE TABLE IF NOT EXISTS `xxl_job_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) NOT NULL COMMENT '执行器名称',
  `address_type` tinyint NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` text COMMENT '执行器地址列表，多地址逗号分隔',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  xxl_job.xxl_job_group 的数据：~2 rows (大约)
DELETE FROM `xxl_job_group`;
INSERT INTO `xxl_job_group` (`id`, `app_name`, `title`, `address_type`, `address_list`, `update_time`) VALUES
	(2, 'learning-service', '学习服务执行器', 0, NULL, '2023-02-16 20:50:48'),
	(4, 'trade-service', '交易服务执行器', 0, NULL, '2023-02-16 20:50:48'),
	(5, 'pay-service', '支付服务', 0, NULL, '2023-02-16 20:50:48'),
	(6, 'promotion-service', '促销中心', 0, NULL, '2023-02-16 20:50:48');

-- 导出  表 xxl_job.xxl_job_info 结构
CREATE TABLE IF NOT EXISTS `xxl_job_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `job_group` int NOT NULL COMMENT '执行器主键ID',
  `job_desc` varchar(255) NOT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
  `schedule_type` varchar(50) NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
  `schedule_conf` varchar(128) DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
  `misfire_strategy` varchar(50) NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
  `executor_route_strategy` varchar(50) DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_timeout` int NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
  `executor_fail_retry_count` int NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `glue_type` varchar(50) NOT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
  `trigger_status` tinyint NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
  `trigger_last_time` bigint NOT NULL DEFAULT '0' COMMENT '上次调度时间',
  `trigger_next_time` bigint NOT NULL DEFAULT '0' COMMENT '下次调度时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  xxl_job.xxl_job_info 的数据：~5 rows (大约)
DELETE FROM `xxl_job_info`;
INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES
	(3, 4, '退款单处理', '2022-08-31 18:05:11', '2022-09-05 18:45:58', 'huyi.zhang', '', 'FIX_RATE', '300', 'DO_NOTHING', 'SHARDING_BROADCAST', 'refundRequestJobHandler', '5', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-08-31 18:05:11', '', 0, 0, 0),
	(4, 2, '持久化积分排行榜', '2022-09-02 16:57:29', '2022-09-02 16:57:29', 'huyi.zhang', '', 'CRON', '1 0 0 1 * ? *', 'DO_NOTHING', 'FIRST', 'initSeasonJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-09-02 16:57:29', '', 0, 0, 0),
	(5, 2, '持久化点赞数据', '2022-09-02 16:58:10', '2022-09-02 16:58:10', 'huyi.zhang', '', 'FIX_RATE', '120', 'DO_NOTHING', 'FIRST', 'replyLikesJobHandler', '500', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-09-02 16:58:10', '', 0, 0, 0),
	(6, 5, '未支付订单检查', '2022-09-05 18:41:46', '2022-09-05 19:17:59', 'huyi.zhang', '', 'FIX_RATE', '120', 'DO_NOTHING', 'SHARDING_BROADCAST', 'payOrderCheckHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-09-05 18:41:46', '', 0, 0, 0),
	(7, 5, '待退款订单检查', '2022-09-05 20:02:29', '2022-09-05 20:02:46', 'huyi.zhang', '', 'FIX_RATE', '123', 'DO_NOTHING', 'SHARDING_BROADCAST', 'refundOrderCheckHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-09-05 20:02:29', '', 0, 0, 0),
	(8, 6, '优惠券发放状态处理', '2022-09-16 12:23:07', '2022-09-16 12:23:07', 'huyi.zhang', '', 'FIX_RATE', '120', 'DO_NOTHING', 'SHARDING_BROADCAST', 'couponIssueJobHandler', '5', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-09-16 12:23:07', '', 0, 0, 0);

-- 导出  表 xxl_job.xxl_job_lock 结构
CREATE TABLE IF NOT EXISTS `xxl_job_lock` (
  `lock_name` varchar(50) NOT NULL COMMENT '锁名称',
  PRIMARY KEY (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  xxl_job.xxl_job_lock 的数据：~0 rows (大约)
DELETE FROM `xxl_job_lock`;
INSERT INTO `xxl_job_lock` (`lock_name`) VALUES
	('schedule_lock');

-- 导出  表 xxl_job.xxl_job_log 结构
CREATE TABLE IF NOT EXISTS `xxl_job_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `job_group` int NOT NULL COMMENT '执行器主键ID',
  `job_id` int NOT NULL COMMENT '任务，主键ID',
  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_sharding_param` varchar(20) DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
  `executor_fail_retry_count` int NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int NOT NULL COMMENT '调度-结果',
  `trigger_msg` text COMMENT '调度-日志',
  `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int NOT NULL COMMENT '执行-状态',
  `handle_msg` text COMMENT '执行-日志',
  `alarm_status` tinyint NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
  PRIMARY KEY (`id`),
  KEY `I_trigger_time` (`trigger_time`),
  KEY `I_handle_code` (`handle_code`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  xxl_job.xxl_job_log 的数据：~0 rows (大约)
DELETE FROM `xxl_job_log`;

-- 导出  表 xxl_job.xxl_job_logglue 结构
CREATE TABLE IF NOT EXISTS `xxl_job_logglue` (
  `id` int NOT NULL AUTO_INCREMENT,
  `job_id` int NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  xxl_job.xxl_job_logglue 的数据：~0 rows (大约)
DELETE FROM `xxl_job_logglue`;

-- 导出  表 xxl_job.xxl_job_log_report 结构
CREATE TABLE IF NOT EXISTS `xxl_job_log_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `trigger_day` datetime DEFAULT NULL COMMENT '调度-时间',
  `running_count` int NOT NULL DEFAULT '0' COMMENT '运行中-日志数量',
  `suc_count` int NOT NULL DEFAULT '0' COMMENT '执行成功-日志数量',
  `fail_count` int NOT NULL DEFAULT '0' COMMENT '执行失败-日志数量',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_trigger_day` (`trigger_day`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=173 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  xxl_job.xxl_job_log_report 的数据：~136 rows (大约)
DELETE FROM `xxl_job_log_report`;
INSERT INTO `xxl_job_log_report` (`id`, `trigger_day`, `running_count`, `suc_count`, `fail_count`, `update_time`) VALUES
	(1, '2022-06-14 00:00:00', 0, 0, 0, NULL),
	(2, '2022-06-13 00:00:00', 0, 0, 0, NULL),
	(3, '2022-06-12 00:00:00', 0, 0, 0, NULL),
	(4, '2022-08-16 00:00:00', 0, 0, 0, NULL),
	(5, '2022-08-15 00:00:00', 0, 0, 0, NULL),
	(6, '2022-08-14 00:00:00', 0, 0, 0, NULL),
	(7, '2022-08-17 00:00:00', 0, 0, 0, NULL),
	(8, '2022-08-20 00:00:00', 0, 0, 0, NULL),
	(9, '2022-08-19 00:00:00', 0, 0, 0, NULL),
	(10, '2022-08-18 00:00:00', 0, 0, 0, NULL),
	(11, '2022-08-21 00:00:00', 0, 0, 0, NULL),
	(12, '2022-08-22 00:00:00', 0, 0, 0, NULL),
	(13, '2022-08-23 00:00:00', 0, 0, 0, NULL),
	(14, '2022-08-24 00:00:00', 0, 0, 0, NULL),
	(15, '2022-08-25 00:00:00', 0, 0, 0, NULL),
	(16, '2022-08-26 00:00:00', 0, 0, 0, NULL),
	(17, '2022-08-27 00:00:00', 0, 0, 0, NULL),
	(18, '2022-08-29 00:00:00', 0, 0, 0, NULL),
	(19, '2022-08-28 00:00:00', 0, 0, 0, NULL),
	(20, '2022-08-30 00:00:00', 0, 0, 0, NULL),
	(21, '2022-08-31 00:00:00', 0, 6, 6, NULL),
	(22, '2022-09-02 00:00:00', 0, 2, 1, NULL),
	(23, '2022-09-01 00:00:00', 0, 0, 0, NULL),
	(24, '2022-09-03 00:00:00', 0, 0, 0, NULL),
	(25, '2022-09-05 00:00:00', 0, 5, 6, NULL),
	(26, '2022-09-04 00:00:00', 0, 0, 0, NULL),
	(27, '2022-09-06 00:00:00', 0, 0, 0, NULL),
	(28, '2022-09-07 00:00:00', 0, 0, 0, NULL),
	(29, '2022-09-08 00:00:00', 0, 0, 0, NULL),
	(30, '2022-09-09 00:00:00', 0, 0, 0, NULL),
	(31, '2022-09-13 00:00:00', 0, 0, 0, NULL),
	(32, '2022-09-12 00:00:00', 0, 0, 0, NULL),
	(33, '2022-09-11 00:00:00', 0, 0, 0, NULL),
	(34, '2022-09-14 00:00:00', 0, 0, 0, NULL),
	(35, '2022-09-15 00:00:00', 0, 0, 0, NULL),
	(36, '2022-09-16 00:00:00', 0, 6, 1, NULL),
	(37, '2022-09-17 00:00:00', 0, 0, 0, NULL),
	(38, '2022-09-19 00:00:00', 0, 0, 0, NULL),
	(39, '2022-09-18 00:00:00', 0, 0, 0, NULL),
	(40, '2022-09-20 00:00:00', 0, 0, 0, NULL),
	(41, '2022-09-21 00:00:00', 0, 0, 0, NULL),
	(42, '2022-09-22 00:00:00', 0, 0, 0, NULL),
	(43, '2022-09-23 00:00:00', 0, 0, 0, NULL),
	(44, '2022-09-24 00:00:00', 0, 0, 0, NULL),
	(45, '2022-09-26 00:00:00', 0, 0, 0, NULL),
	(46, '2022-09-25 00:00:00', 0, 0, 0, NULL),
	(47, '2022-09-27 00:00:00', 0, 0, 0, NULL),
	(48, '2022-09-28 00:00:00', 0, 0, 0, NULL),
	(49, '2022-09-29 00:00:00', 0, 0, 0, NULL),
	(50, '2022-09-30 00:00:00', 0, 0, 0, NULL),
	(51, '2022-10-02 00:00:00', 0, 0, 0, NULL),
	(52, '2022-10-01 00:00:00', 0, 0, 0, NULL),
	(53, '2022-10-06 00:00:00', 0, 7, 2, NULL),
	(54, '2022-10-05 00:00:00', 0, 0, 0, NULL),
	(55, '2022-10-04 00:00:00', 0, 0, 0, NULL),
	(56, '2022-10-07 00:00:00', 0, 0, 0, NULL),
	(57, '2022-10-08 00:00:00', 0, 0, 0, NULL),
	(58, '2022-10-09 00:00:00', 0, 0, 0, NULL),
	(59, '2022-10-10 00:00:00', 0, 0, 0, NULL),
	(60, '2022-10-11 00:00:00', 0, 0, 0, NULL),
	(61, '2022-10-12 00:00:00', 0, 0, 0, NULL),
	(62, '2022-10-13 00:00:00', 0, 0, 0, NULL),
	(63, '2022-10-14 00:00:00', 0, 0, 0, NULL),
	(64, '2022-10-17 00:00:00', 0, 0, 0, NULL),
	(65, '2022-10-16 00:00:00', 0, 0, 0, NULL),
	(66, '2022-10-15 00:00:00', 0, 0, 0, NULL),
	(67, '2022-10-18 00:00:00', 0, 0, 0, NULL),
	(68, '2022-10-19 00:00:00', 0, 0, 0, NULL),
	(69, '2022-10-20 00:00:00', 0, 0, 0, NULL),
	(70, '2022-10-21 00:00:00', 0, 0, 0, NULL),
	(71, '2022-10-22 00:00:00', 0, 0, 0, NULL),
	(72, '2022-10-24 00:00:00', 0, 0, 0, NULL),
	(73, '2022-10-23 00:00:00', 0, 0, 0, NULL),
	(74, '2022-10-25 00:00:00', 0, 0, 0, NULL),
	(75, '2022-10-26 00:00:00', 0, 2, 0, NULL),
	(76, '2022-10-27 00:00:00', 0, 0, 0, NULL),
	(77, '2022-10-28 00:00:00', 0, 0, 0, NULL),
	(78, '2022-10-30 00:00:00', 0, 0, 0, NULL),
	(79, '2022-10-29 00:00:00', 0, 0, 0, NULL),
	(80, '2022-10-31 00:00:00', 0, 0, 0, NULL),
	(81, '2022-11-02 00:00:00', 0, 0, 0, NULL),
	(82, '2022-11-01 00:00:00', 0, 0, 0, NULL),
	(83, '2022-11-03 00:00:00', 0, 3, 3, NULL),
	(84, '2022-11-04 00:00:00', 0, 0, 0, NULL),
	(85, '2022-11-05 00:00:00', 0, 0, 0, NULL),
	(86, '2022-11-06 00:00:00', 0, 0, 0, NULL),
	(87, '2022-11-07 00:00:00', 0, 0, 0, NULL),
	(88, '2022-11-08 00:00:00', 0, 0, 0, NULL),
	(89, '2022-11-09 00:00:00', 0, 0, 0, NULL),
	(90, '2022-11-10 00:00:00', 0, 0, 0, NULL),
	(91, '2022-11-11 00:00:00', 0, 0, 0, NULL),
	(92, '2022-11-12 00:00:00', 0, 0, 0, NULL),
	(93, '2022-11-14 00:00:00', 0, 0, 0, NULL),
	(94, '2022-11-13 00:00:00', 0, 0, 0, NULL),
	(95, '2022-11-15 00:00:00', 0, 0, 0, NULL),
	(96, '2022-11-16 00:00:00', 0, 0, 0, NULL),
	(97, '2022-11-18 00:00:00', 0, 0, 0, NULL),
	(98, '2022-11-17 00:00:00', 0, 0, 0, NULL),
	(99, '2022-11-19 00:00:00', 0, 0, 0, NULL),
	(100, '2022-11-21 00:00:00', 0, 0, 0, NULL),
	(101, '2022-11-20 00:00:00', 0, 0, 0, NULL),
	(102, '2022-11-22 00:00:00', 0, 0, 0, NULL),
	(103, '2022-11-23 00:00:00', 0, 0, 0, NULL),
	(104, '2022-11-28 00:00:00', 0, 0, 0, NULL),
	(105, '2022-11-27 00:00:00', 0, 0, 0, NULL),
	(106, '2022-11-26 00:00:00', 0, 0, 0, NULL),
	(107, '2022-11-29 00:00:00', 0, 0, 0, NULL),
	(108, '2022-11-30 00:00:00', 0, 0, 0, NULL),
	(109, '2022-12-01 00:00:00', 0, 0, 0, NULL),
	(110, '2022-12-02 00:00:00', 0, 0, 0, NULL),
	(111, '2022-12-03 00:00:00', 0, 0, 0, NULL),
	(112, '2022-12-04 00:00:00', 0, 0, 0, NULL),
	(113, '2022-12-05 00:00:00', 0, 0, 0, NULL),
	(114, '2022-12-09 00:00:00', 0, 0, 0, NULL),
	(115, '2022-12-08 00:00:00', 0, 0, 0, NULL),
	(116, '2022-12-07 00:00:00', 0, 0, 0, NULL),
	(117, '2022-12-10 00:00:00', 0, 0, 0, NULL),
	(118, '2022-12-11 00:00:00', 0, 0, 0, NULL),
	(119, '2022-12-21 00:00:00', 0, 0, 0, NULL),
	(120, '2022-12-20 00:00:00', 0, 0, 0, NULL),
	(121, '2022-12-19 00:00:00', 0, 0, 0, NULL),
	(122, '2022-12-22 00:00:00', 0, 0, 0, NULL),
	(123, '2022-12-26 00:00:00', 0, 0, 0, NULL),
	(124, '2022-12-25 00:00:00', 0, 0, 0, NULL),
	(125, '2022-12-24 00:00:00', 0, 0, 0, NULL),
	(126, '2022-12-27 00:00:00', 0, 0, 0, NULL),
	(127, '2022-12-28 00:00:00', 0, 0, 0, NULL),
	(128, '2022-12-29 00:00:00', 0, 0, 0, NULL),
	(129, '2022-12-31 00:00:00', 0, 0, 0, NULL),
	(130, '2022-12-30 00:00:00', 0, 0, 0, NULL),
	(131, '2023-01-01 00:00:00', 0, 0, 0, NULL),
	(132, '2023-01-02 00:00:00', 0, 0, 0, NULL),
	(133, '2023-01-05 00:00:00', 0, 0, 0, NULL),
	(134, '2023-01-04 00:00:00', 0, 0, 0, NULL),
	(135, '2023-01-03 00:00:00', 0, 0, 0, NULL),
	(136, '2023-01-06 00:00:00', 0, 0, 0, NULL),
	(137, '2023-01-07 00:00:00', 0, 0, 0, NULL),
	(138, '2023-01-08 00:00:00', 0, 0, 0, NULL),
	(139, '2023-01-09 00:00:00', 0, 0, 0, NULL),
	(140, '2023-01-10 00:00:00', 0, 0, 0, NULL),
	(141, '2023-01-11 00:00:00', 0, 0, 0, NULL),
	(142, '2023-01-12 00:00:00', 0, 0, 0, NULL),
	(143, '2023-01-13 00:00:00', 0, 0, 0, NULL),
	(144, '2023-01-14 00:00:00', 0, 0, 0, NULL),
	(145, '2023-01-15 00:00:00', 0, 0, 0, NULL),
	(146, '2023-01-16 00:00:00', 0, 0, 0, NULL),
	(147, '2023-01-17 00:00:00', 0, 0, 0, NULL),
	(148, '2023-01-18 00:00:00', 0, 0, 0, NULL),
	(149, '2023-01-26 00:00:00', 0, 0, 0, NULL),
	(150, '2023-01-25 00:00:00', 0, 0, 0, NULL),
	(151, '2023-01-24 00:00:00', 0, 0, 0, NULL),
	(152, '2023-01-27 00:00:00', 0, 0, 0, NULL),
	(153, '2023-01-28 00:00:00', 0, 0, 0, NULL),
	(154, '2023-01-29 00:00:00', 0, 0, 0, NULL),
	(155, '2023-01-30 00:00:00', 0, 0, 0, NULL),
	(156, '2023-01-31 00:00:00', 0, 0, 0, NULL),
	(157, '2023-02-01 00:00:00', 0, 0, 0, NULL),
	(158, '2023-02-02 00:00:00', 0, 0, 0, NULL),
	(159, '2023-02-03 00:00:00', 0, 0, 0, NULL),
	(160, '2023-02-04 00:00:00', 0, 0, 0, NULL),
	(161, '2023-02-05 00:00:00', 0, 0, 0, NULL),
	(162, '2023-02-06 00:00:00', 0, 0, 0, NULL),
	(163, '2023-02-07 00:00:00', 0, 0, 0, NULL),
	(164, '2023-02-08 00:00:00', 0, 0, 0, NULL),
	(165, '2023-02-09 00:00:00', 0, 0, 0, NULL),
	(166, '2023-02-10 00:00:00', 0, 0, 0, NULL),
	(167, '2023-02-11 00:00:00', 0, 0, 0, NULL),
	(168, '2023-02-12 00:00:00', 0, 0, 0, NULL),
	(169, '2023-02-13 00:00:00', 0, 0, 0, NULL),
	(170, '2023-02-14 00:00:00', 0, 0, 0, NULL),
	(171, '2023-02-15 00:00:00', 0, 0, 0, NULL),
	(172, '2023-02-16 00:00:00', 0, 0, 0, NULL);

-- 导出  表 xxl_job.xxl_job_registry 结构
CREATE TABLE IF NOT EXISTS `xxl_job_registry` (
  `id` int NOT NULL AUTO_INCREMENT,
  `registry_group` varchar(50) NOT NULL,
  `registry_key` varchar(255) NOT NULL,
  `registry_value` varchar(255) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_g_k_v` (`registry_group`,`registry_key`,`registry_value`)
) ENGINE=InnoDB AUTO_INCREMENT=287 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  xxl_job.xxl_job_registry 的数据：~0 rows (大约)
DELETE FROM `xxl_job_registry`;

-- 导出  表 xxl_job.xxl_job_user 结构
CREATE TABLE IF NOT EXISTS `xxl_job_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `role` tinyint NOT NULL COMMENT '角色：0-普通用户、1-管理员',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  xxl_job.xxl_job_user 的数据：~2 rows (大约)
DELETE FROM `xxl_job_user`;
INSERT INTO `xxl_job_user` (`id`, `username`, `password`, `role`, `permission`) VALUES
	(1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL),
	(2, 'test', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
