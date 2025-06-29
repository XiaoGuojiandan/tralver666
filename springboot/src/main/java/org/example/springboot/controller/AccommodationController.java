package org.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.example.springboot.common.Result;
import org.example.springboot.entity.Accommodation;
import org.example.springboot.service.AccommodationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "住宿管理接口")
@RestController
@RequestMapping("/accommodation")
public class AccommodationController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AccommodationController.class);
    
    @Resource
    private AccommodationService accommodationService;
    
    @Operation(summary = "分页查询住宿列表")
    @GetMapping("/page")
    public Result<Page<Accommodation>> page(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String priceRange,
            @RequestParam(required = false) BigDecimal minStarLevel,
            @RequestParam(required = false) Long scenicId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String province
    ) {
        Page<Accommodation> page = new Page<>(currentPage, size);
        return Result.success(accommodationService.page(page, name, type, priceRange, 
                minStarLevel, scenicId, city, province));
    }
    
    @Operation(summary = "获取住宿详情")
    @GetMapping("/{id}")
    public Result<Accommodation> getById(@PathVariable Long id) {
        return Result.success(accommodationService.getById(id));
    }
    
    @Operation(summary = "添加住宿")
    @PostMapping
    public Result<Boolean> save(@RequestBody Accommodation accommodation) {
        return Result.success(accommodationService.save(accommodation));
    }
    
    @Operation(summary = "更新住宿")
    @PutMapping
    public Result<Boolean> update(@RequestBody Accommodation accommodation) {
        return Result.success(accommodationService.updateById(accommodation));
    }
    
    @Operation(summary = "删除住宿")
    @DeleteMapping("/{id}")
    public Result<Boolean> removeById(@PathVariable Long id) {
        return Result.success(accommodationService.removeById(id));
    }
    
    @Operation(summary = "获取住宿类型列表")
    @GetMapping("/types")
    public Result<List<String>> getAccommodationTypes() {
        return Result.success(accommodationService.getAccommodationTypes());
    }
} 