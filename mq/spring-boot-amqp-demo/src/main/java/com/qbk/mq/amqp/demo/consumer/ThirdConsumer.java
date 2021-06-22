package com.qbk.mq.amqp.demo.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 队列和交换机的绑定都在RabbitConfig里
 */
@Component
@RabbitListener(queues = "THIRD_QUEUE")
public class ThirdConsumer {
    @RabbitHandler
    public void process(String msg, Channel channel, Message message) throws IOException {
        System.out.println(" third queue received msg : " + msg);

        /* 确认收到，手动 ack
         * @param  deliveryTag 接收标记
         * @param  multiple 是否批量
         */
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
