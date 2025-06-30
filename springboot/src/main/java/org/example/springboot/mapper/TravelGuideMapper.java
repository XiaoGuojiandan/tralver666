package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.springboot.entity.TravelGuide;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface TravelGuideMapper extends BaseMapper<TravelGuide> {
    // 无需写SQL

    @Select("SELECT t.* FROM travel_guide t " +
            "WHERE t.title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR t.content LIKE CONCAT('%', #{keyword}, '%')")
    List<TravelGuide> findByKeyword(String keyword);

    @Select("SELECT t.* FROM travel_guide t " +
            "WHERE (t.title LIKE CONCAT('%', #{location}, '%') " +
            "OR t.content LIKE CONCAT('%', #{location}, '%')) " +
            "AND (t.title LIKE CONCAT('%', #{type}, '%') " +
            "OR t.content LIKE CONCAT('%', #{type}, '%')) " +
            "ORDER BY t.create_time DESC LIMIT 10")
    List<TravelGuide> findByLocationAndType(String location, String type);
} 