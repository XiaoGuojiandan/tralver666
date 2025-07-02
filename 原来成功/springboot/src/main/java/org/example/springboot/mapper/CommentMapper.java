package org.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.springboot.entity.Comment;

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
} 