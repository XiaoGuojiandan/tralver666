package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.springboot.entity.FoodComment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodCommentMapper extends BaseMapper<FoodComment> {
} 