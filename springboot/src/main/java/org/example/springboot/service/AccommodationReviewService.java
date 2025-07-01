package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.example.springboot.entity.Accommodation;
import org.example.springboot.entity.AccommodationReview;
import org.example.springboot.mapper.AccommodationMapper;
import org.example.springboot.mapper.AccommodationReviewMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccommodationReviewService {
    
    @Resource
    private AccommodationReviewMapper reviewMapper;
    
    @Resource
    private AccommodationMapper accommodationMapper;

    /**
     * 分页查询评价列表
     */
    public Page<AccommodationReview> page(Page<AccommodationReview> page, Long accommodationId) {
        return reviewMapper.selectPageWithUserInfo(page, accommodationId);
    }

    /**
     * 根据ID查询评价
     */
    public AccommodationReview getById(Long id) {
        return reviewMapper.selectById(id);
    }

    /**
     * 添加评价
     */
    public boolean save(AccommodationReview review) {
        boolean result = reviewMapper.insert(review) > 0;
        if (result) {
            updateAccommodationRating(review.getAccommodationId());
        }
        return result;
    }

    /**
     * 更新评价
     */
    public boolean updateById(AccommodationReview review) {
        boolean result = reviewMapper.updateById(review) > 0;
        if (result) {
            updateAccommodationRating(review.getAccommodationId());
        }
        return result;
    }

    /**
     * 删除评价
     */
    public boolean removeById(Long id) {
        AccommodationReview review = reviewMapper.selectById(id);
        if (review != null) {
            boolean result = reviewMapper.deleteById(id) > 0;
            if (result) {
                updateAccommodationRating(review.getAccommodationId());
            }
            return result;
        }
        return false;
    }

    /**
     * 更新住宿评分
     */
    private void updateAccommodationRating(Long accommodationId) {
        // 计算平均评分
        Double avgRating = reviewMapper.getAverageRating(accommodationId);
        if (avgRating != null) {
            // 更新住宿评分
            Accommodation accommodation = accommodationMapper.selectById(accommodationId);
            if (accommodation != null) {
                accommodation.setRating(avgRating);
                accommodation.setStarLevel(BigDecimal.valueOf(avgRating));
                accommodationMapper.updateById(accommodation);
            }
        }
    }

    /**
     * 获取住宿的平均评分
     */
    public Double getAverageRating(Long accommodationId) {
        return reviewMapper.getAverageRating(accommodationId);
    }
} 