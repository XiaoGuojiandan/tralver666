package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.springboot.entity.ScenicSpot;
import java.util.List;
import java.util.Map;

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

    @Select("SELECT s.id, s.name, s.image_url, " +
            "COUNT(DISTINCT c.id) as collection_count, " +
            "COUNT(DISTINCT o.id) as order_count, " +
            "(COUNT(DISTINCT c.id) + COUNT(DISTINCT o.id)) as total_count " +
            "FROM scenic_spot s " +
            "LEFT JOIN scenic_collection c ON s.id = c.scenic_id " +
            "LEFT JOIN ticket t ON s.id = t.scenic_id " +
            "LEFT JOIN ticket_order o ON t.id = o.ticket_id " +
            "GROUP BY s.id, s.name, s.image_url " +
            "ORDER BY total_count DESC, collection_count DESC " +
            "LIMIT 10")
    List<Map<String, Object>> getTopScenicSpots();
} 