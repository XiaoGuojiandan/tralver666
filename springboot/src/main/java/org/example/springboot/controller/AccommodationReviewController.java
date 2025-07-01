package org.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.springboot.common.Result;
import org.example.springboot.entity.AccommodationReview;
import org.example.springboot.entity.User;
import org.example.springboot.service.AccommodationReviewService;
import org.example.springboot.util.JwtTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "住宿评价接口")
@RestController
@RequestMapping("/accommodation/review")
public class AccommodationReviewController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AccommodationReviewController.class);
    
    @Resource
    private AccommodationReviewService reviewService;
    
    @Operation(summary = "分页查询评价")
    @GetMapping("/page")
    public Result<Page<AccommodationReview>> page(
            @RequestParam(required = false) Long accommodationId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<AccommodationReview> page = new Page<>(current, size);
        return Result.success(reviewService.page(page, accommodationId));
    }
    
    @Operation(summary = "添加评价")
    @PostMapping
    public Result<?> add(@RequestBody AccommodationReview review) {
        // 获取当前登录用户
        User currentUser = JwtTokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }

        // 设置用户ID和创建时间
        review.setUserId(currentUser.getId());
        review.setCreateTime(LocalDateTime.now());

        if (reviewService.save(review)) {
            return Result.success();
        }
        return Result.error("添加失败");
    }
    
    @Operation(summary = "删除评价")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        // 获取评价信息
        AccommodationReview review = reviewService.getById(id);
        if (review == null) {
            return Result.error("评价不存在");
        }

        // 检查权限（只有管理员或评价的发布者可以删除）
        User currentUser = JwtTokenUtils.getCurrentUser();
        if (currentUser == null || 
            (!currentUser.getRoleCode().equals("ADMIN") && !currentUser.getId().equals(review.getUserId()))) {
            return Result.error("无权限删除");
        }

        if (reviewService.removeById(id)) {
            return Result.success();
        }
        return Result.error("删除失败");
    }
} 