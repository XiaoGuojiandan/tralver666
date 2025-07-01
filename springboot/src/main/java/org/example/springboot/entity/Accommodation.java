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
@TableName("accommodation")
@Schema(description = "住宿信息")
public class Accommodation {
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "景点ID")
    private Long scenicId;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "价格区间")
    private String priceRange;

    @Schema(description = "星级")
    private BigDecimal starLevel;

    @Schema(description = "图片URL")
    private String imageUrl;

    @Schema(description = "特色服务")
    private String features;

    @Schema(description = "距离")
    private String distance;

    @Schema(description = "所在城市")
    private String city;

    @Schema(description = "所在省份")
    private String province;

    @Schema(description = "评分")
    private Double rating;

    @Schema(description = "评论数")
    private Long reviewCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    @Schema(description = "关联景点名称")
    private String scenicName;
} 