package org.example.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.springboot.common.Result;
import org.example.springboot.config.RabbitMQConfig;
import org.example.springboot.util.JwtTokenUtils;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 系统监控控制器
 */
@Tag(name = "系统监控接口")
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Resource
    private AmqpAdmin amqpAdmin;
    
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 获取队列状态信息
     */
    @Operation(summary = "获取RabbitMQ队列状态")
    @GetMapping("/queue-status")
    public Result<?> getQueueStatus() {
        // 验证权限，只允许管理员访问
        var currentUser = JwtTokenUtils.getCurrentUser();
        if (currentUser == null || !"ADMIN".equals(currentUser.getRoleCode())) {
            return Result.error("无权限访问");
        }
        
        Map<String, Object> result = new HashMap<>();
        
        // 获取各队列信息
        result.put("email", getQueueInfo(RabbitMQConfig.EMAIL_QUEUE));
        result.put("orderNotification", getQueueInfo(RabbitMQConfig.ORDER_NOTIFICATION_QUEUE));
        result.put("orderComplete", getQueueInfo(RabbitMQConfig.ORDER_COMPLETE_QUEUE));
        result.put("event", getQueueInfo(RabbitMQConfig.EVENT_QUEUE));
        
        return Result.success(result);
    }
    
    /**
     * 获取队列详细信息
     */
    private Map<String, Object> getQueueInfo(String queueName) {
        Map<String, Object> queueInfo = new HashMap<>();
        try {
            // 获取队列属性
            Properties properties = amqpAdmin.getQueueProperties(queueName);
            if (properties != null) {
                // 消息数量
                queueInfo.put("messageCount", properties.get("QUEUE_MESSAGE_COUNT"));
                // 消费者数量
                queueInfo.put("consumerCount", properties.get("QUEUE_CONSUMER_COUNT"));
                // 是否存在
                queueInfo.put("exists", true);
            } else {
                queueInfo.put("exists", false);
            }
        } catch (Exception e) {
            queueInfo.put("exists", false);
            queueInfo.put("error", e.getMessage());
        }
        return queueInfo;
    }

    /**
     * 清空队列
     */
    @Operation(summary = "清空RabbitMQ队列")
    @GetMapping("/purge-queues")
    public Result<?> purgeQueues() {
        // 验证权限，只允许管理员访问
        var currentUser = JwtTokenUtils.getCurrentUser();
        if (currentUser == null || !"ADMIN".equals(currentUser.getRoleCode())) {
            return Result.error("无权限访问");
        }
        
        try {
            // 清空各队列
            amqpAdmin.purgeQueue(RabbitMQConfig.EMAIL_QUEUE, false);
            amqpAdmin.purgeQueue(RabbitMQConfig.ORDER_NOTIFICATION_QUEUE, false);
            amqpAdmin.purgeQueue(RabbitMQConfig.ORDER_COMPLETE_QUEUE, false);
            amqpAdmin.purgeQueue(RabbitMQConfig.EVENT_QUEUE, false);
            
            return Result.success("所有队列已清空");
        } catch (Exception e) {
            return Result.error("清空队列失败: " + e.getMessage());
        }
    }
    
    /**
     * 发送测试消息
     */
    @Operation(summary = "发送测试消息")
    @GetMapping("/send-test-message")
    public Result<?> sendTestMessage() {
        // 验证权限，只允许管理员访问
        var currentUser = JwtTokenUtils.getCurrentUser();
        if (currentUser == null || !"ADMIN".equals(currentUser.getRoleCode())) {
            return Result.error("无权限访问");
        }
        
        try {
            // 发送测试消息到事件队列
            Map<String, Object> testMessage = new HashMap<>();
            testMessage.put("type", "test");
            testMessage.put("timestamp", System.currentTimeMillis());
            testMessage.put("content", "这是一条测试消息");
            
            rabbitTemplate.convertAndSend(RabbitMQConfig.EVENT_EXCHANGE, "tourism.event.test", testMessage);
            
            return Result.success("测试消息已发送");
        } catch (Exception e) {
            return Result.error("发送测试消息失败: " + e.getMessage());
        }
    }
} 