package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.springboot.entity.ScenicSpot;
import java.util.List;

@Mapper
public interface ScenicSpotMapper extends BaseMapper<ScenicSpot> {

    @Select("SELECT s.* FROM scenic_spot s " +
            "LEFT JOIN scenic_category c ON s.category_id = c.id " +
            "WHERE s.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR s.description LIKE CONCAT('%', #{keyword}, '%') " +
            "OR s.location LIKE CONCAT('%', #{keyword}, '%') " +
            "OR s.city LIKE CONCAT('%', #{keyword}, '%')")
    List<ScenicSpot> findByKeyword(String keyword);

    @Select("SELECT s.*, IFNULL(AVG(c.rating), 0) as rating " +
            "FROM scenic_spot s " +
            "LEFT JOIN comment c ON s.id = c.scenic_id " +
            "WHERE s.city LIKE CONCAT('%', #{location}, '%') " +
            "OR s.location LIKE CONCAT('%', #{location}, '%') " +
            "GROUP BY s.id " +
            "ORDER BY rating DESC LIMIT 10")
    List<ScenicSpot> findByLocation(String location);
} 