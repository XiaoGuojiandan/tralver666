package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.example.springboot.entity.Food;
import org.example.springboot.entity.FoodCategory;
import org.example.springboot.entity.Comment;
import org.example.springboot.entity.FoodComment;
import org.example.springboot.entity.User;
import org.example.springboot.mapper.FoodMapper;
import org.example.springboot.mapper.FoodCategoryMapper;
import org.example.springboot.mapper.CommentMapper;
import org.example.springboot.mapper.FoodCommentMapper;
import org.example.springboot.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoodService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FoodService.class);

    @Resource
    private FoodMapper foodMapper;

    @Resource
    private FoodCategoryMapper categoryMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private FoodCommentMapper foodCommentMapper;

    @Resource
    private UserMapper userMapper;

    public Page<Food> getFoodList(Integer pageNum, Integer pageSize, String city, Long categoryId, String keyword) {
        Page<Food> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.isNotBlank(city)) {
            wrapper.eq(Food::getCity, city);
        }
        if (categoryId != null) {
            wrapper.eq(Food::getCategoryId, categoryId);
        }
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.like(Food::getName, keyword)
                  .or()
                  .like(Food::getDescription, keyword);
        }
        
        // 查询并返回结果
        Page<Food> foodPage = foodMapper.selectPage(page, wrapper);
        
        // 填充分类信息
        if (foodPage.getRecords() != null && !foodPage.getRecords().isEmpty()) {
            for (Food food : foodPage.getRecords()) {
                if (food.getCategoryId() != null) {
                    food.setCategoryInfo(categoryMapper.selectById(food.getCategoryId()));
                }
            }
        }
        
        return foodPage;
    }

    public Food getFoodDetail(Long id) {
        LOGGER.info("开始从数据库获取美食信息 - id: {}", id);
        
        Food food = foodMapper.selectById(id);
        if (food == null) {
            LOGGER.warn("数据库中未找到美食 - id: {}", id);
            return null;
        }
        
        LOGGER.info("成功获取美食基本信息 - name: {}, categoryId: {}", food.getName(), food.getCategoryId());
        
        // 获取分类信息
        if (food.getCategoryId() != null) {
            LOGGER.info("开始获取分类信息 - categoryId: {}", food.getCategoryId());
            food.setCategoryInfo(categoryMapper.selectById(food.getCategoryId()));
            if (food.getCategoryInfo() != null) {
                LOGGER.info("成功获取分类信息 - categoryName: {}", food.getCategoryInfo().getName());
            } else {
                LOGGER.warn("未找到对应的分类信息 - categoryId: {}", food.getCategoryId());
            }
        }
        
        // 获取评分和评论数
        LOGGER.info("开始获取评论信息 - foodId: {}", id);
        LambdaQueryWrapper<FoodComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FoodComment::getFoodId, id);
        List<FoodComment> comments = foodCommentMapper.selectList(wrapper);
        
        if (!comments.isEmpty()) {
            double avgRating = comments.stream()
                    .mapToInt(FoodComment::getRating)
                    .average()
                    .orElse(0.0);
            food.setRating(avgRating);
            food.setReviewCount(comments.size());
            LOGGER.info("成功获取评论信息 - 评论数: {}, 平均评分: {}", comments.size(), avgRating);
        } else {
            LOGGER.info("该美食暂无评论");
            food.setRating(0.0);
            food.setReviewCount(0);
        }
        
        return food;
    }

    public List<FoodCategory> getCategoryList() {
        return categoryMapper.selectList(null);
    }

    @Transactional
    public void addComment(FoodComment comment) {
        comment.setCreateTime(LocalDateTime.now());
        comment.setLikes(0);
        foodCommentMapper.insert(comment);
    }

    public Page<FoodComment> getCommentList(Long foodId, Integer pageNum, Integer pageSize) {
        Page<FoodComment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<FoodComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FoodComment::getFoodId, foodId)
               .orderByDesc(FoodComment::getCreateTime);
        Page<FoodComment> commentPage = foodCommentMapper.selectPage(page, wrapper);
        
        // 填充用户信息
        if (commentPage.getRecords() != null && !commentPage.getRecords().isEmpty()) {
            for (FoodComment comment : commentPage.getRecords()) {
                if (comment.getUserId() != null) {
                    User user = userMapper.selectById(comment.getUserId());
                    if (user != null) {
                        comment.setUserNickname(user.getNickname());
                        comment.setUserAvatar(user.getAvatar());
                    }
                }
            }
        }
        
        return commentPage;
    }

    @Transactional
    public void likeComment(Long commentId, Long userId) {
        FoodComment comment = foodCommentMapper.selectById(commentId);
        if (comment != null) {
            comment.setLikes(comment.getLikes() + 1);
            foodCommentMapper.updateById(comment);
        }
    }

    public List<Food> getRecommendedFood(String city, int limit) {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(city)) {
            wrapper.eq(Food::getCity, city);
        }
        wrapper.orderByDesc(Food::getCreateTime)
               .last("LIMIT " + limit);
        
        return foodMapper.selectList(wrapper);
    }

    public List<Food> getFoodByCategory(Long categoryId, int limit) {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getCategoryId, categoryId)
               .orderByDesc(Food::getCreateTime)
               .last("LIMIT " + limit);
        
        return foodMapper.selectList(wrapper);
    }

    // 基础的CRUD方法
    public boolean save(Food food) {
        return foodMapper.insert(food) > 0;
    }

    public boolean update(Food food) {
        return foodMapper.updateById(food) > 0;
    }

    public boolean delete(Long id) {
        return foodMapper.deleteById(id) > 0;
    }

    public Food getById(Long id) {
        return foodMapper.selectById(id);
    }
} 