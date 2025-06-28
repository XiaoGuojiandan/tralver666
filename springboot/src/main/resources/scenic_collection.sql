-- 创建景点收藏表
CREATE TABLE IF NOT EXISTS `scenic_collection` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `scenic_id` bigint NOT NULL COMMENT '景点ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_scenic` (`user_id`,`scenic_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_scenic_id` (`scenic_id`),
  CONSTRAINT `scenic_collection_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `scenic_collection_ibfk_2` FOREIGN KEY (`scenic_id`) REFERENCES `scenic_spot` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='景点收藏表'; 