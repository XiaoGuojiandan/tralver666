package org.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.springboot.common.Result;
import org.example.springboot.entity.Accommodation;
import org.example.springboot.service.AccommodationService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "住宿接口")
@RestController
@RequestMapping("/accommodation")
public class AccommodationController {

    @Resource
    private AccommodationService accommodationService;

    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<Page<Accommodation>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String priceRange,
            @RequestParam(required = false) BigDecimal minStarLevel,
            @RequestParam(required = false) Long scenicId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String province) {
        Page<Accommodation> page = new Page<>(current, size);
        return Result.success(accommodationService.page(page, name, type, priceRange, 
                minStarLevel, scenicId, city, province));
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Result<Accommodation> getById(@PathVariable Long id) {
        return Result.success(accommodationService.getById(id));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<?> save(@RequestBody Accommodation accommodation) {
        if (accommodationService.save(accommodation)) {
            return Result.success();
        }
        return Result.error("保存失败");
    }

    @Operation(summary = "修改")
    @PutMapping
    public Result<?> update(@RequestBody Accommodation accommodation) {
        if (accommodationService.updateById(accommodation)) {
            return Result.success();
        }
        return Result.error("修改失败");
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (accommodationService.removeById(id)) {
            return Result.success();
        }
        return Result.error("删除失败");
    }

    @Operation(summary = "获取所有住宿类型")
    @GetMapping("/types")
    public Result<List<String>> getAllTypes() {
        return Result.success(accommodationService.getAllTypes());
    }
} 