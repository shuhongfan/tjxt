/*
 Navicat Premium Dump SQL

 Source Server         : 192.168.150.101_3306
 Source Server Type    : MySQL
 Source Server Version : 80029 (8.0.29)
 Source Host           : 192.168.150.101:3306
 Source Schema         : tj_message

 Target Server Type    : MySQL
 Target Server Version : 80029 (8.0.29)
 File Encoding         : 65001

 Date: 01/07/2024 23:19:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message_template
-- ----------------------------
DROP TABLE IF EXISTS `message_template`;
CREATE TABLE `message_template`  (
  `id` bigint NOT NULL COMMENT '短信发送模板id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板名称',
  `platform_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '第三方短信平台代号',
  `sign_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '签名',
  `third_template_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '第三方短信模板code',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '第三方短信模板内容预览',
  `template_id` bigint NOT NULL COMMENT '通知模板id',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '模板状态:  0-禁用，1-启用',
  `creater` bigint NOT NULL COMMENT '创建者',
  `updater` bigint NOT NULL COMMENT '更新者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_template_id`(`template_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '第三方短信平台签名和模板信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_template
-- ----------------------------
INSERT INTO `message_template` VALUES (1561895814765240322, '短信验证码', 'aliYun', '天机学堂', 'SMS_251685292', '验证码:{code},您在使用天机学堂短信验证功能，仅限本人使用，请勿向他人泄露验证码信息', 1561895814668771330, 0, 0, 0, '2022-08-23 01:59:16', '2022-09-24 11:23:32');
INSERT INTO `message_template` VALUES (1561897355307925505, '短信验证码', 'tencent', '天机学堂', 'SMS_185822702', '您的验证码为xxx,验证码5分钟内有效，请勿泄露给他人！', 1561895814668771330, 0, 0, 0, '2022-08-23 02:05:24', '2022-08-23 02:05:24');

-- ----------------------------
-- Table structure for notice_task
-- ----------------------------
DROP TABLE IF EXISTS `notice_task`;
CREATE TABLE `notice_task`  (
  `id` bigint NOT NULL COMMENT '公告任务id',
  `template_id` bigint NOT NULL COMMENT '任务对应的通知模板id',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名称',
  `partial` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是部分人的通告，默认false',
  `push_time` datetime NULL DEFAULT NULL COMMENT '任务预期执行时间',
  `interval` int NULL DEFAULT NULL COMMENT '任务延迟执行时间间隔，单位是分钟',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '任务失效时间',
  `max_times` int NULL DEFAULT 1 COMMENT '任务重复执行次数上限，1则只发一次',
  `finished` bit(1) NULL DEFAULT b'0' COMMENT '任务是否完成，默认false',
  `creater` bigint NOT NULL COMMENT '创建人',
  `updater` bigint NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统通告的任务表，可以延期或定期发送通告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice_task
-- ----------------------------

-- ----------------------------
-- Table structure for notice_task_target
-- ----------------------------
DROP TABLE IF EXISTS `notice_task_target`;
CREATE TABLE `notice_task_target`  (
  `task_id` bigint NOT NULL COMMENT '任务id',
  `target_id` bigint NOT NULL COMMENT '目标用户id',
  PRIMARY KEY (`task_id`, `target_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知任务的目标用户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice_task_target
-- ----------------------------

-- ----------------------------
-- Table structure for notice_template
-- ----------------------------
DROP TABLE IF EXISTS `notice_template`;
CREATE TABLE `notice_template`  (
  `id` bigint NOT NULL COMMENT '通知模板id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通知模板名称',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板代号，例如：verify-code',
  `type` tinyint NOT NULL COMMENT '通知类型：0-系统通知，1-笔记通知，2-问答通知，3-其它通知',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '模板状态:  0-草稿，1-使用中，2-停用',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通知标题，短信模板可以不填',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通知内容模板',
  `is_sms_template` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是短信模板，默认false',
  `creater` bigint NOT NULL COMMENT '创建人',
  `updater` bigint NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通知模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice_template
-- ----------------------------
INSERT INTO `notice_template` VALUES (1561895814668771330, '短信验证码', 'VERIFY_CODE', 4, 1, NULL, '您的验证码为xxx,验证码5分钟内有效，请勿泄露给他人！', b'1', 0, 0, '2022-08-23 01:59:16', '2022-09-24 11:31:08');

-- ----------------------------
-- Table structure for public_notice
-- ----------------------------
DROP TABLE IF EXISTS `public_notice`;
CREATE TABLE `public_notice`  (
  `id` bigint NOT NULL COMMENT '公告id',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告标题',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告通知内容，可以存放公告消息模板',
  `type` tinyint NOT NULL COMMENT '通知类型：0-系统通知，1-笔记通知，2-问答通知，3-其它通知',
  `push_time` datetime NOT NULL COMMENT '通知发布时间',
  `expire_time` datetime NOT NULL COMMENT '通知失效时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公告消息模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of public_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sms_third_platform
-- ----------------------------
DROP TABLE IF EXISTS `sms_third_platform`;
CREATE TABLE `sms_third_platform`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '短信平台id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '短信平台名称',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '短信平台代码，例如：ali',
  `priority` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '数字越小优先级越高，最小为0',
  `status` int NOT NULL DEFAULT 1 COMMENT '短信平台状态：0-禁用，1-启用',
  `creater` bigint NOT NULL COMMENT '创建人',
  `updater` bigint NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '第三方云通讯平台' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_third_platform
-- ----------------------------
INSERT INTO `sms_third_platform` VALUES (1, '阿里云', 'aliYun', 0, 1, 0, 0, '2022-08-23 01:04:57', '2022-08-23 01:04:57');
INSERT INTO `sms_third_platform` VALUES (2, '腾讯云', 'tencent', 1, 1, 0, 0, '2022-08-23 01:05:18', '2022-08-23 01:05:18');
INSERT INTO `sms_third_platform` VALUES (3, '优刻得', 'uCloud', 3, 0, 0, 0, '2022-08-23 01:06:14', '2022-08-23 01:07:19');

-- ----------------------------
-- Table structure for user_inbox
-- ----------------------------
DROP TABLE IF EXISTS `user_inbox`;
CREATE TABLE `user_inbox`  (
  `id` bigint NOT NULL COMMENT '用户通知id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `type` tinyint NULL DEFAULT 4 COMMENT '通知类型：0-系统通知，1-笔记通知，2-问答通知，3-其它通知，4-私信',
  `title` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '通知标题',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通知或私信内容',
  `is_read` bit(1) NOT NULL DEFAULT b'0' COMMENT '公告是否已读',
  `publisher` bigint NOT NULL DEFAULT 0 COMMENT '通知的发送者id，0则代表是系统',
  `push_time` datetime NOT NULL COMMENT '创建时间',
  `expire_time` datetime NOT NULL COMMENT '过期时间，一旦过期用户端不在展示',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `push_time`(`push_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户通知记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_inbox
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
