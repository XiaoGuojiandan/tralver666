package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.springboot.entity.AccommodationReview;

@Mapper
public interface AccommodationReviewMapper extends BaseMapper<AccommodationReview> {
    // 使用MyBatis-Plus提供的方法，无需额外定义SQL

    @Select("SELECT AVG(rating) FROM accommodation_review WHERE accommodation_id = #{accommodationId}")
    Double getAverageRating(Long accommodationId);

    @Select("SELECT ar.*, u.nickname, u.avatar " +
            "FROM accommodation_review ar " +
            "LEFT JOIN user u ON ar.user_id = u.id " +
            "WHERE ar.accommodation_id = #{accommodationId} " +
            "ORDER BY ar.create_time DESC")
    Page<AccommodationReview> selectPageWithUserInfo(Page<AccommodationReview> page, Long accommodationId);
} 