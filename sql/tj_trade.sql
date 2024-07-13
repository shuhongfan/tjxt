/*
 Navicat Premium Dump SQL

 Source Server         : 192.168.150.101_3306
 Source Server Type    : MySQL
 Source Server Version : 80029 (8.0.29)
 Source Host           : 192.168.150.101:3306
 Source Schema         : tj_trade

 Target Server Type    : MySQL
 Target Server Version : 80029 (8.0.29)
 File Encoding         : 65001

 Date: 01/07/2024 23:20:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint NOT NULL COMMENT '购物车条目id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `course_id` bigint NOT NULL COMMENT '课程id',
  `cover_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程封面路径',
  `course_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名称',
  `price` int NOT NULL COMMENT '单价',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车条目信息，也就是购物车中的课程' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (1638714274064904194, 2, 1552558707325374467, '/img-tx/d15c718da3a24343b57b06becd79a032.jpg', '前端工程师2022版12', 32900, '2023-03-23 09:28:25', '2023-03-23 09:28:25');

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` bigint NOT NULL COMMENT '订单id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `pay_order_no` bigint NULL DEFAULT NULL COMMENT '交易流水支付单号',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '订单状态，1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名，6：已申请退款',
  `message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '状态备注',
  `total_amount` int NOT NULL COMMENT '订单总金额，单位分',
  `real_amount` int NOT NULL COMMENT '实付金额，单位分',
  `discount_amount` int NOT NULL DEFAULT 0 COMMENT '优惠金额，单位分',
  `pay_channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '支付渠道',
  `coupon_ids` json NULL COMMENT '优惠券id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建订单时间',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `close_time` datetime NULL DEFAULT NULL COMMENT '订单关闭时间',
  `finish_time` datetime NULL DEFAULT NULL COMMENT '订单完成时间，支付后30天',
  `refund_time` datetime NULL DEFAULT NULL COMMENT '申请退款时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creater` bigint NOT NULL COMMENT '创建人',
  `updater` bigint NOT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (1564802575146967041, 2, NULL, 3, '用户取消订单', 20000, 20000, 0, '', NULL, '2022-08-31 10:29:42', NULL, '2022-08-31 11:00:36', NULL, NULL, '2023-04-13 15:08:44', 2, 2, 0);
INSERT INTO `order` VALUES (1564813681701732354, 2, NULL, 3, '超时关闭', 250, 250, 0, '', NULL, '2022-08-31 11:13:50', NULL, '2022-08-31 11:43:50', NULL, NULL, '2023-04-13 15:08:44', 2, 2, 0);
INSERT INTO `order` VALUES (1564890487310106626, 2, 1564900201771053057, 6, '已申请退款', 150, 150, 0, 'aliPay', NULL, '2022-08-31 16:39:02', '2022-08-31 16:58:04', NULL, NULL, '2022-08-31 18:34:04', '2022-09-05 15:10:50', 2, 2, 0);
INSERT INTO `order` VALUES (1564936237008142338, 2, 1564936293425733633, 2, '用户支付成功', 200, 200, 0, 'aliPay', NULL, '2022-08-31 19:20:49', '2022-08-31 19:21:32', NULL, NULL, '2022-08-31 19:28:22', '2022-11-28 14:04:41', 5, 0, 0);
INSERT INTO `order` VALUES (1564966779539763201, 129, 1564966915594645506, 6, '已申请退款', 150, 150, 0, 'aliPay', NULL, '2022-08-31 21:22:11', '2022-08-31 21:23:13', NULL, NULL, '2022-08-31 21:42:40', '2022-11-29 15:22:09', 5, 1, 0);
INSERT INTO `order` VALUES (1564972284802809858, 129, 1564972354021486594, 6, '已申请退款', 100, 100, 0, 'wxPay', NULL, '2022-08-31 21:44:04', '2022-08-31 21:44:43', NULL, NULL, '2022-08-31 21:46:13', '2022-11-29 15:22:10', 5, 1, 0);
INSERT INTO `order` VALUES (1564976544881885185, 129, 1564976768236969985, 6, '已申请退款', 200, 200, 0, 'wxPay', NULL, '2022-08-31 22:00:59', '2022-08-31 22:02:16', NULL, NULL, '2022-08-31 22:04:12', '2022-11-29 16:08:01', 5, 1, 0);
INSERT INTO `order` VALUES (1585094394241888257, 129, 1585094474797690882, 6, '学员申请退款', 100, 100, 0, 'wxPay', NULL, '2022-10-26 10:22:16', '2022-10-26 10:22:41', NULL, NULL, '2022-10-26 10:37:52', '2022-11-29 16:07:58', 5, 5, 0);
INSERT INTO `order` VALUES (1585119741775749122, 129, 1585120188968230913, 6, '学员申请退款', 100, 100, 0, 'wxPay', NULL, '2022-10-26 12:04:36', '2022-10-26 12:04:52', NULL, NULL, '2022-10-26 14:33:53', '2022-11-29 15:22:12', 5, 5, 0);
INSERT INTO `order` VALUES (1585159419128225794, 129, 1585159465781452801, 6, '学员申请退款', 100, 100, 0, 'aliPay', NULL, '2022-10-26 14:40:35', '2022-10-26 14:41:10', NULL, NULL, '2022-10-26 14:42:41', '2022-11-29 15:22:13', 5, 5, 0);
INSERT INTO `order` VALUES (1585167979069325314, 129, 1585168008492376066, 2, '用户支付成功', 100, 100, 0, 'wxPay', NULL, '2022-10-26 15:14:33', '2022-10-26 15:14:54', NULL, NULL, NULL, '2022-11-29 15:22:14', 5, 5, 0);
INSERT INTO `order` VALUES (1589930049848053761, 129, NULL, 5, '免费报名', 0, 0, 0, '', NULL, '2022-11-08 18:37:18', NULL, NULL, '2022-11-08 18:37:18', NULL, '2023-04-13 15:08:43', 5, 5, 0);
INSERT INTO `order` VALUES (1597502678241378305, 129, NULL, 3, '用户取消订单', 10, 10, 0, '', NULL, '2022-11-29 16:08:16', NULL, '2022-11-29 16:26:50', NULL, NULL, '2022-11-30 16:40:49', 129, 129, 0);

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` bigint NOT NULL COMMENT '订单明细id',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `course_id` bigint NOT NULL COMMENT '课程id',
  `price` int NOT NULL COMMENT '课程价格',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名称',
  `cover_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封面地址',
  `valid_duration` int NULL DEFAULT NULL COMMENT '课程学习有效期，单位月。空则代表永久有效',
  `course_expire_time` datetime NULL DEFAULT NULL COMMENT '课程学习的过期时间，支付成功开始计时',
  `discount_amount` int NOT NULL DEFAULT 0 COMMENT '折扣金额',
  `real_pay_amount` int NOT NULL COMMENT '实付金额',
  `status` tinyint NOT NULL COMMENT '订单详情状态，1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名',
  `refund_status` tinyint NULL DEFAULT NULL COMMENT '1：待审批，2：取消退款，3：同意退款，4：拒绝退款，5：退款成功，6：退款失败\'',
  `pay_channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '支付渠道名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creater` bigint NOT NULL COMMENT '创建人',
  `updater` bigint NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order`(`order_id` ASC) USING BTREE,
  INDEX `idx_user_course`(`user_id` ASC, `course_id` ASC) USING BTREE,
  INDEX `idx_course_expire_time`(`course_expire_time` ASC) USING BTREE,
  INDEX `idx_pay_channel`(`pay_channel` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单明细' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_detail
-- ----------------------------
INSERT INTO `order_detail` VALUES (1564802575625117697, 1564802575146967041, 2, 2, 10000, '课程2', '/img-tx/default-cover-url.jpg', 12, '2023-08-31 15:01:46', 0, 10000, 3, 0, '', '2022-08-31 10:29:42', '2023-03-20 23:33:34', 2, 2);
INSERT INTO `order_detail` VALUES (1564802575641894913, 1564802575146967041, 2, 3, 10000, '课程3', '/img-tx/default-cover-url.jpg', 12, '2023-08-31 15:01:46', 0, 10000, 3, 0, '', '2022-08-31 10:29:42', '2023-03-20 23:33:34', 2, 2);
INSERT INTO `order_detail` VALUES (1564813681781424129, 1564813681701732354, 2, 2, 100, '课程2', '/img-tx/default-cover-url.jpg', 12, NULL, 0, 100, 1, 0, '', '2022-08-31 11:13:50', '2023-03-20 23:33:34', 2, 2);
INSERT INTO `order_detail` VALUES (1564813681789812737, 1564813681701732354, 2, 3, 100, '课程3', '/img-tx/default-cover-url.jpg', 12, NULL, 0, 100, 1, 0, '', '2022-08-31 11:13:50', '2023-03-20 23:33:34', 2, 2);
INSERT INTO `order_detail` VALUES (1564813681798201345, 1564813681701732354, 2, 4, 50, '课程4', '/img-tx/default-cover-url.jpg', 12, NULL, 0, 50, 1, 0, '', '2022-08-31 11:13:50', '2023-03-20 23:33:34', 2, 2);
INSERT INTO `order_detail` VALUES (1564890487339466754, 1564890487310106626, 2, 3, 100, '课程3', '/img-tx/default-cover-url.jpg', 12, NULL, 0, 100, 6, 5, 'aliPay', '2022-08-31 16:19:02', '2023-03-20 23:33:34', 2, 2);
INSERT INTO `order_detail` VALUES (1564890487347855362, 1564890487310106626, 2, 4, 50, '课程4', '/img-tx/default-cover-url.jpg', 12, '2023-08-31 15:01:46', 0, 50, 6, 5, 'aliPay', '2022-08-31 16:19:02', '2023-03-20 23:33:34', 2, 2);
INSERT INTO `order_detail` VALUES (1564936237138165761, 1564936237008142338, 2, 2, 100, '课程2', '/img-tx/default-cover-url.jpg', 12, '2022-08-31 19:33:32', 0, 100, 2, 5, 'aliPay', '2022-08-31 19:20:49', '2023-03-20 23:33:34', 5, 5);
INSERT INTO `order_detail` VALUES (1564936237150748674, 1564936237008142338, 2, 3, 100, '课程3', '/img-tx/default-cover-url.jpg', 12, '2022-08-31 19:33:32', 0, 50, 2, 5, 'aliPay', '2022-08-31 19:20:49', '2023-03-20 23:33:34', 5, 5);
INSERT INTO `order_detail` VALUES (1564966779736895489, 1564966779539763201, 129, 2, 100, '课程2', '/img-tx/default-cover-url.jpg', 12, '2023-08-31 15:01:46', 0, 100, 6, 5, 'aliPay', '2022-08-31 21:22:11', '2023-03-20 23:33:34', 5, 5);
INSERT INTO `order_detail` VALUES (1564966779757867010, 1564966779539763201, 129, 4, 50, '课程4', '/img-tx/default-cover-url.jpg', 12, '2023-08-31 15:01:46', 0, 50, 6, 5, 'aliPay', '2022-08-31 21:22:11', '2023-03-20 23:33:34', 5, 1);
INSERT INTO `order_detail` VALUES (1564972284895084545, 1564972284802809858, 129, 3, 100, '课程3', '/img-tx/default-cover-url.jpg', 12, '2023-08-31 15:01:46', 0, 100, 6, 6, 'wxPay', '2022-08-31 21:44:04', '2023-03-20 23:33:34', 5, 1);
INSERT INTO `order_detail` VALUES (1564976544919633921, 1564976544881885185, 129, 2, 100, '课程2', '/img-tx/default-cover-url.jpg', 12, '2023-08-31 15:01:46', 0, 100, 6, 5, 'wxPay', '2022-08-31 22:00:59', '2023-03-20 23:33:34', 5, 1);
INSERT INTO `order_detail` VALUES (1564976544928022530, 1564976544881885185, 129, 3, 100, '课程3', '/img-tx/default-cover-url.jpg', 12, '2023-08-31 15:01:46', 0, 100, 6, 5, 'wxPay', '2022-08-31 22:00:59', '2023-03-20 23:33:34', 5, 1);
INSERT INTO `order_detail` VALUES (1585094425212628994, 1585094394241888257, 129, 2, 100, '课程2', '/img-tx/default-cover-url.jpg', 12, '2022-10-26 10:34:41', 0, 100, 6, 5, 'wxPay', '2022-10-26 10:22:16', '2023-03-20 23:33:34', 5, 5);
INSERT INTO `order_detail` VALUES (1585120177966587905, 1585119741775749122, 129, 2, 100, '课程2', '/img-tx/default-cover-url.jpg', 12, '2022-10-26 12:16:52', 0, 100, 6, 5, 'wxPay', '2022-10-26 12:04:36', '2023-03-20 23:33:34', 5, 5);
INSERT INTO `order_detail` VALUES (1585159438023565314, 1585159419128225794, 129, 2, 100, '课程2', '/img-tx/default-cover-url.jpg', 12, '2022-10-26 14:53:10', 0, 100, 6, 5, 'aliPay', '2022-10-26 14:40:35', '2023-03-20 23:33:34', 5, 5);
INSERT INTO `order_detail` VALUES (1585167987554402305, 1585167979069325314, 129, 2, 100, '课程2', '/img-tx/default-cover-url.jpg', 12, '2022-10-26 15:26:54', 0, 100, 2, NULL, 'wxPay', '2022-10-26 15:14:33', '2023-03-20 23:33:34', 5, 5);
INSERT INTO `order_detail` VALUES (1589930049877413889, 1589930049848053761, 129, 1549025085494521857, 0, 'java泛型', '/img-tx/default-cover-url.jpg', 12, NULL, 0, 0, 5, NULL, '', '2022-11-08 18:37:18', '2023-03-20 23:33:34', 5, 5);
INSERT INTO `order_detail` VALUES (1597502690467774465, 1597502678241378305, 129, 1589905661084430337, 10, '可能是史上最全的微服务技术栈课程', '/img-tx/dafa5df0b10146a6881d3f26e1d091c4.jpg', 9999, NULL, 0, 10, 3, NULL, '', '2022-11-29 16:08:16', '2023-03-20 23:33:34', 129, 129);

-- ----------------------------
-- Table structure for refund_apply
-- ----------------------------
DROP TABLE IF EXISTS `refund_apply`;
CREATE TABLE `refund_apply`  (
  `id` bigint NOT NULL COMMENT '退款id',
  `order_detail_id` bigint NOT NULL COMMENT '订单明细id',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `pay_order_no` bigint NULL DEFAULT NULL COMMENT '流水支付单号',
  `refund_order_no` bigint NULL DEFAULT NULL COMMENT '流水退款单号',
  `user_id` bigint NOT NULL DEFAULT 0 COMMENT '订单所属用户id',
  `refund_amount` bigint NOT NULL COMMENT '退款金额',
  `status` int NOT NULL DEFAULT 1 COMMENT '退款状态，1：待审批，2：取消退款，3：同意退款，4：拒绝退款，5：退款成功，6：退款失败',
  `refund_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请退款原因',
  `message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款状态描述',
  `approver` bigint NULL DEFAULT NULL COMMENT '审批人id',
  `approve_opinion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批意见',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批备注',
  `failed_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款失败原因',
  `question_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款问题说明',
  `refund_channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款渠道',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建退款申请时间',
  `approve_time` datetime NULL DEFAULT NULL COMMENT '审批时间',
  `finish_time` datetime NULL DEFAULT NULL COMMENT '退款完成时间（成功或失败）',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creater` bigint NOT NULL COMMENT '创建人',
  `updater` bigint NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退款申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_apply
-- ----------------------------
INSERT INTO `refund_apply` VALUES (1564900201800413155, 1564890487339466754, 1564890487310106626, NULL, 1564905916325769217, 2, 100, 5, '不想学了', '退款成功', 1, '同意', '尚未学习', NULL, NULL, 'ALIPAYACCOUNT', '2022-08-31 18:09:44', '2022-08-31 18:12:21', '2022-08-31 18:15:26', '2022-08-31 18:32:52', 2, 1);
INSERT INTO `refund_apply` VALUES (1564924473382125569, 1564890487347855362, 1564890487310106626, NULL, 1564924474254438402, 2, 50, 5, '买错了', '退款成功', 1, '同意', '尚未学习', NULL, NULL, 'ALIPAYACCOUNT', '2022-08-31 18:34:04', '2022-08-31 19:12:21', '2022-08-31 19:15:21', '2022-09-05 15:04:04', 2, 1);
INSERT INTO `refund_apply` VALUES (1564937691953385474, 1564936237138165761, 1564936237008142338, NULL, 1564955175427305473, 5, 100, 5, '后悔了', '退款成功', 1, '同意', '课还没学习', NULL, NULL, 'ALIPAYACCOUNT', '2022-08-31 19:26:36', '2022-08-31 20:36:03', '2022-08-31 21:10:16', '2022-11-03 19:55:39', 5, 2);
INSERT INTO `refund_apply` VALUES (1564938139590479873, 1564936237150748674, 1564936237008142338, NULL, 1564964859169337345, 129, 50, 5, '后悔了2', '退款成功', 1, '同意', '课还没学习', NULL, NULL, 'ALIPAYACCOUNT', '2022-08-31 19:28:23', '2022-08-31 21:14:33', '2022-08-31 21:14:35', '2022-11-29 15:23:24', 5, 2);
INSERT INTO `refund_apply` VALUES (1564967572783312897, 1564966779736895489, 1564966779539763201, NULL, NULL, 129, 100, 4, '后悔了2', '拒绝退款', 1, '拒绝', '频繁退款', NULL, NULL, NULL, '2022-08-31 21:25:20', '2022-08-31 21:26:31', NULL, '2022-11-29 15:23:25', 5, 2);
INSERT INTO `refund_apply` VALUES (1564970494694834177, 1564966779736895489, 1564966779539763201, NULL, 1564970934861950978, 129, 100, 5, '后悔了2', '退款成功', 1, '同意', '被你的诚意打动了', NULL, NULL, 'ALIPAYACCOUNT', '2022-08-31 21:36:57', '2022-08-31 21:38:41', '2022-08-31 21:38:43', '2022-11-29 15:23:26', 5, 2);
INSERT INTO `refund_apply` VALUES (1564971934364516354, 1564966779757867010, 1564966779539763201, NULL, 1564971934540754946, 129, 50, 5, '学生我认识', '退款成功', 1, '同意', NULL, NULL, NULL, 'ALIPAYACCOUNT', '2022-08-31 21:42:40', NULL, '2022-08-31 21:42:41', '2022-11-29 15:23:29', 1, 0);
INSERT INTO `refund_apply` VALUES (1564972829177331714, 1564972284895084545, 1564972284802809858, NULL, 1564972829353570305, 129, 100, 6, '学生我认识', '退款失败', 1, NULL, NULL, NULL, NULL, 'ORIGINAL', '2022-08-31 21:46:13', NULL, '2022-09-03 18:14:29', '2022-11-29 15:23:29', 1, 0);
INSERT INTO `refund_apply` VALUES (1564977011280101377, 1564976544919633921, 1564976544881885185, NULL, 1564977011519184897, 129, 100, 5, '学生我认识', '退款成功', 1, '同意', NULL, NULL, NULL, 'ORIGINAL', '2022-08-31 22:02:50', NULL, '2022-09-03 18:14:29', '2022-11-29 15:23:30', 1, 0);
INSERT INTO `refund_apply` VALUES (1564977356685230082, 1564976544928022530, 1564976544881885185, NULL, 1564977356857204738, 129, 100, 5, '学生我认识', '退款成功', 1, '同意', NULL, NULL, NULL, 'ORIGINAL', '2022-08-31 22:04:13', NULL, '2022-09-03 18:14:29', '2022-11-29 15:23:34', 1, 0);
INSERT INTO `refund_apply` VALUES (1566756253126975489, 1566734268023988225, 1566734267944296450, NULL, 1566757591042822145, 2, 100, 5, '课程内容不感兴趣', '退款成功', 1, '同意', '被你的诚意打动了', NULL, NULL, 'ORIGINAL', '2022-09-05 19:52:54', '2022-09-05 19:58:13', '2022-09-05 20:03:30', '2022-09-05 20:03:29', 2, 1);
INSERT INTO `refund_apply` VALUES (1566760126075985921, 1566759758885642242, 1566759758852087809, NULL, 1566762087391260673, 2, 100, 5, '由于技术故障引发课程无法学习', '退款成功', 1, '同意', '被你的诚意打动了', NULL, NULL, 'ORIGINAL', '2022-09-05 20:08:17', '2022-09-05 20:16:05', '2022-09-05 20:16:21', '2022-09-05 20:16:20', 2, 1);
INSERT INTO `refund_apply` VALUES (1566761403136720898, 1566759758894030849, 1566759758852087809, NULL, 1566764853954179073, 2, 100, 5, '个人原因（个人网络原因，计划有变，账号异常等）', '退款成功', 1, '同意', '被你的诚意打动了', NULL, NULL, 'ORIGINAL', '2022-09-05 20:13:22', '2022-09-05 20:27:05', '2022-09-05 20:27:21', '2022-09-05 20:27:21', 2, 1);
INSERT INTO `refund_apply` VALUES (1566761696473759745, 1566734268032376833, 1566734267944296450, NULL, 1566764874791481345, 2, 50, 5, '由于技术故障引发课程无法学习', '退款成功', 1, '同意', '被你的诚意打动了', NULL, NULL, 'ORIGINAL', '2022-09-05 20:14:32', '2022-09-05 20:27:10', '2022-09-05 20:27:25', '2022-09-05 20:27:25', 2, 1);
INSERT INTO `refund_apply` VALUES (1566765549709426690, 1566765110351888385, 1566765110318333954, NULL, 1566769410230808578, 2, 50, 5, '课程内容不感兴趣', '退款成功', 1, '同意', '被你的诚意打动了', NULL, NULL, 'ALIPAYACCOUNT', '2022-09-05 20:29:50', '2022-09-05 20:45:11', '2022-09-05 20:45:13', '2022-09-05 20:45:12', 2, 1);
INSERT INTO `refund_apply` VALUES (1566766917211914241, 1566766774924345346, 1566766774903373825, NULL, 1566769554514866178, 2, 100, 5, '其他', '退款成功', 1, '同意', '被你的诚意打动了', NULL, NULL, 'ALIPAYACCOUNT', '2022-09-05 20:35:16', '2022-09-05 20:45:46', '2022-09-05 20:45:47', '2022-09-05 20:45:47', 2, 1);
INSERT INTO `refund_apply` VALUES (1585098353484537858, 1585094425212628994, 1585094394241888257, NULL, 1585119251402874881, 129, 100, 5, '课程内容不感兴趣', '退款成功', 1, '同意', '被你的诚意打动了', NULL, NULL, 'ORIGINAL', '2022-10-26 10:37:53', '2022-10-26 12:00:54', '2022-10-26 12:01:09', '2022-11-29 15:23:36', 5, 5);
INSERT INTO `refund_apply` VALUES (1585157751812694018, 1585120177966587905, 1585119741775749122, NULL, 1585159247224659969, 129, 100, 5, '由于技术故障引发课程无法学习', '退款成功', 1, '同意', '被你的诚意打动了', NULL, NULL, 'ORIGINAL', '2022-10-26 14:33:53', '2022-10-26 14:39:50', '2022-10-26 14:40:05', '2022-11-29 15:23:37', 5, 5);
INSERT INTO `refund_apply` VALUES (1585159965629235201, 1585159438023565314, 1585159419128225794, NULL, 1585160031853109250, 129, 100, 5, '个人原因（个人网络原因，计划有变，账号异常等）', '退款成功', 1, '同意', '被你的诚意打动了', NULL, NULL, 'ALIPAYACCOUNT', '2022-10-26 14:42:41', '2022-10-26 14:42:57', '2022-10-26 14:42:58', '2022-11-29 15:23:41', 5, 5);
INSERT INTO `refund_apply` VALUES (1588158959359934465, 1577886182530924546, 1577886182488981505, NULL, 1588164197009612801, 2, 100, 5, '课程内容不感兴趣', '退款成功', 1, NULL, '好吧，这是我侄子', NULL, '是的', 'ALIPAYACCOUNT', '2022-11-03 20:35:13', '2022-11-03 21:30:02', '2022-11-03 21:40:28', '2022-11-03 20:57:48', 2, 2);

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
