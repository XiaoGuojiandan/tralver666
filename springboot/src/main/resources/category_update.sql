-- 清空并重新插入景点分类数据
TRUNCATE TABLE `scenic_category`;

-- 插入新的分类数据
INSERT INTO `scenic_category` VALUES (1, '自然风光', '山水、湖泊、森林等自然景观', '🏞️', 0, 1, '2025-06-28 17:11:16', '2025-06-28 17:11:16', NULL);
INSERT INTO `scenic_category` VALUES (2, '人文古迹', '历史遗迹、古建筑、文化遗产等', '🏛️', 0, 2, '2025-06-28 17:11:16', '2025-06-28 17:11:16', NULL);
INSERT INTO `scenic_category` VALUES (3, '民族风情', '少数民族文化、传统习俗展示等', '👘', 0, 3, '2025-06-28 17:11:16', '2025-06-28 17:11:16', NULL);
INSERT INTO `scenic_category` VALUES (4, '主题公园', '游乐园、主题乐园等娱乐场所', '🎡', 0, 4, '2025-06-28 17:11:16', '2025-06-28 17:11:16', NULL);
INSERT INTO `scenic_category` VALUES (5, '美食街区', '特色美食、小吃街等', '🍜', 0, 5, '2025-06-28 17:11:16', '2025-06-28 17:11:16', NULL);
INSERT INTO `scenic_category` VALUES (6, '宗教圣地', '寺庙、道观等宗教场所', '🏯', 0, 6, '2025-06-28 17:11:16', '2025-06-28 17:11:16', NULL);
INSERT INTO `scenic_category` VALUES (7, '红色景区', '革命遗址、纪念馆等', '🏴', 0, 7, '2025-06-28 17:11:16', '2025-06-28 17:11:16', NULL);
INSERT INTO `scenic_category` VALUES (8, '乡村旅游', '农家乐、生态园等乡村景点', '🌾', 0, 8, '2025-06-28 17:11:16', '2025-06-28 17:11:16', NULL);
INSERT INTO `scenic_category` VALUES (9, '海滨度假', '海滩、海岛等滨海景点', '🏖️', 0, 9, '2025-06-28 17:11:16', '2025-06-28 17:11:16', NULL);
INSERT INTO `scenic_category` VALUES (10, '工业遗产', '工业博物馆、遗址公园等', '🏭', 0, 10, '2025-06-28 17:11:16', '2025-06-28 17:11:16', NULL); 