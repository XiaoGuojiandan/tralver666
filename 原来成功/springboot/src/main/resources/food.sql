-- 创建美食分类表
CREATE TABLE IF NOT EXISTS `food_category` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL COMMENT '分类名称',
    `icon` varchar(50) DEFAULT NULL COMMENT '分类图标',
    `description` varchar(200) DEFAULT NULL COMMENT '分类描述',
    `sort_order` int(11) DEFAULT 0 COMMENT '排序顺序',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='美食分类表';

-- 插入美食分类数据
INSERT INTO `food_category` (`name`, `icon`, `description`, `sort_order`, `create_time`, `update_time`) VALUES
('美味小吃', '🥘', '地道街边小吃，品味本地风味', 1, NOW(), NOW()),
('特产水果', '🍎', '新鲜当季水果，应时而采', 2, NOW(), NOW()),
('地方特色', '🍜', '传统地方美食，特色风味', 3, NOW(), NOW()),
('甜品饮品', '🧋', '甜蜜可口，解暑消渴', 4, NOW(), NOW()),
('海鲜水产', '🦐', '新鲜海鲜，大海风味', 5, NOW(), NOW()),
('农家美食', '🥘', '农家特色，田园风味', 6, NOW(), NOW()),
('烧烤火锅', '🍖', '烤炉飘香，锅气氤氲', 7, NOW(), NOW()),
('传统糕点', '🥮', '传统美味，甜蜜记忆', 8, NOW(), NOW());

-- 创建美食表
CREATE TABLE IF NOT EXISTS `food` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL COMMENT '美食名称',
    `description` text COMMENT '美食描述',
    `location` varchar(200) DEFAULT NULL COMMENT '地理位置',
    `city` varchar(50) DEFAULT NULL COMMENT '所在城市',
    `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID',
    `price_range` varchar(50) DEFAULT NULL COMMENT '价格范围',
    `business_hours` varchar(100) DEFAULT NULL COMMENT '营业时间',
    `image_url` varchar(255) DEFAULT NULL COMMENT '图片URL',
    `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
    `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category_id`),
    KEY `idx_city` (`city`),
    CONSTRAINT `fk_food_category` FOREIGN KEY (`category_id`) REFERENCES `food_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='美食表';

-- 插入美食数据
INSERT INTO `food` (`name`, `description`, `location`, `city`, `category_id`, `price_range`, `business_hours`, `image_url`, `longitude`, `latitude`, `create_time`, `update_time`) VALUES
-- 美味小吃
('螺蛳粉', '广西柳州特色小吃，酸辣鲜美，配料丰富，汤料浓郁，是广西最具代表性的美食之一', '柳州市螺蛳粉文化街', '柳州', (SELECT id FROM food_category WHERE name = '美味小吃'), '¥15-30', '07:00-24:00', '/files/img/luosifen.jpg', 109.415953, 24.325850, NOW(), NOW()),
('老友粉', '南宁特色米粉，以其独特的酸辣味和浓郁的蒜香闻名，是南宁人最爱的早餐之一', '南宁市朝阳街', '南宁', (SELECT id FROM food_category WHERE name = '美味小吃'), '¥8-15', '06:00-22:00', '/files/img/laoyoufen.jpg', 108.320004, 22.815978, NOW(), NOW()),
('桂林米粉', '桂林特色小吃，以其清淡爽滑、汤鲜味美著称，配以卤牛肉、花生等配料', '桂林市正阳步行街', '桂林', (SELECT id FROM food_category WHERE name = '美味小吃'), '¥10-20', '06:30-21:00', '/files/img/guilinmifen.jpg', 110.290195, 25.273566, NOW(), NOW()),

-- 特产水果
('荔浦芋头', '广西荔浦县特产，肉质细腻，口感绵密，香甜可口', '荔浦县特产市场', '桂林', (SELECT id FROM food_category WHERE name = '特产水果'), '¥15-30/斤', '08:00-18:00', '/files/img/lipuyutou.jpg', 110.395309, 24.497058, NOW(), NOW()),
('百色芒果', '百色特产，果肉细腻，香甜多汁，是广西知名水果之一', '百色市田东芒果基地', '百色', (SELECT id FROM food_category WHERE name = '特产水果'), '¥10-25/斤', '08:30-17:30', '/files/img/mangguo.jpg', 106.616285, 23.901512, NOW(), NOW()),
('沙田柚', '广西特产水果，果肉鲜嫩，清甜爽口，营养丰富', '防城港市上思县沙田柚基地', '防城港', (SELECT id FROM food_category WHERE name = '特产水果'), '¥8-15/斤', '09:00-17:00', '/files/img/shatianyou.jpg', 107.983906, 22.153973, NOW(), NOW()),

-- 地方特色
('梧州龟苓膏', '梧州特产，清凉解暑，滋补养生，是传统的养生美食', '梧州市龟苓膏文化街', '梧州', (SELECT id FROM food_category WHERE name = '地方特色'), '¥20-50', '09:00-21:00', '/files/img/guilinggao.jpg', 111.279115, 23.476962, NOW(), NOW()),
('北海烤生蚝', '北海特色海鲜，新鲜肥美，烤制后香气四溢', '北海市银海区侨港镇', '北海', (SELECT id FROM food_category WHERE name = '地方特色'), '¥50-100', '10:00-22:00', '/files/img/shengao.jpg', 109.193970, 21.473343, NOW(), NOW()),
('桂林啤酒鱼', '桂林特色菜，鱼肉鲜美，配以啤酒烹制，香味独特', '桂林市阳朔西街', '桂林', (SELECT id FROM food_category WHERE name = '地方特色'), '¥88-158', '10:30-22:00', '/files/img/pijiuyu.jpg', 110.496727, 24.778445, NOW(), NOW()),

-- 甜品饮品
('桂林马蹄糕', '桂林传统糕点，清甜爽口，质地细腻', '桂林市中心广场', '桂林', (SELECT id FROM food_category WHERE name = '甜品饮品'), '¥15-25', '09:00-20:00', '/files/img/matigao.jpg', 110.290195, 25.273566, NOW(), NOW()),
('柳州沙糕', '柳州特色糕点，香甜可口，口感独特', '柳州市文昌路', '柳州', (SELECT id FROM food_category WHERE name = '甜品饮品'), '¥10-20', '08:00-19:00', '/files/img/shagao.jpg', 109.415953, 24.325850, NOW(), NOW()),
('桂林油茶', '桂林特色早点，以油茶为主料，配以花生、炒米等', '桂林市正阳步行街', '桂林', (SELECT id FROM food_category WHERE name = '甜品饮品'), '¥8-15', '06:00-11:00', '/files/img/youcha.jpg', 110.290195, 25.273566, NOW(), NOW()),

-- 海鲜水产
('北海海鲜粥', '北海特色美食，用新鲜海鲜熬制，鲜美可口', '北海市北部湾广场', '北海', (SELECT id FROM food_category WHERE name = '海鲜水产'), '¥30-50', '10:00-22:00', '/files/img/haixianzhou.jpg', 109.119254, 21.481233, NOW(), NOW()),
('防城港八带', '防城港特色海鲜，新鲜美味，烹饪方法多样', '防城港市防城区', '防城港', (SELECT id FROM food_category WHERE name = '海鲜水产'), '¥60-100', '10:30-21:30', '/files/img/baidai.jpg', 108.345478, 21.768997, NOW(), NOW()),
('钦州大蚝', '钦州特产海鲜，个大肉肥，鲜美可口', '钦州市钦南区', '钦州', (SELECT id FROM food_category WHERE name = '海鲜水产'), '¥40-80', '11:00-22:00', '/files/img/dazao.jpg', 108.654146, 21.981240, NOW(), NOW()),

-- 农家美食
('三江侗族油茶', '三江特色美食，传统侗族风味，营养丰富', '三江侗族自治县', '柳州', (SELECT id FROM food_category WHERE name = '农家美食'), '¥20-40', '07:00-20:00', '/files/img/dongyoucha.jpg', 109.607675, 25.783198, NOW(), NOW()),
('龙胜糯米饭', '龙胜特色美食，糯米香甜，配料丰富', '龙胜各族自治县', '桂林', (SELECT id FROM food_category WHERE name = '农家美食'), '¥15-30', '08:00-19:00', '/files/img/nuomifan.jpg', 110.011239, 25.797931, NOW(), NOW()),
('融水苗家腊肉', '融水特产，传统苗族腊肉，香味浓郁', '融水苗族自治县', '柳州', (SELECT id FROM food_category WHERE name = '农家美食'), '¥60-120', '09:00-18:00', '/files/img/larou.jpg', 109.252744, 25.065955, NOW(), NOW()),

-- 烧烤火锅
('南宁老友粉火锅', '南宁特色火锅，以老友粉汤底，配料丰富', '南宁市青秀区', '南宁', (SELECT id FROM food_category WHERE name = '烧烤火锅'), '¥60-100/人', '11:00-24:00', '/files/img/laoyouhuoguo.jpg', 108.366543, 22.817002, NOW(), NOW()),
('柳州螺蛳粉火锅', '柳州特色火锅，以螺蛳粉汤底，风味独特', '柳州市鱼峰区', '柳州', (SELECT id FROM food_category WHERE name = '烧烤火锅'), '¥50-90/人', '11:30-23:30', '/files/img/luosihuoguo.jpg', 109.415953, 24.325850, NOW(), NOW()),
('桂林烤肉', '桂林特色烧烤，以其独特的烤制方法和调味料闻名', '桂林市七星区', '桂林', (SELECT id FROM food_category WHERE name = '烧烤火锅'), '¥40-80/人', '17:00-02:00', '/files/img/kaorou.jpg', 110.290195, 25.273566, NOW(), NOW()),

-- 传统糕点
('合浦月饼', '合浦特产，传统广式月饼，馅料丰富', '北海市合浦县', '北海', (SELECT id FROM food_category WHERE name = '传统糕点'), '¥30-60', '08:30-20:30', '/files/img/yuebing.jpg', 109.207354, 21.661428, NOW(), NOW()),
('桂林甑糕', '桂林传统糕点，松软可口，香甜适中', '桂林市叠彩区', '桂林', (SELECT id FROM food_category WHERE name = '传统糕点'), '¥15-30', '07:00-19:00', '/files/img/zenggao.jpg', 110.300259, 25.304585, NOW(), NOW()),
('南宁八角饼', '南宁特色糕点，香脆可口，形状独特', '南宁市兴宁区', '南宁', (SELECT id FROM food_category WHERE name = '传统糕点'), '¥10-25', '07:30-20:00', '/files/img/bajiao.jpg', 108.320004, 22.815978, NOW(), NOW());

-- 创建美食评论表
CREATE TABLE IF NOT EXISTS `food_comment` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `food_id` bigint(20) NOT NULL COMMENT '美食ID',
    `content` text NOT NULL COMMENT '评论内容',
    `rating` int(11) DEFAULT 5 COMMENT '评分(1-5)',
    `likes` int(11) DEFAULT 0 COMMENT '点赞数',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user` (`user_id`),
    KEY `idx_food` (`food_id`),
    CONSTRAINT `fk_food_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_food_comment_food` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='美食评论表';

-- 插入评论数据
INSERT INTO `food_comment` (`user_id`, `food_id`, `content`, `rating`, `likes`, `create_time`) VALUES
(1, 1, '螺蛳粉真的很好吃，酸辣适中，配料丰富，值得推荐！', 5, 10, NOW()),
(2, 1, '正宗的柳州螺蛳粉，汤料很浓郁，粉条劲道', 4, 5, NOW()),
(3, 2, '老友粉的蒜香很浓郁，很有南宁特色', 5, 8, NOW()),
(4, 3, '桂林米粉确实名不虚传，汤头清淡鲜美', 4, 6, NOW()),
(5, 4, '荔浦芋头质地细腻，蒸熟后香甜可口', 5, 12, NOW()),
(1, 5, '百色芒果真的很甜，个头大，果肉多', 5, 15, NOW()),
(2, 6, '沙田柚汁多味甜，很解暑', 4, 7, NOW()),
(3, 7, '梧州龟苓膏清凉爽滑，夏天必备', 5, 9, NOW()),
(4, 8, '北海的烤生蚝新鲜肥美，一定要尝试', 5, 20, NOW()),
(5, 9, '桂林啤酒鱼做法独特，鱼肉鲜嫩', 4, 11, NOW()); 