package org.example.springboot.service;

import lombok.extern.slf4j.Slf4j;
import org.example.springboot.entity.FoodCategory;
import org.example.springboot.mapper.FoodCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FoodCategoryService {
    
    @Autowired
    private FoodCategoryMapper foodCategoryMapper;
    
    /**
     * 获取所有美食分类
     */
    public List<FoodCategory> getAllCategories() {
        log.info("开始获取所有美食分类");
        return foodCategoryMapper.selectList(null);
    }
    
    /**
     * 根据ID获取分类
     */
    public FoodCategory getById(Long id) {
        return foodCategoryMapper.selectById(id);
    }
    
    /**
     * 添加美食分类
     */
    public boolean addCategory(FoodCategory category) {
        log.info("开始添加美食分类 - name: {}", category.getName());
        return foodCategoryMapper.insert(category) > 0;
    }
    
    /**
     * 更新美食分类
     */
    public boolean updateCategory(FoodCategory category) {
        log.info("开始更新美食分类 - id: {}, name: {}", category.getId(), category.getName());
        return foodCategoryMapper.updateById(category) > 0;
    }
    
    /**
     * 删除美食分类
     */
    public boolean deleteCategory(Long id) {
        log.info("开始删除美食分类 - id: {}", id);
        return foodCategoryMapper.deleteById(id) > 0;
    }
} 