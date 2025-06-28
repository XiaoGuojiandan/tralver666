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

 Date: 29/06/2025 00:54:30
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
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `scenic_id`(`scenic_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of accommodation
-- ----------------------------
INSERT INTO `accommodation` VALUES (1, '紫禁城皇家酒店', '酒店', '北京市东城区景山前街8号', 1, '紫禁城皇家酒店位于故宫博物院东侧，是一家五星级豪华酒店。酒店设计融合了中国传统皇家风格与现代奢华元素，为宾客提供极致的住宿体验。', '010-66180808', '1200-3800', 4.8, '/img/1748616500926.jpg', '免费WiFi,24小时前台,室内游泳池,健身中心,中西餐厅', '500米', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (2, '北京四合院民宿', '民宿', '北京市东城区交道口南大街45号', 1, '传统北京四合院改造的精品民宿，保留了老北京的建筑特色和文化韵味，每个房间都有不同主题，体验京城老味道。', '010-65128765', '680-1280', 4.6, '/img/1748616665832.jpg', '免费早餐,传统茶艺体验,四合院文化讲解,自行车租赁', '1.2公里', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (3, '故宫青年旅舍', '青旅', '北京市东城区五四大街12号', 1, '位于故宫附近的经济实惠青年旅舍，干净整洁，是背包客和年轻游客的理想选择。提供多人间和独立房间选择。', '010-65223366', '120-380', 4.2, '/img/accommodation/gugong_hostel.jpg', '公共厨房,行李寄存,免费市区地图,旅游咨询服务', '1.5公里', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (4, '长城脚下的公社', '度假村', '北京市怀柔区慕田峪长城景区附近', 2, '坐落于慕田峪长城脚下的度假村，由废弃小学改建而成，保留了原有建筑风格，融入现代设计元素，提供独特的住宿体验。', '010-61626688', '1580-2980', 4.5, '/img/1748616775441.jpg', '观景露台,特色餐厅,免费接送长城服务,文化体验活动', '1公里', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (5, '长城客栈', '客栈', '北京市怀柔区水长城路18号', 2, '传统北方风格的农家客栈，热情好客的主人，地道的北方农家菜，让您在长城脚下感受淳朴的民风。', '010-61618899', '450-680', 4.3, '/img/accommodation/changcheng_inn.jpg', '农家饭,烧烤,免费停车,登长城向导服务', '800米', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (6, '长城山水民宿', '民宿', '北京市怀柔区慕田峪村56号', 2, '依山而建的特色民宿，每个房间都能欣赏到长城美景，装修典雅温馨，融入当地文化元素。', '010-61657788', '780-1380', 4.7, '/img/1748616557730.jpg', '观景阳台,手工艺课程,长城摄影指导,采摘体验', '1.5公里', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (7, '西湖雅园酒店', '酒店', '浙江省杭州市西湖区杨公堤28号', 3, '位于杨公堤的高档酒店，落地窗可直接观赏西湖美景，中式庭院设计，结合现代设施，尽显江南雅致。', '0571-87982233', '1080-2580', 4.8, '/img/1748616523565.jpg', '湖景房,私家花园,中式下午茶,游船服务,健身房', '200米', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (8, '西湖边的院子', '民宿', '浙江省杭州市西湖区满觉陇路120号', 3, '位于满觉陇的江南风格民宿，小桥流水，竹林环绕，安静雅致，适合放松身心。', '0571-86546789', '680-1280', 4.7, '/img/1748616596201.jpg', '茶室,古琴演奏,插花课程,私家厨师,自行车租赁', '500米', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (9, '湖畔青年旅舍', '青旅', '浙江省杭州市西湖区北山街84号', 3, '临湖而建的文艺青旅，书香气息浓厚，聚集了各地文艺青年，常有读书会、音乐分享等活动。', '0571-85234567', '120-350', 4.4, '/img/accommodation/xihu_hostel.jpg', '共享图书角,音乐角落,公共厨房,自助洗衣房,免费WiFi', '800米', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (10, '黄山悦榕庄', '酒店', '安徽省黄山市黄山区温泉镇汤口镇新建中路18号', 4, '融合徽派建筑风格与现代奢华的度假酒店，提供极致住宿体验，拥有温泉SPA和多种休闲设施。', '0559-5586888', '1680-5800', 4.9, '/img/1748616463211.jpg', '私人温泉,徽派餐厅,黄山观景台,户外泳池,SPA中心,免费登山向导', '3公里', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (11, '老街客栈', '客栈', '安徽省黄山市黄山区汤口镇老街45号', 4, '位于汤口古镇老街的传统徽派客栈，木质结构，雕花窗棂，充满历史韵味，提供温馨舒适的住宿环境。', '0559-5581234', '380-680', 4.5, '/img/1748616804380.jpg', '徽派早餐,古法泡茶,登山物资租赁,旅游咨询,接站服务', '4.5公里', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (12, '云端小院', '民宿', '安徽省黄山市黄山区黟县西递村78号', 4, '坐落于黟县西递村的传统徽派民居，小院幽静，竹林环抱，远离喧嚣，尽享山间宁静。', '0559-5156677', '580-980', 4.6, '/img/1748616728373.jpg', '徽州菜私房菜,手工艺课程,免费班车,茶室,医疗包', '15公里', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (13, '漓江山水间酒店', '酒店', '广西壮族自治区桂林市阳朔县阳朔西街23号', 5, '位于阳朔西街的精品酒店，依江而建，客房阳台直面漓江美景，现代中式装修，舒适典雅。', '0773-8821888', '880-1980', 4.7, '/img/1748616638950.jpg', '漓江景观房,户外泳池,中西自助早餐,免费自行车,乐器演奏', '1公里', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (14, '阳朔田园农舍', '民宿', '广西壮族自治区桂林市阳朔县高田镇兴坪古镇56号', 5, '位于兴坪古镇的特色民宿，四周是广袤的田野和壮观的喀斯特地貌，远离城市喧嚣，回归自然生活。', '0773-8786543', '450-780', 4.5, '/img/accommodation/guilin_farmstay.jpg', '农家饭,果园采摘,钓鱼,篝火晚会,民族歌舞表演', '5公里', '2025-05-30 17:50:11', '2025-05-30 17:50:11');
INSERT INTO `accommodation` VALUES (15, '桂林青舍', '青旅', '广西壮族自治区桂林市象山区正阳路6号', 5, '位于桂林市中心的现代化青年旅舍，交通便利，设施齐全，是背包客和自由行游客的理想选择。', '0773-2825678', '100-300', 4.3, '/img/accommodation/guilin_hostel.jpg', '免费早餐,旅游信息咨询,行李寄存,洗衣服务,共享厨房', '3公里', '2025-05-30 17:50:11', '2025-05-30 17:50:11');

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
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of accommodation_review
-- ----------------------------
INSERT INTO `accommodation_review` VALUES (1, 2, 1, '酒店环境优雅，服务周到，距离故宫步行只需几分钟，地理位置非常好！房间宽敞明亮，中式装修很有特色，早餐种类丰富，体验感极佳。', 5.0, '2025-05-15 10:30:00');
INSERT INTO `accommodation_review` VALUES (2, 3, 1, '房间干净整洁，前台服务热情专业，唯一不足是价格稍高，但考虑到地理位置和服务质量，还是很值得的。', 4.5, '2025-05-16 14:20:00');
INSERT INTO `accommodation_review` VALUES (3, 4, 1, '住宿体验很好，尤其喜欢酒店的中式风格设计，与故宫的文化氛围相得益彰。泳池和健身房设施一流，晚上泡个澡很舒服！', 5.0, '2025-05-17 20:15:00');
INSERT INTO `accommodation_review` VALUES (4, 2, 4, '住在长城脚下的感觉太棒了！清晨推开窗就能看到蜿蜒的长城，景色壮观。度假村设施完善，工作人员服务态度很好，晚上参加了篝火party，很有意思。', 5.0, '2025-05-18 09:40:00');
INSERT INTO `accommodation_review` VALUES (5, 3, 4, '环境不错，但价格偏高，感觉性价比不是特别高。接送长城服务很方便，餐厅的食物味道不错，但选择不多。', 4.0, '2025-05-19 16:50:00');
INSERT INTO `accommodation_review` VALUES (6, 4, 7, '酒店位置绝佳，房间阳台直面西湖，景色美不胜收。早上醒来就能看到雾气缭绕的湖面，非常诗意。服务也很到位，工作人员热情有礼。', 5.0, '2025-05-20 11:25:00');
INSERT INTO `accommodation_review` VALUES (7, 2, 7, '西湖雅园是我住过最美的酒店之一，江南园林式的设计很有韵味，客房宽敞舒适，下午茶也很精致。唯一遗憾的是周末人太多，有点吵。', 4.5, '2025-05-21 13:10:00');
INSERT INTO `accommodation_review` VALUES (8, 3, 10, '酒店环境超出预期，温泉SPA非常享受，缓解了一天登山的疲劳。餐厅的徽州菜做得地道美味，服务员都很专业友善。', 5.0, '2025-05-22 19:20:00');
INSERT INTO `accommodation_review` VALUES (9, 4, 10, '设施一流，服务周到，但价格确实不便宜。不过登山前后住这里很值得，特别是私人温泉，泡完整个人都轻松了！', 4.5, '2025-05-23 21:35:00');
INSERT INTO `accommodation_review` VALUES (10, 2, 13, '酒店位置好，就在西街上，出行方便。房间阳台可以看到漓江和喀斯特山峰，风景如画。房间干净舒适，服务热情，值得推荐！', 4.5, '2025-05-24 10:40:00');
INSERT INTO `accommodation_review` VALUES (11, 3, 13, '整体体验不错，但房间隔音稍差，西街晚上比较热闹，对睡眠质量有些影响。不过风景确实很美，早餐也不错。', 4.0, '2025-05-25 12:30:00');

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
) ENGINE = InnoDB AUTO_INCREMENT = 143 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '景点分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scenic_category
-- ----------------------------
INSERT INTO `scenic_category` VALUES (1, '自然风光', '包括山水、湖泊、森林等自然景观', '🏞️', 0, 1, '2025-06-28 22:21:51', '2025-06-28 22:21:51', NULL);
INSERT INTO `scenic_category` VALUES (2, '文化古迹', '包括历史遗迹、古建筑、博物馆等', '🏛️', 0, 2, '2025-06-28 22:21:51', '2025-06-28 22:21:51', NULL);
INSERT INTO `scenic_category` VALUES (3, '民族风情', '展示各民族特色文化和传统习俗', '👘', 0, 3, '2025-06-28 22:21:51', '2025-06-28 22:21:51', NULL);
INSERT INTO `scenic_category` VALUES (4, '主题公园', '各类主题游乐园和休闲娱乐场所', '🎡', 0, 4, '2025-06-28 22:21:51', '2025-06-28 22:21:51', NULL);
INSERT INTO `scenic_category` VALUES (5, '美食街区', '特色美食聚集地和小吃街', '🍜', 0, 5, '2025-06-28 22:21:51', '2025-06-28 22:21:51', NULL);
INSERT INTO `scenic_category` VALUES (6, '宗教圣地', '寺庙、道观等宗教场所', '🏯', 0, 6, '2025-06-28 22:21:51', '2025-06-28 22:21:51', NULL);
INSERT INTO `scenic_category` VALUES (7, '红色旅游', '革命历史纪念地和教育基地', '🏴', 0, 7, '2025-06-28 22:21:51', '2025-06-28 22:21:51', NULL);
INSERT INTO `scenic_category` VALUES (8, '乡村旅游', '农家乐、田园风光等乡村景点', '🌾', 0, 8, '2025-06-28 22:21:51', '2025-06-28 22:21:51', NULL);
INSERT INTO `scenic_category` VALUES (9, '滨海度假', '海滩、海岛等滨海旅游景点', '🏖️', 0, 9, '2025-06-28 22:21:51', '2025-06-28 22:21:51', NULL);
INSERT INTO `scenic_category` VALUES (10, '工业遗产', '具有历史价值的工业遗址和设施', '🏭', 0, 10, '2025-06-28 22:21:51', '2025-06-28 22:21:51', NULL);
INSERT INTO `scenic_category` VALUES (11, '南宁市', '广西壮族自治区首府', '🏛️', 0, 11, '2025-06-29 00:44:40', '2025-06-29 00:44:40', '广西');
INSERT INTO `scenic_category` VALUES (12, '桂林市', '世界著名的旅游城市', '🗻', 0, 12, '2025-06-29 00:44:40', '2025-06-29 00:44:40', '广西');
INSERT INTO `scenic_category` VALUES (13, '北海市', '著名的滨海旅游城市', '🏖️', 0, 13, '2025-06-29 00:44:40', '2025-06-29 00:44:40', '广西');
INSERT INTO `scenic_category` VALUES (14, '防城港市', '中国-东盟海上合作战略支点', '🚢', 0, 14, '2025-06-29 00:44:40', '2025-06-29 00:44:40', '广西');
INSERT INTO `scenic_category` VALUES (15, '自然风光-南宁', '南宁市自然景观', '🏞️', 11, 15, '2025-06-29 00:44:40', '2025-06-29 00:44:40', '南宁');
INSERT INTO `scenic_category` VALUES (16, '自然风光-桂林', '桂林市自然景观', '🏞️', 12, 16, '2025-06-29 00:44:40', '2025-06-29 00:44:40', '桂林');
INSERT INTO `scenic_category` VALUES (17, '自然风光-北海', '北海市自然景观', '🏞️', 13, 17, '2025-06-29 00:44:40', '2025-06-29 00:44:40', '北海');
INSERT INTO `scenic_category` VALUES (30, '人文古迹-南宁', '南宁市历史文化景点', '🏛️', 11, 30, '2025-06-29 00:44:40', '2025-06-29 00:44:40', '南宁');
INSERT INTO `scenic_category` VALUES (31, '人文古迹-桂林', '桂林市历史文化景点', '🏛️', 12, 31, '2025-06-29 00:44:40', '2025-06-29 00:44:40', '桂林');
INSERT INTO `scenic_category` VALUES (32, '人文古迹-北海', '北海市历史文化景点', '🏛️', 13, 32, '2025-06-29 00:44:40', '2025-06-29 00:44:40', '北海');
INSERT INTO `scenic_category` VALUES (45, '民族风情-南宁', '南宁市民族特色景点', '👘', 11, 45, '2025-06-29 00:44:40', '2025-06-29 00:44:40', '南宁');
INSERT INTO `scenic_category` VALUES (46, '民族风情-北海', '北海市民族特色景点', '👘', 13, 46, '2025-06-29 00:44:40', '2025-06-29 00:44:40', '北海');
INSERT INTO `scenic_category` VALUES (75, '美食街区-桂林', '桂林市特色美食街区', '🍜', 12, 75, '2025-06-29 00:44:40', '2025-06-29 00:44:40', '桂林');
INSERT INTO `scenic_category` VALUES (101, '南宁市', '广西壮族自治区首府', '🏛️', 0, 11, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '广西');
INSERT INTO `scenic_category` VALUES (102, '桂林市', '世界著名的旅游城市', '🗻', 0, 12, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '广西');
INSERT INTO `scenic_category` VALUES (103, '北海市', '著名的滨海旅游城市', '🏖️', 0, 13, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '广西');
INSERT INTO `scenic_category` VALUES (104, '柳州市', '工业旅游城市', '🏭', 0, 14, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '广西');
INSERT INTO `scenic_category` VALUES (111, '自然风光-南宁', '南宁市自然景观', '🏞️', 101, 15, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '南宁');
INSERT INTO `scenic_category` VALUES (112, '人文古迹-南宁', '南宁市历史文化景点', '🏛️', 101, 30, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '南宁');
INSERT INTO `scenic_category` VALUES (113, '民族风情-南宁', '南宁市民族特色景点', '👘', 101, 45, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '南宁');
INSERT INTO `scenic_category` VALUES (121, '自然风光-桂林', '桂林市自然景观', '🏞️', 102, 16, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '桂林');
INSERT INTO `scenic_category` VALUES (122, '人文古迹-桂林', '桂林市历史文化景点', '🏛️', 102, 31, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '桂林');
INSERT INTO `scenic_category` VALUES (123, '美食街区-桂林', '桂林市特色美食街区', '🍜', 102, 75, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '桂林');
INSERT INTO `scenic_category` VALUES (131, '自然风光-北海', '北海市自然景观', '🏞️', 103, 17, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '北海');
INSERT INTO `scenic_category` VALUES (132, '人文古迹-北海', '北海市历史文化景点', '🏛️', 103, 32, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '北海');
INSERT INTO `scenic_category` VALUES (133, '民族风情-北海', '北海市民族特色景点', '👘', 103, 46, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '北海');
INSERT INTO `scenic_category` VALUES (141, '自然风光-柳州', '柳州市自然景观', '🏞️', 104, 19, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '柳州');
INSERT INTO `scenic_category` VALUES (142, '人文古迹-柳州', '柳州市历史文化景点', '🏛️', 104, 33, '2025-06-29 00:49:16', '2025-06-29 00:49:16', '柳州');

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
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '景点收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scenic_collection
-- ----------------------------
INSERT INTO `scenic_collection` VALUES (9, 6, 4, '2025-06-19 21:37:42');
INSERT INTO `scenic_collection` VALUES (10, 6, 1, '2025-06-19 21:38:51');

-- ----------------------------
-- Table structure for scenic_spot
-- ----------------------------
DROP TABLE IF EXISTS `scenic_spot`;
CREATE TABLE `scenic_spot`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '景点ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '景点名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '景点描述',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地理位置',
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
  INDEX `fk_scenic_category`(`category_id` ASC) USING BTREE,
  INDEX `idx_region_id`(`region_id` ASC) USING BTREE,
  CONSTRAINT `fk_scenic_category` FOREIGN KEY (`category_id`) REFERENCES `scenic_category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_scenic_region` FOREIGN KEY (`region_id`) REFERENCES `scenic_category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '景点信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scenic_spot
-- ----------------------------
INSERT INTO `scenic_spot` VALUES (34, '青秀山', '青秀山风景名胜区是国家AAAA级旅游景区，以其独特的自然景观和人文景观而闻名。景区内有青秀塔、茶花园、盆景园等景点。', '广西南宁市青秀区青秀山路', 60.00, '08:00-18:00', '/img/1748616033437.jpg', 108.347745, 22.787468, '2025-06-29 00:49:16', '2025-06-29 00:49:16', 111, 101);
INSERT INTO `scenic_spot` VALUES (35, '南宁民族博物馆', '南宁民族博物馆是展示广西少数民族历史文化的重要场所，收藏了大量珍贵的民族文物。', '广西南宁市青秀区民族大道', 0.00, '09:00-17:00', '/img/1748616061193.jpg', 108.367745, 22.817468, '2025-06-29 00:49:16', '2025-06-29 00:49:16', 113, 101);
INSERT INTO `scenic_spot` VALUES (36, '南湖公园', '南湖公园是南宁市区最大的综合性公园，园内湖水碧波荡漾，绿树成荫，是市民休闲娱乐的好去处。', '广西南宁市青秀区南湖路', 0.00, '06:00-22:00', '/img/1748616086868.jpg', 108.357745, 22.807468, '2025-06-29 00:49:16', '2025-06-29 00:49:16', 111, 101);
INSERT INTO `scenic_spot` VALUES (37, '象鼻山', '象鼻山是桂林市的标志性景观，因山形酷似一头巨象临江汲水而得名。', '广西桂林市象山区环城西一路', 75.00, '08:00-17:30', '/img/1748616133671.jpg', 110.287745, 25.277468, '2025-06-29 00:49:16', '2025-06-29 00:49:16', 121, 102);
INSERT INTO `scenic_spot` VALUES (38, '阳朔西街', '阳朔西街是阳朔最热闹的商业街，汇集了各种特色小店、餐馆和酒吧。', '广西桂林市阳朔县西街', 0.00, '全天开放', '/img/1748616162313.jpg', 110.487745, 24.777468, '2025-06-29 00:49:16', '2025-06-29 00:49:16', 123, 102);
INSERT INTO `scenic_spot` VALUES (39, '漓江', '漓江是桂林山水的精华所在，江水清澈，群峰倒映，是世界著名的旅游胜地。', '广西桂林市秀峰区', 220.00, '全天开放', '/img/1748616225252.jpg', 110.287745, 25.277468, '2025-06-29 00:49:16', '2025-06-29 00:49:16', 121, 102);
INSERT INTO `scenic_spot` VALUES (40, '银滩', '北海银滩是中国最好的海滩之一，因细腻洁白的沙滩而得名，是著名的度假胜地。', '广西北海市银海区银滩路', 60.00, '全天开放', '/img/1748616338014.jpg', 109.187745, 21.477468, '2025-06-29 00:49:16', '2025-06-29 00:49:16', 131, 103);
INSERT INTO `scenic_spot` VALUES (41, '北海老街', '北海老街保留了众多具有历史价值的骑楼建筑，是感受北海历史文化的最佳去处。', '广西北海市海城区北海老街', 0.00, '全天开放', '/img/1748616360745.jpg', 109.117745, 21.477468, '2025-06-29 00:49:16', '2025-06-29 00:49:16', 132, 103);
INSERT INTO `scenic_spot` VALUES (42, '涠洲岛', '涠洲岛是中国最大的火山岛，以火山地貌、奇特礁石和美丽的海滩闻名。', '广西北海市涠洲岛', 120.00, '全天开放', '/img/1748616386868.jpg', 109.107745, 21.017468, '2025-06-29 00:49:16', '2025-06-29 00:49:16', 131, 103);
INSERT INTO `scenic_spot` VALUES (43, '柳侯公园', '柳侯公园是柳州市最著名的公园之一，园内有柳侯祠、放生池等景点。', '广西柳州市城中区北站路', 0.00, '06:00-22:00', '/img/1748616271030.jpg', 109.407745, 24.327468, '2025-06-29 00:49:16', '2025-06-29 00:49:16', 141, 104);
INSERT INTO `scenic_spot` VALUES (44, '龙潭公园', '龙潭公园是柳州市区重要的生态公园，以其独特的喀斯特地貌和水体景观著称。', '广西柳州市鱼峰区龙潭路', 20.00, '07:00-18:00', '/img/1748616297105.jpg', 109.417745, 24.337468, '2025-06-29 00:49:16', '2025-06-29 00:49:16', 141, 104);

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
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ticket
-- ----------------------------
INSERT INTO `ticket` VALUES (1, 1, '故宫博物院-成人票', 80.00, 60.00, '成人票', '购买后30天内有效', '故宫博物院标准成人票，含基本参观项目，不包含珍宝馆、钟表馆等特殊展厅', 999, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');
INSERT INTO `ticket` VALUES (2, 1, '故宫博物院-学生票', 60.00, 40.00, '学生票', '购买后30天内有效，需出示有效学生证', '故宫博物院学生优惠票，含基本参观项目，不包含珍宝馆、钟表馆等特殊展厅', 498, 1, '2025-05-30 11:18:59', '2025-05-31 02:21:21');
INSERT INTO `ticket` VALUES (3, 1, '故宫博物院-特惠套票', 120.00, 100.00, '特惠套票', '购买后30天内有效', '故宫博物院特惠套票，含基本参观项目及珍宝馆、钟表馆特殊展厅', 299, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');
INSERT INTO `ticket` VALUES (4, 2, '八达岭长城-成人票', 60.00, 45.00, '成人票', '仅限当天使用', '长城八达岭段成人标准门票，含往返缆车', 1500, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');
INSERT INTO `ticket` VALUES (5, 2, '八达岭长城-儿童票', 30.00, 25.00, '儿童票', '仅限当天使用，1.2米以下儿童免票', '长城八达岭段儿童优惠门票，含往返缆车', 499, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');
INSERT INTO `ticket` VALUES (6, 2, '八达岭长城-老人票', 30.00, 25.00, '老人票', '仅限当天使用，需出示老年证', '长城八达岭段老人优惠门票，含往返缆车', 299, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');
INSERT INTO `ticket` VALUES (7, 3, '西湖景区-联票', 80.00, 70.00, '联票', '购买后7天内有效', '西湖景区联票，包含断桥、雷峰塔、三潭印月等景点', 2000, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');
INSERT INTO `ticket` VALUES (8, 3, '西湖游船票-成人', 60.00, 55.00, '成人票', '购买后当天有效', '西湖游船标准票，含环湖游船服务', 999, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');
INSERT INTO `ticket` VALUES (9, 3, '西湖游船票-儿童', 30.00, 25.00, '儿童票', '购买后当天有效，1.2米以下儿童免票', '西湖游船儿童票，含环湖游船服务', 500, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');
INSERT INTO `ticket` VALUES (10, 4, '黄山风景区-成人票', 230.00, 190.00, '成人票', '购买后2天内有效', '黄山风景区成人门票，不含缆车费用', 800, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');
INSERT INTO `ticket` VALUES (11, 4, '黄山风景区-学生票', 160.00, 115.00, '学生票', '购买后2天内有效，需出示有效学生证', '黄山风景区学生优惠票，不含缆车费用', 400, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');
INSERT INTO `ticket` VALUES (12, 4, '黄山风景区-两日游套票', 350.00, 320.00, '套票', '购买后7天内有效，连续两天使用', '黄山风景区两日游套票，含山上住宿一晚，不含缆车费用', 200, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');
INSERT INTO `ticket` VALUES (13, 5, '漓江精华游-成人票', 220.00, 180.00, '成人票', '购买后15天内有效', '漓江精华段竹筏漂流，含接送服务', 600, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');
INSERT INTO `ticket` VALUES (14, 5, '漓江精华游-儿童票', 120.00, 100.00, '儿童票', '购买后15天内有效，1.2米以下儿童免票', '漓江精华段竹筏漂流儿童票，含接送服务', 300, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');
INSERT INTO `ticket` VALUES (15, 5, '桂林山水一日游套票', 280.00, 250.00, '套票', '购买后15天内有效', '含漓江精华段漂流、象山公园、七星公园等景点门票，含午餐和接送服务', 200, 1, '2025-05-30 11:18:59', '2025-05-30 11:18:59');

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
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ticket_order
-- ----------------------------
INSERT INTO `ticket_order` VALUES (1, 'ORD202505151001', 2, 1, 2, '张三', '13712345678', '110101199001011234', '2025-06-01', 120.00, 4, '2025-05-15 10:30:00', 'ALIPAY', '2025-05-15 10:01:00', '2025-05-15 10:30:00');
INSERT INTO `ticket_order` VALUES (2, 'ORD202505151002', 3, 4, 3, '李四', '13812345679', '310101199102023456', '2025-06-05', 135.00, 1, '2025-05-15 11:15:00', 'WECHAT', '2025-05-15 11:02:00', '2025-05-15 11:15:00');
INSERT INTO `ticket_order` VALUES (3, 'ORD202505151003', 4, 10, 1, '王五', '13912345670', '440101199203034567', '2025-06-10', 190.00, 0, NULL, NULL, '2025-05-15 14:05:00', '2025-05-15 14:05:00');
INSERT INTO `ticket_order` VALUES (4, 'ORD202505151004', 2, 7, 2, '张三', '13712345678', '110101199001011234', '2025-06-15', 140.00, 3, '2025-05-15 15:30:00', 'ALIPAY', '2025-05-15 15:10:00', '2025-05-15 16:20:00');
INSERT INTO `ticket_order` VALUES (5, 'ORD202505151005', 3, 13, 1, '李四', '13812345679', '310101199102023456', '2025-06-20', 180.00, 2, NULL, NULL, '2025-05-15 16:30:00', '2025-05-15 16:45:00');
INSERT INTO `ticket_order` VALUES (6, '20250530114026df64', 2, 2, 2, 'jx', '13456789991', '320821200104093911', '2025-05-29', 80.00, 1, '2025-05-30 11:40:33', 'WECHAT', NULL, NULL);
INSERT INTO `ticket_order` VALUES (8, '2025053111515351c2', 2, 8, 1, '测试', '13123456789', '', '2025-05-30', 55.00, 3, '2025-05-30 12:07:33', 'ALIPAY', '2025-05-30 11:52:53', '2025-05-30 12:06:44');
INSERT INTO `ticket_order` VALUES (9, '20250530121135b910', 2, 1, 1, 'ces', '15252393500', '', '2025-05-29', 60.00, 1, '2025-05-30 12:12:00', 'ALIPAY', '2025-05-30 12:11:35', '2025-05-30 12:12:00');
INSERT INTO `ticket_order` VALUES (10, '202505301212522f0a', 2, 6, 1, 'ces', '13123456789', '', '2025-06-04', 25.00, 1, '2025-05-30 12:13:17', 'ALIPAY', '2025-05-30 12:12:52', '2025-05-30 12:13:17');
INSERT INTO `ticket_order` VALUES (11, '202505301116277781', 2, 8, 1, 'ces', '13456789987', '', '2025-05-29', 55.00, 4, '2025-05-30 12:19:36', 'ALIPAY', '2025-05-30 12:16:27', '2025-05-30 12:18:35');
INSERT INTO `ticket_order` VALUES (12, '20250530231406b2a1', 2, 5, 1, '张三', '13712345678', '', '2025-05-30', 25.00, 0, NULL, NULL, '2025-05-30 23:14:06', NULL);
INSERT INTO `ticket_order` VALUES (13, '20250531022028b885', 2, 3, 1, '张三', '13712345678', '', '2025-05-30', 100.00, 1, '2025-05-31 02:20:33', 'BANK_CARD', '2025-05-31 02:20:28', '2025-05-31 02:20:32');

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
INSERT INTO `travel_guide` VALUES (4, '桂林山水精华三日游', '<p>桂林山水精华三日游</p><p><br></p><p>第一天：象山公园 + 七星公园</p><p>游览市区内的象山公园和七星公园，感受桂林市区的山水之美。</p><p><br></p><p> 第二天：漓江精华段漂流</p><p>从桂林乘船至阳朔，欣赏漓江两岸的壮丽风光，这是桂林最精华的景观。</p><p><br></p><p>第三天：阳朔西街 + 十里画廊</p><p>上午游览阳朔西街，下午骑行或乘车游览十里画廊，欣赏田园风光。</p>', 2, '/img/1748616225252.jpg', 886, '2025-05-14 13:03:00', '2025-05-31 02:23:33');
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

SET FOREIGN_KEY_CHECKS = 1;
