-- 插入广西地区的住宿数据
INSERT INTO accommodation (name, type, address, scenic_id, description, contact_phone, price_range, star_level, image_url, features, distance, city, province, create_time, update_time) VALUES
-- 桂林住宿
('桂林香格里拉大酒店', '豪华酒店', '广西桂林市七星区漓江路111号', 1, '坐落于漓江畔，是桂林地标性的五星级酒店。酒店以现代豪华风格为主，融入桂林山水元素，为宾客提供奢华舒适的住宿体验。', '0773-2222888', '￥1000-2000', 4.8, '/files/img/guilin_shangri_la.jpg', '免费WiFi,健身房,游泳池,商务中心,餐厅,会议室,SPA', '距离象鼻山景区0.5公里', '桂林', '广西', NOW(), NOW()),

('阳朔西街智选假日酒店', '精品酒店', '广西桂林市阳朔县西街口', 2, '位于阳朔最繁华的西街，周边景点众多，交通便利。酒店装修现代简约，干净整洁。', '0773-8888999', '￥300-600', 4.5, '/files/img/yangshuo_holiday.jpg', '免费WiFi,餐厅,24小时前台,行李寄存,商务中心', '距离西街步行街0.1公里', '阳朔', '广西', NOW(), NOW()),

('桂林榕湖边精品客栈', '民宿', '广西桂林市秀峰区正阳路23号', 3, '临近榕湖，环境优美，客栈风格复古文艺，房间布置温馨舒适。', '0773-3333666', '￥200-400', 4.3, '/files/img/guilin_lakeside.jpg', '免费WiFi,早餐,自行车租赁,观景平台,茶室', '距离榕湖公园0.2公里', '桂林', '广西', NOW(), NOW()),

-- 南宁住宿
('南宁万达文华酒店', '豪华酒店', '广西南宁市青秀区民族大道136号', 4, '位于南宁市中心商务区，是集商务、休闲、娱乐为一体的五星级酒店。', '0771-5555888', '￥800-1500', 4.7, '/files/img/nanning_wanda.jpg', '免费WiFi,健身房,游泳池,餐厅,会议室,商务中心,停车场', '距离青秀山5公里', '南宁', '广西', NOW(), NOW()),

('南宁青秀山景酒店', '商务酒店', '广西南宁市青秀区柳沙路189号', 5, '紧邻青秀山风景区，环境幽静，适合商务和休闲度假。', '0771-6666999', '￥400-800', 4.4, '/files/img/nanning_qingxiu.jpg', '免费WiFi,餐厅,会议室,健身房,停车场', '距离青秀山景区0.3公里', '南宁', '广西', NOW(), NOW()),

-- 北海住宿
('北海银滩度假酒店', '度假酒店', '广西北海市银海区银滩路66号', 6, '面朝北部湾，紧邻银滩，是观赏海景和享受海滨度假的理想选择。', '0779-3333888', '￥500-1000', 4.6, '/files/img/beihai_beach.jpg', '免费WiFi,游泳池,沙滩椅,餐厅,酒吧,spa,健身房', '距离银滩0.1公里', '北海', '广西', NOW(), NOW()),

('北海老街客栈', '民宿', '广西北海市海城区老街45号', 7, '位于北海老街，建筑保留了老北海的历史风貌，内部设施现代化。', '0779-2222666', '￥200-400', 4.2, '/files/img/beihai_oldstreet.jpg', '免费WiFi,早餐,接送服务,旅游咨询,自行车租赁', '距离北海老街0.1公里', '北海', '广西', NOW(), NOW()),

-- 崇左住宿
('德天瀑布度假酒店', '度假酒店', '广西崇左市大新县德天瀑布景区', 8, '位于德天跨国瀑布景区内，是观赏瀑布的最佳住宿选择。', '0771-7777888', '￥400-800', 4.5, '/files/img/detian_hotel.jpg', '免费WiFi,观景台,餐厅,停车场,旅游服务,会议室', '距离德天瀑布0.5公里', '崇左', '广西', NOW(), NOW()),

-- 防城港住宿
('东兴边贸酒店', '商务酒店', '广西防城港市东兴市边贸路88号', 9, '位于中越边境，靠近东兴口岸，适合商务和旅游住宿。', '0770-6666888', '￥300-600', 4.3, '/files/img/dongxing_hotel.jpg', '免费WiFi,餐厅,商务中心,会议室,停车场', '距离东兴口岸1公里', '防城港', '广西', NOW(), NOW()),

-- 河池住宿
('巴马长寿村度假酒店', '度假酒店', '广西河池市巴马瑶族自治县巴马镇长寿村', 10, '位于世界长寿之乡，环境清幽，空气清新，适合养生度假。', '0778-6666999', '￥400-800', 4.4, '/files/img/bama_hotel.jpg', '免费WiFi,养生餐厅,温泉,瑜伽室,中医理疗,户外活动', '距离百魔洞1公里', '河池', '广西', NOW(), NOW());

-- 更新一些关联数据
UPDATE accommodation SET scenic_id = (SELECT id FROM scenic_spot WHERE name LIKE '%象鼻山%' LIMIT 1) WHERE name = '桂林香格里拉大酒店';
UPDATE accommodation SET scenic_id = (SELECT id FROM scenic_spot WHERE name LIKE '%西街%' LIMIT 1) WHERE name = '阳朔西街智选假日酒店';
UPDATE accommodation SET scenic_id = (SELECT id FROM scenic_spot WHERE name LIKE '%榕湖%' LIMIT 1) WHERE name = '桂林榕湖边精品客栈';
UPDATE accommodation SET scenic_id = (SELECT id FROM scenic_spot WHERE name LIKE '%青秀山%' LIMIT 1) WHERE name = '南宁万达文华酒店';
UPDATE accommodation SET scenic_id = (SELECT id FROM scenic_spot WHERE name LIKE '%青秀山%' LIMIT 1) WHERE name = '南宁青秀山景酒店';
UPDATE accommodation SET scenic_id = (SELECT id FROM scenic_spot WHERE name LIKE '%银滩%' LIMIT 1) WHERE name = '北海银滩度假酒店';
UPDATE accommodation SET scenic_id = (SELECT id FROM scenic_spot WHERE name LIKE '%北海老街%' LIMIT 1) WHERE name = '北海老街客栈';
UPDATE accommodation SET scenic_id = (SELECT id FROM scenic_spot WHERE name LIKE '%德天瀑布%' LIMIT 1) WHERE name = '德天瀑布度假酒店';
UPDATE accommodation SET scenic_id = (SELECT id FROM scenic_spot WHERE name LIKE '%东兴%' LIMIT 1) WHERE name = '东兴边贸酒店';
UPDATE accommodation SET scenic_id = (SELECT id FROM scenic_spot WHERE name LIKE '%巴马%' LIMIT 1) WHERE name = '巴马长寿村度假酒店'; 