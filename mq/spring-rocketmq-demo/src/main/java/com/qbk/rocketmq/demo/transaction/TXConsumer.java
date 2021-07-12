package com.qbk.rocketmq.demo.transaction;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 事务消费者
 *
 * spring rocketmq底层通过DefaultRocketMQListenerContainer这个类封装原生的Consumer对象来消费消息，
 * 其内部的DefaultMessageListenerConcurrently和DefaultMessageListenerOrderly
 * 两个消息监听器会根据我们的业务是否抛出异常来决定消息是否ack！
 */
@Component
@RocketMQMessageListener(consumerGroup = "tx_consumer",topic = "tx_topic")
public class TXConsumer implements RocketMQListener<String> {

    /*
    消费失败会触发消息重试机制

    消费成功：返回ConsumeConcurrentlyStatus.CONSUME_SUCCESS

    消费失败：
    方式①：返回RECONSUME_LATER，消息将重试
    方式②：返回null，消息将重试
    方式③：直接抛出异常， 消息将重试

    RocketMQ默认允许每条消息最多重试16次，每次重试的间隔时间与延迟消息的延迟级别是对应的。不过取的是延迟级别的后16级别。如果消息重试16次后仍然失败，消息将不再投递。转为进入死信队列。
    延时级别如下：messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h

    RocketMQ保持的是分布式数据最终一致性的，而不是强一致性的。如果消费者确实重试了15次之后还是没有消费成功，还是最终交给由人工处理。
     */
    @Override
    public void onMessage(String message) {
        System.out.println("事务消费：" + message);
        int i = 10 / 0;
    }
}