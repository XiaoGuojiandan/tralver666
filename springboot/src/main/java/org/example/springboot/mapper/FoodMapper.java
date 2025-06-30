package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.springboot.entity.Food;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FoodMapper extends BaseMapper<Food> {
    
    @Select("SELECT f.*, " +
            "COALESCE(AVG(fc.rating), 0) as rating, " +
            "COUNT(fc.id) as review_count " +
            "FROM food f " +
            "LEFT JOIN food_comment fc ON f.id = fc.food_id " +
            "WHERE f.id = #{id} " +
            "GROUP BY f.id")
    Food getFoodWithStats(@Param("id") Long id);

    @Select("SELECT f.*, " +
            "COALESCE(AVG(fc.rating), 0) as rating, " +
            "COUNT(fc.id) as review_count " +
            "FROM food f " +
            "LEFT JOIN food_comment fc ON f.id = fc.food_id " +
            "GROUP BY f.id " +
            "ORDER BY rating DESC " +
            "LIMIT #{limit}")
    List<Food> getHotFoods(@Param("limit") Integer limit);

    @Select("SELECT f.*, " +
            "COALESCE(AVG(fc.rating), 0) as rating, " +
            "COUNT(fc.id) as review_count " +
            "FROM food f " +
            "LEFT JOIN food_comment fc ON f.id = fc.food_id " +
            "WHERE f.category_id = #{categoryId} " +
            "GROUP BY f.id " +
            "LIMIT #{limit}")
    List<Food> getFoodsByCategory(@Param("categoryId") Long categoryId, @Param("limit") Integer limit);
} 