/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80027 (8.0.27)
 Source Host           : localhost:3306
 Source Schema         : tourism_system

 Target Server Type    : MySQL
 Target Server Version : 80027 (8.0.27)
 File Encoding         : 65001

 Date: 29/06/2025 21:12:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for accommodation
-- ----------------------------
DROP TABLE IF EXISTS `accommodation`;
CREATE TABLE `accommodation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '酒店/民宿/客栈等',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `scenic_id` bigint NULL DEFAULT NULL COMMENT '关联景点ID',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `price_range` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '价格区间',
  `star_level` decimal(2, 1) NULL DEFAULT NULL COMMENT '评分',
  `image_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `features` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '特色服务',
  `distance` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '距景点距离',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在城市',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在省份',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `scenic_id`(`scenic_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of accommodation
-- ----------------------------
INSERT INTO `accommodation` VALUES (13, '漓江山水间酒店', '酒店', '广西壮族自治区桂林市阳朔县阳朔西街23号', 5, '位于阳朔西街的精品酒店，依江而建，客房阳台直面漓江美景，现代中式装修，舒适典雅。', '0773-8821888', '880-1980', 4.7, '/img/1748616638950.jpg', '漓江景观房,户外泳池,中西自助早餐,免费自行车,乐器演奏', '1公里', '桂林', '广西', '2025-05-30 17:50:11', '2025-06-29 20:42:44');
INSERT INTO `accommodation` VALUES (14, '阳朔田园农舍', '民宿', '广西壮族自治区桂林市阳朔县高田镇兴坪古镇56号', 5, '位于兴坪古镇的特色民宿，四周是广袤的田野和壮观的喀斯特地貌，远离城市喧嚣，回归自然生活。', '0773-8786543', '450-780', 4.5, '/img/accommodation/guilin_farmstay.jpg', '农家饭,果园采摘,钓鱼,篝火晚会,民族歌舞表演', '5公里', '桂林', '广西', '2025-05-30 17:50:11', '2025-06-29 20:42:48');
INSERT INTO `accommodation` VALUES (15, '桂林青舍', '青旅', '广西壮族自治区桂林市象山区正阳路6号', 5, '位于桂林市中心的现代化青年旅舍，交通便利，设施齐全，是背包客和自由行游客的理想选择。', '0773-2825678', '100-300', 4.3, '/img/accommodation/guilin_hostel.jpg', '免费早餐,旅游信息咨询,行李寄存,洗衣服务,共享厨房', '3公里', '桂林', '广西', '2025-05-30 17:50:11', '2025-06-29 20:42:42');
INSERT INTO `accommodation` VALUES (16, '桂林香格里拉大酒店', '豪华酒店', '广西桂林市七星区漓江路111号', 56, '坐落于漓江畔，是桂林地标性的五星级酒店。酒店以现代豪华风格为主，融入桂林山水元素，为宾客提供奢华舒适的住宿体验。', '0773-2222888', '￥1000-2000', 4.8, '/files/img/guilin_shangri_la.jpg', '免费WiFi,健身房,游泳池,商务中心,餐厅,会议室,SPA', '距离象鼻山景区0.5公里', '桂林', '广西', '2025-06-29 20:42:06', '2025-06-29 20:42:06');
INSERT INTO `accommodation` VALUES (17, '阳朔西街智选假日酒店', '精品酒店', '广西桂林市阳朔县西街口', 57, '位于阳朔最繁华的西街，周边景点众多，交通便利。酒店装修现代简约，干净整洁。', '0773-8888999', '￥300-600', 4.5, '/files/img/yangshuo_holiday.jpg', '免费WiFi,餐厅,24小时前台,行李寄存,商务中心', '距离西街步行街0.1公里', '阳朔', '广西', '2025-06-29 20:42:06', '2025-06-29 20:42:06');
INSERT INTO `accommodation` VALUES (18, '桂林榕湖边精品客栈', '民宿', '广西桂林市秀峰区正阳路23号', NULL, '临近榕湖，环境优美，客栈风格复古文艺，房间布置温馨舒适。', '0773-3333666', '￥200-400', 4.3, '/files/img/guilin_lakeside.jpg', '免费WiFi,早餐,自行车租赁,观景平台,茶室', '距离榕湖公园0.2公里', '桂林', '广西', '2025-06-29 20:42:06', '2025-06-29 20:42:06');
INSERT INTO `accommodation` VALUES (19, '南宁万达文华酒店', '豪华酒店', '广西南宁市青秀区民族大道136号', 64, '位于南宁市中心商务区，是集商务、休闲、娱乐为一体的五星级酒店。', '0771-5555888', '￥800-1500', 4.7, '/files/img/nanning_wanda.jpg', '免费WiFi,健身房,游泳池,餐厅,会议室,商务中心,停车场', '距离青秀山5公里', '南宁', '广西', '2025-06-29 20:42:06', '2025-06-29 20:42:06');
INSERT INTO `accommodation` VALUES (20, '南宁青秀山景酒店', '商务酒店', '广西南宁市青秀区柳沙路189号', 64, '紧邻青秀山风景区，环境幽静，适合商务和休闲度假。', '0771-6666999', '￥400-800', 4.4, '/files/img/nanning_qingxiu.jpg', '免费WiFi,餐厅,会议室,健身房,停车场', '距离青秀山景区0.3公里', '南宁', '广西', '2025-06-29 20:42:06', '2025-06-29 20:42:06');
INSERT INTO `accommodation` VALUES (21, '北海银滩度假酒店', '度假酒店', '广西北海市银海区银滩路66号', 59, '面朝北部湾，紧邻银滩，是观赏海景和享受海滨度假的理想选择。', '0779-3333888', '￥500-1000', 5.0, '/files/img/beihai_beach.jpg', '免费WiFi,游泳池,沙滩椅,餐厅,酒吧,spa,健身房', '距离银滩0.1公里', '北海', '广西', '2025-06-29 20:42:06', '2025-06-29 20:42:06');
INSERT INTO `accommodation` VALUES (22, '北海老街客栈', '民宿', '广西北海市海城区老街45号', 60, '位于北海老街，建筑保留了老北海的历史风貌，内部设施现代化。', '0779-2222666', '￥200-400', 4.2, '/files/img/beihai_oldstreet.jpg', '免费WiFi,早餐,接送服务,旅游咨询,自行车租赁', '距离北海老街0.1公里', '北海', '广西', '2025-06-29 20:42:06', '2025-06-29 20:42:06');
INSERT INTO `accommodation` VALUES (23, '德天瀑布度假酒店', '度假酒店', '广西崇左市大新县德天瀑布景区', NULL, '位于德天跨国瀑布景区内，是观赏瀑布的最佳住宿选择。', '0771-7777888', '￥400-800', 4.5, '/files/img/detian_hotel.jpg', '免费WiFi,观景台,餐厅,停车场,旅游服务,会议室', '距离德天瀑布0.5公里', '崇左', '广西', '2025-06-29 20:42:06', '2025-06-29 20:42:06');
INSERT INTO `accommodation` VALUES (24, '东兴边贸酒店', '商务酒店', '广西防城港市东兴市边贸路88号', NULL, '位于中越边境，靠近东兴口岸，适合商务和旅游住宿。', '0770-6666888', '￥300-600', 4.3, '/files/img/dongxing_hotel.jpg', '免费WiFi,餐厅,商务中心,会议室,停车场', '距离东兴口岸1公里', '防城港', '广西', '2025-06-29 20:42:06', '2025-06-29 20:42:06');
INSERT INTO `accommodation` VALUES (25, '巴马长寿村度假酒店', '度假酒店', '广西河池市巴马瑶族自治县巴马镇长寿村', NULL, '位于世界长寿之乡，环境清幽，空气清新，适合养生度假。', '0778-6666999', '￥400-800', 4.4, '/files/img/bama_hotel.jpg', '免费WiFi,养生餐厅,温泉,瑜伽室,中医理疗,户外活动', '距离百魔洞1公里', '河池', '广西', '2025-06-29 20:42:06', '2025-06-29 20:42:06');

-- ----------------------------
-- Table structure for accommodation_review
-- ----------------------------
DROP TABLE IF EXISTS `accommodation_review`;
CREATE TABLE `accommodation_review`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `accommodation_id` bigint NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `rating` decimal(2, 1) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `accommodation_id`(`accommodation_id` ASC) USING BTREE,
  CONSTRAINT `accommodation_review_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `accommodation_review_ibfk_2` FOREIGN KEY (`accommodation_id`) REFERENCES `accommodation` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of accommodation_review
-- ----------------------------
INSERT INTO `accommodation_review` VALUES (13, 8, 21, '非常非常不错的一次住宿体验！！！！！！！！！！！！！', 5.0, '2025-06-29 20:55:23');

-- ----------------------------
-- Table structure for carousel
-- ----------------------------
DROP TABLE IF EXISTS `carousel`;
CREATE TABLE `carousel`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '轮播图标题',
  `image_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '轮播图片地址',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态: 1-启用, 0-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of carousel
-- ----------------------------
INSERT INTO `carousel` VALUES (1, '美丽山水', '/img/1748615396726.jpeg', 1, '2025-05-30 22:25:21', '2025-05-30 22:25:21');
INSERT INTO `carousel` VALUES (2, '古城风貌', '/img/1748615456649.jpeg', 1, '2025-05-30 22:25:21', '2025-05-30 22:25:21');
INSERT INTO `carousel` VALUES (3, '海滨度假', '/img/1748615479053.jpeg', 1, '2025-05-30 22:25:21', '2025-05-30 22:25:21');
INSERT INTO `carousel` VALUES (4, '民俗文化', '/img/1748615578372.jpg', 1, '2025-05-30 22:25:21', '2025-05-30 22:25:21');
INSERT INTO `carousel` VALUES (5, '城市夜景', '/img/1748615616796.jpg', 0, '2025-05-30 22:25:21', '2025-05-30 22:33:40');

-- ----------------------------
-- Table structure for collection
-- ----------------------------
DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `guide_id` bigint NULL DEFAULT NULL COMMENT '攻略ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `guide_id`(`guide_id` ASC) USING BTREE,
  CONSTRAINT `collection_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `collection_ibfk_2` FOREIGN KEY (`guide_id`) REFERENCES `travel_guide` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of collection
-- ----------------------------
INSERT INTO `collection` VALUES (3, 3, 1, '2025-05-14 14:02:00');
INSERT INTO `collection` VALUES (4, 3, 4, '2025-05-14 14:03:00');
INSERT INTO `collection` VALUES (5, 4, 1, '2025-05-14 14:04:00');
INSERT INTO `collection` VALUES (6, 4, 2, '2025-05-14 14:05:00');
INSERT INTO `collection` VALUES (13, 2, 4, '2025-05-31 02:12:48');
INSERT INTO `collection` VALUES (14, 6, 8, '2025-06-19 21:55:57');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `scenic_id` bigint NULL DEFAULT NULL COMMENT '景点ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评论内容',
  `rating` int NULL DEFAULT NULL COMMENT '评分(1-5)',
  `likes` int NULL DEFAULT 0 COMMENT '点赞数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `scenic_id`(`scenic_id` ASC) USING BTREE,
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 2, 1, '故宫真的很壮观，历史感很强，值得一去！', 5, 14, '2025-05-14 12:00:00');
INSERT INTO `comment` VALUES (3, 4, 2, '长城很雄伟，爬起来有点累，但风景绝佳！', 5, 15, '2025-05-14 12:02:00');
INSERT INTO `comment` VALUES (4, 2, 3, '西湖真的名不虚传，太美了，而且免费开放。', 5, 20, '2025-05-14 12:03:00');
INSERT INTO `comment` VALUES (5, 3, 4, '黄山的日出非常壮观，住山上看日出是绝佳体验。', 5, 18, '2025-05-14 12:04:00');
INSERT INTO `comment` VALUES (6, 4, 5, '桂林山水甲天下，确实名不虚传！', 5, 25, '2025-05-14 12:05:00');

-- ----------------------------
-- Table structure for comment_like
-- ----------------------------
DROP TABLE IF EXISTS `comment_like`;
CREATE TABLE `comment_like`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `comment_id` bigint NULL DEFAULT NULL COMMENT '评论ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_comment`(`user_id` ASC, `comment_id` ASC) USING BTREE,
  INDEX `comment_id`(`comment_id` ASC) USING BTREE,
  CONSTRAINT `comment_like_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `comment_like_ibfk_2` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论点赞关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comment_like
-- ----------------------------

-- ----------------------------
-- Table structure for scenic_category
-- ----------------------------
DROP TABLE IF EXISTS `scenic_category`;
CREATE TABLE `scenic_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类描述',
  `icon` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类图标',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父分类ID，0表示一级分类',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序序号',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `region` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属区域',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '景点分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scenic_category
-- ----------------------------
INSERT INTO `scenic_category` VALUES (1, '自然风光', '包括山水、湖泊、森林等自然景观', '🏞️', 0, 1, '2025-06-29 01:04:10', '2025-06-29 01:04:10', NULL);
INSERT INTO `scenic_category` VALUES (2, '文化古迹', '包括历史遗迹、古建筑、博物馆等', '🏛️', 0, 2, '2025-06-29 01:04:10', '2025-06-29 01:04:10', NULL);
INSERT INTO `scenic_category` VALUES (3, '民族风情', '展示各民族特色文化和传统习俗', '👘', 0, 3, '2025-06-29 01:04:10', '2025-06-29 01:04:10', NULL);
INSERT INTO `scenic_category` VALUES (4, '主题公园', '各类主题游乐园和休闲娱乐场所', '🎡', 0, 4, '2025-06-29 01:04:10', '2025-06-29 01:04:10', NULL);
INSERT INTO `scenic_category` VALUES (5, '美食街区', '特色美食聚集地和小吃街', '🍜', 0, 5, '2025-06-29 01:04:10', '2025-06-29 01:04:10', NULL);
INSERT INTO `scenic_category` VALUES (6, '宗教圣地', '寺庙、道观等宗教场所', '🏯', 0, 6, '2025-06-29 01:04:10', '2025-06-29 01:04:10', NULL);
INSERT INTO `scenic_category` VALUES (7, '红色旅游', '革命历史纪念地和教育基地', '🏴', 0, 7, '2025-06-29 01:04:10', '2025-06-29 01:04:10', NULL);
INSERT INTO `scenic_category` VALUES (8, '乡村旅游', '农家乐、田园风光等乡村景点', '🌾', 0, 8, '2025-06-29 01:04:10', '2025-06-29 01:04:10', NULL);
INSERT INTO `scenic_category` VALUES (9, '滨海度假', '海滩、海岛等滨海旅游景点', '🏖️', 0, 9, '2025-06-29 01:04:10', '2025-06-29 01:04:10', NULL);
INSERT INTO `scenic_category` VALUES (10, '工业遗产', '具有历史价值的工业遗址和设施', '🏭', 0, 10, '2025-06-29 01:04:10', '2025-06-29 01:04:10', NULL);

-- ----------------------------
-- Table structure for scenic_collection
-- ----------------------------
DROP TABLE IF EXISTS `scenic_collection`;
CREATE TABLE `scenic_collection`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `scenic_id` bigint NOT NULL COMMENT '景点ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_scenic`(`user_id` ASC, `scenic_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_scenic_id`(`scenic_id` ASC) USING BTREE,
  CONSTRAINT `scenic_collection_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '景点收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scenic_collection
-- ----------------------------
INSERT INTO `scenic_collection` VALUES (9, 6, 4, '2025-06-19 21:37:42');
INSERT INTO `scenic_collection` VALUES (10, 6, 1, '2025-06-19 21:38:51');
INSERT INTO `scenic_collection` VALUES (12, 8, 45, '2025-06-29 12:34:01');

-- ----------------------------
-- Table structure for scenic_spot
-- ----------------------------
DROP TABLE IF EXISTS `scenic_spot`;
CREATE TABLE `scenic_spot`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '景点ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '景点名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '景点描述',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地理位置',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在城市',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '票价',
  `opening_hours` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开放时间',
  `image_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片URL',
  `longitude` decimal(10, 6) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10, 6) NULL DEFAULT NULL COMMENT '纬度',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类ID',
  `region_id` bigint NULL DEFAULT NULL COMMENT '区域ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_scenic_category`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '景点信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scenic_spot
-- ----------------------------
INSERT INTO `scenic_spot` VALUES (56, '象鼻山', '象鼻山是桂林市的标志性景观，因山形酷似一头巨象临江汲水而得名。', '广西桂林市象山区环城西一路', '桂林', 75.00, '08:00-17:30', '/img/1748616133671.jpg', 110.287745, 25.277468, '2025-06-29 17:40:00', '2025-06-29 17:40:00', 1, NULL);
INSERT INTO `scenic_spot` VALUES (57, '阳朔西街', '阳朔西街是阳朔最热闹的商业街，汇集了各种特色小店、餐馆和酒吧。', '广西桂林市阳朔县西街', '桂林', 0.00, '全天开放', '/img/1748616162313.jpg', 110.487745, 24.777468, '2025-06-29 17:40:00', '2025-06-29 17:40:00', 5, NULL);
INSERT INTO `scenic_spot` VALUES (58, '漓江', '漓江是桂林山水的精华所在，江水清澈，群峰倒映，是世界著名的旅游胜地。', '广西桂林市秀峰区', '桂林', 220.00, '全天开放', '/img/1748616225252.jpg', 110.287745, 25.277468, '2025-06-29 17:40:00', '2025-06-29 17:40:00', 1, NULL);
INSERT INTO `scenic_spot` VALUES (59, '银滩', '北海银滩是中国最好的海滩之一，因细腻洁白的沙滩而得名，是著名的度假胜地。', '广西北海市银海区银滩路', '北海', 60.00, '全天开放', '/img/1748616338014.jpg', 109.187745, 21.477468, '2025-06-29 17:40:00', '2025-06-29 17:40:00', 9, NULL);
INSERT INTO `scenic_spot` VALUES (60, '北海老街', '北海老街保留了众多具有历史价值的骑楼建筑，是感受北海历史文化的最佳去处。', '广西北海市海城区北海老街', '北海', 0.00, '全天开放', '/img/1748616360745.jpg', 109.117745, 21.477468, '2025-06-29 17:40:00', '2025-06-29 17:40:00', 2, NULL);
INSERT INTO `scenic_spot` VALUES (61, '涠洲岛', '涠洲岛是中国最大的火山岛，以火山地貌、奇特礁石和美丽的海滩闻名。', '广西北海市涠洲岛', '北海', 120.00, '全天开放', '/img/1748616386868.jpg', 109.107745, 21.017468, '2025-06-29 17:40:00', '2025-06-29 17:40:00', 9, NULL);
INSERT INTO `scenic_spot` VALUES (62, '柳侯公园', '柳侯公园是柳州市最著名的公园之一，园内有柳侯祠、放生池等景点。', '广西柳州市城中区北站路', '柳州', 0.00, '06:00-22:00', '/img/1748616271030.jpg', 109.407745, 24.327468, '2025-06-29 17:40:00', '2025-06-29 17:40:00', 2, NULL);
INSERT INTO `scenic_spot` VALUES (63, '龙潭公园', '龙潭公园是柳州市区重要的生态公园，以其独特的喀斯特地貌和水体景观著称。', '广西柳州市鱼峰区龙潭路', '柳州', 20.00, '07:00-18:00', '/img/1748616297105.jpg', 109.417745, 24.337468, '2025-06-29 17:40:00', '2025-06-29 17:40:00', 1, NULL);
INSERT INTO `scenic_spot` VALUES (64, '青秀山', '青秀山风景名胜区是国家AAAA级旅游景区，以其独特的自然景观和人文景观而闻名。景区内有青秀塔、茶花园、盆景园等景点。', '广西南宁市青秀区青秀山路', '南宁', 60.00, '08:00-18:00', '/img/1748616033437.jpg', 108.347745, 22.787468, '2025-06-29 17:40:00', '2025-06-29 17:40:00', 1, NULL);
INSERT INTO `scenic_spot` VALUES (65, '南宁民族博物馆', '南宁民族博物馆是展示广西少数民族历史文化的重要场所，收藏了大量珍贵的民族文物。', '广西南宁市青秀区民族大道', '南宁', 0.00, '09:00-17:00', '/img/1748616061193.jpg', 108.367745, 22.817468, '2025-06-29 17:40:00', '2025-06-29 17:40:00', 3, NULL);
INSERT INTO `scenic_spot` VALUES (66, '南湖公园', '南湖公园是南宁市区最大的综合性公园，园内湖水碧波荡漾，绿树成荫，是市民休闲娱乐的好去处。', '广西南宁市青秀区南湖路', '南宁', 0.00, '06:00-22:00', '/img/1748616086868.jpg', 108.357745, 22.807468, '2025-06-29 17:40:00', '2025-06-29 17:40:00', 1, NULL);

-- ----------------------------
-- Table structure for ticket
-- ----------------------------
DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `scenic_id` bigint NOT NULL,
  `ticket_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '门票名称',
  `price` decimal(10, 2) NOT NULL,
  `discount_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠价格',
  `ticket_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '门票类型(成人票/儿童票/学生票等)',
  `valid_period` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '有效期',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `stock` int NULL DEFAULT NULL COMMENT '剩余数量',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态: 1-可预订, 0-不可预订',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `scenic_id`(`scenic_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ticket
-- ----------------------------
INSERT INTO `ticket` VALUES (16, 45, '青秀山-成人票', 60.00, 50.00, '成人票', '仅限当天使用', '青秀山景区成人标准门票，含景区内主要景点参观', 998, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (17, 45, '青秀山-学生票', 30.00, 25.00, '学生票', '仅限当天使用', '青秀山景区学生优惠票，需出示有效学生证', 500, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (18, 45, '青秀山-儿童票', 30.00, 25.00, '儿童票', '仅限当天使用', '青秀山景区儿童票，1.2米以下儿童免票', 300, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (19, 48, '象鼻山-成人票', 75.00, 65.00, '成人票', '仅限当天使用', '象鼻山景区成人标准门票，含景区内主要景点参观', 1000, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (20, 48, '象鼻山-学生票', 40.00, 35.00, '学生票', '仅限当天使用', '象鼻山景区学生优惠票，需出示有效学生证', 500, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (21, 48, '象鼻山-套票', 100.00, 90.00, '套票', '仅限当天使用', '象鼻山景区套票，含夜游项目', 300, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (22, 50, '漓江精华游-成人票', 220.00, 200.00, '成人票', '购买后15天内有效', '漓江精华段竹筏漂流，含接送服务', 800, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (23, 50, '漓江精华游-儿童票', 110.00, 100.00, '儿童票', '购买后15天内有效', '漓江精华段竹筏漂流儿童票，1.2米以下儿童免票', 400, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (24, 50, '漓江全程游-套票', 350.00, 320.00, '套票', '购买后15天内有效', '漓江全程游船票，含午餐和景点讲解', 200, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (25, 51, '银滩-成人票', 60.00, 50.00, '成人票', '仅限当天使用', '银滩景区成人标准门票', 1000, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (26, 51, '银滩-儿童票', 30.00, 25.00, '儿童票', '仅限当天使用', '银滩景区儿童票，1.2米以下儿童免票', 500, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (27, 51, '银滩-家庭套票', 150.00, 130.00, '套票', '仅限当天使用', '银滩景区家庭套票（2大1小），含沙滩椅和遮阳伞', 300, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (28, 53, '涠洲岛-成人票', 120.00, 100.00, '成人票', '购买后7天内有效', '涠洲岛景区成人标准门票，含环岛观光车', 800, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (29, 53, '涠洲岛-学生票', 60.00, 50.00, '学生票', '购买后7天内有效', '涠洲岛景区学生优惠票，需出示有效学生证', 400, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (30, 53, '涠洲岛-两日游套票', 280.00, 250.00, '套票', '购买后7天内有效', '涠洲岛两日游套票，含住宿和早餐', 200, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (31, 55, '龙潭公园-成人票', 20.00, 15.00, '成人票', '仅限当天使用', '龙潭公园成人标准门票', 1000, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (32, 55, '龙潭公园-老人票', 10.00, 8.00, '老人票', '仅限当天使用', '龙潭公园老人优惠票，需出示老年证', 500, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (33, 55, '龙潭公园-套票', 35.00, 30.00, '套票', '仅限当天使用', '龙潭公园套票，含游船项目', 300, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (34, 46, '民族博物馆-讲解票', 30.00, 25.00, '讲解票', '仅限当天使用', '民族博物馆专业讲解服务票', 100, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (35, 46, '民族博物馆-团体票', 200.00, 180.00, '团体票', '仅限当天使用', '民族博物馆20人团体讲解票', 50, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (36, 49, '阳朔印象-演出票A', 260.00, 230.00, '演出票', '仅限当天使用', '阳朔印象实景演出A区座位票', 500, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (37, 49, '阳朔印象-演出票B', 200.00, 180.00, '演出票', '仅限当天使用', '阳朔印象实景演出B区座位票', 800, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (38, 49, '阳朔文化体验票', 150.00, 130.00, '体验票', '仅限当天使用', '阳朔特色文化体验活动票，含制作桂林米粉等', 200, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (39, 54, '柳侯公园-讲解票', 30.00, 25.00, '讲解票', '仅限当天使用', '柳侯公园专业讲解服务票', 100, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');
INSERT INTO `ticket` VALUES (40, 54, '柳侯公园-茶艺体验', 88.00, 80.00, '体验票', '仅限当天使用', '柳侯公园传统茶艺体验票，含点心', 50, 1, '2025-06-29 17:40:10', '2025-06-29 17:40:10');

-- ----------------------------
-- Table structure for ticket_order
-- ----------------------------
DROP TABLE IF EXISTS `ticket_order`;
CREATE TABLE `ticket_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL,
  `ticket_id` bigint NOT NULL,
  `quantity` int NOT NULL COMMENT '购买数量',
  `visitor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '游客姓名',
  `visitor_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '游客手机号',
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `visit_date` date NOT NULL COMMENT '游玩日期',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `status` tinyint NULL DEFAULT 0 COMMENT '0-待支付, 1-已支付, 2-已取消, 3-已退款, 4-已完成',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `payment_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付方式',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `ticket_id`(`ticket_id` ASC) USING BTREE,
  CONSTRAINT `ticket_order_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ticket_order_ibfk_2` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ticket_order
-- ----------------------------
INSERT INTO `ticket_order` VALUES (14, '20250629190306a342', 8, 16, 1, 'xiaoguo', '15078555489', '', '2025-06-29', 50.00, 1, '2025-06-29 19:03:25', 'BANK_CARD', '2025-06-29 19:03:06', '2025-06-29 19:03:25');
INSERT INTO `ticket_order` VALUES (15, '202506292037271267', 8, 16, 1, 'xiaoguo', '15078555489', '', '2025-06-29', 50.00, 1, '2025-06-29 20:37:32', 'WECHAT', '2025-06-29 20:37:27', '2025-06-29 20:37:31');

-- ----------------------------
-- Table structure for travel_guide
-- ----------------------------
DROP TABLE IF EXISTS `travel_guide`;
CREATE TABLE `travel_guide`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '攻略ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容',
  `user_id` bigint NULL DEFAULT NULL COMMENT '作者ID',
  `cover_image` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图片',
  `views` int NULL DEFAULT 0 COMMENT '浏览量',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `travel_guide_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '旅游攻略表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of travel_guide
-- ----------------------------
INSERT INTO `travel_guide` VALUES (1, '北京三日游完全攻略', '<p># 北京三日游完全攻略</p><p><img src=\"/api/img/1748498785197.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p>## 第一天：故宫 + 天安门广场 + 王府井</p><p>上午参观故宫博物院，下午游览天安门广场，晚上可以去王府井逛街品尝小吃。</p><p><br></p><p>## 第二天：长城 + 颐和园1</p><p>上午前往八达岭长城，下午游览颐和园，感受皇家园林的魅力。</p><p><br></p><p>## 第三天：798艺术区 + 南锣鼓巷</p><p>上午参观798艺术区，感受现代艺术氛围，下午游览南锣鼓巷，体验老北京胡同文化。</p>', 2, '/img/1748616360745.jpg', 1269, '2025-05-14 13:00:00', '2025-05-31 02:12:50');
INSERT INTO `travel_guide` VALUES (2, '杭州西湖一日游', '<p># 杭州西湖一日游</p><p><br></p><p>## 上午：断桥残雪 + 白堤</p><p>从断桥开始，沿着白堤漫步，欣赏西湖美景。</p><p><br></p><p>## 中午：楼外楼用餐</p><p>在楼外楼品尝正宗杭帮菜，如西湖醋鱼等特色美食。</p><p><br></p><p>## 下午：雷峰塔 + 三潭印月</p><p>参观雷峰塔，乘船游览三潭印月，感受\"西湖十景\"的魅力。</p>', 3, '/img/1748616297105.jpg', 986, '2025-05-14 13:01:00', '2025-05-30 22:44:58');
INSERT INTO `travel_guide` VALUES (3, '黄山二日游攻略', '<p># 黄山二日游攻略</p><p><br></p><p>## 第一天：云谷寺 → 白鹅岭 → 北海景区 → 光明顶</p><p>上午从云谷寺进山，经白鹅岭到达北海景区，下午登顶光明顶，晚上入住山顶酒店。</p><p><br></p><p>## 第二天：观日出 → 西海大峡谷 → 排云亭 → 温泉</p><p>清晨观赏日出，上午游览西海大峡谷，下午经排云亭下山，可以在山脚的温泉放松身心。</p>', 4, '/img/1748616271030.jpg', 1104, '2025-05-14 13:02:00', '2025-05-30 23:05:50');
INSERT INTO `travel_guide` VALUES (4, '桂林山水精华三日游', '<p>桂林山水精华三日游</p><p><br></p><p>第一天：象山公园 + 七星公园</p><p>游览市区内的象山公园和七星公园，感受桂林市区的山水之美。</p><p><br></p><p> 第二天：漓江精华段漂流</p><p>从桂林乘船至阳朔，欣赏漓江两岸的壮丽风光，这是桂林最精华的景观。</p><p><br></p><p>第三天：阳朔西街 + 十里画廊</p><p>上午游览阳朔西街，下午骑行或乘车游览十里画廊，欣赏田园风光。</p>', 2, '/img/1748616225252.jpg', 887, '2025-05-14 13:03:00', '2025-06-29 02:37:46');
INSERT INTO `travel_guide` VALUES (8, '111', '<p>111</p>', NULL, '/img/1748628603522.jpg', 3, '2025-05-31 02:10:05', '2025-06-19 21:57:03');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码(加密存储)',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'USER' COMMENT '角色code',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(0:禁用,1:正常)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$nenjbP2ZyyltXrlihN2Z.erOiy49eQfG03GHovS./zqlwchM.86LK', '管理员', 'admin@tourism.com', '13800138000', 'ADMIN', '/img/1751102492059.jpg', 1, '2025-05-14 10:00:00', '2025-06-28 17:21:32', '女');
INSERT INTO `user` VALUES (2, 'zhangsan', '$2a$10$Qy7d8PZ47Vndpbwi9a7ZZ.GHgzdcQ4R8DYlbsUmR0d7dOTi165s.6', '张三', 'zhangsan@example.com', '13712345678', 'USER', '/img/1748492051307.jpg', 1, '2025-05-14 10:01:00', '2025-05-29 12:14:11', '男');
INSERT INTO `user` VALUES (3, 'lisi', '$2a$10$ReTkGd2vPXJ1o6ijBm9sHOuJ0V80Cn4Qtvb7zMs3VWklHcb0mvF7q', '李四', 'lisi@example.com', '13812345679', 'USER', '/img/avatar/user2.png', 1, '2025-05-14 10:02:00', '2025-05-14 10:02:00', '女');
INSERT INTO `user` VALUES (4, 'wangwu', '$2a$10$whhSExzkWkHgBV9uZwed..i6LZ4tX3mPUbI2Rweo99M5q8eEQYkjq', '王五', 'wangwu@example.com', '13912345670', 'USER', '/img/avatar/user3.png', 1, '2025-05-14 10:03:00', '2025-05-14 10:03:00', '男');
INSERT INTO `user` VALUES (5, 'w11', '$2a$10$J4a5xpAL.Z2nDw0fS03Tv.Nnww3R1Y8kQM90Oa90REMkx58zyhxh.', '111', '11111@qq.com', '', 'USER', NULL, 1, '2025-05-31 01:45:50', '2025-05-31 01:45:50', '男');
INSERT INTO `user` VALUES (6, '1796145608', '$2a$10$4/bnnP72ShMPQhs4vJtXOuivvi1TyHZwkQchu3PyFwBB7uSM17QQi', NULL, '1796145608@qq.com', NULL, 'USER', NULL, 1, '2025-06-19 21:11:58', '2025-06-19 21:11:58', NULL);
INSERT INTO `user` VALUES (7, '123123', '$2a$10$nenjbP2ZyyltXrlihN2Z.erOiy49eQfG03GHovS./zqlwchM.86LK', '123123', '1193899475@qq.com', '', 'USER', NULL, 1, '2025-06-28 17:06:07', '2025-06-28 17:06:07', NULL);
INSERT INTO `user` VALUES (8, 'xiaoguo', '$2a$10$14pCjkg3n2Gx9XtFLaKXce44Qud0E84ZDVHh99oWI/E9opqgwm/Ri', 'xiaoguo', '1745742048@qq.com', '', 'USER', NULL, 1, '2025-06-28 17:22:00', '2025-06-28 17:22:00', NULL);

-- 更新ticket表中的scenic_id以匹配新的scenic_spot表
UPDATE `ticket` SET `scenic_id` = 56 WHERE `scenic_id` = 48; -- 更新象鼻山的门票
UPDATE `ticket` SET `scenic_id` = 58 WHERE `scenic_id` = 50; -- 更新漓江的门票
UPDATE `ticket` SET `scenic_id` = 57 WHERE `scenic_id` = 49; -- 更新阳朔相关的门票
UPDATE `ticket` SET `scenic_id` = 59 WHERE `scenic_id` = 51; -- 更新银滩的门票
UPDATE `ticket` SET `scenic_id` = 61 WHERE `scenic_id` = 53; -- 更新涠洲岛的门票
UPDATE `ticket` SET `scenic_id` = 63 WHERE `scenic_id` = 55; -- 更新龙潭公园的门票
UPDATE `ticket` SET `scenic_id` = 65 WHERE `scenic_id` = 46; -- 更新民族博物馆的门票
UPDATE `ticket` SET `scenic_id` = 62 WHERE `scenic_id` = 54; -- 更新柳侯公园的门票
UPDATE `ticket` SET `scenic_id` = 64 WHERE `scenic_id` = 45; -- 更新青秀山的门票

SET FOREIGN_KEY_CHECKS = 1;
