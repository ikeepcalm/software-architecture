package dev.ikeepcalm.software.backend.eventdriven;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange names
    public static final String REPAIR_EXCHANGE = "repair.exchange";
    
    // Queue names
    public static final String STATUS_CHANGE_QUEUE = "repair.status.queue";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String SCHEDULER_QUEUE = "scheduler.queue";
    
    // Routing keys
    public static final String STATUS_CHANGE_KEY = "repair.status.changed";
    public static final String NOTIFICATION_KEY = "notification.send";
    public static final String SCHEDULER_KEY = "appointment.update";
    
    @Bean
    public TopicExchange repairExchange() {
        return new TopicExchange(REPAIR_EXCHANGE);
    }
    
    @Bean
    public Queue statusChangeQueue() {
        return new Queue(STATUS_CHANGE_QUEUE, true);
    }
    
    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }
    
    @Bean
    public Queue schedulerQueue() {
        return new Queue(SCHEDULER_QUEUE, true);
    }
    
    @Bean
    public Binding statusChangeBinding(Queue statusChangeQueue, TopicExchange repairExchange) {
        return BindingBuilder.bind(statusChangeQueue).to(repairExchange).with(STATUS_CHANGE_KEY);
    }
    
    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange repairExchange) {
        return BindingBuilder.bind(notificationQueue).to(repairExchange).with(NOTIFICATION_KEY);
    }
    
    @Bean
    public Binding schedulerBinding(Queue schedulerQueue, TopicExchange repairExchange) {
        return BindingBuilder.bind(schedulerQueue).to(repairExchange).with(SCHEDULER_KEY);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}