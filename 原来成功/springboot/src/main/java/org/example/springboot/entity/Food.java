package org.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("food")
@Schema(description = "美食实体类")
public class Food {
    @TableId(type = IdType.AUTO)
    @Schema(description = "美食ID")
    private Long id;

    @Schema(description = "美食名称")
    private String name;

    @Schema(description = "美食描述")
    private String description;

    @Schema(description = "地理位置")
    private String location;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "价格范围")
    private String priceRange;

    @Schema(description = "营业时间")
    private String businessHours;

    @Schema(description = "图片URL")
    private String imageUrl;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @TableField(exist = false)
    @Schema(description = "分类信息")
    private FoodCategory categoryInfo;

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