package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.example.springboot.entity.Comment;
import org.example.springboot.entity.ScenicSpot;
import org.example.springboot.entity.User;
import org.example.springboot.exception.ServiceException;
import org.example.springboot.mapper.AccommodationReviewMapper;
import org.example.springboot.mapper.CommentMapper;
import org.example.springboot.mapper.FoodCommentMapper;
import org.example.springboot.mapper.ScenicSpotMapper;
import org.example.springboot.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> implements IService<Comment> {
    @Resource
    private CommentMapper commentMapper;
    
    @Resource
    private ScenicSpotMapper scenicSpotMapper;
    
    @Resource
    private UserMapper userMapper;

    @Resource
    private FoodCommentMapper foodCommentMapper;

    @Resource
    private AccommodationReviewMapper accommodationReviewMapper;

    public Page<Comment> getCommentsByPage(Long targetId, String scenicName, String userName, String content, Integer currentPage, Integer size) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        
        // 如果提供了目标ID，直接使用ID查询
        if (targetId != null) {
            queryWrapper.eq(Comment::getTargetId, targetId);
        } 
        // 如果提供了景点名称，先查询景点ID，再使用ID查询评论
        else if (StringUtils.isNotBlank(scenicName)) {
            List<Long> scenicIds = getScenicIdsByName(scenicName);
            if (scenicIds.isEmpty()) {
                // 如果没有找到匹配的景点，返回空结果
                return new Page<>(currentPage, size);
            }
            queryWrapper.in(Comment::getTargetId, scenicIds);
        }
        
        // 如果提供了用户名/昵称，先查询用户ID，再使用ID查询评论
        if (StringUtils.isNotBlank(userName)) {
            List<Long> userIds = getUserIdsByName(userName);
            if (userIds.isEmpty()) {
                // 如果没有找到匹配的用户，返回空结果
                return new Page<>(currentPage, size);
            }
            queryWrapper.in(Comment::getUserId, userIds);
        }
        
        // 如果提供了内容关键词，按内容过滤
        if (StringUtils.isNotBlank(content)) {
            queryWrapper.like(Comment::getContent, content);
        }
        
        queryWrapper.orderByDesc(Comment::getCreateTime);
        return commentMapper.selectPage(new Page<>(currentPage, size), queryWrapper);
    }
    
    private List<Long> getScenicIdsByName(String name) {
        if (StringUtils.isBlank(name)) {
            return Collections.emptyList();
        }
        
        LambdaQueryWrapper<ScenicSpot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ScenicSpot::getName, name);
        return scenicSpotMapper.selectList(queryWrapper)
            .stream()
            .map(ScenicSpot::getId)
            .toList();
    }
    
    private List<Long> getUserIdsByName(String name) {
        if (StringUtils.isBlank(name)) {
            return Collections.emptyList();
        }
        
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getUsername, name)
                   .or()
                   .like(User::getNickname, name);
        return userMapper.selectList(queryWrapper)
            .stream()
            .map(User::getId)
            .toList();
    }

    public void addComment(Comment comment) {
        if (commentMapper.insert(comment) <= 0) throw new ServiceException("评论失败");
    }

    public void deleteComment(Long id, Long userId, boolean isAdmin) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) throw new ServiceException("评论不存在");
        if (!isAdmin && !comment.getUserId().equals(userId)) throw new ServiceException("无权删除");
        if (commentMapper.deleteById(id) <= 0) throw new ServiceException("删除失败");
    }

    public void likeComment(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) throw new ServiceException("评论不存在");
        comment.setLikes(comment.getLikes() == null ? 1 : comment.getLikes() + 1);
        if (commentMapper.updateById(comment) <= 0) throw new ServiceException("点赞失败");
    }

    public Comment getById(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) throw new ServiceException("评论不存在");
        return comment;
    }

    public List<Comment> getAllByScenicId(Long scenicId) {
        return commentMapper.selectList(
            new LambdaQueryWrapper<Comment>()
                .eq(Comment::getTargetId, scenicId)
        );
    }

    /**
     * 获取评论统计数据
     */
    public Map<String, Object> getCommentStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 统计景点评论数（comment表中的评论）
        long scenicComments = commentMapper.selectCount(null);
        
        // 统计美食评论数（food_comment表中的评论）
        long foodComments = foodCommentMapper.selectCount(null);
        
        // 统计住宿评论数（accommodation_review表中的评论）
        long accommodationComments = accommodationReviewMapper.selectCount(null);
        
        // 计算总评论数
        long totalComments = scenicComments + foodComments + accommodationComments;
        
        // 计算各类型评论的活跃度（百分比）
        stats.put("scenic", calculatePercentage(scenicComments, totalComments));
        stats.put("food", calculatePercentage(foodComments, totalComments));
        stats.put("accommodation", calculatePercentage(accommodationComments, totalComments));
        
        // 获取热门景点排行
        List<Map<String, Object>> topScenics = scenicSpotMapper.getTopScenicSpots()
            .stream()
            .map(spot -> {
                Map<String, Object> scenicData = new HashMap<>();
                scenicData.put("name", spot.get("name"));
                scenicData.put("imageUrl", spot.get("image_url"));
                scenicData.put("rating", spot.get("rating"));
                scenicData.put("collectionCount", spot.get("collection_count"));
                scenicData.put("orderCount", spot.get("order_count"));
                scenicData.put("totalCount", spot.get("total_count"));
                return scenicData;
            })
            .collect(Collectors.toList());
        
        stats.put("topScenics", topScenics);
        
        return stats;
    }

    /**
     * 获取评论总数
     */
    public long count() {
        return commentMapper.selectCount(null);
    }
    
    /**
     * 计算百分比
     */
    private double calculatePercentage(long part, long total) {
        if (total == 0) return 0;
        return Math.round((double) part / total * 100);
    }
} 