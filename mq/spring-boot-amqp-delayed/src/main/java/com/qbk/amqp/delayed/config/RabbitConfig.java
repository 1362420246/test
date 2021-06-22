package com.qbk.amqp.delayed.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * TTL+DLX 实现延时队列
 */
@Configuration
public class RabbitConfig {

    /**
     * 订单消息实际消费队列所绑定的交换机
     */
    @Bean
    DirectExchange orderDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange("order.direct")
                .durable(true)
                .build();
    }
    /**
     * 订单延迟队列队列所绑定的交换机
     */
    @Bean
    DirectExchange orderTtlDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange("order.direct.ttl")
                .durable(true)
                .build();
    }

    /**
     * 订单实际消费队列
     */
    @Bean
    public Queue orderQueue() {
        return new Queue("order.cancel");
    }

    /**
     * 订单延迟队列（死信队列）
     */
    @Bean
    public Queue orderTtlQueue() {
        Map<String, Object> args = new HashMap<String, Object>();
        // 第一种设置ttl过期时间方式： 队列中设置过期
//        args.put("x-message-ttl",6000);
        //到期后转发的交换机
        args.put("x-dead-letter-exchange","order.direct");
        //到期后转发的路由键
        args.put("x-dead-letter-routing-key","order.cancel.key");
        return new Queue("order.cancel.ttl", true, false, false, args);
    }

    /**
     * 将订单队列绑定到交换机
     */
    @Bean
    Binding orderBinding(DirectExchange orderDirect, Queue orderQueue){
        return BindingBuilder
                .bind(orderQueue)
                .to(orderDirect)
                .with("order.cancel.key");
    }

    /**
     * 将订单延迟队列绑定到交换机
     */
    @Bean
    Binding orderTtlBinding(DirectExchange orderTtlDirect,Queue orderTtlQueue){
        return BindingBuilder
                .bind(orderTtlQueue)
                .to(orderTtlDirect)
                .with("order.cancel.ttl.key");
    }

}
