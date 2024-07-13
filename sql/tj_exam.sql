/*
 Navicat Premium Dump SQL

 Source Server         : 192.168.150.101_3306
 Source Server Type    : MySQL
 Source Server Version : 80029 (8.0.29)
 Source Host           : 192.168.150.101:3306
 Source Schema         : tj_exam

 Target Server Type    : MySQL
 Target Server Version : 80029 (8.0.29)
 File Encoding         : 65001

 Date: 01/07/2024 23:18:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` bigint NOT NULL COMMENT '题目id',
  `name` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '题干',
  `type` tinyint NOT NULL COMMENT '题目类型，1：单选题，2：多选题，3：不定向选择题，4：判断题，5：主观题',
  `cate_id1` bigint NOT NULL COMMENT '1级课程分类id',
  `cate_id2` bigint NOT NULL COMMENT '2级课程分类id',
  `cate_id3` bigint NOT NULL COMMENT '3级课程分类id',
  `difficulty` tinyint NOT NULL COMMENT '难易度，1：简单，2：中等，3：困难',
  `answer_times` int NOT NULL DEFAULT 0 COMMENT '回答次数',
  `correct_times` int NOT NULL DEFAULT 0 COMMENT '回答正确次数',
  `score` int NOT NULL COMMENT '分值',
  `dep_id` bigint NULL DEFAULT NULL COMMENT '部门id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creater` bigint NOT NULL DEFAULT 1 COMMENT '创建人',
  `updater` bigint NOT NULL DEFAULT 1 COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '题目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (1561504460092592130, 'Java的基本数据类型有几种？', 1, 1001, 2002, 3007, 1, 21, 20, 5, NULL, '2022-08-21 02:29:14', '2022-11-03 11:28:37', 1, 1);
INSERT INTO `question` VALUES (1561505580659625985, 'JDk安装的步骤包括哪几步？', 2, 1001, 2002, 3007, 1, 18, 16, 5, NULL, '2022-08-21 02:34:04', '2022-11-03 11:28:42', 1, 1);
INSERT INTO `question` VALUES (1561507025660268546, '下列类型哪些是线程安全的？', 2, 1001, 2002, 3007, 1, 21, 21, 5, NULL, '2022-08-21 02:40:17', '2022-11-03 11:28:50', 1, 1);
INSERT INTO `question` VALUES (1566029032905007000, '<p>以下选项中，哪个在微服务调用中起到负载均衡的作用？</p>', 1, 1001, 2002, 3007, 2, 21, 15, 5, NULL, '2022-09-03 20:09:01', '2022-11-03 11:28:55', 1, 1);
INSERT INTO `question` VALUES (1587999779978223618, 'Java之父是谁？', 1, 1001, 2002, 3371, 1, 21, 18, 5, NULL, '2022-11-03 10:47:05', '2022-11-03 11:29:06', 1, 1);
INSERT INTO `question` VALUES (1589901500625817602, 'Html的结构类型元素有：head、body、arm、foot', 4, 1001, 2004, 3381, 1, 0, 0, 5, NULL, '2022-11-08 16:43:51', '2023-02-08 10:39:37', 1, 1);

-- ----------------------------
-- Table structure for question_biz
-- ----------------------------
DROP TABLE IF EXISTS `question_biz`;
CREATE TABLE `question_biz`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `biz_id` bigint NULL DEFAULT NULL COMMENT '业务id，要关联问题的某业务id，例如小节id',
  `question_id` bigint NULL DEFAULT NULL COMMENT '问题id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `biz_id`(`biz_id` ASC, `question_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 147 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '问题和业务关联表，例如把小节id和问题id关联，一个小节下可以有多个问题' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question_biz
-- ----------------------------
INSERT INTO `question_biz` VALUES (52, 1, 1561504460092592130);
INSERT INTO `question_biz` VALUES (53, 5, 1561505580659625985);
INSERT INTO `question_biz` VALUES (54, 8, 1561505580659625985);
INSERT INTO `question_biz` VALUES (55, 14, 1561504460092592130);
INSERT INTO `question_biz` VALUES (56, 14, 1561505580659625985);
INSERT INTO `question_biz` VALUES (57, 14, 1561507025660268546);
INSERT INTO `question_biz` VALUES (63, 27, 1561505580659625985);
INSERT INTO `question_biz` VALUES (64, 27, 1561507025660268546);
INSERT INTO `question_biz` VALUES (140, 29, 1561504460092592130);
INSERT INTO `question_biz` VALUES (139, 29, 1561505580659625985);
INSERT INTO `question_biz` VALUES (138, 29, 1587999779978223618);
INSERT INTO `question_biz` VALUES (141, 40, 1561505580659625985);
INSERT INTO `question_biz` VALUES (142, 40, 1561507025660268546);
INSERT INTO `question_biz` VALUES (65, 53, 1561505580659625985);
INSERT INTO `question_biz` VALUES (66, 53, 1561507025660268546);
INSERT INTO `question_biz` VALUES (67, 66, 1561505580659625985);
INSERT INTO `question_biz` VALUES (68, 66, 1561507025660268546);
INSERT INTO `question_biz` VALUES (145, 79, 1561505580659625985);
INSERT INTO `question_biz` VALUES (146, 79, 1561507025660268546);
INSERT INTO `question_biz` VALUES (14, 92, 1561505580659625985);
INSERT INTO `question_biz` VALUES (15, 92, 1561507025660268546);
INSERT INTO `question_biz` VALUES (102, 105, 1561505580659625985);
INSERT INTO `question_biz` VALUES (103, 105, 1561507025660268546);
INSERT INTO `question_biz` VALUES (104, 118, 1561505580659625985);
INSERT INTO `question_biz` VALUES (105, 118, 1561507025660268546);
INSERT INTO `question_biz` VALUES (128, 120, 1561507025660268546);
INSERT INTO `question_biz` VALUES (127, 120, 1566029032905007000);
INSERT INTO `question_biz` VALUES (126, 120, 1587999779978223618);
INSERT INTO `question_biz` VALUES (125, 120, 1589901500625817602);
INSERT INTO `question_biz` VALUES (132, 131, 1561504460092592130);
INSERT INTO `question_biz` VALUES (131, 131, 1561505580659625985);
INSERT INTO `question_biz` VALUES (130, 131, 1561507025660268546);
INSERT INTO `question_biz` VALUES (129, 131, 1587999779978223618);
INSERT INTO `question_biz` VALUES (91, 1550383240983875590, 1561504460092592130);
INSERT INTO `question_biz` VALUES (133, 1587990530850062342, 1561505580659625985);
INSERT INTO `question_biz` VALUES (134, 1587990530850062342, 1561507025660268546);
INSERT INTO `question_biz` VALUES (135, 1587991626851057665, 1561504460092592130);
INSERT INTO `question_biz` VALUES (136, 1587991626851057665, 1561505580659625985);
INSERT INTO `question_biz` VALUES (137, 1587991626851057665, 1561507025660268546);
INSERT INTO `question_biz` VALUES (122, 1589891033688305666, 1561507025660268546);
INSERT INTO `question_biz` VALUES (123, 1589891033688305666, 1566029032905007000);
INSERT INTO `question_biz` VALUES (124, 1589891033688305666, 1587999779978223618);
INSERT INTO `question_biz` VALUES (114, 1589906354734231554, 1561507025660268546);
INSERT INTO `question_biz` VALUES (115, 1589906354734231554, 1566029032905007000);
INSERT INTO `question_biz` VALUES (116, 1589906354734231554, 1587999779978223618);

-- ----------------------------
-- Table structure for question_detail
-- ----------------------------
DROP TABLE IF EXISTS `question_detail`;
CREATE TABLE `question_detail`  (
  `id` bigint NOT NULL COMMENT '题目id',
  `options` json NULL COMMENT '选择题选项，json数组格式',
  `answer` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '选择题正确答案1到10，如果有多个答案，中间使用逗号隔开，如果是判断题，1：代表正确，其他代表错误',
  `analysis` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '答案解析',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '题目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question_detail
-- ----------------------------
INSERT INTO `question_detail` VALUES (1561504460092592130, '[\"6\", \"7\", \"8\", \"9\"]', '3', '分别是：byte short int long float double char boolean');
INSERT INTO `question_detail` VALUES (1561505580659625985, '[\"下载JDK\", \"安装JDK\", \"配置环境变量\", \"重启电脑\", \"配置classpath\"]', '1,2,3', '安装完成JDK，只需要配置下环境变量即可，无需重启电脑，也无需配置classpath');
INSERT INTO `question_detail` VALUES (1561507025660268546, '[\"HashMap\", \"ConcurrentHashMap\", \"SpringBuilder\", \"StringBuffer\", \"DatetimeFormatter\"]', '2,4,5', 'HashMap和StringBuilder并未做线程安全的防范，使用时需要注意控制并发安全问题，尽量不要多线程共享使用');
INSERT INTO `question_detail` VALUES (1566029032905007000, '[\"Feign\", \"Nacos\", \"Eureka\", \"Ribbon\"]', '4', '<p>Feign主要用来做远程调用；Eureka和Nacos主要用来实现服务注册和发现的管理；只有Ribbon起到了负载均衡的作用。2</p>');
INSERT INTO `question_detail` VALUES (1587999779978223618, '[\"詹姆斯.邦德\", \"詹姆斯.迪斯林\", \"詹姆斯.高斯林\", \"詹姆斯.卢卡斯\"]', '3', '众所周知，毋庸置疑');
INSERT INTO `question_detail` VALUES (1589901500625817602, NULL, '2', '错的很离谱');

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
