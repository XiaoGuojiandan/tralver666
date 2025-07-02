package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.springboot.entity.FoodComment;
import org.example.springboot.mapper.FoodCommentMapper;
import org.springframework.stereotype.Service;

@Service
public class FoodCommentService extends ServiceImpl<FoodCommentMapper, FoodComment> {

    @Resource
    private FoodCommentMapper foodCommentMapper;

    /**
     * 获取美食评论列表
     *
     * @param page   分页参数
     * @param foodId 美食ID
     * @return 评论列表
     */
    public IPage<FoodComment> getCommentsByFoodId(Page<FoodComment> page, Long foodId) {
        LambdaQueryWrapper<FoodComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FoodComment::getFoodId, foodId)
                .orderByDesc(FoodComment::getCreateTime);
        return foodCommentMapper.selectPage(page, wrapper);
    }

    /**
     * 点赞评论
     *
     * @param commentId 评论ID
     * @return 是否成功
     */
    public Boolean likeComment(Long commentId) {
        FoodComment comment = getById(commentId);
        if (comment != null) {
            comment.setLikes(comment.getLikes() + 1);
            return updateById(comment);
        }
        return false;
    }
} 