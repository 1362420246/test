package com.qbk.rocketmq.demo.transaction;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 事务消费者
 */
@Component
@RocketMQMessageListener(consumerGroup = "tx_consumer",topic = "tx_topic")
public class TXConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("事务消费：" + message);
    }
}