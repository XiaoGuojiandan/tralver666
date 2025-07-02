package org.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("food_comment")
@Schema(description = "美食评论实体类")
public class FoodComment {
    @TableId(type = IdType.AUTO)
    @Schema(description = "评论ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "美食ID")
    private Long foodId;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "评分(1-5)")
    private Integer rating;

    @Schema(description = "点赞数")
    private Integer likes;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(exist = false)
    @Schema(description = "用户昵称")
    private String userNickname;

    @TableField(exist = false)
    @Schema(description = "用户头像")
    private String userAvatar;

    @TableField(exist = false)
    @Schema(description = "美食名称")
    private String foodName;
} 