package org.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.springboot.annotation.RedisCache;
import org.example.springboot.entity.TravelGuide;
import org.example.springboot.entity.User;
import org.example.springboot.exception.ServiceException;
import org.example.springboot.mapper.TravelGuideMapper;
import org.example.springboot.mapper.UserMapper;
import org.example.springboot.util.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TravelGuideService extends ServiceImpl<TravelGuideMapper, TravelGuide> {
    @Resource
    private TravelGuideMapper travelGuideMapper;
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private RedisUtil redisUtil;

    public Page<TravelGuide> getGuidesByPage(String title, Long userId, Integer currentPage, Integer size) {
        Page<TravelGuide> page = new Page<>(currentPage, size);
        LambdaQueryWrapper<TravelGuide> wrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        wrapper.like(StringUtils.isNotBlank(title), TravelGuide::getTitle, title)
                .eq(userId != null, TravelGuide::getUserId, userId)
                .orderByDesc(TravelGuide::getCreateTime);
        
        // 执行查询
        Page<TravelGuide> guidePage = travelGuideMapper.selectPage(page, wrapper);
        
        // 填充用户信息
        List<TravelGuide> records = guidePage.getRecords();
        for (TravelGuide guide : records) {
            User user = userMapper.selectById(guide.getUserId());
            if (user != null) {
                guide.setUserNickname(user.getNickname());
                guide.setUserAvatar(user.getAvatar());
            }
        }
        
        return guidePage;
    }

    @Transactional
    public void addGuide(TravelGuide guide) {
        if (travelGuideMapper.insert(guide) <= 0) {
            throw new ServiceException("添加攻略失败");
        }
    }

    @Transactional
    public void updateGuide(TravelGuide guide) {
        if (travelGuideMapper.updateById(guide) <= 0) {
            throw new ServiceException("更新攻略失败");
        }
    }

    @Transactional
    public void deleteGuide(Long id) {
        if (travelGuideMapper.deleteById(id) <= 0) {
            throw new ServiceException("删除攻略失败");
        }
    }

    public List<Map<String, Object>> getHotGuides(Integer limit) {
        LambdaQueryWrapper<TravelGuide> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(TravelGuide::getViews)
                .last("LIMIT " + limit);
        
        List<TravelGuide> guides = travelGuideMapper.selectList(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (TravelGuide guide : guides) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", guide.getId());
            map.put("title", guide.getTitle());
            map.put("views", guide.getViews());
            map.put("coverImage", guide.getCoverImage());
            
            // 获取作者信息
            User user = userMapper.selectById(guide.getUserId());
            if (user != null) {
                map.put("authorNickname", user.getNickname());
                map.put("authorAvatar", user.getAvatar());
            }
            
            result.add(map);
        }
        
        return result;
    }

    public List<Map<String, Object>> getGuideSuggestions(String keyword, Integer limit) {
        LambdaQueryWrapper<TravelGuide> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(TravelGuide::getTitle, keyword)
                .orderByDesc(TravelGuide::getViews)
                .last("LIMIT " + limit);
        
        List<TravelGuide> guides = travelGuideMapper.selectList(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (TravelGuide guide : guides) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", guide.getId());
            map.put("title", guide.getTitle());
            map.put("views", guide.getViews());
            result.add(map);
        }
        
        return result;
    }

    public List<TravelGuide> getRelatedGuides(Long guideId, Integer limit) {
        // 获取当前攻略
        TravelGuide currentGuide = getById(guideId);
        if (currentGuide == null) {
            throw new ServiceException("攻略不存在");
        }

        // 构建查询条件：同一用户的其他攻略，或者标题相似的攻略
        LambdaQueryWrapper<TravelGuide> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(TravelGuide::getId, guideId) // 排除当前攻略
                .and(w -> w
                        .eq(TravelGuide::getUserId, currentGuide.getUserId()) // 同一用户的攻略
                        .or()
                        .like(TravelGuide::getTitle, currentGuide.getTitle()) // 标题相似的攻略
                )
                .orderByDesc(TravelGuide::getViews) // 按浏览量排序
                .last("LIMIT " + limit); // 限制返回数量

        return list(wrapper);
    }

    @Transactional
    public void addView(Long id) {
        TravelGuide guide = getById(id);
        if (guide != null) {
            guide.setViews(guide.getViews() + 1);
            updateById(guide);
        }
    }
}