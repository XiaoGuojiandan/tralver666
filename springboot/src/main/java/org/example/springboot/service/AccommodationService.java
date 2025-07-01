package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.example.springboot.entity.Accommodation;
import org.example.springboot.entity.AccommodationReview;
import org.example.springboot.entity.ScenicSpot;
import org.example.springboot.mapper.AccommodationMapper;
import org.example.springboot.mapper.AccommodationReviewMapper;
import org.example.springboot.mapper.ScenicSpotMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccommodationService {
    
    @Resource
    private AccommodationMapper accommodationMapper;
    
    @Resource
    private ScenicSpotMapper scenicSpotMapper;
    
    @Resource
    private AccommodationReviewMapper reviewMapper;

    /**
     * 分页查询住宿列表
     */
    public Page<Accommodation> page(Page<Accommodation> page, String name, String type, 
                                  String priceRange, BigDecimal minStarLevel, 
                                  Long scenicId, String city, String province) {
        LambdaQueryWrapper<Accommodation> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        queryWrapper.like(StringUtils.isNotBlank(name), Accommodation::getName, name)
                .eq(StringUtils.isNotBlank(type), Accommodation::getType, type)
                .eq(StringUtils.isNotBlank(priceRange), Accommodation::getPriceRange, priceRange)
                .ge(minStarLevel != null, Accommodation::getStarLevel, minStarLevel)
                .eq(scenicId != null, Accommodation::getScenicId, scenicId)
                .eq(StringUtils.isNotBlank(city), Accommodation::getCity, city)
                .eq(StringUtils.isNotBlank(province), Accommodation::getProvince, province)
                .orderByDesc(Accommodation::getCreateTime);

        // 执行分页查询
        Page<Accommodation> resultPage = accommodationMapper.selectPage(page, queryWrapper);
        
        // 获取关联的景点信息
        List<Long> scenicIds = resultPage.getRecords().stream()
                .map(Accommodation::getScenicId)
                .filter(id -> id != null)
                .collect(Collectors.toList());
        
        if (!scenicIds.isEmpty()) {
            List<ScenicSpot> scenicSpots = scenicSpotMapper.selectBatchIds(scenicIds);
            Map<Long, String> scenicNameMap = scenicSpots.stream()
                    .collect(Collectors.toMap(ScenicSpot::getId, ScenicSpot::getName));
            
            // 设置关联的景点名称
            resultPage.getRecords().forEach(accommodation -> {
                if (accommodation.getScenicId() != null) {
                    accommodation.setScenicName(scenicNameMap.get(accommodation.getScenicId()));
                }
                
                // 获取评论数
                LambdaQueryWrapper<AccommodationReview> reviewWrapper = new LambdaQueryWrapper<>();
                reviewWrapper.eq(AccommodationReview::getAccommodationId, accommodation.getId());
                Long count = reviewMapper.selectCount(reviewWrapper);
                accommodation.setReviewCount(count);
                
                // 计算平均评分
                if (count > 0) {
                    Double avgRating = reviewMapper.getAverageRating(accommodation.getId());
                    if (avgRating != null) {
                        accommodation.setRating(avgRating);
                    }
                }
            });
        }
        
        return resultPage;
    }

    /**
     * 获取住宿详情
     */
    public Accommodation getById(Long id) {
        Accommodation accommodation = accommodationMapper.selectById(id);
        if (accommodation != null && accommodation.getScenicId() != null) {
            // 查询关联景点信息
            ScenicSpot scenicSpot = scenicSpotMapper.selectById(accommodation.getScenicId());
            if (scenicSpot != null) {
                accommodation.setScenicName(scenicSpot.getName());
            }
            
            // 查询评价数量和平均评分
            LambdaQueryWrapper<AccommodationReview> reviewWrapper = new LambdaQueryWrapper<>();
            reviewWrapper.eq(AccommodationReview::getAccommodationId, id);
            Long reviewCount = reviewMapper.selectCount(reviewWrapper);
            accommodation.setReviewCount(reviewCount);
            
            if (reviewCount > 0) {
                Double avgRating = reviewMapper.getAverageRating(id);
                if (avgRating != null) {
                    accommodation.setRating(avgRating);
                }
            }
        }
        return accommodation;
    }

    /**
     * 保存住宿信息
     */
    public boolean save(Accommodation accommodation) {
        return accommodationMapper.insert(accommodation) > 0;
    }

    /**
     * 更新住宿信息
     */
    public boolean updateById(Accommodation accommodation) {
        return accommodationMapper.updateById(accommodation) > 0;
    }

    /**
     * 删除住宿信息
     */
    public boolean removeById(Long id) {
        return accommodationMapper.deleteById(id) > 0;
    }

    /**
     * 获取所有住宿类型
     */
    public List<String> getAllTypes() {
        LambdaQueryWrapper<Accommodation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Accommodation::getType);
        queryWrapper.groupBy(Accommodation::getType);
        
        List<Accommodation> accommodations = accommodationMapper.selectList(queryWrapper);
        return accommodations.stream()
                .map(Accommodation::getType)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
    }
} 