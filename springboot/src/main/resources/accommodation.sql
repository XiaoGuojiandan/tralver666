-- 添加城市和省份字段
ALTER TABLE accommodation
ADD COLUMN city VARCHAR(50) COMMENT '所在城市' AFTER distance,
ADD COLUMN province VARCHAR(50) COMMENT '所在省份' AFTER city; 