package org.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.springboot.common.Result;
import org.example.springboot.entity.Food;
import org.example.springboot.entity.FoodCategory;
import org.example.springboot.entity.FoodComment;
import org.example.springboot.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
@Tag(name = "美食管理", description = "美食相关接口")
public class FoodController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FoodController.class);

    @Autowired
    private FoodService foodService;

    @GetMapping("/list")
    @Operation(summary = "获取美食列表（用户端）")
    public Result<Page<Food>> getFoodList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String city) {
        try {
            LOGGER.info("获取美食列表请求（用户端） - pageNum: {}, pageSize: {}, keyword: {}, categoryId: {}, city: {}", 
                       pageNum, pageSize, keyword, categoryId, city);
            Page<Food> foodPage = foodService.getFoodList(keyword, categoryId, city, pageNum, pageSize);
            LOGGER.info("获取美食列表成功（用户端） - 总记录数: {}", foodPage.getTotal());
            return Result.success(foodPage);
        } catch (Exception e) {
            LOGGER.error("获取美食列表失败（用户端）", e);
            return Result.error("获取美食列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取美食列表（管理员端）")
    public Result<Page<Food>> getFoodListForAdmin(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String city) {
        try {
            LOGGER.info("获取美食列表请求（管理员端） - pageNum: {}, pageSize: {}, name: {}, categoryId: {}, city: {}", 
                       pageNum, pageSize, name, categoryId, city);
            Page<Food> foodPage = foodService.getFoodList(name, categoryId, city, pageNum, pageSize);
            LOGGER.info("获取美食列表成功（管理员端） - 总记录数: {}", foodPage.getTotal());
            return Result.success(foodPage);
        } catch (Exception e) {
            LOGGER.error("获取美食列表失败（管理员端）", e);
            return Result.error("获取美食列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "获取美食详情")
    public Result<Food> getFoodDetail(@PathVariable Long id) {
        try {
            LOGGER.info("开始获取美食详情 - id: {}", id);
            
            // 获取美食详情
            Food food = foodService.getFoodDetail(id);
            
            // 检查结果
            if (food == null) {
                LOGGER.warn("未找到美食详情 - id: {}", id);
                return Result.error("未找到该美食");
            }
            
            LOGGER.info("成功获取美食详情 - id: {}, name: {}, categoryId: {}", 
                       id, food.getName(), food.getCategoryId());
            return Result.success(food);
        } catch (Exception e) {
            LOGGER.error("获取美食详情失败 - id: " + id, e);
            return Result.error("获取美食详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/categories")
    @Operation(summary = "获取美食分类列表")
    public Result getCategoryList() {
        try {
            LOGGER.info("获取美食分类列表请求");
            var result = foodService.getCategoryList();
            LOGGER.info("获取美食分类列表成功 - 数量: {}", result.size());
            return Result.success(result);
        } catch (Exception e) {
            LOGGER.error("获取美食分类列表失败", e);
            return Result.error("获取美食分类列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/comment")
    @Operation(summary = "添加美食评论")
    public Result addComment(@RequestBody FoodComment comment) {
        try {
            LOGGER.info("添加美食评论请求 - foodId: {}, userId: {}", comment.getFoodId(), comment.getUserId());
            foodService.addComment(comment);
            LOGGER.info("添加美食评论成功");
            return Result.success();
        } catch (Exception e) {
            LOGGER.error("添加美食评论失败", e);
            return Result.error("添加美食评论失败: " + e.getMessage());
        }
    }

    @GetMapping("/comments/{foodId}")
    @Operation(summary = "获取美食评论列表")
    public Result getCommentList(
            @PathVariable Long foodId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            LOGGER.info("获取美食评论列表请求 - foodId: {}, pageNum: {}, pageSize: {}", foodId, pageNum, pageSize);
            var result = foodService.getCommentList(foodId, pageNum, pageSize);
            LOGGER.info("获取美食评论列表成功 - 总记录数: {}", result.getTotal());
            return Result.success(result);
        } catch (Exception e) {
            LOGGER.error("获取美食评论列表失败 - foodId: " + foodId, e);
            return Result.error("获取美食评论列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/comment/like/{commentId}")
    @Operation(summary = "点赞评论")
    public Result likeComment(@PathVariable Long commentId, @RequestParam Long userId) {
        try {
            LOGGER.info("点赞评论请求 - commentId: {}, userId: {}", commentId, userId);
            foodService.likeComment(commentId, userId);
            LOGGER.info("点赞评论成功");
            return Result.success();
        } catch (Exception e) {
            LOGGER.error("点赞评论失败 - commentId: " + commentId, e);
            return Result.error("点赞评论失败: " + e.getMessage());
        }
    }

    @GetMapping("/recommended")
    @Operation(summary = "获取推荐美食")
    public Result<List<Food>> getRecommendedFood(
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "6") Integer limit) {
        try {
            LOGGER.info("获取推荐美食请求 - city: {}, limit: {}", city, limit);
            var result = foodService.getRecommendedFood(city, limit);
            LOGGER.info("获取推荐美食成功 - 数量: {}", result.size());
            return Result.success(result);
        } catch (Exception e) {
            LOGGER.error("获取推荐美食失败", e);
            return Result.error("获取推荐美食失败: " + e.getMessage());
        }
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类获取美食列表")
    public Result<List<Food>> getFoodByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "6") Integer limit) {
        try {
            LOGGER.info("根据分类获取美食列表请求 - categoryId: {}, limit: {}", categoryId, limit);
            var result = foodService.getFoodByCategory(categoryId, limit);
            LOGGER.info("根据分类获取美食列表成功 - 数量: {}", result.size());
            return Result.success(result);
        } catch (Exception e) {
            LOGGER.error("根据分类获取美食列表失败 - categoryId: " + categoryId, e);
            return Result.error("根据分类获取美食列表失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新美食信息")
    public Result updateFood(@PathVariable Long id, @RequestBody Food food) {
        try {
            LOGGER.info("更新美食信息请求 - id: {}, name: {}", id, food.getName());
            food.setId(id);
            boolean success = foodService.updateFood(food);
            if (success) {
                LOGGER.info("更新美食信息成功 - id: {}", id);
                return Result.success();
            } else {
                LOGGER.warn("更新美食信息失败 - id: {}", id);
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            LOGGER.error("更新美食信息失败 - id: " + id, e);
            return Result.error("更新失败: " + e.getMessage());
        }
    }
} 