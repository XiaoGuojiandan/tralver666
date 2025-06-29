package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.springboot.entity.ScenicCategory;
import org.example.springboot.entity.ScenicSpot;
import org.example.springboot.mapper.ScenicSpotMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ScenicSpotService {
    @Resource
    private ScenicSpotMapper scenicSpotMapper;
    
    @Resource
    private ScenicCategoryService scenicCategoryService;

    public IPage<ScenicSpot> getScenicSpotsByPage(String name, String city, Long categoryId, Integer currentPage, Integer size) {
        log.info("开始查询景点分页数据: name={}, city={}, categoryId={}, currentPage={}, size={}", 
            name, city, categoryId, currentPage, size);
            
        Page<ScenicSpot> page = new Page<>(currentPage, size);
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();

        // 添加名称搜索条件
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(ScenicSpot::getName, name)
                .or()
                .like(ScenicSpot::getDescription, name)
                   .or()
                   .like(ScenicSpot::getLocation, name);
        }

        // 添加城市筛选条件
        if (StringUtils.isNotBlank(city)) {
            wrapper.eq(ScenicSpot::getCity, city);
        }

        // 添加分类筛选条件
        if (categoryId != null) {
            wrapper.eq(ScenicSpot::getCategoryId, categoryId);
        }

        // 执行查询
        IPage<ScenicSpot> result = scenicSpotMapper.selectPage(page, wrapper);

        // 填充分类信息
        if (result.getRecords() != null && !result.getRecords().isEmpty()) {
            fillCategoryInfo(result.getRecords());
        }
        
        log.info("查询结果: 总记录数={}, 当前页记录数={}", 
            result.getTotal(), 
            result.getRecords() != null ? result.getRecords().size() : 0);
        
        return result;
    }

    public ScenicSpot getScenicSpotById(Long id) {
        ScenicSpot spot = scenicSpotMapper.selectById(id);
        if (spot != null && spot.getCategoryId() != null) {
            spot.setCategoryInfo(scenicCategoryService.getById(spot.getCategoryId()));
        }
        return spot;
    }

    public ScenicSpot getById(Long id) {
        return getScenicSpotById(id);
    }
    
    public List<ScenicSpot> getScenicSpotsByCategoryId(Long categoryId) {
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScenicSpot::getCategoryId, categoryId);
        List<ScenicSpot> spots = scenicSpotMapper.selectList(wrapper);
        fillCategoryInfo(spots);
        return spots;
    }

    public void createScenicSpot(ScenicSpot spot) {
        scenicSpotMapper.insert(spot);
    }

    public void updateScenicSpot(Long id, ScenicSpot spot) {
        spot.setId(id);
        scenicSpotMapper.updateById(spot);
    }

    public void deleteScenicSpot(Long id) {
        scenicSpotMapper.deleteById(id);
    }

    public List<ScenicSpot> getAll() {
        List<ScenicSpot> spots = scenicSpotMapper.selectList(null);
        fillCategoryInfo(spots);
        return spots;
    }
    
    public List<ScenicSpot> getHotScenics(Integer limit) {
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ScenicSpot::getCreateTime);
        wrapper.last("LIMIT " + limit);
        List<ScenicSpot> spots = scenicSpotMapper.selectList(wrapper);
        fillCategoryInfo(spots);
        return spots;
    }

    public List<ScenicSpot> getScenicSpotsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ScenicSpot::getId, ids);
        List<ScenicSpot> spots = scenicSpotMapper.selectList(wrapper);
        fillCategoryInfo(spots);
        return spots;
    }
    
    public List<Map<String, Object>> getSearchSuggestions(String keyword, Integer limit) {
        if (StringUtils.isBlank(keyword)) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<ScenicSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(ScenicSpot::getName, keyword)
               .or()
               .like(ScenicSpot::getLocation, keyword)
               .last("LIMIT " + limit);
               
        List<ScenicSpot> spots = scenicSpotMapper.selectList(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (ScenicSpot spot : spots) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", spot.getId());
            map.put("name", spot.getName());
            map.put("location", spot.getLocation());
            result.add(map);
        }
        
        return result;
    }

    private void fillCategoryInfo(List<ScenicSpot> spots) {
        if (spots == null || spots.isEmpty()) {
            return;
        }
        
        // 获取所有涉及到的分类ID
        Set<Long> categoryIds = new HashSet<>();
        for (ScenicSpot spot : spots) {
            if (spot.getCategoryId() != null) {
                categoryIds.add(spot.getCategoryId());
            }
        }
        
        if (categoryIds.isEmpty()) {
            return;
        }
        
        // 批量获取分类信息
        Map<Long, ScenicCategory> categoryMap = new HashMap<>();
        for (Long categoryId : categoryIds) {
            ScenicCategory category = scenicCategoryService.getById(categoryId);
            if (category != null) {
                categoryMap.put(categoryId, category);
            }
        }
        
        // 填充分类信息
        for (ScenicSpot spot : spots) {
            if (spot.getCategoryId() != null && categoryMap.containsKey(spot.getCategoryId())) {
                spot.setCategoryInfo(categoryMap.get(spot.getCategoryId()));
            }
        }
    }
}