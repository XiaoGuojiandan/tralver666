package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.springboot.entity.Collection;

import java.util.List;
import java.util.Map;

@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {
    // 无需写SQL，使用MyBatis-Plus提供的方法
    
    @Select("SELECT guide_id, COUNT(*) as count FROM collection " +
            "WHERE guide_id IS NOT NULL " +
            "GROUP BY guide_id " +
            "ORDER BY count DESC " +
            "LIMIT 10")
    List<Map<String, Object>> getTopCollections();

    @Select("SELECT s.name, COUNT(*) as count " +
            "FROM scenic_collection c " +
            "JOIN scenic_spot s ON c.scenic_id = s.id " +
            "GROUP BY c.scenic_id, s.name " +
            "ORDER BY count DESC " +
            "LIMIT 10")
    List<Map<String, Object>> findTopCollectedScenic();
} 