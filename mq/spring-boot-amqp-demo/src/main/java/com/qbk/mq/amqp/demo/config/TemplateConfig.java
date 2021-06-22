package com.qbk.mq.amqp.demo.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemplateConfig {
    /**
     * Spring AMQP 的连接工厂接口，用于创建连接。CachingConnectionFactory 是ConnectionFactory 的一个实现类
     */
//    @Bean
//    public ConnectionFactory connectionFactory() throws Exception {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("39.106.33.10", 5672);
//        connectionFactory.setUsername("quboka");
//        connectionFactory.setPassword("quboka");
//        connectionFactory.setVirtualHost("/");
//        // 设置 生产者 confirms
//        connectionFactory.setPublisherConfirms(true);
//        // 设置 生产者 Returns
//        connectionFactory.setPublisherReturns(true);
//        return connectionFactory;
//    }

    /**
     * RabbitAdmin 是 AmqpAdmin 的实现，封装了对 RabbitMQ 的基础管理操作，比 如对交换机、队列、绑定的声明和删除等
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * MessageListenerContainer可以理解为MessageListener的容器，
     * 一个Container 只有一个 Listener，但是可以生成多个线程使用相同的 MessageListener 同时消费消息
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        return container;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //开启事务
        //rabbitTemplate.setChannelTransacted(true);

        //开启异步 confirms 模式
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (!ack) {
                    System.out.println("发送消息失败：" + cause);
                    throw new RuntimeException("发送异常：" + cause);
                }else {
                    System.out.println("消息发送到 RabbitMQ 服务器成功");
                }
            }
        });

        //开启 returns 模式
        //如果消息从交换机路由到队列失败
        //开启消息回发
        rabbitTemplate.setMandatory(true);
        //开启消息回发
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback(){
            @Override
            public void returnedMessage(Message message,
                                        int replyCode,
                                        String replyText,
                                        String exchange,
                                        String routingKey){
                System.out.println("回发的消息：");
                System.out.println("replyCode: "+replyCode);
                System.out.println("replyText: "+replyText);
                System.out.println("exchange: "+exchange);
                System.out.println("routingKey: "+routingKey);
            }
        });
        return rabbitTemplate;
    }
}