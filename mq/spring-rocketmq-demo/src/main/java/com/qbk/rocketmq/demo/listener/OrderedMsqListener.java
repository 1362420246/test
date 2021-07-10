package com.qbk.rocketmq.demo.listener;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 监听顺序消息，保证顺序消费
 *
 * 消费者在消费的时候，默认是异步多线程消费的，所以无法保证顺序，需要指定同步消费才行 ，consumeMode = ConsumeMode.ORDERLY
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "TopicTest", consumerGroup = "ordered-consumer",consumeMode = ConsumeMode.ORDERLY)
public class OrderedMsqListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("consumer 顺序消费，收到消息{}",message);
    }
}
