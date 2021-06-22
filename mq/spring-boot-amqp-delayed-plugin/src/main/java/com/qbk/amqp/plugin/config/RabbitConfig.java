package com.qbk.amqp.plugin.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延时队列插件
 */
@Configuration
public class RabbitConfig {

    /**
     * 延时队列交换机
     * 注意这里的交换机类型：CustomExchange
     */
    @Bean
    public CustomExchange delayExchange(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        //声明一个 x-delayed-message 类型的 Exchange 来使用 delayed-messaging 特性
        return new CustomExchange("delay_exchange","x-delayed-message",true, false,args);
    }

    /**
     * 延时队列
     */
    @Bean
    public Queue delayQueue(){
        return new Queue("delay_queue",true);
    }

    /**
     * 给延时队列绑定交换机
     */
    @Bean
    public Binding delayBinding(Queue delayQueue, CustomExchange delayExchange){
        return BindingBuilder.bind(delayQueue).to(delayExchange).with("delay_key").noargs();
    }

}
