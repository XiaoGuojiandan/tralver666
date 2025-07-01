-- 删除现有表
DROP TABLE IF EXISTS `accommodation`;

-- 创建新表
CREATE TABLE `accommodation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '酒店/民宿/客栈等',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `scenic_id` bigint NULL DEFAULT NULL COMMENT '关联景点ID',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `price_range` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '价格区间',
  `star_level` decimal(2, 1) NULL DEFAULT NULL COMMENT '星级',
  `image_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `features` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '特色服务',
  `distance` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '距景点距离',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在城市',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在省份',
  `rating` double NULL DEFAULT NULL COMMENT '评分',
  `review_count` bigint NULL DEFAULT 0 COMMENT '评论数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `scenic_id`(`scenic_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- 插入数据
INSERT INTO `accommodation` VALUES (13, '漓江山水间酒店', '酒店', '广西壮族自治区桂林市阳朔县阳朔西街23号', 5, '位于阳朔西街的精品酒店，依江而建，客房阳台直面漓江美景，现代中式装修，舒适典雅。', '0773-8821888', '880-1980', 4.7, '/img/1748616638950.jpg', '漓江景观房,户外泳池,中西自助早餐,免费自行车,乐器演奏', '1公里', '桂林', '广西', 4.7, 0, '2025-05-30 17:50:11', '2025-06-29 20:42:44');
INSERT INTO `accommodation` VALUES (14, '阳朔田园农舍', '民宿', '广西壮族自治区桂林市阳朔县高田镇兴坪古镇56号', 5, '位于兴坪古镇的特色民宿，四周是广袤的田野和壮观的喀斯特地貌，远离城市喧嚣，回归自然生活。', '0773-8786543', '450-780', 4.5, '/img/1748616638950.jpg', '农家饭,果园采摘,钓鱼,篝火晚会,民族歌舞表演', '5公里', '桂林', '广西', 4.5, 0, '2025-05-30 17:50:11', '2025-07-01 00:05:39');
INSERT INTO `accommodation` VALUES (15, '桂林青舍', '青旅', '广西壮族自治区桂林市象山区正阳路6号', 5, '位于桂林市中心的现代化青年旅舍，交通便利，设施齐全，是背包客和自由行游客的理想选择。', '0773-2825678', '100-300', 4.3, '/img/1748616638950.jpg', '免费早餐,旅游信息咨询,行李寄存,洗衣服务,共享厨房', '3公里', '桂林', '广西', 4.3, 0, '2025-05-30 17:50:11', '2025-07-01 00:05:40');
INSERT INTO `accommodation` VALUES (16, '桂林香格里拉大酒店', '豪华酒店', '广西桂林市七星区漓江路111号', 56, '坐落于漓江畔，是桂林地标性的五星级酒店。酒店以现代豪华风格为主，融入桂林山水元素，为宾客提供奢华舒适的住宿体验。', '0773-2222888', '￥1000-2500', 5.0, '/img/1748616638950.jpg', '免费WiFi,健身房,游泳池,商务中心,餐厅,会议室,SPA', '距离象鼻山景区0.5公里', '桂林', '广西', 5.0, 0, '2025-06-29 20:42:06', '2025-07-01 00:05:44');
INSERT INTO `accommodation` VALUES (17, '阳朔西街智选假日酒店', '精品酒店', '广西桂林市阳朔县西街口', 57, '位于阳朔最繁华的西街，周边景点众多，交通便利。酒店装修现代简约，干净整洁。', '0773-8888999', '￥300-600', 4.5, '/img/1751299581878.jpg', '免费WiFi,餐厅,24小时前台,行李寄存,商务中心', '距离西街步行街0.1公里', '阳朔', '广西', 4.5, 0, '2025-06-29 20:42:06', '2025-07-01 00:05:45');
INSERT INTO `accommodation` VALUES (18, '桂林榕湖边精品客栈', '民宿', '广西桂林市秀峰区正阳路23号', NULL, '临近榕湖，环境优美，客栈风格复古文艺，房间布置温馨舒适。', '0773-3333666', '￥200-400', 4.3, '/img/1748616638950.jpg', '免费WiFi,早餐,自行车租赁,观景平台,茶室', '距离榕湖公园0.2公里', '桂林', '广西', 4.3, 0, '2025-06-29 20:42:06', '2025-07-01 00:05:47');
INSERT INTO `accommodation` VALUES (19, '南宁万达文华酒店', '豪华酒店', '广西南宁市青秀区民族大道136号', 64, '位于南宁市中心商务区，是集商务、休闲、娱乐为一体的五星级酒店。', '0771-5555888', '￥800-1500', 4.7, '/img/1748616638950.jpg', '免费WiFi,健身房,游泳池,餐厅,会议室,商务中心,停车场', '距离青秀山5公里', '南宁', '广西', 4.7, 0, '2025-06-29 20:42:06', '2025-07-01 00:05:48');
INSERT INTO `accommodation` VALUES (20, '南宁青秀山景酒店', '商务酒店', '广西南宁市青秀区柳沙路189号', 64, '紧邻青秀山风景区，环境幽静，适合商务和休闲度假。', '0771-6666999', '￥400-800', 4.4, '/img/1748616638950.jpg', '免费WiFi,餐厅,会议室,健身房,停车场', '距离青秀山景区0.3公里', '南宁', '广西', 4.4, 0, '2025-06-29 20:42:06', '2025-07-01 00:05:49');
INSERT INTO `accommodation` VALUES (21, '北海银滩度假酒店', '度假酒店', '广西北海市银海区银滩路66号', 59, '面朝北部湾，紧邻银滩，是观赏海景和享受海滨度假的理想选择。', '0779-3333888', '￥500-1000', 5.0, '/img/1748616638950.jpg', '免费WiFi,游泳池,沙滩椅,餐厅,酒吧,spa,健身房', '距离银滩0.1公里', '北海', '广西', 5.0, 0, '2025-06-29 20:42:06', '2025-07-01 00:05:50');
INSERT INTO `accommodation` VALUES (22, '北海老街客栈', '民宿', '广西北海市海城区老街45号', 60, '位于北海老街，建筑保留了老北海的历史风貌，内部设施现代化。', '0779-2222666', '￥200-400', 4.2, '/img/1748616638950.jpg', '免费WiFi,早餐,接送服务,旅游咨询,自行车租赁', '距离北海老街0.1公里', '北海', '广西', 4.2, 0, '2025-06-29 20:42:06', '2025-07-01 00:05:52');
INSERT INTO `accommodation` VALUES (23, '德天瀑布度假酒店', '度假酒店', '广西崇左市大新县德天瀑布景区', NULL, '位于德天跨国瀑布景区内，是观赏瀑布的最佳住宿选择。', '0771-7777888', '￥400-800', 4.5, '/img/1748616638950.jpg', '免费WiFi,观景台,餐厅,停车场,旅游服务,会议室', '距离德天瀑布0.5公里', '崇左', '广西', 4.5, 0, '2025-06-29 20:42:06', '2025-07-01 00:05:53');
INSERT INTO `accommodation` VALUES (24, '东兴边贸酒店', '商务酒店', '广西防城港市东兴市边贸路88号', NULL, '位于中越边境，靠近东兴口岸，适合商务和旅游住宿。', '0770-6666888', '￥300-600', 4.3, '/img/1748616638950.jpg', '免费WiFi,餐厅,商务中心,会议室,停车场', '距离东兴口岸1公里', '防城港', '广西', 4.3, 0, '2025-06-29 20:42:06', '2025-07-01 00:05:57');
INSERT INTO `accommodation` VALUES (25, '巴马长寿村度假酒店', '度假酒店', '广西河池市巴马瑶族自治县巴马镇长寿村', NULL, '位于世界长寿之乡，环境清幽，空气清新，适合养生度假。', '0778-6666999', '￥400-800', 4.4, '/img/1748616638950.jpg', '免费WiFi,养生餐厅,温泉,瑜伽室,中医理疗,户外活动', '距离百魔洞1公里', '河池', '广西', 4.4, 0, '2025-06-29 20:42:06', '2025-07-01 00:05:55'); 