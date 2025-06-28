package org.example.springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    // 交换机名称
    public static final String EMAIL_EXCHANGE = "tourism.email.exchange";
    public static final String ORDER_EXCHANGE = "tourism.order.exchange";
    public static final String EVENT_EXCHANGE = "tourism.event.exchange";
    
    // 队列名称
    public static final String EMAIL_QUEUE = "tourism.email.queue";
    public static final String ORDER_NOTIFICATION_QUEUE = "tourism.order.notification.queue";
    public static final String ORDER_COMPLETE_QUEUE = "tourism.order.complete.queue";
    public static final String EVENT_QUEUE = "tourism.event.queue";
    
    // 路由键
    public static final String EMAIL_ROUTING_KEY = "tourism.email";
    public static final String ORDER_NOTIFICATION_ROUTING_KEY = "tourism.order.notification";
    public static final String ORDER_COMPLETE_ROUTING_KEY = "tourism.order.complete";
    public static final String EVENT_ROUTING_KEY = "tourism.event.#";
    
    /**
     * 消息转换器 - 使用JSON格式
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    /**
     * 自定义RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        
        // 设置消息确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.out.println("消息发送到交换机失败: " + correlationData + ", 原因: " + cause);
            }
        });
        
        // 设置消息返回回调
        rabbitTemplate.setReturnsCallback(returned -> {
            System.out.println("消息发送到队列失败: " + returned.getMessage() + 
                    ", 回应码: " + returned.getReplyCode() + 
                    ", 回应信息: " + returned.getReplyText() +
                    ", 交换机: " + returned.getExchange() + 
                    ", 路由键: " + returned.getRoutingKey());
        });
        
        // 设置消息返回
        rabbitTemplate.setMandatory(true);
        
        return rabbitTemplate;
    }
    
    /**
     * 邮件服务相关配置
     */
    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE);
    }
    
    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE).build();
    }
    
    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange()).with(EMAIL_ROUTING_KEY);
    }
    
    /**
     * 订单服务相关配置
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }
    
    @Bean
    public Queue orderNotificationQueue() {
        return QueueBuilder.durable(ORDER_NOTIFICATION_QUEUE).build();
    }
    
    @Bean
    public Queue orderCompleteQueue() {
        return QueueBuilder.durable(ORDER_COMPLETE_QUEUE).build();
    }
    
    @Bean
    public Binding orderNotificationBinding() {
        return BindingBuilder.bind(orderNotificationQueue()).to(orderExchange()).with(ORDER_NOTIFICATION_ROUTING_KEY);
    }
    
    @Bean
    public Binding orderCompleteBinding() {
        return BindingBuilder.bind(orderCompleteQueue()).to(orderExchange()).with(ORDER_COMPLETE_ROUTING_KEY);
    }
    
    /**
     * 事件总线相关配置
     */
    @Bean
    public TopicExchange eventExchange() {
        return new TopicExchange(EVENT_EXCHANGE);
    }
    
    @Bean
    public Queue eventQueue() {
        return QueueBuilder.durable(EVENT_QUEUE).build();
    }
    
    @Bean
    public Binding eventBinding() {
        return BindingBuilder.bind(eventQueue()).to(eventExchange()).with(EVENT_ROUTING_KEY);
    }
} 