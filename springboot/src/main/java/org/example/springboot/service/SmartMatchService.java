package org.example.springboot.service;

import org.example.springboot.mapper.ScenicSpotMapper;
import org.example.springboot.mapper.FoodMapper;
import org.example.springboot.mapper.AccommodationMapper;
import org.example.springboot.mapper.TravelGuideMapper;
import org.example.springboot.entity.ScenicSpot;
import org.example.springboot.entity.Food;
import org.example.springboot.entity.Accommodation;
import org.example.springboot.entity.TravelGuide;
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

    // 预定义的关键词类别映射
    private static final Map<String, List<String>> CATEGORY_KEYWORDS = new HashMap<String, List<String>>() {{
        put("山水风光", Arrays.asList("山", "水", "风景", "自然", "景色", "风光", "山水", "景点", "公园", "湖"));
        put("特色美食", Arrays.asList("美食", "小吃", "特色", "菜", "饮食", "餐", "味", "饭", "馆", "店"));
        put("文化古迹", Arrays.asList("文化", "历史", "古迹", "遗址", "古", "传统", "庙", "寺", "塔"));
        put("民族风情", Arrays.asList("民族", "风情", "特色", "传统", "文化", "习俗", "村"));
        put("水果之旅", Arrays.asList("水果", "果园", "采摘", "果", "新鲜", "园"));
        put("红色旅游", Arrays.asList("红色", "革命", "历史", "纪念", "爱国", "基地"));
        put("户外探险", Arrays.asList("户外", "探险", "运动", "刺激", "野外", "徒步", "登山"));
        put("温泉度假", Arrays.asList("温泉", "度假", "休闲", "放松", "养生", "酒店"));
    }};

    public Map<String, Object> getRecommendations(String query) {
        log.info("开始执行智能匹配服务，查询关键词: {}", query);
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 分析查询关键词属于哪些类别
            Map<String, Double> categoryScores = analyzeCategoryScores(query);
            log.info("关键词类别分析结果: {}", categoryScores);

            // 2. 首先搜索所有类型的结果
            log.info("正在查询景点信息...");
            List<ScenicSpot> scenicSpots = scenicSpotMapper.findByKeyword(query);
            
            log.info("正在查询美食信息...");
            List<Food> foods = foodMapper.findByKeyword(query);
            
            log.info("正在查询住宿信息...");
            List<Accommodation> accommodations = accommodationMapper.findByKeyword(query);
            
            log.info("正在查询旅游攻略...");
            List<TravelGuide> guides = travelGuideMapper.findByKeyword(query);

            // 3. 确定主要搜索类型和地区
            String mainLocation = null;
            String searchType = determineSearchType(scenicSpots, foods, accommodations, guides);
            
            // 4. 根据主要搜索类型获取位置信息
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
                    // 对于攻略，我们需要分析内容来提取位置信息
                    if (!guides.isEmpty()) {
                        mainLocation = extractLocationFromGuide(guides.get(0));
                    }
                    break;
            }

            // 5. 如果找到了位置信息，获取相关推荐
            if (mainLocation != null) {
                log.info("找到主要位置信息: {}, 搜索类型: {}", mainLocation, searchType);
                
                // 不是主要搜索类型时，基于位置获取相关推荐
                if (!"scenic".equals(searchType)) {
                    List<ScenicSpot> relatedScenic = scenicSpotMapper.findByLocation(mainLocation);
                    scenicSpots = !relatedScenic.isEmpty() ? relatedScenic : scenicSpots;
                }
                
                if (!"food".equals(searchType)) {
                    List<Food> relatedFood = foodMapper.findByLocation(mainLocation);
                    foods = !relatedFood.isEmpty() ? relatedFood : foods;
                }
                
                if (!"accommodation".equals(searchType)) {
                    List<Accommodation> relatedAccommodation = accommodationMapper.findByLocation(mainLocation);
                    accommodations = !relatedAccommodation.isEmpty() ? relatedAccommodation : accommodations;
                }
                
                // 攻略总是基于位置和类型来推荐
                List<TravelGuide> relatedGuides = travelGuideMapper.findByLocationAndType(mainLocation, searchType);
                guides = !relatedGuides.isEmpty() ? relatedGuides : guides;
            }

            // 6. 对结果进行排序和限制
            result.put("scenicSpots", rankResults(scenicSpots, query, categoryScores));
            result.put("foods", rankResults(foods, query, categoryScores));
            result.put("accommodations", rankResults(accommodations, query, categoryScores));
            result.put("guides", rankResults(guides, query, categoryScores));
            
            // 7. 添加搜索上下文信息
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

    private String determineSearchType(List<ScenicSpot> scenicSpots, List<Food> foods, 
                                     List<Accommodation> accommodations, List<TravelGuide> guides) {
        // 根据结果数量和相关性确定主要搜索类型
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

    private String extractLocationFromGuide(TravelGuide guide) {
        // 从攻略标题和内容中提取位置信息
        // 这里可以实现更复杂的位置提取逻辑
        String content = guide.getTitle() + " " + guide.getContent();
        // 简单示例：假设位置信息在开头的城市名中
        String[] commonCities = {"桂林", "阳朔", "北海", "南宁", "柳州", "梧州"};
        for (String city : commonCities) {
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

    // 计算编辑距离（Levenshtein Distance）
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