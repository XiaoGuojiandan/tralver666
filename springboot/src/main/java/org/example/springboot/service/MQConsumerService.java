package org.example.springboot.service;

import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import org.example.springboot.DTO.EmailMessageDTO;
import org.example.springboot.DTO.OrderMessageDTO;
import org.example.springboot.config.RabbitMQConfig;
import org.example.springboot.entity.User;
import org.example.springboot.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 消息消费者服务
 */
@Service
public class MQConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(MQConsumerService.class);
    
    @Resource
    private EmailService emailService;
    
    @Resource
    private UserMapper userMapper;
    
    /**
     * 处理邮件消息
     */
    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void handleEmailMessage(EmailMessageDTO emailMessage, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        
        try {
            logger.info("收到邮件消息：{}", emailMessage);
            
            // 调用邮件服务发送邮件
            emailService.sendEmail(emailMessage);
            
            // 确认消息处理成功
            channel.basicAck(deliveryTag, false);
            logger.info("邮件消息处理成功");
        } catch (Exception e) {
            logger.error("处理邮件消息失败：{}", e.getMessage(), e);
            
            // 拒绝消息并重新入队
            // 参数: deliveryTag, multiple, requeue
            channel.basicNack(deliveryTag, false, true);
            logger.info("邮件消息处理失败，已重新入队");
        }
    }
    
    /**
     * 处理订单通知消息
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_NOTIFICATION_QUEUE)
    public void handleOrderNotification(OrderMessageDTO orderMessage, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        
        try {
            logger.info("收到订单通知消息：{}", orderMessage);
            
            // 查询用户信息
            User user = userMapper.selectById(orderMessage.getUserId());
            if (user != null && user.getEmail() != null) {
                // 根据订单状态构造邮件内容
                String subject = "订单状态更新通知";
                String content = buildOrderNotificationContent(orderMessage);
                
                // 发送邮件通知
                EmailMessageDTO emailMessage = EmailMessageDTO.createNotificationEmail(
                        user.getEmail(), subject, content);
                emailService.sendEmail(emailMessage);
                
                logger.info("已发送订单通知邮件至：{}", user.getEmail());
            }
            
            // 确认消息处理成功
            channel.basicAck(deliveryTag, false);
            logger.info("订单通知消息处理成功");
        } catch (Exception e) {
            logger.error("处理订单通知消息失败：{}", e.getMessage(), e);
            
            // 拒绝消息并重新入队
            channel.basicNack(deliveryTag, false, true);
            logger.info("订单通知消息处理失败，已重新入队");
        }
    }
    
    /**
     * 处理订单完成消息
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_COMPLETE_QUEUE)
    public void handleOrderComplete(OrderMessageDTO orderMessage, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        
        try {
            logger.info("收到订单完成消息：{}", orderMessage);
            
            // 这里可以添加订单完成后的业务逻辑，如积分增加等
            
            // 确认消息处理成功
            channel.basicAck(deliveryTag, false);
            logger.info("订单完成消息处理成功");
        } catch (Exception e) {
            logger.error("处理订单完成消息失败：{}", e.getMessage(), e);
            
            // 拒绝消息并重新入队
            channel.basicNack(deliveryTag, false, true);
            logger.info("订单完成消息处理失败，已重新入队");
        }
    }
    
    /**
     * 处理系统事件消息
     */
    @RabbitListener(queues = RabbitMQConfig.EVENT_QUEUE)
    public void handleEventMessage(Object eventMessage, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        
        try {
            logger.info("收到系统事件消息：{}", eventMessage);
            
            // 根据不同的事件类型处理不同的业务逻辑
            
            // 确认消息处理成功
            channel.basicAck(deliveryTag, false);
            logger.info("系统事件消息处理成功");
        } catch (Exception e) {
            logger.error("处理系统事件消息失败：{}", e.getMessage(), e);
            
            // 拒绝消息并重新入队
            channel.basicNack(deliveryTag, false, true);
            logger.info("系统事件消息处理失败，已重新入队");
        }
    }
    
    /**
     * 构建订单通知内容
     */
    private String buildOrderNotificationContent(OrderMessageDTO orderMessage) {
        StringBuilder content = new StringBuilder();
        content.append("尊敬的用户，您的订单状态已更新：\n\n");
        content.append("订单编号：").append(orderMessage.getOrderNo()).append("\n");
        content.append("产品名称：").append(orderMessage.getProductName()).append("\n");
        content.append("数量：").append(orderMessage.getQuantity()).append("\n");
        content.append("金额：").append(orderMessage.getAmount()).append("\n");
        
        // 根据事件类型构造不同内容
        switch (orderMessage.getEventType()) {
            case "create":
                content.append("订单状态：待支付\n");
                content.append("请在30分钟内完成支付，否则订单将自动取消。");
                break;
            case "pay":
                content.append("订单状态：已支付\n");
                content.append("支付时间：").append(orderMessage.getPaymentTime()).append("\n");
                content.append("感谢您的支付，祝您旅途愉快！");
                break;
            case "cancel":
                content.append("订单状态：已取消\n");
                content.append("您的订单已取消，期待您的再次预订。");
                break;
            case "refund":
                content.append("订单状态：已退款\n");
                content.append("退款将在1-7个工作日内退回您的支付账户。");
                break;
            case "complete":
                content.append("订单状态：已完成\n");
                content.append("您的订单已完成，感谢您的选择，欢迎再次光临！");
                break;
            default:
                content.append("订单状态：").append(orderMessage.getStatus()).append("\n");
        }
        
        return content.toString();
    }
} 