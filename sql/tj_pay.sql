/*
 Navicat Premium Dump SQL

 Source Server         : 192.168.150.101_3306
 Source Server Type    : MySQL
 Source Server Version : 80029 (8.0.29)
 Source Host           : 192.168.150.101:3306
 Source Schema         : tj_pay

 Target Server Type    : MySQL
 Target Server Version : 80029 (8.0.29)
 File Encoding         : 65001

 Date: 01/07/2024 23:19:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pay_channel
-- ----------------------------
DROP TABLE IF EXISTS `pay_channel`;
CREATE TABLE `pay_channel`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '支付渠道id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付渠道名称',
  `channel_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付渠道编码，用于获取支付实现',
  `channel_priority` int NOT NULL COMMENT '渠道优先级，数字越小优先级越高',
  `channel_icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '渠道图标',
  `status` int NOT NULL DEFAULT 1 COMMENT '支付渠道状态，1：使用中，2：停用',
  `creater` bigint NOT NULL COMMENT '创建人',
  `updater` bigint NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付渠道' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_channel
-- ----------------------------
INSERT INTO `pay_channel` VALUES (1, '微信', 'wxPay', 0, '/img-tx/icon_weixin.png', 1, 1, 1, '2022-06-30 03:17:30', '2022-09-05 17:59:59');
INSERT INTO `pay_channel` VALUES (2, '支付宝', 'aliPay', 1, '/img-tx/icon_zhifubao.png', 1, 1, 1, '2022-06-30 03:24:07', '2022-09-05 17:53:04');

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `biz_order_no` bigint NOT NULL COMMENT '业务订单号',
  `pay_order_no` bigint NOT NULL DEFAULT 0 COMMENT '支付单号',
  `biz_user_id` bigint NOT NULL COMMENT '支付用户id',
  `pay_channel_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '支付渠道编码',
  `amount` int NOT NULL COMMENT '支付金额，单位位分',
  `pay_type` tinyint NOT NULL DEFAULT 4 COMMENT '支付类型，1：h5,2:小程序，3：公众号，4：扫码',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '支付状态，0：待提交，1:待支付，2：支付超时或取消，3：支付成功',
  `expand_json` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '拓展字段，用于传递不同渠道单独处理的字段',
  `notify_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '业务端回调接口',
  `notify_times` int NOT NULL DEFAULT 0 COMMENT '业务端回调次数',
  `notify_status` int NOT NULL DEFAULT 0 COMMENT '回调状态，0：待回调，1：回调成功，2：回调失败',
  `result_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '第三方返回业务码',
  `result_msg` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '第三方返回提示信息',
  `pay_success_time` datetime NULL DEFAULT NULL COMMENT '支付成功时间',
  `pay_over_time` datetime NOT NULL COMMENT '支付超时时间',
  `qr_code_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付二维码链接',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creater` bigint NOT NULL DEFAULT 0 COMMENT '创建人',
  `updater` bigint NOT NULL DEFAULT 0 COMMENT '更新人',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `biz_order_no`(`biz_order_no` ASC) USING BTREE,
  UNIQUE INDEX `pay_order_no`(`pay_order_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1585168008500764674 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_order
-- ----------------------------
INSERT INTO `pay_order` VALUES (1, 202208271107001, 1563373004522557442, 2, 'aliPay', 200, 4, 3, '', 'http://order-service/pay/notify', 0, 0, '', '', NULL, '2022-08-27 09:49:05', 'https://qr.alipay.com/bax04360ecww1rnk8guk300f', '2022-08-25 21:10:14', '2022-08-26 08:01:29', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (2, 202208271107002, 1563377706308022274, 2, 'aliPay', 200, 4, 3, '', 'http://order-service/pay/notify', 0, 0, '', '', NULL, '2022-08-27 10:07:46', 'https://qr.alipay.com/bax06308ctv9pazahgst256f', '2022-08-25 21:30:29', '2022-08-26 08:01:30', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (3, 202208271107003, 1563380337285832705, 2, 'aliPay', 200, 4, 3, '', 'http://order-service/pay/notify', 0, 1, '', '', '2022-08-27 21:46:12', '2022-08-27 10:18:13', 'https://qr.alipay.com/bax01207du5hn3e5mzsk008c', '2022-08-25 21:41:48', '2022-08-26 07:57:07', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (4, 202208271107004, 1563382593401982978, 2, 'aliPay', 200, 4, 3, '', 'http://order-service/pay/notify', 0, 0, '', '', NULL, '2022-08-27 10:27:11', 'https://qr.alipay.com/bax045090mvevcgov8h4555c', '2022-08-25 21:51:31', '2022-08-26 08:01:33', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (5, 202208271107005, 1563413354477793282, 2, 'aliPay', 200, 4, 3, '', 'http://order-service/pay/notify', 0, 1, '', '', '2022-08-27 14:29:54', '2022-08-27 12:29:25', 'https://qr.alipay.com/bax01677wjucbh2hgbei559c', '2022-08-26 00:03:56', '2022-08-26 01:40:33', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (6, 202208271107006, 1563441627136012289, 2, 'wxPay', 200, 4, 3, '', 'http://order-service/pay/notify', 0, 0, '', '', NULL, '2022-08-27 14:21:46', 'weixin://wxpay/bizpayurl?pr=U89CqHDzz', '2022-08-26 02:05:39', '2022-08-26 08:01:34', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (7, 202208271107007, 1563462024665600001, 2, 'wxPay', 100, 4, 3, '', 'http://order-service/pay/notify', 0, 0, '', '', NULL, '2022-08-27 15:42:49', 'weixin://wxpay/bizpayurl?pr=4ALpRtgzz', '2022-08-26 03:33:27', '2022-08-26 08:01:35', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (8, 202208271107008, 1563462802897735682, 2, 'wxPay', 100, 4, 3, '', 'http://order-service/pay/notify', 0, 0, '', '', NULL, '2022-08-27 15:45:55', 'weixin://wxpay/bizpayurl?pr=Ed6b0RBzz', '2022-08-26 03:36:48', '2022-08-26 08:01:37', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (9, 202208271107009, 1563466157623607297, 2, 'wxPay', 100, 4, 3, '', 'http://order-service/pay/notify', 0, 1, '', '', '2022-08-27 17:59:39', '2022-08-27 15:59:14', 'weixin://wxpay/bizpayurl?pr=wjXXb62zz', '2022-08-26 03:51:15', '2022-08-26 03:51:43', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (1563696893995905027, 202208281107001, 1563696893995905026, 2, 'aliPay', 200, 4, 3, '', '', 0, 1, '', '', '2022-08-28 09:16:35', '2022-08-28 07:16:06', 'https://qr.alipay.com/bax04387fmmbcjjmke0j00ff', '2022-08-26 08:44:05', '2022-08-26 08:44:37', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (1563698357992550402, 202208281107002, 1563698357984161794, 2, 'aliPay', 300, 4, 3, '', '', 0, 1, '', '', '2022-08-28 09:22:35', '2022-08-28 07:21:55', 'https://qr.alipay.com/bax09859bg2n1zev1mnj5584', '2022-08-26 08:50:23', '2022-08-26 08:51:07', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (1563699395487088641, 202208281107003, 1563699395457728514, 5, 'aliPay', 300, 4, 3, '', '', 0, 1, '', '', '2022-08-28 09:26:23', '2022-08-28 07:26:03', 'https://qr.alipay.com/bax01120jayewny4kt9d2576', '2022-08-26 08:54:51', '2022-08-26 08:55:14', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (1577886385300332545, 1577886182488981505, 1577886385287749633, 2, 'aliPay', 200, 4, 3, '', '', 0, 0, '', '', NULL, '2022-10-06 16:35:05', 'https://qr.alipay.com/bax06374ywzsyisu7wkt2592', '2022-10-06 13:00:05', '2022-10-06 14:58:27', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (1585094474818662402, 1585094394241888257, 1585094474797690882, 5, 'wxPay', 100, 4, 3, '', '', 0, 0, '', '支付成功', NULL, '2022-10-26 12:22:27', 'weixin://wxpay/bizpayurl?pr=Xe85Vqdzz', '2022-10-26 10:22:28', '2022-10-26 10:25:46', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (1585120188976619521, 1585119741775749122, 1585120188968230913, 5, 'wxPay', 100, 4, 3, '', '', 0, 1, '', '', '2022-10-26 12:04:52', '2022-10-26 14:04:38', 'weixin://wxpay/bizpayurl?pr=YRVfmmGzz', '2022-10-26 12:04:39', '2022-10-26 12:04:53', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (1585159465781452802, 1585159419128225794, 1585159465781452801, 5, 'aliPay', 100, 4, 3, '', '', 0, 1, '', '', '2022-10-26 14:41:10', '2022-10-26 16:40:42', 'https://qr.alipay.com/bax09148vf9eowmm158500e1', '2022-10-26 14:40:42', '2022-10-26 14:41:10', 0, 0, b'0');
INSERT INTO `pay_order` VALUES (1585168008500764673, 1585167979069325314, 1585168008492376066, 5, 'wxPay', 100, 4, 3, '', '', 0, 1, '', '', '2022-10-26 15:14:54', '2022-10-26 17:14:39', 'weixin://wxpay/bizpayurl?pr=A767x91zz', '2022-10-26 15:14:38', '2022-10-26 15:14:54', 0, 0, b'0');

-- ----------------------------
-- Table structure for refund_order
-- ----------------------------
DROP TABLE IF EXISTS `refund_order`;
CREATE TABLE `refund_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `biz_order_no` bigint NOT NULL COMMENT '业务端已支付的订单id',
  `biz_refund_order_no` bigint NOT NULL COMMENT '业务端要退款的订单id，也就是子订单id',
  `pay_order_no` bigint NOT NULL COMMENT '付款时传入的支付单号',
  `refund_order_no` bigint NOT NULL COMMENT '退款单号，每次退款的唯一标示',
  `refund_amount` int NOT NULL COMMENT '本次退款金额，单位分',
  `total_amount` int NOT NULL COMMENT '总金额，单位分',
  `is_split` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是拆单退款，默认false',
  `pay_channel_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '支付渠道编码',
  `result_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '第三方交易编码',
  `result_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '第三方交易信息',
  `status` int NOT NULL DEFAULT 0 COMMENT '退款状态，0：未提交，1：退款中，2：退款失败，3：退款成功',
  `refund_channel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退款渠道',
  `notify_failed_times` int NOT NULL DEFAULT 0 COMMENT '业务端退款通知失败次数',
  `notify_status` int NOT NULL DEFAULT 0 COMMENT '退款接口通知状态，0：待通知，1：通知成功，2：通知中，3：通知失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '退款单据创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '退款单据修改时间',
  `creater` bigint NOT NULL DEFAULT 0 COMMENT '单据创建人，一般手动对账产生的单据才有值',
  `updater` bigint NOT NULL DEFAULT 0 COMMENT '单据修改人，一般手动对账产生的单据才有值',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_biz_order_id`(`biz_refund_order_no` ASC) USING BTREE,
  INDEX `index_create_time`(`create_time` ASC) USING BTREE,
  INDEX `index_refund_order_id`(`refund_order_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1588164197026390019 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '退款订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_order
-- ----------------------------
INSERT INTO `refund_order` VALUES (1563525304759173121, 202208271107001, 20220827110700101, 1563373004522557442, 1563525304750784514, 200, 200, b'0', 'aliPay', '40004', 'Business Failed', 2, 'ALIPAYACCOUNT', 0, 0, '2022-08-26 08:05:51', '2022-08-26 08:57:58', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563528427980492801, 202208271107002, 20220827110700201, 1563377706308022274, 1563528427837886465, 200, 200, b'0', 'aliPay', '40004', 'Business Failed', 2, 'ALIPAYACCOUNT', 0, 0, '2022-08-26 08:19:18', '2022-08-26 08:57:55', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563528622180962306, 202208271107003, 20220827110700301, 1563380337285832705, 1563528622172573698, 200, 200, b'0', 'aliPay', '40004', 'Business Failed', 2, 'ALIPAYACCOUNT', 0, 0, '2022-08-26 08:20:08', '2022-08-26 08:57:52', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563528818310811649, 202208271107004, 20220827110700401, 1563382593401982978, 1563528818302423041, 200, 200, b'0', 'aliPay', '40004', 'Business Failed', 2, 'ALIPAYACCOUNT', 0, 0, '2022-08-26 08:20:59', '2022-08-26 08:57:49', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563528958169878531, 202208271107005, 20220827110700501, 1563413354477793282, 1563528958169878530, 200, 200, b'0', 'aliPay', '40004', 'Business Failed', 2, 'ALIPAYACCOUNT', 0, 0, '2022-08-26 08:21:35', '2022-08-26 08:57:47', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563529898780291073, 202208271107005, 20220827110700502, 1563413354477793282, 1563529898742542337, 200, 200, b'0', 'aliPay', 'ACQ.TRADE_HAS_CLOSE', '交易已经关闭', 3, NULL, 0, 0, '2022-08-26 08:25:38', '2022-08-26 08:57:44', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563530914804301825, 202208271107006, 20220827110700601, 1563441627136012289, 1563530914774941698, 100, 200, b'0', 'wxPay', 'INVALID_REQUEST', '订单金额或退款金额与之前请求不一致，请核实后再试', 2, NULL, 0, 0, '2022-08-26 08:30:00', '2022-08-26 09:38:12', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563697391687823362, 202208281107001, 20220828110700101, 1563696893995905026, 1563697391620714497, 200, 200, b'0', 'aliPay', '', '', 2, 'ALIPAYACCOUNT', 0, 0, '2022-08-26 08:46:13', '2022-08-26 08:57:38', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563698692819644418, 202208281107002, 20220828110700201, 1563698357984161794, 1563698692798672898, 300, 300, b'0', 'aliPay', '', '', 2, 'ALIPAYACCOUNT', 0, 0, '2022-08-26 08:51:49', '2022-08-26 08:57:31', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563699708059205633, 202208281107003, 20220828110700301, 1563699395457728514, 1563699708000485378, 100, 300, b'0', 'aliPay', '', '', 2, 'ALIPAYACCOUNT', 0, 0, '2022-08-26 08:56:11', '2022-08-26 08:57:15', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563700446407704577, 202208281107003, 20220828110700302, 1563699395457728514, 1563700446390927362, 100, 300, b'0', 'aliPay', '', '', 2, 'ALIPAYACCOUNT', 0, 0, '2022-08-26 08:59:22', '2022-08-26 08:59:46', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563700660812136450, 202208281107003, 20220828110700303, 1563699395457728514, 1563700660795359233, 100, 300, b'0', 'aliPay', 'ACQ.REASON_TRADE_REFUND_FEE_ERR', '退款金额无效', 2, 'ALIPAYACCOUNT', 0, 0, '2022-08-26 09:00:18', '2022-08-26 09:15:46', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563709164901924866, 202208271107006, 20220827110700602, 1563441627136012289, 1563709164780290049, 100, 200, b'0', 'wxPay', NULL, NULL, 2, 'ORIGINAL', 0, 0, '2022-08-26 09:36:54', '2022-08-26 09:37:35', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563709994438787074, 202208271107006, 20220827110700603, 1563441627136012289, 1563709994430398465, 100, 200, b'0', 'wxPay', 'RESOURCE_NOT_EXISTS', '退款单不存在', 0, NULL, 0, 0, '2022-08-26 09:40:28', '2022-08-26 09:56:23', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563710222940274690, 202208271107007, 20220827110700701, 1563462024665600001, 1563710222940274689, 100, 100, b'1', 'wxPay', NULL, NULL, 2, 'ORIGINAL', 0, 0, '2022-08-26 09:41:27', '2022-08-26 09:41:44', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563710383498231809, 202208271107008, 20220827110700801, 1563462802897735682, 1563710383485648898, 100, 100, b'0', 'wxPay', NULL, NULL, 2, 'ORIGINAL', 0, 0, '2022-08-26 09:42:09', '2022-08-26 09:45:51', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1563711428827840513, 202208271107009, 20220827110700901, 1563466157623607297, 1563711428819451905, 100, 100, b'1', 'wxPay', NULL, NULL, 2, 'ORIGINAL', 0, 0, '2022-08-26 09:46:39', '2022-08-26 09:46:55', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1585119251423846401, 1585094394241888257, 1585098353484537858, 1585094474797690882, 1585119251402874881, 100, 100, b'1', 'wxPay', '', '', 2, 'ORIGINAL', 0, 0, '2022-10-26 12:00:55', '2022-10-26 12:01:10', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1585159247224659970, 1585119741775749122, 1585157751812694018, 1585120188968230913, 1585159247224659969, 100, 100, b'1', 'wxPay', '', '', 2, 'ORIGINAL', 0, 0, '2022-10-26 14:39:50', '2022-10-26 14:40:04', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1585160031853109251, 1585159419128225794, 1585159965629235201, 1585159465781452801, 1585160031853109250, 100, 100, b'1', 'aliPay', '', '', 2, 'ALIPAYACCOUNT', 0, 0, '2022-10-26 14:42:57', '2022-10-26 14:42:58', 0, 0, b'0');
INSERT INTO `refund_order` VALUES (1588164197026390018, 1577886182488981505, 1588158959359934465, 1577886385287749633, 1588164197009612801, 100, 200, b'0', 'aliPay', '', '', 2, 'ALIPAYACCOUNT', 0, 0, '2022-11-03 20:57:46', '2022-11-03 20:57:47', 0, 0, b'0');

SET FOREIGN_KEY_CHECKS = 1;
