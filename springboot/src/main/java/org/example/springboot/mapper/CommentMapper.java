package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.springboot.entity.Comment;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("SELECT c.*, u.nickname as userNickname, u.avatar as userAvatar " +
            "FROM comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "WHERE c.scenic_id = #{targetId} " +
            "ORDER BY c.create_time DESC")
    IPage<Comment> getCommentsByTargetId(Page<Comment> page,
                                       @Param("targetId") Long targetId,
                                       @Param("targetType") String targetType);

    @Select("SELECT scenic_id AS targetId, COUNT(*) as commentCount, " +
            "MAX(create_time) as latestCommentTime " +
            "FROM comment " +
            "WHERE scenic_id IS NOT NULL " +
            "GROUP BY scenic_id " +
            "ORDER BY commentCount DESC, latestCommentTime DESC " +
            "LIMIT 10")
    List<Map<String, Object>> getTopScenicsByComments();
} 