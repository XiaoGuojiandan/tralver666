package org.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("scenic_collection")
@Schema(description = "景点收藏")
public class ScenicCollection {
    @TableId(type = IdType.AUTO)
    @Schema(description = "ID")
    private Long id;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "景点ID")
    private Long scenicId;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @TableField(exist = false)
    @Schema(description = "景点信息")
    private ScenicSpot scenicSpot;
} 