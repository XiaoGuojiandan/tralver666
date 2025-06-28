package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.springboot.entity.ScenicCategory;
import org.example.springboot.entity.ScenicSpot;
import org.example.springboot.mapper.ScenicCategoryMapper;
import org.example.springboot.mapper.ScenicSpotMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScenicSpotService extends ServiceImpl<ScenicSpotMapper, ScenicSpot> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScenicSpotService.class);
    
    @Autowired
    private ScenicCategoryMapper scenicCategoryMapper;
    
    @Autowired
    private ScenicCategoryService scenicCategoryService;

    public IPage<ScenicSpot> getScenicSpotsByPage(String name, Long categoryId, Integer currentPage, Integer size) {
        LOGGER.info("开始查询景点分页数据: name={}, categoryId={}, currentPage={}, size={}", 
            name, categoryId, currentPage, size);
            
        Page<ScenicSpot> page = new Page<>(currentPage, size);
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        
        // 添加名称搜索条件
        if (StringUtils.hasText(name)) {
            LOGGER.info("添加名称搜索条件: {}", name);
            wrapper.like(ScenicSpot::getName, name)
                  .or()
                  .like(ScenicSpot::getDescription, name)
                  .or()
                  .like(ScenicSpot::getLocation, name);
        }
        
        // 添加分类筛选条件
        if (categoryId != null) {
            LOGGER.info("添加分类筛选条件: {}", categoryId);
            wrapper.eq(ScenicSpot::getCategoryId, categoryId);
        }
        
        // 按创建时间降序排序
        wrapper.orderByDesc(ScenicSpot::getCreateTime);
        
        // 获取分页数据
        IPage<ScenicSpot> result = page(page, wrapper);
        LOGGER.info("查询到景点数据: 总记录数={}, 当前页记录数={}", 
            result.getTotal(), 
            result.getRecords() != null ? result.getRecords().size() : 0);
        
        // 填充分类信息
        fillCategoryInfo(result.getRecords());
        
        return result;
    }

    public ScenicSpot getScenicSpotById(Long id) {
        ScenicSpot spot = getById(id);
        if (spot != null && spot.getCategoryId() != null) {
            spot.setCategoryInfo(scenicCategoryMapper.selectById(spot.getCategoryId()));
        }
        return spot;
    }

    public void createScenicSpot(ScenicSpot scenicSpot) {
        save(scenicSpot);
    }

    public void updateScenicSpot(Long id, ScenicSpot scenicSpot) {
        scenicSpot.setId(id);
        updateById(scenicSpot);
    }

    public boolean deleteScenicSpot(Long id) {
        return removeById(id);
    }
    
    /**
     * 根据分类ID查询景点
     */
    public List<ScenicSpot> getScenicSpotsByCategoryId(Long categoryId) {
        LambdaQueryWrapper<ScenicSpot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScenicSpot::getCategoryId, categoryId);
        List<ScenicSpot> spots = list(queryWrapper);
        fillCategoryInfo(spots);
        return spots;
    }
    
    /**
     * 获取所有景点
     */
    public List<ScenicSpot> getAll() {
        List<ScenicSpot> spots = list();
        fillCategoryInfo(spots);
        return spots;
    }
    
    /**
     * 获取热门景点
     */
    public List<ScenicSpot> getHotScenics(Integer limit) {
        LambdaQueryWrapper<ScenicSpot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ScenicSpot::getCreateTime);
        queryWrapper.last("LIMIT " + limit);
        List<ScenicSpot> spots = list(queryWrapper);
        fillCategoryInfo(spots);
        return spots;
    }
    
    /**
     * 根据ID列表批量查询景点
     */
    public List<ScenicSpot> getScenicSpotsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ScenicSpot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ScenicSpot::getId, ids);
        List<ScenicSpot> spots = list(queryWrapper);
        fillCategoryInfo(spots);
        return spots;
    }
    
    /**
     * 获取搜索建议
     */
    public List<Map<String, Object>> getSearchSuggestions(String keyword, Integer limit) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (!StringUtils.hasText(keyword)) {
            return result;
        }

        LambdaQueryWrapper<ScenicSpot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper
            .like(ScenicSpot::getName, keyword)
            .or()
            .like(ScenicSpot::getLocation, keyword)
        );
        queryWrapper.orderByDesc(ScenicSpot::getCreateTime);
        queryWrapper.last("LIMIT " + limit);

        List<ScenicSpot> scenics = list(queryWrapper);
        fillCategoryInfo(scenics);

        for (ScenicSpot scenic : scenics) {
            Map<String, Object> suggestion = new HashMap<>();
            suggestion.put("id", scenic.getId());
            suggestion.put("name", scenic.getName());
            suggestion.put("location", scenic.getLocation());
            suggestion.put("imageUrl", scenic.getImageUrl());
            suggestion.put("type", "scenic");
            suggestion.put("price", scenic.getPrice());
            suggestion.put("categoryInfo", scenic.getCategoryInfo());
            result.add(suggestion);
        }

        return result;
    }
    
    /**
     * 填充景点的分类信息
     */
    private void fillCategoryInfo(List<ScenicSpot> spots) {
        if (spots == null || spots.isEmpty()) {
            LOGGER.info("没有需要填充分类信息的景点数据");
            return;
        }
        
        // 获取所有涉及到的分类ID
        List<Long> categoryIds = spots.stream()
                .map(ScenicSpot::getCategoryId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        
        LOGGER.info("需要查询的分类ID: {}", categoryIds);
        
        if (categoryIds.isEmpty()) {
            LOGGER.warn("没有找到有效的分类ID");
            return;
        }
        
        // 批量查询分类信息
        LambdaQueryWrapper<ScenicCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ScenicCategory::getId, categoryIds);
        List<ScenicCategory> categories = scenicCategoryMapper.selectList(queryWrapper);
        
        LOGGER.info("查询到的分类信息数量: {}", categories.size());
        categories.forEach(category -> 
            LOGGER.info("分类信息: id={}, name={}", category.getId(), category.getName()));
        
        // 转换为Map便于查找
        Map<Long, ScenicCategory> categoryMap = categories.stream()
                .collect(Collectors.toMap(ScenicCategory::getId, category -> category));
        
        // 填充分类信息
        spots.forEach(spot -> {
            if (spot.getCategoryId() != null && categoryMap.containsKey(spot.getCategoryId())) {
                spot.setCategoryInfo(categoryMap.get(spot.getCategoryId()));
                LOGGER.info("已为景点{}填充分类信息: {}", 
                    spot.getName(), 
                    spot.getCategoryInfo().getName());
            } else {
                LOGGER.warn("景点{}({})的分类信息未找到", spot.getName(), spot.getCategoryId());
            }
        });
    }
}