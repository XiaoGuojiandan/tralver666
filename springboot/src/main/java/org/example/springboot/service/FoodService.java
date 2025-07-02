package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class FoodService {

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private FoodCategoryService foodCategoryService;

    @Autowired
    private FoodCategoryMapper foodCategoryMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private FoodCommentMapper foodCommentMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取美食详情（包含评分和评论数）
     */
    public Food getFoodDetail(Long id) {
        log.info("开始获取美食详情 - id: {}", id);
        Food food = foodMapper.getFoodWithStats(id);
        if (food != null && food.getCategoryId() != null) {
            log.info("开始获取分类信息 - categoryId: {}", food.getCategoryId());
            FoodCategory category = foodCategoryService.getById(food.getCategoryId());
            food.setCategoryInfo(category);
            log.info("成功获取分类信息 - categoryName: {}", category.getName());
        }
        return food;
    }

    /**
     * 分页获取美食列表
     */
    public Page<Food> getFoodList(String name, Long categoryId, String city, int pageNum, int pageSize) {
        log.info("开始获取美食列表 - name: {}, categoryId: {}, city: {}, pageNum: {}, pageSize: {}", 
                name, categoryId, city, pageNum, pageSize);
        
        Page<Food> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(name)) {
            wrapper.like(Food::getName, name);
        }
        if (categoryId != null) {
            wrapper.eq(Food::getCategoryId, categoryId);
        }
        if (StringUtils.hasText(city)) {
            wrapper.eq(Food::getCity, city);
        }
        
        // 按创建时间降序排序
        wrapper.orderByDesc(Food::getCreateTime);
        
        // 执行查询
        Page<Food> foodPage = foodMapper.selectPage(page, wrapper);
        
        // 填充分类信息
        foodPage.getRecords().forEach(food -> {
            if (food.getCategoryId() != null) {
                food.setCategoryInfo(foodCategoryService.getById(food.getCategoryId()));
            }
        });
        
        return foodPage;
    }

    /**
     * 获取热门美食
     */
    public List<Food> getHotFoods(Integer limit) {
        log.info("开始获取热门美食 - limit: {}", limit);
        return foodMapper.getHotFoods(limit);
    }

    /**
     * 根据分类获取美食列表
     */
    public List<Food> getFoodsByCategory(Long categoryId, Integer limit) {
        log.info("开始获取分类美食 - categoryId: {}, limit: {}", categoryId, limit);
        return foodMapper.getFoodsByCategory(categoryId, limit);
    }

    /**
     * 添加美食
     */
    @Transactional
    public boolean addFood(Food food) {
        log.info("开始添加美食 - name: {}", food.getName());
        return foodMapper.insert(food) > 0;
    }

    /**
     * 更新美食信息
     */
    @Transactional
    public boolean updateFood(Food food) {
        log.info("开始更新美食 - id: {}, name: {}", food.getId(), food.getName());
        return foodMapper.updateById(food) > 0;
    }

    /**
     * 删除美食
     */
    @Transactional
    public boolean deleteFood(Long id) {
        log.info("开始删除美食 - id: {}", id);
        return foodMapper.deleteById(id) > 0;
    }

    public List<FoodCategory> getCategoryList() {
        return foodCategoryMapper.selectList(null);
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

    /**
     * 获取推荐美食
     */
    public List<Food> getRecommendedFood(String city, int limit) {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(city)) {
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

    /**
     * 获取美食总数
     */
    public long count() {
        return foodMapper.selectCount(null);
    }
} 