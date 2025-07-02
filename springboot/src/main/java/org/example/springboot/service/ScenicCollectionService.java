package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.example.springboot.entity.ScenicCollection;
import org.example.springboot.entity.ScenicSpot;
import org.example.springboot.entity.User;
import org.example.springboot.exception.ServiceException;
import org.example.springboot.mapper.ScenicCollectionMapper;
import org.example.springboot.mapper.ScenicSpotMapper;
import org.example.springboot.mapper.UserMapper;
import org.example.springboot.util.JwtTokenUtils;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScenicCollectionService {
    private static final Logger logger = LoggerFactory.getLogger(ScenicCollectionService.class);
    
    @Resource
    private ScenicCollectionMapper scenicCollectionMapper;
    
    @Resource
    private ScenicSpotMapper scenicSpotMapper;
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private ScenicSpotService scenicSpotService;

    /**
     * 添加景点收藏
     */
    public void addCollection(Long scenicId) {
        // 获取当前用户
        User currentUser = JwtTokenUtils.getCurrentUser();
        if (currentUser == null) {
            throw new ServiceException("用户未登录");
        }
        
        // 检查景点是否存在
        ScenicSpot scenicSpot = scenicSpotService.getScenicSpotById(scenicId);
        if (scenicSpot == null) {
            throw new ServiceException("景点不存在");
        }
        
        // 检查是否已收藏
        LambdaQueryWrapper<ScenicCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScenicCollection::getUserId, currentUser.getId())
                   .eq(ScenicCollection::getScenicId, scenicId);
        if (scenicCollectionMapper.selectOne(queryWrapper) != null) {
            throw new ServiceException("已收藏该景点");
        }
        
        // 添加收藏
        ScenicCollection collection = new ScenicCollection();
        collection.setUserId(currentUser.getId());
        collection.setScenicId(scenicId);
        collection.setCreateTime(LocalDateTime.now());
        
        if (scenicCollectionMapper.insert(collection) <= 0) {
            throw new ServiceException("收藏失败");
        }
    }
    
    /**
     * 取消景点收藏
     */
    public void cancelCollection(Long scenicId) {
        // 获取当前用户
        User currentUser = JwtTokenUtils.getCurrentUser();
        if (currentUser == null) {
            throw new ServiceException("用户未登录");
        }
        
        // 删除收藏记录
        LambdaQueryWrapper<ScenicCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScenicCollection::getUserId, currentUser.getId())
                   .eq(ScenicCollection::getScenicId, scenicId);
                   
        if (scenicCollectionMapper.delete(queryWrapper) <= 0) {
            throw new ServiceException("取消收藏失败，可能未收藏该景点");
        }
    }
    
    /**
     * 查询用户是否已收藏某景点
     */
    public boolean isCollected(Long scenicId) {
        // 获取当前用户
        User currentUser = JwtTokenUtils.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        // 查询是否存在收藏记录
        System.out.printf("当前登录："+currentUser.getId()+"<UNK>");
        LambdaQueryWrapper<ScenicCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScenicCollection::getUserId, currentUser.getId())
                   .eq(ScenicCollection::getScenicId, scenicId);
                   
        return scenicCollectionMapper.selectCount(queryWrapper) > 0;
    }
    
    /**
     * 查询用户收藏的景点列表（分页）
     */
    public Page<ScenicCollection> getUserCollections(Long userId, Integer currentPage, Integer size) {
        logger.info("开始查询用户景点收藏列表, userId={}, currentPage={}, size={}", userId, currentPage, size);
        
        if (userId == null) {
            // 获取当前用户
            User currentUser = JwtTokenUtils.getCurrentUser();
            if (currentUser == null) {
                logger.error("用户未登录");
                throw new ServiceException("用户未登录");
            }
            userId = currentUser.getId();
            logger.info("使用当前登录用户ID: {}", userId);
        }
        
        // 查询收藏记录
        LambdaQueryWrapper<ScenicCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScenicCollection::getUserId, userId)
                   .orderByDesc(ScenicCollection::getCreateTime);
                   
        Page<ScenicCollection> page = scenicCollectionMapper.selectPage(new Page<>(currentPage, size), queryWrapper);
        logger.info("查询到景点收藏记录数: {}", page.getRecords().size());
        
        // 填充景点信息
        fillScenicInfo(page.getRecords());
        
        return page;
    }
    
    /**
     * 查询用户收藏的所有景点ID
     */
    public List<Long> getUserCollectionIds(Long userId) {
        if (userId == null) {
            // 获取当前用户
            User currentUser = JwtTokenUtils.getCurrentUser();
            if (currentUser == null) {
                return new ArrayList<>();
            }
            userId = currentUser.getId();
        }
        
        // 查询收藏记录
        LambdaQueryWrapper<ScenicCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScenicCollection::getUserId, userId)
                   .select(ScenicCollection::getScenicId);
                   
        List<ScenicCollection> collections = scenicCollectionMapper.selectList(queryWrapper);
        
        return collections.stream()
                .map(ScenicCollection::getScenicId)
                .collect(Collectors.toList());
    }
    
    /**
     * 批量查询用户是否已收藏景点
     */
    public Map<Long, Boolean> batchIsCollected(List<Long> scenicIds) {
        // 获取当前用户
        User currentUser = JwtTokenUtils.getCurrentUser();
        if (currentUser == null || scenicIds == null || scenicIds.isEmpty()) {
            return scenicIds.stream().collect(Collectors.toMap(id -> id, id -> false));
        }
        
        // 查询用户收藏的景点ID
        List<Long> collectedIds = getUserCollectionIds(currentUser.getId());
        Set<Long> collectedIdSet = Set.copyOf(collectedIds);
        
        // 构建结果
        return scenicIds.stream()
                .collect(Collectors.toMap(
                    id -> id,
                    collectedIdSet::contains
                ));
    }
    
    /**
     * 填充景点信息
     */
    private void fillScenicInfo(List<ScenicCollection> collections) {
        if (collections == null || collections.isEmpty()) {
            logger.info("收藏列表为空，无需填充景点信息");
            return;
        }
        
        // 提取景点ID
        List<Long> scenicIds = collections.stream()
                .map(ScenicCollection::getScenicId)
                .collect(Collectors.toList());
                
        // 批量查询景点信息
        LambdaQueryWrapper<ScenicSpot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ScenicSpot::getId, scenicIds);
        List<ScenicSpot> scenicSpots = scenicSpotMapper.selectList(queryWrapper);
        
        // 构建景点ID到景点信息的映射
        Map<Long, ScenicSpot> scenicSpotMap = scenicSpots.stream()
                .collect(Collectors.toMap(ScenicSpot::getId, spot -> spot));
                
        // 填充景点信息
        collections.forEach(collection -> {
            ScenicSpot scenicSpot = scenicSpotMap.get(collection.getScenicId());
            if (scenicSpot != null) {
                collection.setScenicSpot(scenicSpot);
                collection.setScenicName(scenicSpot.getName());
                collection.setScenicImage(scenicSpot.getImageUrl());
            } else {
                logger.warn("未找到景点信息: scenicId={}", collection.getScenicId());
            }
        });
        
        logger.info("成功填充{}条景点信息", scenicSpots.size());
    }

    /**
     * 管理员查询所有收藏
     */
    public Page<ScenicCollection> getCollectionsByAdmin(String username, String scenicName, Integer currentPage, Integer size) {
        // 首先获取符合条件的用户
        List<Long> userIds = new ArrayList<>();
        if (StringUtils.hasText(username)) {
            LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
            userQuery.like(User::getUsername, username);
            List<User> users = userMapper.selectList(userQuery);
            if (!users.isEmpty()) {
                userIds = users.stream().map(User::getId).collect(Collectors.toList());
            } else {
                // 如果没找到用户，直接返回空结果
                return new Page<>(currentPage, size);
            }
        }
        
        // 查询符合条件的景点
        List<Long> scenicIds = new ArrayList<>();
        if (StringUtils.hasText(scenicName)) {
            LambdaQueryWrapper<ScenicSpot> scenicQuery = new LambdaQueryWrapper<>();
            scenicQuery.like(ScenicSpot::getName, scenicName);
            List<ScenicSpot> scenics = scenicSpotMapper.selectList(scenicQuery);
            if (!scenics.isEmpty()) {
                scenicIds = scenics.stream().map(ScenicSpot::getId).collect(Collectors.toList());
            } else {
                // 如果没找到景点，直接返回空结果
                return new Page<>(currentPage, size);
            }
        }
        
        // 构建查询条件
        LambdaQueryWrapper<ScenicCollection> queryWrapper = new LambdaQueryWrapper<>();
        if (!userIds.isEmpty()) {
            queryWrapper.in(ScenicCollection::getUserId, userIds);
        }
        if (!scenicIds.isEmpty()) {
            queryWrapper.in(ScenicCollection::getScenicId, scenicIds);
        }
        
        queryWrapper.orderByDesc(ScenicCollection::getCreateTime);
        Page<ScenicCollection> page = scenicCollectionMapper.selectPage(new Page<>(currentPage, size), queryWrapper);
        
        // 填充景点和用户信息
        fillScenicAndUserInfo(page.getRecords());
        
        return page;
    }
    
    /**
     * 管理员删除收藏
     */
    public void deleteCollection(Long id) {
        if (scenicCollectionMapper.deleteById(id) <= 0) {
            throw new ServiceException("删除收藏失败");
        }
    }
    
    /**
     * 填充景点和用户信息
     */
    private void fillScenicAndUserInfo(List<ScenicCollection> collections) {
        if (collections.isEmpty()) {
            return;
        }
        
        // 获取所有用户ID和景点ID
        List<Long> userIds = collections.stream()
                .map(ScenicCollection::getUserId)
                .distinct()
                .collect(Collectors.toList());
                
        List<Long> scenicIds = collections.stream()
                .map(ScenicCollection::getScenicId)
                .distinct()
                .collect(Collectors.toList());
        
        // 批量查询用户和景点信息
        Map<Long, User> userMap = userMapper.selectBatchIds(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, user -> user));
                
        Map<Long, ScenicSpot> scenicMap = scenicSpotMapper.selectBatchIds(scenicIds)
                .stream()
                .collect(Collectors.toMap(ScenicSpot::getId, scenic -> scenic));
        
        // 填充信息
        for (ScenicCollection collection : collections) {
            // 填充用户信息
            User user = userMap.get(collection.getUserId());
            if (user != null) {
                collection.setUsername(user.getUsername());
                collection.setUserNickname(user.getNickname());
                collection.setUserAvatar(user.getAvatar());
            }
            
            // 填充景点信息
            ScenicSpot scenic = scenicMap.get(collection.getScenicId());
            if (scenic != null) {
                collection.setScenicName(scenic.getName());
                collection.setScenicImage(scenic.getImageUrl());
            }
        }
    }
} 