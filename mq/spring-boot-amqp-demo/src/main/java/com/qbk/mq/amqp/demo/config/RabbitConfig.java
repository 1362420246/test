package com.qbk.mq.amqp.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;
/**
 * 配置 交换机、队列、绑定
 */
@Configuration
public class RabbitConfig {

    /**
     * 主题交换机
     */
    @Bean("topicExchange")
    public TopicExchange getTopicExchange(){
        //交换机的名字
        return new TopicExchange("TOPIC_EXCHANGE");
    }

    /**
     * 扇形（广播）交换机
     */
    @Bean("fanoutExchange")
    public FanoutExchange getFanoutExchange(){
        //交换机的名字
        return  new FanoutExchange("FANOUT_EXCHANGE");
    }

    /**
     * 队列1
     * 一个queue若不指定binding的交换机,就被绑定到默认交换机上,默认交换机其实就是直连交换机
     * (AMQP default)  direct
     */
    @Bean("firstQueue")
    public Queue getFirstQueue(){
        Map<String, Object> args = new HashMap<String, Object>();
        // 队列中的消息未被消费 6 秒后过期
        args.put("x-message-ttl",6000);
        /*
         * @param name 队列名字，不能为空
	     * @param durable  默认 true 。是否持久化，代表队列在服务器重启后是否还存在
	     * @param exclusive 默认 false 。是否排他性队列。排他性队列只能在声明它的 Connection 中使用（可以在同一个 Connection 的不同的 channel 中使用），连接断开时自动删除。
	     * @param autoDelete 默认 false 。是否自动删除。如果为 true，至少有一个消费者连接到 这个队列，之后所有与这个队列连接的消费者都断开时，队列会自动删除。
	     * @param arguments 队列的其他属性
         */
        Queue queue = new Queue("FIRST_QUEUE", true, false, false, args);
        //Queue queue = new Queue("FIRST_QUEUE");
        return queue;
    }

    /**
     * 队列2
     */
    @Bean("secondQueue")
    public Queue getSecondQueue(){
        return new Queue("SECOND_QUEUE");
    }

    /**
     * 队列3
     */
    @Bean("thirdQueue")
    public Queue getThirdQueue(){
        return new Queue("THIRD_QUEUE");
    }

    /**
     * 主题交换机和队列的绑定
     */
    @Bean
    public Binding bindSecond(
            @Qualifier("secondQueue") Queue queue,
            @Qualifier("topicExchange") TopicExchange exchange){
        //routingKey 的通配符
        //1）# 0 个或者多个单词
        //2）* 不多不少一个单词
        return BindingBuilder.bind(queue).to(exchange).with("#.qbk.#");
    }

    /**
     * 广播交换机和队列 的绑定
     */
    @Bean
    public Binding bindThird(
            @Qualifier("thirdQueue") Queue queue,
            @Qualifier("fanoutExchange") FanoutExchange exchange){
        //广播交换机  不需要指定绑定键
        return BindingBuilder.bind(queue).to(exchange);
    }

}
