package org.example.springboot.service;

import org.example.springboot.mapper.ScenicSpotMapper;
import org.example.springboot.mapper.FoodMapper;
import org.example.springboot.mapper.AccommodationMapper;
import org.example.springboot.mapper.TravelGuideMapper;
import org.example.springboot.mapper.UserMapper;
import org.example.springboot.entity.ScenicSpot;
import org.example.springboot.entity.Food;
import org.example.springboot.entity.Accommodation;
import org.example.springboot.entity.TravelGuide;
import org.example.springboot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
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

    // 预定义的关键词类别映射
    private static final Map<String, List<String>> CATEGORY_KEYWORDS = new HashMap<String, List<String>>() {{
        put("山水风光", Arrays.asList("山", "水", "风景", "自然", "景色", "风光", "山水", "景点", "公园", "湖"));
        put("特色美食", Arrays.asList("美食", "小吃", "特色", "菜", "饮食", "餐", "味", "饭", "馆", "店", "粉", "面", "肉", "汤"));
        put("文化古迹", Arrays.asList("文化", "历史", "古迹", "遗址", "古", "传统", "庙", "寺", "塔"));
        put("民族风情", Arrays.asList("民族", "风情", "特色", "传统", "文化", "习俗", "村"));
        put("水果之旅", Arrays.asList("水果", "果园", "采摘", "果", "新鲜", "园"));
        put("红色旅游", Arrays.asList("红色", "革命", "历史", "纪念", "爱国", "基地"));
        put("户外探险", Arrays.asList("户外", "探险", "运动", "刺激", "野外", "徒步", "登山"));
        put("温泉度假", Arrays.asList("温泉", "度假", "休闲", "放松", "养生", "酒店"));
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
            // 1. 分析查询关键词属于哪些类别
            Map<String, Double> categoryScores = analyzeCategoryScores(query);
            log.info("关键词类别分析结果: {}", categoryScores);

            // 2. 提取查询中的城市信息
            String cityInQuery = extractCityFromQuery(query);
            log.info("从查询中提取的城市信息: {}", cityInQuery);

            // 3. 首先搜索所有类型的结果
            log.info("正在查询景点信息...");
            List<ScenicSpot> scenicSpots = scenicSpotMapper.findByKeyword(query);
            
            log.info("正在查询美食信息...");
            List<Food> foods = foodMapper.findByKeyword(query);
            
            log.info("正在查询住宿信息...");
            List<Accommodation> accommodations = accommodationMapper.findByKeyword(query);
            
            log.info("正在查询旅游攻略...");
            List<TravelGuide> guides = travelGuideMapper.findByKeyword(query);

            // 4. 确定主要搜索类型和地区
            String mainLocation = null;
            String searchType = determineSearchType(query, categoryScores, scenicSpots, foods, accommodations, guides);
            log.info("确定的主要搜索类型: {}", searchType);

            // 5. 根据主要搜索类型获取位置信息
            switch (searchType) {
                case "scenic":
                    if (!scenicSpots.isEmpty()) {
                        ScenicSpot spot = scenicSpots.get(0);
                        mainLocation = spot.getCity() != null ? spot.getCity() : spot.getLocation();
                    }
                    break;
                case "food":
                    if (!foods.isEmpty()) {
                        Food food = foods.get(0);
                        mainLocation = food.getCity() != null ? food.getCity() : food.getLocation();
                    }
                    break;
                case "accommodation":
                    if (!accommodations.isEmpty()) {
                        Accommodation acc = accommodations.get(0);
                        mainLocation = acc.getCity() != null ? acc.getCity() : acc.getAddress();
                    }
                    break;
                case "guide":
                    if (!guides.isEmpty()) {
                        mainLocation = extractLocationFromGuide(guides.get(0));
                    }
                    break;
            }

            // 如果没有从搜索结果中找到位置信息，使用查询中提取的城市
            if (mainLocation == null) {
                mainLocation = cityInQuery;
            }

            // 6. 获取相关推荐
            if (mainLocation != null) {
                log.info("找到主要位置信息: {}, 搜索类型: {}", mainLocation, searchType);
                
                // 根据不同的搜索类型和类别，调整推荐策略
                switch (searchType) {
                    case "scenic":
                        // 如果搜索的是景点，优先推荐同城市的相关景点
                        if (scenicSpots.isEmpty() || categoryScores.containsKey("山水风光")) {
                            List<ScenicSpot> relatedScenic = scenicSpotMapper.findByLocation(mainLocation);
                            scenicSpots = !relatedScenic.isEmpty() ? relatedScenic : scenicSpots;
                        }
                        // 推荐景点周边的美食和住宿
                        if (!scenicSpots.isEmpty()) {
                            ScenicSpot mainSpot = scenicSpots.get(0);
                            foods = foodMapper.findByLocation(mainSpot.getLocation());
                            accommodations = accommodationMapper.findByLocation(mainSpot.getLocation());
                        }
                        break;
                        
                    case "food":
                        // 如果搜索的是美食，优先推荐同类型的美食
                        if (foods.isEmpty()) {
                            List<Food> relatedFood = foodMapper.findByLocation(mainLocation);
                            foods = !relatedFood.isEmpty() ? relatedFood : foods;
                        }
                        // 推荐美食所在地的景点和住宿
                        List<ScenicSpot> nearbyScenic = scenicSpotMapper.findByLocation(mainLocation);
                        scenicSpots = !nearbyScenic.isEmpty() ? nearbyScenic : scenicSpots;
                        List<Accommodation> nearbyAccommodation = accommodationMapper.findByLocation(mainLocation);
                        accommodations = !nearbyAccommodation.isEmpty() ? nearbyAccommodation : accommodations;
                        break;
                        
                    case "accommodation":
                        // 如果搜索的是住宿，优先推荐同类型的住宿
                        if (accommodations.isEmpty()) {
                            List<Accommodation> relatedAccommodation = accommodationMapper.findByLocation(mainLocation);
                            accommodations = !relatedAccommodation.isEmpty() ? relatedAccommodation : accommodations;
                        }
                        // 推荐住宿周边的景点和美食
                        List<ScenicSpot> nearbySpots = scenicSpotMapper.findByLocation(mainLocation);
                        scenicSpots = !nearbySpots.isEmpty() ? nearbySpots : scenicSpots;
                        List<Food> nearbyFood = foodMapper.findByLocation(mainLocation);
                        foods = !nearbyFood.isEmpty() ? nearbyFood : foods;
                        break;
                }
                
                // 攻略总是基于位置和类型来推荐
                List<TravelGuide> relatedGuides = travelGuideMapper.findByLocationAndType(mainLocation, searchType);
                guides = !relatedGuides.isEmpty() ? relatedGuides : guides;
            }

            // 7. 对结果进行排序和限制
            result.put("scenicSpots", rankResults(scenicSpots, query, categoryScores));
            result.put("foods", rankResults(foods, query, categoryScores));
            result.put("accommodations", rankResults(accommodations, query, categoryScores));
            
            // Fill user information for guides
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
            result.put("guides", rankResults(guides, query, categoryScores));
            
            // 8. 添加搜索上下文信息
            final String finalMainLocation = mainLocation;
            final String finalSearchType = searchType;
            final Map<String, Double> finalCategoryScores = categoryScores;
            
            result.put("searchContext", new HashMap<String, Object>() {{
                put("mainLocation", finalMainLocation);
                put("searchType", finalSearchType);
                put("categories", finalCategoryScores);
            }});
            
            log.info("智能匹配服务执行完成");
            return result;
        } catch (Exception e) {
            log.error("智能匹配服务执行出错", e);
            throw e;
        }
    }

    private String determineSearchType(String query, Map<String, Double> categoryScores,
                                     List<ScenicSpot> scenicSpots, List<Food> foods,
                                     List<Accommodation> accommodations, List<TravelGuide> guides) {
        // 1. 首先检查类别得分
        if (!categoryScores.isEmpty()) {
            Map.Entry<String, Double> highestCategory = categoryScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get();
            
            // 如果某个类别得分特别高（>0.5），直接返回对应的搜索类型
            if (highestCategory.getValue() > 0.5) {
                switch (highestCategory.getKey()) {
                    case "山水风光":
                    case "文化古迹":
                    case "红色旅游":
                    case "户外探险":
                        return "scenic";
                    case "特色美食":
                        return "food";
                    case "温泉度假":
                        return "accommodation";
                }
            }
        }
        
        // 2. 检查查询词中的关键字
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
        if (lowerQuery.contains("攻略") || lowerQuery.contains("游记") || lowerQuery.contains("指南")) {
            return "guide";
        }
        
        // 3. 根据结果数量判断
        Map<String, Integer> counts = new HashMap<>();
        counts.put("scenic", scenicSpots.size());
        counts.put("food", foods.size());
        counts.put("accommodation", accommodations.size());
        counts.put("guide", guides.size());
        
        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("scenic"); // 默认为景点类型
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
        
        // 对每个预定义类别计算匹配分数
        for (Map.Entry<String, List<String>> entry : CATEGORY_KEYWORDS.entrySet()) {
            double score = calculateCategoryScore(query, entry.getValue());
            if (score > 0) {
                scores.put(entry.getKey(), score);
            }
        }
        
        return scores;
    }

    private double calculateCategoryScore(String query, List<String> keywords) {
        double score = 0;
        for (String keyword : keywords) {
            if (query.contains(keyword)) {
                score += 1.0;
            }
        }
        return score / keywords.size();
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

    private double calculateCategoryMatchScore(String categoryName, Map<String, Double> categoryScores) {
        if (categoryName == null || categoryScores.isEmpty()) {
            return 0;
        }
        
        return categoryScores.getOrDefault(categoryName, 0.0);
    }
} 