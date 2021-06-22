package com.qbk.amqp.plugin.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 消费者类
 */
@Component
@Slf4j
public class RabbitConsumer {

    /**
     * @param list 监听的内容
     */
    @RabbitListener(queues = "delay_queue")
    public void receiveDealy(List<Integer> list, Message message, Channel channel) throws IOException {
        log.info("===============接收队列接收消息====================");
        log.info("接收时间:{},接受内容:{}", LocalDateTime.now(), list.toString());
    }
}
