package com.qbk.rocketmq.demo.transaction;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 事务生产者
 */
@RestController
public class TXProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private String topic = "tx_topic" ;

    /**
     *  发送事务消息 sendMessageInTransaction
     */
    @GetMapping("/send_transaction")
    public SendResult sendMessageIntransaction(String msg) {

        Message<String> message = MessageBuilder.withPayload(msg).build();

        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(topic, message, topic);

        return sendResult;
    }
}
