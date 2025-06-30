package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.springboot.entity.FoodCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodCategoryMapper extends BaseMapper<FoodCategory> {
} 