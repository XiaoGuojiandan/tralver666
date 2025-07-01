package org.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("accommodation_review")
@Schema(description = "住宿评价")
public class AccommodationReview {
    
    @TableId(type = IdType.AUTO)
    @Schema(description = "评价ID")
    private Long id;
    
    @Schema(description = "住宿ID")
    private Long accommodationId;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "评分")
    private BigDecimal rating;
    
    @Schema(description = "评价内容")
    private String content;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @TableField(exist = false)
    @Schema(description = "用户昵称")
    private String nickname;
    
    @TableField(exist = false)
    @Schema(description = "用户头像")
    private String avatar;
} 