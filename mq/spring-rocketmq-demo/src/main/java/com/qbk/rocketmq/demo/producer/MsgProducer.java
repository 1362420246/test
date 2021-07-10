package com.qbk.rocketmq.demo.producer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
public class MsgProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private String topic = "TopicTest" ;

    /**
     * 发送消息 convertAndSend
     */
    @GetMapping("/send1")
    public void sendMessage(String msg) {
        rocketMQTemplate.convertAndSend(topic, msg);
    }
    /**
     * 发送同步消息 syncSend
     *
     * 发送可靠同步消息 ,可以拿到SendResult 返回数据
     * 同步发送是指消息发送出去后，会在收到mq发出响应之后才会发送下一个数据包的通讯方式。
     * 这种方式应用场景非常广泛，例如重要的右键通知、报名短信通知、营销短信等。
     */
    @GetMapping("/send2")
    public SendResult sendMsg(String msgBody){
        SendResult sendResult = rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(msgBody).build());
        return sendResult;
    }

    /**
     * 发送带tag的消息,直接在topic后面加上":tag"  syncSend
     */
    @GetMapping("/send_tag")
    public SendResult sendTagMsg(String msgBody){
        SendResult sendResult = rocketMQTemplate.syncSend(topic + ":tag1",MessageBuilder.withPayload(msgBody).build());
        return sendResult;
    }

    /**
     * 发送单向消息
     * 参数1： topic:tag
     * 参数2:  消息体 可以为一个对象
     */
    @GetMapping("/send_OneWay")
    public void sendOneWay(){
        rocketMQTemplate.sendOneWay(topic, "这是一条单向消息");
    }

    /**
     * 批量发送 syncSend
     */
    @GetMapping("/send_batch")
    public SendResult sendBatchMsg(){
        List<Message<String>> messages = new ArrayList<>();
        Message<String> msg1 = MessageBuilder.withPayload("1").build();
        Message<String> msg2 = MessageBuilder.withPayload("2").build();
        Message<String> msg3 = MessageBuilder.withPayload("3").build();
        Message<String> msg4 = MessageBuilder.withPayload("4").build();
        messages.addAll(Arrays.asList(msg1,msg2,msg3,msg4));

        SendResult sendResult = rocketMQTemplate.syncSend(topic ,messages);
        return sendResult;
    }

    /**
     * 发送异步消息 在SendCallback中可处理相关成功失败时的逻辑   asyncSend
     */
    @GetMapping("/send_async")
    public void sendAsyncMsg(String msgBody){

        rocketMQTemplate.asyncSend(topic,MessageBuilder.withPayload(msgBody).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 处理消息发送成功逻辑
                System.out.printf("发送结果：%s%n",sendResult);
            }
            @Override
            public void onException(Throwable e) {
                // 处理消息发送异常逻辑
                System.out.printf(e.getMessage() );
            }
        });
    }

    /**
     * 发送延时消息 syncSend
     * 在start版本中 延时消息一共分为18个等级分别为：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    @GetMapping("/send_delay")
    public SendResult sendDelayMsg(String msgBody, Integer delayLevel){
        final SendResult sendResult = rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(msgBody).build(),30000, delayLevel);
        return sendResult;
    }

}
