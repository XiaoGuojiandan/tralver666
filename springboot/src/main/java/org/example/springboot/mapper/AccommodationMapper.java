package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.springboot.entity.Accommodation;

import java.util.List;

@Mapper
public interface AccommodationMapper extends BaseMapper<Accommodation> {
    // 使用MyBatis-Plus提供的方法，无需额外定义SQL

    @Select("SELECT a.* FROM accommodation a " +
            "WHERE a.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR a.description LIKE CONCAT('%', #{keyword}, '%') " +
            "OR a.address LIKE CONCAT('%', #{keyword}, '%') " +
            "OR a.type LIKE CONCAT('%', #{keyword}, '%') " +
            "OR a.features LIKE CONCAT('%', #{keyword}, '%')")
    List<Accommodation> findByKeyword(String keyword);

    @Select("SELECT a.* FROM accommodation a " +
            "WHERE a.city LIKE CONCAT('%', #{location}, '%') " +
            "OR a.address LIKE CONCAT('%', #{location}, '%') " +
            "ORDER BY a.star_level DESC LIMIT 10")
    List<Accommodation> findByLocation(String location);
} 