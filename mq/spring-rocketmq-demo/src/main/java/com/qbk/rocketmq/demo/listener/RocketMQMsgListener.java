package com.qbk.rocketmq.demo.listener;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * rocketmq 消息监听
 *
 * 广播模式  messageModel = MessageModel.BROADCASTING
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "TopicTest",consumerGroup = "GROUP_TEST",messageModel = MessageModel.BROADCASTING)
public class RocketMQMsgListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        byte[] body = message.getBody();
        String msg = new String(body);
        log.info("接收到消息：{}", msg);
        System.out.printf("%s%n",message);
    }

}