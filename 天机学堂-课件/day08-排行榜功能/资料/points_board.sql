CREATE TABLE `${tableName}`
(
	`id`      BIGINT NOT NULL AUTO_INCREMENT COMMENT '榜单id',
	`user_id` BIGINT NOT NULL COMMENT '学生id',
	`points`  INT    NOT NULL COMMENT '积分值',
	PRIMARY KEY (`id`) USING BTREE,
	INDEX `idx_user_id` (`user_id`) USING BTREE
)
	COMMENT ='学霸天梯榜'
	COLLATE = 'utf8mb4_0900_ai_ci'
	ENGINE = InnoDB
	ROW_FORMAT = DYNAMIC