package com.qbk.mq.amqp.demo.dynamic;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 动态创建队列
 */
@Component
public class DynamicQueue {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SimpleMessageListenerContainer simpleMessageListenerContainer;

    /**
     * 创建交换机
     */
    public Exchange createExchange(String exchangeName){
        /**
         * FanoutExchange、TopicExchange、HeadersExchange 、DirectExchange等
         * 都是同一个父类 AbstractExchange
         */
        //FanoutExchange fanoutExchange = new FanoutExchange(exchangeName,true,false);
        // TopicExchange dataExchange = new TopicExchange(exchangeName,true,false);
        DirectExchange directExchange = new DirectExchange(exchangeName,true,false);
        rabbitAdmin.declareExchange(directExchange);
        return directExchange;
    }

    /**
     * 删除交换机
     */
    public void deleteExchange(String exchangeName){
        rabbitAdmin.deleteExchange(exchangeName);
    }

    /**
     * 创建队列
     */
    public Queue createQueue(String queueName){
        /*
         * @param name 队列名字，不能为空
	     * @param durable  默认 true 。是否持久化，代表队列在服务器重启后是否还存在
	     * @param exclusive 默认 false 。是否排他性队列。排他性队列只能在声明它的 Connection 中使用（可以在同一个 Connection 的不同的 channel 中使用），连接断开时自动删除。
	     * @param autoDelete 默认 false 。是否自动删除。如果为 true，至少有一个消费者连接到 这个队列，之后所有与这个队列连接的消费者都断开时，队列会自动删除。
	     * @param arguments 队列的其他属性
         */
        Queue queue = new Queue(queueName, true, false, false);
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    /**
     * 删除队列
     */
    public void deleteQueue(String queueName){
        rabbitAdmin.deleteQueue(queueName);
    }

    /**
     * 绑定
     */
    public void binding(Exchange exchange , Queue queue ,String key){
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(key).noargs();
        rabbitAdmin.declareBinding(binding);
    }

    /**
     * 有绑定Key的Exchange发送
     */
    public void sendMessage(Exchange exchange , String routingKey, Object msg){
        rabbitTemplate.setExchange(exchange.getName());
        rabbitTemplate.setRoutingKey(routingKey);
        //send方法复杂不会序列化
        rabbitTemplate.convertAndSend(routingKey,msg);
    }

    /**
     * 没有绑定KEY的Exchange发送
     */
    public void sendMessage( AbstractExchange exchange, String msg){
        rabbitAdmin.declareExchange(exchange);
        rabbitTemplate.setExchange(exchange.getName());
        rabbitTemplate.send(new Message(msg.getBytes(), new MessageProperties()));
    }

    /**
     * 动态监听队列
     */
    public void createListener(Queue queue) throws Exception {
        //将队列加入监听器
        simpleMessageListenerContainer.setQueueNames(queue.getName());
        simpleMessageListenerContainer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("====动态监听接收到:"+message.getMessageProperties().getConsumerQueue()+"队列的消息=====");
                System.out.println("====动态监听:" + message.getMessageProperties());
                System.out.println("====动态监听:" +new String(message.getBody()));
            }
        });
        TimeUnit.SECONDS.sleep(5);
        simpleMessageListenerContainer.addQueueNames(queue.getName());
    }
}
