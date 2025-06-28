package org.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;

@Data
@TableName("scenic_spot")
@Schema(description = "景点实体类")
public class ScenicSpot {
    @TableId(type = IdType.AUTO)
    @Schema(description = "景点ID")
    private Long id;

    @Schema(description = "景点名称")
    private String name;

    @Schema(description = "景点描述")
    private String description;

    @Schema(description = "地理位置")
    private String location;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "票价")
    private BigDecimal price;

    @Schema(description = "开放时间")
    private String openingHours;

    @Schema(description = "图片URL")
    private String imageUrl;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "区域ID")
    private Long regionId;

    @TableField(exist = false)
    @Schema(description = "分类信息")
    private ScenicCategory categoryInfo;

    @TableField(exist = false)
    @Schema(description = "评分")
    private Double rating;

    @TableField(exist = false)
    @Schema(description = "评论数量")
    private Integer reviewCount;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 