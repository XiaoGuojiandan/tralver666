package org.example.springboot.service;

import jakarta.annotation.Resource;
import org.example.springboot.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 消息生产者服务
 */
@Service
public class MQProducerService {
    
    private static final Logger logger = LoggerFactory.getLogger(MQProducerService.class);
    
    @Resource
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 发送邮件消息
     * @param emailMessage 邮件消息对象
     */
    public void sendEmailMessage(Object emailMessage) {
        String messageId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(messageId);
        
        logger.info("发送邮件消息，ID：{}，内容：{}", messageId, emailMessage);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_EXCHANGE,
                RabbitMQConfig.EMAIL_ROUTING_KEY, 
                emailMessage, 
                correlationData);
    }
    
    /**
     * 发送订单通知消息
     * @param orderMessage 订单消息对象
     */
    public void sendOrderNotification(Object orderMessage) {
        String messageId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(messageId);
        
        logger.info("发送订单通知消息，ID：{}，内容：{}", messageId, orderMessage);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_NOTIFICATION_ROUTING_KEY, 
                orderMessage, 
                correlationData);
    }
    
    /**
     * 发送订单完成消息
     * @param orderMessage 订单消息对象
     */
    public void sendOrderComplete(Object orderMessage) {
        String messageId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(messageId);
        
        logger.info("发送订单完成消息，ID：{}，内容：{}", messageId, orderMessage);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_COMPLETE_ROUTING_KEY, 
                orderMessage, 
                correlationData);
    }
    
    /**
     * 发送系统事件消息
     * @param eventType 事件类型
     * @param eventData 事件数据
     */
    public void sendEventMessage(String eventType, Object eventData) {
        String messageId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(messageId);
        
        String routingKey = "tourism.event." + eventType;
        
        logger.info("发送系统事件消息，ID：{}，类型：{}，内容：{}", messageId, eventType, eventData);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EVENT_EXCHANGE,
                routingKey, 
                eventData, 
                correlationData);
    }
} 