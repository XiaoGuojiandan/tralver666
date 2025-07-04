package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.example.springboot.entity.Collection;
import org.example.springboot.entity.TravelGuide;
import org.example.springboot.entity.User;
import org.example.springboot.exception.ServiceException;
import org.example.springboot.mapper.CollectionMapper;
import org.example.springboot.mapper.TravelGuideMapper;
import org.example.springboot.mapper.UserMapper;
import org.example.springboot.util.JwtTokenUtils;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.springboot.entity.ScenicSpot;
import org.example.springboot.mapper.ScenicSpotMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class CollectionService {
    private static final Logger logger = LoggerFactory.getLogger(CollectionService.class);
    
    @Resource
    private CollectionMapper collectionMapper;
    
    @Resource
    private TravelGuideMapper travelGuideMapper;
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private ScenicSpotMapper scenicSpotMapper;
    
    /**
     * 添加收藏
     */
    public void addCollection(Collection collection) {
        // 验证用户是否存在
        if (userMapper.selectById(collection.getUserId()) == null) {
            throw new ServiceException("用户不存在");
        }
        
        // 验证攻略是否存在
        if (travelGuideMapper.selectById(collection.getGuideId()) == null) {
            throw new ServiceException("攻略不存在");
        }
        
        // 检查是否已收藏
        LambdaQueryWrapper<Collection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collection::getUserId, collection.getUserId())
                   .eq(Collection::getGuideId, collection.getGuideId());
        if (collectionMapper.selectOne(queryWrapper) != null) {
            throw new ServiceException("已经收藏过该攻略");
        }
        
        // 添加收藏
        if (collectionMapper.insert(collection) <= 0) {
            throw new ServiceException("收藏失败");
        }
    }
    
    /**
     * 取消收藏
     */
    public void cancelCollection(Long userId, Long guideId) {
        LambdaQueryWrapper<Collection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collection::getUserId, userId)
                   .eq(Collection::getGuideId, guideId);
        
        if (collectionMapper.delete(queryWrapper) <= 0) {
            throw new ServiceException("取消收藏失败");
        }
    }
    
    /**
     * 分页查询用户的收藏列表
     */
    public Page<Collection> getCollectionsByPage(Long userId, Integer currentPage, Integer size) {
        logger.info("开始查询用户收藏列表, userId={}, currentPage={}, size={}", userId, currentPage, size);
        
        // 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            logger.error("用户不存在, userId={}", userId);
            throw new ServiceException("用户不存在");
        }
        
        // 查询用户收藏
        LambdaQueryWrapper<Collection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collection::getUserId, userId)
                   .orderByDesc(Collection::getCreateTime);
        
        Page<Collection> page = collectionMapper.selectPage(new Page<>(currentPage, size), queryWrapper);
        logger.info("查询到收藏记录数: {}", page.getRecords().size());
        
        // 填充攻略信息
        fillGuideInfo(page.getRecords());
        
        return page;
    }
    
    /**
     * 检查用户是否已收藏某攻略
     */
    public boolean isCollected(Long userId, Long guideId) {
        LambdaQueryWrapper<Collection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Collection::getUserId, userId)
                   .eq(Collection::getGuideId, guideId);
        return collectionMapper.selectCount(queryWrapper) > 0;
    }
    
    /**
     * 获取当前登录用户的收藏列表
     */
    public Page<Collection> getCurrentUserCollections(Integer currentPage, Integer size) {
        User currentUser = JwtTokenUtils.getCurrentUser();
        logger.info("获取当前用户收藏列表, userId={}", currentUser != null ? currentUser.getId() : null);
        
        if (currentUser == null) {
            logger.error("用户未登录");
            throw new ServiceException("未登录");
        }
        return getCollectionsByPage(currentUser.getId(), currentPage, size);
    }
    
    /**
     * 管理员查询所有收藏
     */
    public Page<Collection> getCollectionsByAdmin(String username, String guideTitle, Integer currentPage, Integer size) {
        // 构建查询条件
        LambdaQueryWrapper<Collection> queryWrapper = new LambdaQueryWrapper<>();
        
        // 如果提供了用户名，先查询用户
        if (StringUtils.isNotBlank(username)) {
            LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
            userQuery.like(User::getUsername, username);
            List<User> users = userMapper.selectList(userQuery);
            if (!users.isEmpty()) {
                List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
                queryWrapper.in(Collection::getUserId, userIds);
            } else {
                // 如果没找到用户，直接返回空结果
                return new Page<>(currentPage, size);
            }
        }
        
        // 如果提供了攻略标题，先查询攻略
        if (StringUtils.isNotBlank(guideTitle)) {
            LambdaQueryWrapper<TravelGuide> guideQuery = new LambdaQueryWrapper<>();
            guideQuery.like(TravelGuide::getTitle, guideTitle);
            List<TravelGuide> guides = travelGuideMapper.selectList(guideQuery);
            if (!guides.isEmpty()) {
                List<Long> guideIds = guides.stream().map(TravelGuide::getId).collect(Collectors.toList());
                queryWrapper.in(Collection::getGuideId, guideIds);
            } else {
                // 如果没找到攻略，直接返回空结果
                return new Page<>(currentPage, size);
            }
        }
        
        // 按创建时间倒序排序
        queryWrapper.orderByDesc(Collection::getCreateTime);
        
        // 执行分页查询
        Page<Collection> page = collectionMapper.selectPage(new Page<>(currentPage, size), queryWrapper);
        
        // 填充攻略和用户信息
        if (!page.getRecords().isEmpty()) {
            fillGuideAndUserInfo(page.getRecords());
        }
        
        return page;
    }
    
    /**
     * 管理员删除收藏
     */
    public void deleteCollection(Long id) {
        if (collectionMapper.deleteById(id) <= 0) {
            throw new ServiceException("删除收藏失败");
        }
    }
    
    /**
     * 填充攻略信息
     */
    private void fillGuideInfo(List<Collection> collections) {
        if (collections == null || collections.isEmpty()) {
            logger.info("收藏列表为空，无需填充攻略信息");
            return;
        }
        
        // 获取所有攻略ID
        List<Long> guideIds = collections.stream()
                               .map(Collection::getGuideId)
                               .filter(id -> id != null)
                               .distinct()
                               .collect(Collectors.toList());
        
        if (!guideIds.isEmpty()) {
            logger.info("开始填充攻略信息, guideIds={}", guideIds);
            
            // 批量查询攻略信息
            List<TravelGuide> guides = travelGuideMapper.selectBatchIds(guideIds);
            logger.info("查询到攻略数量: {}", guides.size());
            
            Map<Long, TravelGuide> guideMap = guides.stream()
                                           .collect(Collectors.toMap(TravelGuide::getId, guide -> guide));
            
            // 填充攻略信息
            for (Collection collection : collections) {
                if (collection.getGuideId() != null) {
                    TravelGuide guide = guideMap.get(collection.getGuideId());
                    if (guide != null) {
                        collection.setGuideTitle(guide.getTitle());
                        collection.setGuideCoverImage(guide.getCoverImage());
                        collection.setGuideViews(guide.getViews());
                        logger.debug("填充攻略信息成功: collectionId={}, guideTitle={}", 
                                   collection.getId(), guide.getTitle());
                    } else {
                        logger.warn("未找到对应的攻略信息: collectionId={}, guideId={}", 
                                  collection.getId(), collection.getGuideId());
                    }
                }
            }
        }
    }
    
    /**
     * 填充攻略和用户信息
     */
    private void fillGuideAndUserInfo(List<Collection> collections) {
        if (collections == null || collections.isEmpty()) {
            logger.info("收藏列表为空，无需填充信息");
            return;
        }

        // 填充攻略信息
        fillGuideInfo(collections);
        
        // 获取所有用户ID
        List<Long> userIds = collections.stream()
                             .map(Collection::getUserId)
                             .distinct()
                             .collect(Collectors.toList());
        
        if (!userIds.isEmpty()) {
            // 批量查询用户信息
            List<User> users = userMapper.selectBatchIds(userIds);
            Map<Long, User> userMap = users.stream()
                                     .collect(Collectors.toMap(User::getId, user -> user));
            
            // 填充用户信息
            for (Collection collection : collections) {
                User user = userMap.get(collection.getUserId());
                if (user != null) {
                    collection.setUsername(user.getUsername());
                    collection.setUserNickname(user.getNickname());
                    collection.setUserAvatar(user.getAvatar());
                    logger.debug("填充用户信息成功: collectionId={}, username={}", 
                               collection.getId(), user.getUsername());
                } else {
                    logger.warn("未找到用户信息: userId={}", collection.getUserId());
                }
            }
        }
    }

    /**
     * 获取收藏排行榜
     */
    public List<Map<String, Object>> getTopCollections() {
        return collectionMapper.findTopCollectedScenic();
    }

    /**
     * 获取收藏总数
     */
    public long count() {
        return collectionMapper.selectCount(null);
    }
} 