package com.qbk.rocketmq.demo.producer;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 顺序消费
 *
 * 消息有序指的是可以按照消息的发送顺序来消费(FIFO)。
 *
 * RocketMQ可以严格的保证消息有序，可以分为分区有序或者全局有序。
 *
 * 顺序消费的原理解析，在默认的情况下消息发送会采取Round Robin轮询方式把消息发送到不同的queue(分区队列)；
 * 而消费消息的时候从多个queue上拉取消息，这种情况发送和消费是不能保证顺序。
 * 但是如果控制发送的顺序消息只依次发送到同一个queue中，消费的时候只从这个queue上依次拉取，则就保证了顺序。
 * 当发送和消费参与的queue只有一个，则是全局有序；
 * 如果多个queue参与，则为分区有序，即相对每个queue，消息都是有序的。
 */
@RestController
public class FifoProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private String topic = "TopicTest" ;

    /**
     * 顺序发送 syncSendOrderly
     */
    @GetMapping("/send/ordered")
    public SendResult sendOrderedMsg() {
        //hashKey: 保证分到同一个队列中
        rocketMQTemplate.syncSendOrderly(topic, MessageBuilder.withPayload("no1").build(), "hashKey");
        rocketMQTemplate.syncSendOrderly(topic, "no2", "hashKey");
        rocketMQTemplate.syncSendOrderly(topic, "no3", "hashKey");
        final SendResult sendResult = rocketMQTemplate.syncSendOrderly(topic, "no4", "hashKey");
        return sendResult;
    }
}
