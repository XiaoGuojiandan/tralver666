package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.example.springboot.entity.ScenicCategory;
import org.example.springboot.mapper.ScenicCategoryMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScenicCategoryService {
    
    @Resource
    private ScenicCategoryMapper scenicCategoryMapper;
    
    /**
     * 获取所有分类
     */
    public List<ScenicCategory> getAllCategories() {
        return scenicCategoryMapper.selectList(
            new LambdaQueryWrapper<ScenicCategory>()
                .orderByAsc(ScenicCategory::getSortOrder)
        );
    }
    
    /**
     * 获取分类树结构
     */
    public List<ScenicCategory> getTree() {
        // 获取所有分类
        List<ScenicCategory> allCategories = getAllCategories();
        
        // 转换为树形结构
        List<ScenicCategory> rootCategories = allCategories.stream()
                .filter(category -> category.getParentId() == 0)
                .collect(Collectors.toList());
                
        rootCategories.forEach(root -> {
            root.setChildren(getChildren(root, allCategories));
        });
        
        return rootCategories;
    }
    
    /**
     * 递归获取子分类
     */
    private List<ScenicCategory> getChildren(ScenicCategory parent, List<ScenicCategory> allCategories) {
        return allCategories.stream()
                .filter(category -> category.getParentId().equals(parent.getId()))
                .peek(child -> child.setChildren(getChildren(child, allCategories)))
                .collect(Collectors.toList());
    }
    
    /**
     * 分页查询分类列表
     */
    public Page<ScenicCategory> getCategoriesByPage(String name, Integer currentPage, Integer size) {
        LambdaQueryWrapper<ScenicCategory> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(ScenicCategory::getName, name);
        }
        
        queryWrapper.orderByAsc(ScenicCategory::getSortOrder);
        
        return scenicCategoryMapper.selectPage(new Page<>(currentPage, size), queryWrapper);
    }
    
    /**
     * 新增分类
     */
    public boolean addCategory(ScenicCategory category) {
        return scenicCategoryMapper.insert(category) > 0;
    }
    
    /**
     * 更新分类
     */
    public boolean updateCategory(ScenicCategory category) {
        return scenicCategoryMapper.updateById(category) > 0;
    }
    
    /**
     * 删除分类
     */
    public boolean deleteCategory(Long id) {
        return scenicCategoryMapper.deleteById(id) > 0;
    }
    
    /**
     * 根据ID获取分类
     */
    public ScenicCategory getById(Long id) {
        return scenicCategoryMapper.selectById(id);
    }
} 