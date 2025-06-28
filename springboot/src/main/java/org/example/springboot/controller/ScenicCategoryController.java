package org.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.springboot.common.Result;
import org.example.springboot.entity.ScenicCategory;
import org.example.springboot.service.ScenicCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scenic-category")
@Tag(name = "景点分类管理")
public class ScenicCategoryController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ScenicCategoryController.class);
    
    @Resource
    private ScenicCategoryService scenicCategoryService;
    
    @Operation(summary = "分页查询分类")
    @GetMapping("/page")
    public Result<?> getCategoriesByPage(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(scenicCategoryService.getCategoriesByPage(name, currentPage, size));
    }
    
    @GetMapping("/tree")
    @Operation(summary = "获取分类树")
    public Result<List<ScenicCategory>> tree() {
        return Result.success(scenicCategoryService.getTree());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取分类详情")
    public Result<ScenicCategory> getById(@PathVariable Long id) {
        return Result.success(scenicCategoryService.getById(id));
    }
    
    @PostMapping
    @Operation(summary = "新增分类")
    public Result<Boolean> add(@RequestBody ScenicCategory category) {
        return Result.success(scenicCategoryService.addCategory(category));
    }
    
    @PutMapping
    @Operation(summary = "更新分类")
    public Result<Boolean> update(@RequestBody ScenicCategory category) {
        return Result.success(scenicCategoryService.updateCategory(category));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(scenicCategoryService.deleteCategory(id));
    }
} 