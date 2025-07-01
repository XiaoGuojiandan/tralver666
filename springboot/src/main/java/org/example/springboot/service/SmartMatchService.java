package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.springboot.entity.Accommodation;
import org.example.springboot.entity.Food;
import org.example.springboot.entity.ScenicSpot;
import org.example.springboot.entity.TravelGuide;
import org.example.springboot.entity.User;
import org.example.springboot.entity.ScenicCategory;
import org.example.springboot.mapper.AccommodationMapper;
import org.example.springboot.mapper.FoodMapper;
import org.example.springboot.mapper.ScenicSpotMapper;
import org.example.springboot.mapper.TravelGuideMapper;
import org.example.springboot.mapper.UserMapper;
import org.example.springboot.mapper.ScenicCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SmartMatchService {

    @Autowired
    private ScenicSpotMapper scenicSpotMapper;

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private AccommodationMapper accommodationMapper;

    @Autowired
    private TravelGuideMapper travelGuideMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ScenicCategoryMapper scenicCategoryMapper;

    // 预定义的关键词类别映射
    private static final Map<String, List<String>> CATEGORY_KEYWORDS = new HashMap<String, List<String>>() {{
        put("自然风光", Arrays.asList("山", "水", "风景", "自然", "景色", "风光", "山水", "景点", "公园", "湖", "瀑布", "峡谷", "森林", "山峰", "江河"));
        put("人文古迹", Arrays.asList("文化", "历史", "古迹", "遗址", "古", "传统", "庙", "寺", "塔", "博物馆", "纪念馆", "古建筑", "古城", "遗产"));
        put("民族风情", Arrays.asList("民族", "风情", "特色", "传统", "文化", "习俗", "村", "少数民族", "民俗", "节日", "歌舞", "服饰", "工艺"));
        put("主题公园", Arrays.asList("主题", "游乐", "乐园", "公园", "游园", "娱乐", "项目", "游玩", "体验", "设施", "表演", "互动"));
        put("美食街区", Arrays.asList("美食", "小吃", "特色", "菜", "饮食", "餐", "味", "饭", "馆", "店", "街", "夜市", "市场", "广场"));
        put("宗教圣地", Arrays.asList("宗教", "寺庙", "道观", "佛", "道", "神", "圣", "祈福", "朝拜", "香火", "修行", "禅"));
        put("红色景区", Arrays.asList("红色", "革命", "历史", "纪念", "爱国", "基地", "教育", "党", "军", "战争", "胜利", "英雄"));
        put("乡村旅游", Arrays.asList("乡村", "农家", "田园", "农业", "生态", "农场", "果园", "采摘", "农庄", "民宿", "农家乐"));
        put("海滨度假", Arrays.asList("海", "沙滩", "海滩", "岛", "海岛", "度假", "海湾", "渔", "港", "海鲜", "游艇", "冲浪", "潜水"));
        put("工业遗产", Arrays.asList("工业", "遗址", "厂", "矿", "博物馆", "科技", "现代", "创意", "艺术", "改造", "园区"));
    }};

    // 广西主要城市列表
    private static final List<String> GUANGXI_CITIES = Arrays.asList(
        "南宁", "桂林", "柳州", "梧州", "北海", "防城港", "钦州", "贵港", "玉林", 
        "百色", "贺州", "河池", "来宾", "崇左", "阳朔"
    );

    public Map<String, Object> getRecommendations(String query) {
        log.info("开始执行智能匹配服务，查询关键词: {}", query);
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 先尝试模糊匹配主要对象
            Map<String, Object> fuzzyResults = findAllTypesByFuzzyKeyword(query);
            
            if (!fuzzyResults.isEmpty()) {
                log.info("找到模糊匹配结果，开始补充地域相关推荐");
                
                // 2. 从模糊匹配结果中获取位置信息
                String location = null;
                String city = null;
                
                // 尝试从景点获取位置
                List<ScenicSpot> spots = (List<ScenicSpot>) fuzzyResults.get("scenicSpots");
                if (spots != null && !spots.isEmpty()) {
                    location = spots.get(0).getLocation();
                    city = extractAreaAndCity(location);
                }
                
                // 如果没有景点，尝试从美食获取位置
                if (location == null) {
                    List<Food> foods = (List<Food>) fuzzyResults.get("foods");
                    if (foods != null && !foods.isEmpty()) {
                        location = foods.get(0).getLocation();
                        city = extractAreaAndCity(location);
                    }
                }
                
                // 如果没有美食，尝试从住宿获取位置
                if (location == null) {
                    List<Accommodation> accommodations = (List<Accommodation>) fuzzyResults.get("accommodations");
                    if (accommodations != null && !accommodations.isEmpty()) {
                        location = accommodations.get(0).getAddress();
                        city = extractAreaAndCity(location);
                    }
                }
                
                // 3. 如果找到了位置信息，补充地域相关推荐
                if (location != null) {
                    log.info("从模糊匹配结果中获取到位置信息: {}, 城市: {}", location, city);
                    
                    // 如果某个类型没有模糊匹配结果，则添加基于位置的推荐
                    if (!fuzzyResults.containsKey("scenicSpots")) {
                        List<ScenicSpot> locationSpots = findByLocationHierarchy(location, null, city, scenicSpotMapper::findByLocation);
                        if (!locationSpots.isEmpty()) {
                            fuzzyResults.put("scenicSpots", limitResults(locationSpots, 10));
                        }
                    }
                    
                    if (!fuzzyResults.containsKey("foods")) {
                        List<Food> locationFoods = findByLocationHierarchy(location, null, city, foodMapper::findByLocation);
                        if (!locationFoods.isEmpty()) {
                            fuzzyResults.put("foods", limitResults(locationFoods, 10));
                        }
                    }
                    
                    if (!fuzzyResults.containsKey("accommodations")) {
                        List<Accommodation> locationAccommodations = findByLocationHierarchy(location, null, city, accommodationMapper::findByLocation);
                        if (!locationAccommodations.isEmpty()) {
                            fuzzyResults.put("accommodations", limitResults(locationAccommodations, 10));
                        }
                    }
                }
                
                return fuzzyResults;
            }

            // 4. 如果没有模糊匹配结果，进行常规搜索
            log.info("未找到模糊匹配结果，进行常规搜索");
            
            // 5. 分析查询关键词属于哪些类别
            Map<String, Double> categoryScores = analyzeCategoryScores(query);
            log.info("关键词类别分析结果: {}", categoryScores);

            // 6. 确定主要搜索类型
            String searchType = determineSearchType(query);
            log.info("确定的搜索类型: {}", searchType);

            // 7. 根据不同的搜索类型采用不同的策略
            switch (searchType) {
                case "scenic":
                    handleScenicSearch(query, result);
                    break;
                case "food":
                    handleFoodSearch(query, result);
                    break;
                case "accommodation":
                    handleAccommodationSearch(query, result);
                    break;
                default:
                    handleCategorySearch(query, categoryScores, result);
            }

            // 8. 填充用户信息
            enrichUserInfo(result);
            
            log.info("智能匹配服务执行完成");
            return result;
        } catch (Exception e) {
            log.error("智能匹配服务执行出错", e);
            throw e;
        }
    }

    private String determineSearchType(String query) {
        // 1. 检查是否是具体景点名称
        List<ScenicSpot> exactScenicMatches = scenicSpotMapper.findExactMatch(query);
        if (!exactScenicMatches.isEmpty()) {
            return "scenic";
        }

        // 2. 检查是否是具体美食名称
        List<Food> exactFoodMatches = foodMapper.findExactMatch(query);
        if (!exactFoodMatches.isEmpty()) {
            return "food";
        }

        // 3. 检查是否是具体住宿名称
        List<Accommodation> exactAccommodationMatches = accommodationMapper.findExactMatch(query);
        if (!exactAccommodationMatches.isEmpty()) {
            return "accommodation";
        }

        // 4. 检查查询词中的关键字
        String lowerQuery = query.toLowerCase();
        if (lowerQuery.contains("住") || lowerQuery.contains("酒店") || lowerQuery.contains("客栈") || 
            lowerQuery.contains("民宿") || lowerQuery.contains("度假")) {
            return "accommodation";
        }
        if (lowerQuery.contains("吃") || lowerQuery.contains("美食") || lowerQuery.contains("小吃") || 
            lowerQuery.contains("菜") || lowerQuery.contains("饭") || lowerQuery.contains("粉")) {
            return "food";
        }
        if (lowerQuery.contains("景点") || lowerQuery.contains("景区") || lowerQuery.contains("公园") || 
            lowerQuery.contains("名胜") || lowerQuery.contains("风景")) {
            return "scenic";
        }

        // 5. 默认返回scenic
        return "scenic";
    }

    private void handleScenicSearch(String query, Map<String, Object> result) {
        // 1. 首先查找精确匹配的景点
        List<ScenicSpot> exactMatches = scenicSpotMapper.findExactMatch(query);
        
        if (!exactMatches.isEmpty()) {
            // 找到精确匹配的景点
            ScenicSpot mainSpot = exactMatches.get(0);
            result.put("scenicSpots", Collections.singletonList(mainSpot));
            
            // 获取景点所在地区和城市
            String specificLocation = mainSpot.getLocation(); // 具体位置（如"象鼻山"）
            String city = extractAreaAndCity(specificLocation); // 所在城市（如"桂林"）
            log.info("景点具体位置: {}, 所在城市: {}", specificLocation, city);
            
            // 查找周边美食、住宿和攻略，采用层级查找策略
            result.put("foods", limitResults(findByLocationHierarchy(specificLocation, null, city, foodMapper::findByLocation), 10));
            result.put("accommodations", limitResults(findByLocationHierarchy(specificLocation, null, city, accommodationMapper::findByLocation), 10));
            // 查找相关攻略
            result.put("guides", findRelevantGuides(query, specificLocation));
        } else {
            // 没有精确匹配，执行模糊搜索
            List<ScenicSpot> scenicSpots = scenicSpotMapper.findByKeyword(query);
            result.put("scenicSpots", limitResults(scenicSpots, 10));
            
            if (!scenicSpots.isEmpty()) {
                // 获取第一个景点所在地区和城市
                ScenicSpot firstSpot = scenicSpots.get(0);
                String specificLocation = firstSpot.getLocation();
                String city = extractAreaAndCity(specificLocation);
                log.info("景点具体位置: {}, 所在城市: {}", specificLocation, city);
                
                // 查找相关美食、住宿和攻略
                result.put("foods", limitResults(findByLocationHierarchy(specificLocation, null, city, foodMapper::findByLocation), 10));
                result.put("accommodations", limitResults(findByLocationHierarchy(specificLocation, null, city, accommodationMapper::findByLocation), 10));
                result.put("guides", findRelevantGuides(query, specificLocation));
            }
        }
    }

    private void handleFoodSearch(String query, Map<String, Object> result) {
        // 1. 首先查找精确匹配的美食
        List<Food> exactMatches = foodMapper.findExactMatch(query);
        
        if (!exactMatches.isEmpty()) {
            // 找到精确匹配的美食
            Food mainFood = exactMatches.get(0);
            result.put("foods", Collections.singletonList(mainFood));
            
            // 获取美食所在地区和城市
            String specificLocation = mainFood.getLocation();
            String city = extractAreaAndCity(specificLocation);
            log.info("美食具体位置: {}, 所在城市: {}", specificLocation, city);
            
            // 查找周边景点、住宿和攻略
            result.put("scenicSpots", limitResults(findByLocationHierarchy(specificLocation, null, city, scenicSpotMapper::findByLocation), 10));
            result.put("accommodations", limitResults(findByLocationHierarchy(specificLocation, null, city, accommodationMapper::findByLocation), 10));
            result.put("guides", findRelevantGuides(query, specificLocation));
        } else {
            // 没有精确匹配，执行模糊搜索
            List<Food> foods = foodMapper.findByKeyword(query);
            result.put("foods", limitResults(foods, 10));
            
            if (!foods.isEmpty()) {
                // 获取第一个美食所在地区和城市
                Food firstFood = foods.get(0);
                String specificLocation = firstFood.getLocation();
                String city = extractAreaAndCity(specificLocation);
                
                // 查找相关景点、住宿和攻略
                result.put("scenicSpots", limitResults(findByLocationHierarchy(specificLocation, null, city, scenicSpotMapper::findByLocation), 10));
                result.put("accommodations", limitResults(findByLocationHierarchy(specificLocation, null, city, accommodationMapper::findByLocation), 10));
                result.put("guides", findRelevantGuides(query, specificLocation));
            }
        }
    }

    private void handleAccommodationSearch(String query, Map<String, Object> result) {
        // 1. 首先查找精确匹配的住宿
        List<Accommodation> exactMatches = accommodationMapper.findExactMatch(query);
        
        if (!exactMatches.isEmpty()) {
            // 找到精确匹配的住宿
            Accommodation mainAccommodation = exactMatches.get(0);
            result.put("accommodations", Collections.singletonList(mainAccommodation));
            
            // 获取住宿所在地区和城市
            String specificLocation = mainAccommodation.getAddress();
            String city = extractAreaAndCity(specificLocation);
            log.info("住宿具体位置: {}, 所在城市: {}", specificLocation, city);
            
            // 查找周边景点、美食和攻略
            result.put("scenicSpots", limitResults(findByLocationHierarchy(specificLocation, null, city, scenicSpotMapper::findByLocation), 10));
            result.put("foods", limitResults(findByLocationHierarchy(specificLocation, null, city, foodMapper::findByLocation), 10));
            result.put("guides", findRelevantGuides(query, specificLocation));
        } else {
            // 没有精确匹配，执行模糊搜索
            List<Accommodation> accommodations = accommodationMapper.findByKeyword(query);
            result.put("accommodations", limitResults(accommodations, 10));
            
            if (!accommodations.isEmpty()) {
                // 获取第一个住宿所在地区和城市
                Accommodation firstAccommodation = accommodations.get(0);
                String specificLocation = firstAccommodation.getAddress();
                String city = extractAreaAndCity(specificLocation);
                
                // 查找相关景点、美食和攻略
                result.put("scenicSpots", limitResults(findByLocationHierarchy(specificLocation, null, city, scenicSpotMapper::findByLocation), 10));
                result.put("foods", limitResults(findByLocationHierarchy(specificLocation, null, city, foodMapper::findByLocation), 10));
                result.put("guides", findRelevantGuides(query, specificLocation));
            }
        }
    }

    private void handleCategorySearch(String query, Map<String, Double> categoryScores, Map<String, Object> result) {
        String mainCategory = getMainCategory(categoryScores);
        log.info("执行基于主题的搜索策略: {}", mainCategory);
        
        // 1. 标签解析和关键词提取
        Set<String> searchKeywords = new HashSet<>();
        if (mainCategory != null && CATEGORY_KEYWORDS.containsKey(mainCategory)) {
            searchKeywords.addAll(CATEGORY_KEYWORDS.get(mainCategory));
        }
        searchKeywords.add(query);
        log.info("提取的搜索关键词: {}", searchKeywords);
        
        // 2. 查找相关景点
        List<ScenicSpot> scenicSpots = new ArrayList<>();
        
        // 2.1 先根据分类ID查找
        if (mainCategory != null) {
            LambdaQueryWrapper<ScenicCategory> categoryWrapper = new LambdaQueryWrapper<>();
            categoryWrapper.eq(ScenicCategory::getName, mainCategory);
            ScenicCategory category = scenicCategoryMapper.selectOne(categoryWrapper);
            
            if (category != null) {
                Long categoryId = category.getId();
                log.info("找到对应的分类ID: {}", categoryId);
                
                LambdaQueryWrapper<ScenicSpot> spotWrapper = new LambdaQueryWrapper<>();
                spotWrapper.eq(ScenicSpot::getCategoryId, categoryId);
                scenicSpots = scenicSpotMapper.selectList(spotWrapper);
                log.info("根据分类ID找到{}个景点", scenicSpots.size());
            }
        }
        
        // 2.2 使用关键词进行内容匹配
        if (scenicSpots.isEmpty()) {
            log.info("使用关键词进行内容匹配搜索");
            for (String keyword : searchKeywords) {
                List<ScenicSpot> keywordSpots = scenicSpotMapper.findByKeywordWithCategory(keyword);
                scenicSpots.addAll(keywordSpots);
            }
            // 去重
            scenicSpots = new ArrayList<>(new LinkedHashSet<>(scenicSpots));
            log.info("通过关键词匹配找到{}个景点", scenicSpots.size());
        }
        
        // 3. 查找相关美食
        List<Food> foods = new ArrayList<>();
        for (String keyword : searchKeywords) {
            List<Food> keywordFoods = foodMapper.findByKeyword(keyword);
            foods.addAll(keywordFoods);
        }
        foods = new ArrayList<>(new LinkedHashSet<>(foods));
        log.info("找到{}个相关美食", foods.size());
        
        // 4. 对景点和美食进行评分和排序
        scenicSpots = rankByRelevance(scenicSpots, query, searchKeywords);
        foods = rankByRelevance(foods, query, searchKeywords);
        
        // 限制结果数量并保存
        result.put("scenicSpots", limitResults(scenicSpots, 10));
        result.put("foods", limitResults(foods, 10));
        
        // 5. 获取相关城市
        Set<String> relatedCities = new HashSet<>();
        for (ScenicSpot spot : scenicSpots) {
            String city = extractAreaAndCity(spot.getLocation());
            if (city != null) {
                relatedCities.add(city);
            }
        }
        for (Food food : foods) {
            String city = extractAreaAndCity(food.getLocation());
            if (city != null) {
                relatedCities.add(city);
            }
        }
        log.info("相关城市: {}", relatedCities);
        
        // 6. 查找周边住宿
        List<Accommodation> allAccommodations = new ArrayList<>();
        for (String city : relatedCities) {
            List<Accommodation> cityAccommodations = accommodationMapper.findByLocation(city);
            allAccommodations.addAll(cityAccommodations);
        }
        
        // 7. 对住宿进行排序（考虑评分、评论数等）
        allAccommodations = rankAccommodations(allAccommodations);
        result.put("accommodations", limitResults(allAccommodations, 10));
        
        // 8. 查找相关攻略
        List<TravelGuide> guides = findMatchingGuides(query, searchKeywords, relatedCities);
        result.put("guides", limitResults(guides, 10));
        
        log.info("主题搜索完成，找到{}个景点, {}个美食, {}个住宿, {}个攻略", 
            result.get("scenicSpots") != null ? ((List)result.get("scenicSpots")).size() : 0,
            result.get("foods") != null ? ((List)result.get("foods")).size() : 0,
            result.get("accommodations") != null ? ((List)result.get("accommodations")).size() : 0,
            result.get("guides") != null ? ((List)result.get("guides")).size() : 0);
    }

    private <T> List<T> rankByRelevance(List<T> items, String query, Set<String> keywords) {
        if (items == null || items.isEmpty()) {
            return new ArrayList<>();
        }

        return items.stream()
            .map(item -> {
                double score = 0.0;
                
                // 1. 基础匹配分数
                if (item instanceof ScenicSpot) {
                    ScenicSpot spot = (ScenicSpot) item;
                    score += calculateContentMatchScore(spot.getName(), query, keywords) * 2.0;
                    score += calculateContentMatchScore(spot.getDescription(), query, keywords);
                    
                    // 加入评分和评论数的权重
                    if (spot.getRating() != null) {
                        score += spot.getRating() * 0.3;
                    }
                    if (spot.getReviewCount() != null) {
                        score += Math.min(spot.getReviewCount() * 0.01, 1.0);
                    }
                } else if (item instanceof Food) {
                    Food food = (Food) item;
                    score += calculateContentMatchScore(food.getName(), query, keywords) * 2.0;
                    score += calculateContentMatchScore(food.getDescription(), query, keywords);
                    
                    // 考虑评分
                    if (food.getRating() != null) {
                        score += food.getRating() * 0.3;
                    }
                }
                
                return new AbstractMap.SimpleEntry<>(item, score);
            })
            .filter(entry -> entry.getValue() > 0)
            .sorted(Map.Entry.<T, Double>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private double calculateContentMatchScore(String content, String query, Set<String> keywords) {
        if (content == null) {
            return 0.0;
        }
        
        double score = 0.0;
        content = content.toLowerCase();
        
        // 1. 直接匹配查询词
        if (content.contains(query.toLowerCase())) {
            score += 2.0;
        }
        
        // 2. 关键词匹配
        for (String keyword : keywords) {
            if (content.contains(keyword.toLowerCase())) {
                score += 1.0;
            }
        }
        
        // 3. 模糊匹配
        for (String keyword : keywords) {
            if (keyword.length() > 1) {
                int distance = calculateLevenshteinDistance(content, keyword);
                if (distance <= 2) {
                    score += 0.5 * (1.0 - (distance / 4.0));
                }
            }
        }
        
        return score;
    }

    private List<Accommodation> rankAccommodations(List<Accommodation> accommodations) {
        return accommodations.stream()
            .map(acc -> {
                double score = 0.0;
                
                // 1. 评分权重
                if (acc.getRating() != null) {
                    score += acc.getRating() * 0.4;
                }
                
                // 2. 评论数权重
                if (acc.getReviewCount() != null) {
                    score += Math.min(acc.getReviewCount() * 0.01, 1.0);
                }
                
                // 3. 星级权重
                if (acc.getStarLevel() != null) {
                    try {
                        double starLevel = acc.getStarLevel().doubleValue();
                        score += starLevel * 0.2;
                    } catch (Exception e) {
                        // 忽略无法解析的星级
                    }
                }
                
                // 4. 价格区间权重（假设价格区间格式为"100-200"这样的形式）
                if (acc.getPriceRange() != null) {
                    try {
                        String[] prices = acc.getPriceRange().split("-");
                        if (prices.length == 2) {
                            double avgPrice = (Double.parseDouble(prices[0]) + Double.parseDouble(prices[1])) / 2;
                            double priceScore = 1.0 - Math.min(avgPrice / 1000.0, 1.0);
                            score += priceScore * 0.2;
                        }
                    } catch (Exception e) {
                        // 忽略无法解析的价格区间
                    }
                }
                
                return new AbstractMap.SimpleEntry<>(acc, score);
            })
            .sorted(Map.Entry.<Accommodation, Double>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private List<TravelGuide> findMatchingGuides(String query, Set<String> keywords, Set<String> cities) {
        log.info("开始查找匹配的攻略，查询词: {}, 关键词: {}, 城市: {}", query, keywords, cities);
        
        List<TravelGuide> guides = travelGuideMapper.selectList(null).stream()
            .map(guide -> {
                double score = 0.0;
                
                // 1. 标题匹配
                if (guide.getTitle() != null) {
                    score += calculateContentMatchScore(guide.getTitle(), query, keywords) * 2.0;
                }
                
                // 2. 内容匹配
                if (guide.getContent() != null) {
                    score += calculateContentMatchScore(guide.getContent(), query, keywords);
                }
                
                // 3. 地域相关性
                for (String city : cities) {
                    if ((guide.getTitle() != null && guide.getTitle().contains(city)) ||
                        (guide.getContent() != null && guide.getContent().contains(city))) {
                        score += 1.0;
                        break;
                    }
                }
                
                // 4. 时间衰减因子（新的攻略得分更高）
                if (guide.getCreateTime() != null) {
                    long daysDiff = (System.currentTimeMillis() - guide.getCreateTime().getTime()) / (1000 * 60 * 60 * 24);
                    double timeFactor = Math.exp(-daysDiff / 365.0); // 一年的衰减因子
                    score *= (0.7 + 0.3 * timeFactor); // 保留70%的基础分数
                }
                
                log.debug("攻略评分计算 - ID: {}, 标题: {}, 得分: {}", guide.getId(), guide.getTitle(), score);
                return new AbstractMap.SimpleEntry<>(guide, score);
            })
            .filter(entry -> entry.getValue() > 0)
            .sorted(Map.Entry.<TravelGuide, Double>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        log.info("找到匹配的攻略数量: {}", guides.size());

        // 填充用户信息
        if (!guides.isEmpty()) {
            // 1. 收集所有非空的用户ID
            List<Long> userIds = guides.stream()
                .map(TravelGuide::getUserId)
                .filter(userId -> userId != null)
                .distinct()
                .collect(Collectors.toList());
            
            log.info("需要查询的用户ID列表: {}", userIds);
            
            // 2. 批量查询用户信息
            if (!userIds.isEmpty()) {
                List<User> users = userMapper.selectBatchIds(userIds);
                log.info("查询到的用户数量: {}", users.size());
                
                Map<Long, User> userMap = users.stream()
                    .collect(Collectors.toMap(User::getId, user -> user));
                
                // 3. 填充用户信息
                for (TravelGuide guide : guides) {
                    log.info("处理攻略 - ID: {}, 标题: {}, 用户ID: {}", guide.getId(), guide.getTitle(), guide.getUserId());
                    if (guide.getUserId() != null) {
                        User user = userMap.get(guide.getUserId());
                        if (user != null) {
                            guide.setUserNickname(user.getNickname());
                            guide.setUserAvatar(user.getAvatar());
                            log.info("设置用户信息 - 攻略ID: {}, 用户昵称: {}, 用户头像: {}", 
                                guide.getId(), user.getNickname(), user.getAvatar());
                        } else {
                            log.warn("未找到用户信息 - 攻略ID: {}, 用户ID: {}", guide.getId(), guide.getUserId());
                        }
                    } else {
                        log.warn("攻略没有关联用户ID - 攻略ID: {}", guide.getId());
                    }
                }
            } else {
                log.warn("没有找到需要查询的用户ID");
            }
        } else {
            log.warn("没有找到匹配的攻略");
        }

        return guides;
    }

    private void enrichUserInfo(Map<String, Object> result) {
            List<TravelGuide> guides = (List<TravelGuide>) result.get("guides");
            if (guides != null && !guides.isEmpty()) {
                for (TravelGuide guide : guides) {
                    if (guide.getUserId() != null) {
                        User user = userMapper.selectById(guide.getUserId());
                        if (user != null) {
                            guide.setUserNickname(user.getNickname());
                            guide.setUserAvatar(user.getAvatar());
                        }
                    }
                }
        }
    }

    private <T> List<T> filterAndRankByCategory(List<T> items, Map<String, Double> categoryScores) {
        if (items == null || items.isEmpty() || categoryScores.isEmpty()) {
            return items;
        }
        
        log.info("开始对{}个项目进行分类评分和排序", items.size());
        
        // 获取主要分类和其关键词
        String mainCategory = getMainCategory(categoryScores);
        List<String> keywords = mainCategory != null ? CATEGORY_KEYWORDS.get(mainCategory) : new ArrayList<>();
        
        return items.stream()
            .map(item -> {
                double score = 0.0;
                
                // 1. 基础分数（根据对象类型和属性计算）
                if (item instanceof ScenicSpot) {
                    ScenicSpot spot = (ScenicSpot) item;
                    // 分类匹配得分
                    if (spot.getCategoryInfo() != null && mainCategory != null && 
                        spot.getCategoryInfo().getName().equals(mainCategory)) {
                        score += 2.0;
                    }
                    // 关键词匹配得分
                    score += calculateKeywordMatchScore(spot.getName(), keywords) * 2.0;
                    score += calculateKeywordMatchScore(spot.getDescription(), keywords);
                } else if (item instanceof Food) {
                    Food food = (Food) item;
                    score += calculateKeywordMatchScore(food.getName(), keywords) * 2.0;
                    score += calculateKeywordMatchScore(food.getDescription(), keywords);
                } else if (item instanceof Accommodation) {
                    Accommodation accommodation = (Accommodation) item;
                    score += calculateKeywordMatchScore(accommodation.getName(), keywords) * 2.0;
                    score += calculateKeywordMatchScore(accommodation.getDescription(), keywords);
                } else if (item instanceof TravelGuide) {
                    TravelGuide guide = (TravelGuide) item;
                    score += calculateKeywordMatchScore(guide.getTitle(), keywords) * 2.0;
                    score += calculateKeywordMatchScore(guide.getContent(), keywords);
                }
                
                // 2. 额外加分（根据特定条件）
                if (item instanceof ScenicSpot) {
                    ScenicSpot spot = (ScenicSpot) item;
                    // 如果有评分，根据评分加分
                    if (spot.getRating() != null) {
                        score += spot.getRating() * 0.2;
                    }
                    // 如果有评论数，根据评论数加分
                    if (spot.getReviewCount() != null) {
                        score += Math.min(spot.getReviewCount() * 0.01, 1.0);
                    }
                }
                
                log.debug("项目 {} 得分: {}", item instanceof ScenicSpot ? ((ScenicSpot)item).getName() : 
                                          item instanceof Food ? ((Food)item).getName() : 
                                          item instanceof Accommodation ? ((Accommodation)item).getName() : 
                                          item instanceof TravelGuide ? ((TravelGuide)item).getTitle() : "未知",
                         score);
                
                return new AbstractMap.SimpleEntry<>(item, score);
            })
            .filter(entry -> entry.getValue() > 0) // 只保留得分大于0的项目
            .sorted(Map.Entry.<T, Double>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private double calculateKeywordMatchScore(String text, List<String> keywords) {
        if (text == null || keywords == null || keywords.isEmpty()) {
            return 0.0;
        }
        
        text = text.toLowerCase();
        double score = 0.0;
        
        // 1. 完全匹配
        for (String keyword : keywords) {
                if (text.contains(keyword)) {
                score += 1.0;
            }
        }
        
        // 2. 部分匹配（处理同音字、近义词等情况）
        if (score == 0) {
            for (String keyword : keywords) {
                if (keyword.length() > 1) {
                    for (int i = 0; i < text.length() - 1; i++) {
                        String subText = text.substring(i, Math.min(i + keyword.length() + 1, text.length()));
                        int distance = calculateLevenshteinDistance(keyword, subText);
                        if (distance <= 1) { // 允许最多1个字符的差异
                            score += 0.5;
                            break;
                        }
                    }
                }
            }
        }
        
        return score;
    }

    private String getMainCategory(Map<String, Double> categoryScores) {
        if (categoryScores.isEmpty()) {
            return null;
        }
        return categoryScores.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    private <T> List<T> limitResults(List<T> items, int limit) {
        if (items == null) {
            return new ArrayList<>();
        }
        return items.stream().limit(limit).collect(Collectors.toList());
    }

    private String extractCityFromQuery(String query) {
        // 从查询中提取城市名称
        for (String city : GUANGXI_CITIES) {
            if (query.contains(city)) {
                return city;
            }
        }
        return null;
    }

    private String extractLocationFromGuide(TravelGuide guide) {
        // 从攻略标题和内容中提取位置信息
        String content = guide.getTitle() + " " + guide.getContent();
        for (String city : GUANGXI_CITIES) {
            if (content.contains(city)) {
                return city;
            }
        }
        return null;
    }

    private Map<String, Double> analyzeCategoryScores(String query) {
        Map<String, Double> scores = new HashMap<>();
        log.info("开始分析查询关键词的分类得分: {}", query);
        
        // 对每个预定义类别计算匹配分数
        for (Map.Entry<String, List<String>> entry : CATEGORY_KEYWORDS.entrySet()) {
            String category = entry.getKey();
            List<String> keywords = entry.getValue();
            
            // 1. 直接匹配分类名称
            if (query.contains(category)) {
                scores.put(category, 1.0);
                log.info("分类 {} 直接匹配，得分: 1.0", category);
                continue;
            }
            
            // 2. 关键词匹配
            double score = 0.0;
            int matchCount = 0;
            for (String keyword : keywords) {
                if (query.contains(keyword)) {
                    score += 1.0;
                    matchCount++;
                }
            }
            
            // 3. 计算最终得分
            if (matchCount > 0) {
                // 根据匹配的关键词数量和总关键词数量计算得分
                double finalScore = score / keywords.size();
                scores.put(category, finalScore);
                log.info("分类 {} 通过关键词匹配 {} 个，得分: {}", category, matchCount, finalScore);
            }
        }
        
        // 4. 如果没有任何匹配，尝试模糊匹配
        if (scores.isEmpty()) {
            log.info("没有找到精确匹配，尝试模糊匹配");
            for (Map.Entry<String, List<String>> entry : CATEGORY_KEYWORDS.entrySet()) {
                String category = entry.getKey();
                List<String> keywords = entry.getValue();
                
                // 计算查询词与关键词的最小编辑距离
                double minDistance = keywords.stream()
                    .mapToDouble(keyword -> {
                        int distance = calculateLevenshteinDistance(query, keyword);
                        return 1.0 / (1.0 + distance); // 将距离转换为分数
                    })
                    .max()
                    .orElse(0.0);
                
                if (minDistance > 0.3) { // 设置一个阈值，只保留相似度较高的结果
                    scores.put(category, minDistance);
                    log.info("分类 {} 通过模糊匹配得分: {}", category, minDistance);
                }
            }
        }
        
        log.info("分类得分分析完成: {}", scores);
        return scores;
    }

    private double calculateCategoryMatchScore(String categoryName, Map<String, Double> categoryScores) {
        if (categoryName == null || categoryName.isEmpty() || categoryScores.isEmpty()) {
            return 0.0;
        }
        return categoryScores.getOrDefault(categoryName, 0.0);
    }

    private <T> List<T> rankResults(List<T> items, String query, Map<String, Double> categoryScores) {
        if (items == null || items.isEmpty()) {
            return new ArrayList<>();
        }

        // 计算每个项目的相关度分数
        return items.stream()
            .map(item -> new AbstractMap.SimpleEntry<>(item, calculateRelevanceScore(item, query, categoryScores)))
            .sorted(Map.Entry.<T, Double>comparingByValue().reversed())
            .limit(10) // 限制返回前10个最相关的结果
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private double calculateRelevanceScore(Object item, String query, Map<String, Double> categoryScores) {
        double score = 0;
        
        // 基于对象类型和属性计算相关度分数
        if (item instanceof ScenicSpot) {
            ScenicSpot spot = (ScenicSpot) item;
            score += calculateTextMatchScore(spot.getName(), query) * 2;
            score += calculateTextMatchScore(spot.getDescription(), query);
            score += calculateCategoryMatchScore(
                spot.getCategoryInfo() != null ? spot.getCategoryInfo().getName() : "",
                categoryScores
            );
        } else if (item instanceof Food) {
            Food food = (Food) item;
            score += calculateTextMatchScore(food.getName(), query) * 2;
            score += calculateTextMatchScore(food.getDescription(), query);
        } else if (item instanceof Accommodation) {
            Accommodation accommodation = (Accommodation) item;
            score += calculateTextMatchScore(accommodation.getName(), query) * 2;
            score += calculateTextMatchScore(accommodation.getDescription(), query);
        } else if (item instanceof TravelGuide) {
            TravelGuide guide = (TravelGuide) item;
            score += calculateTextMatchScore(guide.getTitle(), query) * 2;
            score += calculateTextMatchScore(guide.getContent(), query);
        }
        
        return score;
    }

    private double calculateTextMatchScore(String text, String query) {
        if (text == null || query == null) {
            return 0;
        }
        
        double score = 0;
        text = text.toLowerCase();
        query = query.toLowerCase();
        
        // 1. 完全匹配得分最高
        if (text.contains(query)) {
            score += 2.0;
        }
        
        // 2. 分词匹配
        String[] queryWords = query.split("\\s+");
        for (String word : queryWords) {
            if (word.length() < 2) continue; // 忽略过短的词
            
            if (text.contains(word)) {
                score += 1.0;
            } else {
                // 3. 部分匹配（处理同音字、近义词等情况）
                // 如果单词长度大于2，检查是否包含部分匹配
                if (word.length() > 2) {
                    for (int i = 0; i < text.length() - 2; i++) {
                        String subText = text.substring(i, Math.min(i + word.length() + 1, text.length()));
                        int distance = calculateLevenshteinDistance(word, subText);
                        if (distance <= 2) { // 允许最多2个字符的差异
                            score += 0.5 * (1.0 - (distance / 4.0)); // 差异越小，分数越高
                            break;
                        }
                    }
                }
            }
        }
        
        return score;
    }

    private int calculateLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + 1,
                                Math.min(dp[i - 1][j] + 1,
                                        dp[i][j - 1] + 1));
                }
            }
        }
        
        return dp[s1.length()][s2.length()];
    }

    private <T> List<T> findByLocationHierarchy(String specificLocation, String area, String city, Function<String, List<T>> finder) {
        log.info("开始层级位置查找，具体位置: {}, 所在地区: {}, 城市: {}", specificLocation, area, city);
        
        // 1. 先查找具体位置（如"象鼻山"）
        List<T> exactMatches = finder.apply(specificLocation);
        if (!exactMatches.isEmpty()) {
            log.info("在具体位置找到匹配项: {}", exactMatches.size());
            return exactMatches;
        }
        
        // 2. 查找所在地区（如"象山区"）
        if (area != null && !area.equals(specificLocation)) {
            List<T> areaMatches = finder.apply(area);
            if (!areaMatches.isEmpty()) {
                log.info("在所在地区找到匹配项: {}", areaMatches.size());
                return areaMatches;
            }
        }
        
        // 3. 查找所在城市（如"桂林"）
        if (city != null && !city.equals(area)) {
            List<T> cityMatches = finder.apply(city);
            if (!cityMatches.isEmpty()) {
                log.info("在所在城市找到匹配项: {}", cityMatches.size());
                return cityMatches;
            }
        }
        
        return new ArrayList<>();
    }

    private String extractAreaAndCity(String location) {
        // 这里简单处理，实际可能需要更复杂的地理信息处理
        for (String city : GUANGXI_CITIES) {
            if (location.contains(city)) {
                return city;
            }
        }
        return "桂林"; // 默认返回桂林
    }

    private List<TravelGuide> findRelevantGuides(String keyword, String location) {
        log.info("开始查找相关攻略，关键词: {}, 位置: {}", keyword, location);
        
        return travelGuideMapper.selectList(null).stream()
            .filter(guide -> {
                double score = 0;
                
                // 1. 关键词匹配（标题和内容）
                if (guide.getTitle() != null && guide.getTitle().contains(keyword)) {
                    score += 2.0; // 标题匹配权重更高
                }
                if (guide.getContent() != null && guide.getContent().contains(keyword)) {
                    score += 1.0;
                }
                
                // 2. 位置匹配
                if (location != null) {
                    if ((guide.getTitle() != null && guide.getTitle().contains(location)) ||
                        (guide.getContent() != null && guide.getContent().contains(location))) {
                        score += 1.5;
                    }
                    
                    // 尝试城市级别的匹配
                    String city = extractAreaAndCity(location);
                    if (!city.equals(location)) {
                        if ((guide.getTitle() != null && guide.getTitle().contains(city)) ||
                            (guide.getContent() != null && guide.getContent().contains(city))) {
                            score += 1.0;
                        }
                    }
                }
                
                return score > 0;
            })
            .sorted((g1, g2) -> {
                // 按创建时间降序排序
                return g2.getCreateTime().compareTo(g1.getCreateTime());
            })
            .limit(10)
            .collect(Collectors.toList());
    }

    private Map<String, Object> findAllTypesByFuzzyKeyword(String keyword) {
        log.info("开始全类型模糊匹配，关键词: {}", keyword);
        Map<String, Object> result = new HashMap<>();
        
        // 1. 查找景点
        List<ScenicSpot> scenicSpots = scenicSpotMapper.selectList(null).stream()
            .filter(spot -> containsKeywordFuzzy(spot.getName(), keyword) || 
                          containsKeywordFuzzy(spot.getDescription(), keyword))
            .limit(10)
            .collect(Collectors.toList());
        if (!scenicSpots.isEmpty()) {
            result.put("scenicSpots", scenicSpots);
            log.info("找到匹配的景点数量: {}", scenicSpots.size());
        }
        
        // 2. 查找美食
        List<Food> foods = foodMapper.selectList(null).stream()
            .filter(food -> containsKeywordFuzzy(food.getName(), keyword) || 
                          containsKeywordFuzzy(food.getDescription(), keyword))
            .limit(10)
            .collect(Collectors.toList());
        if (!foods.isEmpty()) {
            result.put("foods", foods);
            log.info("找到匹配的美食数量: {}", foods.size());
        }
        
        // 3. 查找住宿
        List<Accommodation> accommodations = accommodationMapper.selectList(null).stream()
            .filter(acc -> containsKeywordFuzzy(acc.getName(), keyword) || 
                          containsKeywordFuzzy(acc.getDescription(), keyword))
            .limit(10)
            .collect(Collectors.toList());
        if (!accommodations.isEmpty()) {
            result.put("accommodations", accommodations);
            log.info("找到匹配的住宿数量: {}", accommodations.size());
        }
        
        // 4. 查找攻略
        List<TravelGuide> guides = travelGuideMapper.selectList(null).stream()
            .filter(guide -> containsKeywordFuzzy(guide.getTitle(), keyword) || 
                           containsKeywordFuzzy(guide.getContent(), keyword))
            .limit(10)
            .collect(Collectors.toList());
        if (!guides.isEmpty()) {
            result.put("guides", guides);
            log.info("找到匹配的攻略数量: {}", guides.size());
        }
        
        return result;
    }

    private boolean containsKeywordFuzzy(String text, String keyword) {
        if (text == null || keyword == null) {
            return false;
        }
        
        text = text.toLowerCase();
        keyword = keyword.toLowerCase();
        
        // 1. 直接包含
        if (text.contains(keyword)) {
            return true;
        }
        
        // 2. 分词匹配（处理复合词，如"沙田柚"包含"柚"）
        String[] words = text.split("[\\s,，。、]+");
        for (String word : words) {
            if (word.contains(keyword)) {
                return true;
            }
        }
        
        // 3. 模糊匹配（处理近似词）
        int maxDistance = Math.max(1, keyword.length() / 3); // 允许的最大编辑距离
        for (String word : words) {
            if (calculateLevenshteinDistance(word, keyword) <= maxDistance) {
                return true;
            }
        }
        
        return false;
    }
} 