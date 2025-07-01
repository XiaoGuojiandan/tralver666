package org.example.springboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.springboot.common.Result;
import org.example.springboot.service.SmartMatchService;
import org.example.springboot.entity.TravelGuide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/smart-match")
@Api(tags = "智能匹配接口")
@CrossOrigin
public class SmartMatchController {

    @Autowired
    private SmartMatchService smartMatchService;

    @GetMapping("/recommendations")
    @ApiOperation("获取智能推荐结果")
    public Result<Map<String, Object>> getRecommendations(@RequestParam(name = "query") String query) {
        log.info("=== 开始处理智能匹配请求 ===");
        log.info("接收到智能匹配请求，原始查询关键词: {}", query);
        try {
            // 解码查询参数
            String decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8.name());
            log.info("解码后的查询关键词: {}", decodedQuery);
            
            Map<String, Object> recommendations = smartMatchService.getRecommendations(decodedQuery);
            
            // 记录结果统计
            int scenicCount = recommendations.containsKey("scenicSpots") ? ((java.util.List)recommendations.get("scenicSpots")).size() : 0;
            int foodCount = recommendations.containsKey("foods") ? ((java.util.List)recommendations.get("foods")).size() : 0;
            int accommodationCount = recommendations.containsKey("accommodations") ? ((java.util.List)recommendations.get("accommodations")).size() : 0;
            int guideCount = recommendations.containsKey("guides") ? ((java.util.List)recommendations.get("guides")).size() : 0;
            
            log.info("智能匹配成功，返回结果统计: 景点={}, 美食={}, 住宿={}, 攻略={}", 
                scenicCount, foodCount, accommodationCount, guideCount);

            // 打印攻略详细信息
            if (recommendations.containsKey("guides")) {
                List<TravelGuide> guides = (List<TravelGuide>) recommendations.get("guides");
                for (TravelGuide guide : guides) {
                    log.info("攻略信息 - ID: {}, 标题: {}, 用户ID: {}, 用户昵称: {}, 用户头像: {}", 
                        guide.getId(), guide.getTitle(), guide.getUserId(), 
                        guide.getUserNickname(), guide.getUserAvatar());
                }
            }
            
            return Result.success(recommendations);
        } catch (Exception e) {
            log.error("智能匹配处理失败: {}", e.getMessage(), e);
            return Result.error("获取推荐失败: " + e.getMessage());
        } finally {
            log.info("=== 智能匹配请求处理完成 ===");
        }
    }
} 