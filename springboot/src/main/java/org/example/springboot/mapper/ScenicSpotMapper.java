package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.springboot.entity.ScenicSpot;
import java.util.List;

@Mapper
public interface ScenicSpotMapper extends BaseMapper<ScenicSpot> {

    @Select("SELECT * FROM scenic_spot WHERE name = #{keyword}")
    List<ScenicSpot> findExactMatch(String keyword);

    @Select("SELECT * FROM scenic_spot WHERE " +
            "name LIKE CONCAT('%', #{keyword}, '%') OR " +
            "description LIKE CONCAT('%', #{keyword}, '%') OR " +
            "location LIKE CONCAT('%', #{keyword}, '%')")
    List<ScenicSpot> findByKeyword(String keyword);

    @Select("SELECT * FROM scenic_spot WHERE location LIKE CONCAT('%', #{location}, '%')")
    List<ScenicSpot> findByLocation(String location);

    @Select("SELECT s.*, c.name as category_name, c.description as category_description " +
            "FROM scenic_spot s " +
            "LEFT JOIN scenic_category c ON s.category_id = c.id " +
            "WHERE s.name LIKE CONCAT('%', #{keyword}, '%') OR " +
            "s.description LIKE CONCAT('%', #{keyword}, '%') OR " +
            "s.location LIKE CONCAT('%', #{keyword}, '%') OR " +
            "c.name LIKE CONCAT('%', #{keyword}, '%') OR " +
            "c.description LIKE CONCAT('%', #{keyword}, '%')")
    List<ScenicSpot> findByKeywordWithCategory(String keyword);
} 