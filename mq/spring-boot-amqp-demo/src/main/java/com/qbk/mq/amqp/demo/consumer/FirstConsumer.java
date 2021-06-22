package com.qbk.mq.amqp.demo.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 一个@RabbitListener就对应一个MessageListenerContainer
 * ConnectionFactory被自动注入，队列名称可以用queues，queuesToDeclare，或是bindings里面的queue指定，
 * 收到消息的处理方法用@RabbitHandler指定,或是直接把@RabbitListener打在对应的方法上就行.
 */
@Component
@RabbitListener(queues = "FIRST_QUEUE")
public class FirstConsumer {
    /**
     * @param msg 消息内容,当只有一个参数的时候可以不加@Payload注解
     */
    @RabbitHandler
    public void process(String msg,Channel channel,Message message){
        boolean success = false;
        try {
            System.out.println(" first queue received msg : " + msg);
            success = true;
        } finally {
            if (success) {
                System.out.println("ACK");
                try {
                     /* 确认收到，手动 ack
                     * @param  deliveryTag 接收标记
                     * @param  multiple 是否批量
                     */
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("NACK");
                try {
                     /* 拒绝一个或多个接收到的消息,手动 nack
                     * @param  deliveryTag 接收标记
                     * @param  multiple 是否批量
                     * @param  requeue 是否重新放回队列。
                     *          如果否 ：将会进入死信队列 或者 丢弃 .
                     *          如果是 ：会返回队列，别其他消费者消费，如果只有者一个消费者，会死循环。多次以后会停止
                     */
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
