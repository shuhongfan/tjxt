
-- 导出  表 tj_promotion.user_coupon 结构
CREATE TABLE IF NOT EXISTS `user_coupon` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户券id',
  `user_id` bigint NOT NULL COMMENT '优惠券的拥有者',
  `coupon_id` bigint NOT NULL COMMENT '优惠券模板id',
  `term_begin_time` datetime DEFAULT NULL COMMENT '优惠券有效期开始时间',
  `term_end_time` datetime NOT NULL COMMENT '优惠券有效期结束时间',
  `used_time` datetime DEFAULT NULL COMMENT '优惠券使用时间（核销时间）',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '优惠券状态，1：未使用，2：已使用，3：已失效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_coupon` (`coupon_id`),
  KEY `idx_user_coupon` (`user_id`,`coupon_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户领取优惠券的记录，是真正使用的优惠券信息';

-- 正在导出表  tj_promotion.user_coupon 的数据：~3 rows (大约)
DELETE FROM `user_coupon`;
INSERT INTO `user_coupon` (`id`, `user_id`, `coupon_id`, `term_begin_time`, `term_end_time`, `used_time`, `status`, `create_time`, `update_time`) VALUES
	(1, 2, 1630563495906942978, '2023-03-01 18:28:19', '2023-03-21 18:28:19', NULL, 1, '2023-03-01 18:28:20', '2023-03-01 18:28:20'),
	(2, 5, 1630563495906942978, '2023-03-01 18:29:24', '2023-03-21 18:29:24', NULL, 1, '2023-03-01 18:29:24', '2023-03-01 18:29:24'),
	(3, 1, 1630563495906942978, '2023-03-01 18:33:29', '2023-03-21 18:33:29', NULL, 1, '2023-03-01 18:33:29', '2023-03-01 18:33:29');