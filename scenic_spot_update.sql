-- 先删除外键约束
ALTER TABLE scenic_spot DROP FOREIGN KEY fk_scenic_category;
ALTER TABLE scenic_spot DROP FOREIGN KEY fk_scenic_region;
ALTER TABLE scenic_spot DROP INDEX idx_region_id;

-- 删除旧表
DROP TABLE IF EXISTS `scenic_category`;

-- 创建新的scenic_category表
CREATE TABLE `scenic_category` (
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
) ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '景点分类表';

-- 添加基础分类数据
INSERT INTO scenic_category (id, name, description, icon, parent_id, sort_order, region) VALUES
-- 基础分类
(1, '自然风光', '包括山水、湖泊、森林等自然景观', '🏞️', 0, 1, NULL),
(2, '文化古迹', '包括历史遗迹、古建筑、博物馆等', '🏛️', 0, 2, NULL),
(3, '民族风情', '展示各民族特色文化和传统习俗', '👘', 0, 3, NULL),
(4, '主题公园', '各类主题游乐园和休闲娱乐场所', '🎡', 0, 4, NULL),
(5, '美食街区', '特色美食聚集地和小吃街', '🍜', 0, 5, NULL),
(6, '宗教圣地', '寺庙、道观等宗教场所', '🏯', 0, 6, NULL),
(7, '红色旅游', '革命历史纪念地和教育基地', '🏴', 0, 7, NULL),
(8, '乡村旅游', '农家乐、田园风光等乡村景点', '🌾', 0, 8, NULL),
(9, '滨海度假', '海滩、海岛等滨海旅游景点', '🏖️', 0, 9, NULL),
(10, '工业遗产', '具有历史价值的工业遗址和设施', '🏭', 0, 10, NULL);

-- 修改scenic_spot表结构
ALTER TABLE scenic_spot ADD COLUMN region_id bigint NULL DEFAULT NULL COMMENT '区域ID' AFTER category_id;
ALTER TABLE scenic_spot ADD INDEX idx_region_id(region_id);
ALTER TABLE scenic_spot ADD CONSTRAINT fk_scenic_category FOREIGN KEY (category_id) REFERENCES scenic_category (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- 清空现有的景点数据
DELETE FROM scenic_spot;

-- 添加广西景点数据
INSERT INTO scenic_spot (name, description, location, price, opening_hours, image_url, longitude, latitude, category_id, region_id) VALUES
-- 南宁市景点
('青秀山', '青秀山风景名胜区是国家AAAA级旅游景区，以其独特的自然景观和人文景观而闻名。景区内有青秀塔、茶花园、盆景园等景点。', '广西南宁市青秀区青秀山路', 60.00, '08:00-18:00', '/img/1748616033437.jpg', 108.347745, 22.787468, 1, NULL),
('南宁民族博物馆', '南宁民族博物馆是展示广西少数民族历史文化的重要场所，收藏了大量珍贵的民族文物。', '广西南宁市青秀区民族大道', 0.00, '09:00-17:00', '/img/1748616061193.jpg', 3, NULL),
('南湖公园', '南湖公园是南宁市区最大的综合性公园，园内湖水碧波荡漾，绿树成荫，是市民休闲娱乐的好去处。', '广西南宁市青秀区南湖路', 0.00, '06:00-22:00', '/img/1748616086868.jpg', 108.357745, 22.807468, 1, NULL),

-- 桂林市景点
('象鼻山', '象鼻山是桂林市的标志性景观，因山形酷似一头巨象临江汲水而得名。', '广西桂林市象山区环城西一路', 75.00, '08:00-17:30', '/img/1748616133671.jpg', 110.287745, 25.277468, 1, NULL),
('阳朔西街', '阳朔西街是阳朔最热闹的商业街，汇集了各种特色小店、餐馆和酒吧。', '广西桂林市阳朔县西街', 0.00, '全天开放', '/img/1748616162313.jpg', 110.487745, 24.777468, 5, NULL),
('漓江', '漓江是桂林山水的精华所在，江水清澈，群峰倒映，是世界著名的旅游胜地。', '广西桂林市秀峰区', 220.00, '全天开放', '/img/1748616225252.jpg', 110.287745, 25.277468, 1, NULL),

-- 北海市景点
('银滩', '北海银滩是中国最好的海滩之一，因细腻洁白的沙滩而得名，是著名的度假胜地。', '广西北海市银海区银滩路', 60.00, '全天开放', '/img/1748616338014.jpg', 109.187745, 21.477468, 9, NULL),
('北海老街', '北海老街保留了众多具有历史价值的骑楼建筑，是感受北海历史文化的最佳去处。', '广西北海市海城区北海老街', 0.00, '全天开放', '/img/1748616360745.jpg', 109.117745, 21.477468, 2, NULL),
('涠洲岛', '涠洲岛是中国最大的火山岛，以火山地貌、奇特礁石和美丽的海滩闻名。', '广西北海市涠洲岛', 120.00, '全天开放', '/img/1748616386868.jpg', 109.107745, 21.017468, 9, NULL),

-- 柳州市景点
('柳侯公园', '柳侯公园是柳州市最著名的公园之一，园内有柳侯祠、放生池等景点。', '广西柳州市城中区北站路', 0.00, '06:00-22:00', '/img/1748616271030.jpg', 109.407745, 24.327468, 2, NULL),
('龙潭公园', '龙潭公园是柳州市区重要的生态公园，以其独特的喀斯特地貌和水体景观著称。', '广西柳州市鱼峰区龙潭路', 20.00, '07:00-18:00', '/img/1748616297105.jpg', 109.417745, 24.337468, 1, NULL);

-- 添加 city 字段到 scenic_spot 表
ALTER TABLE scenic_spot ADD COLUMN city varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在城市' AFTER location;

-- 更新现有数据的城市信息
UPDATE scenic_spot SET city = '南宁' WHERE id IN (34, 35, 36);
UPDATE scenic_spot SET city = '桂林' WHERE id IN (37, 38, 39);
UPDATE scenic_spot SET city = '北海' WHERE id IN (40, 41, 42);
UPDATE scenic_spot SET city = '柳州' WHERE id IN (43, 44);