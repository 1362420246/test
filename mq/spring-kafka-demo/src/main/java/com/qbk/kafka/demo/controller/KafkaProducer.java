package com.qbk.kafka.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 发送消息
     */
    @GetMapping("/kafka/normal/{message}")
    public void sendMessage1(@PathVariable("message") String normalMessage) {
        kafkaTemplate.send("topic1", normalMessage);
    }

    /***********************************************/

    /**
     * 带回调的生产者
     *
     *  kafkaTemplate提供了一个回调方法addCallback，我们可以在回调方法中监控消息是否发送成功 或 失败时做补偿处理，有两种写法
     */
    @GetMapping("/kafka/callbackOne/{message}")
    public void sendMessage2(@PathVariable("message") String callbackMessage) {
        kafkaTemplate.send("topic1", callbackMessage)
                .addCallback(success -> {
                    // 消息发送到的topic
                    String topic = success.getRecordMetadata().topic();
                    // 消息发送到的分区
                    int partition = success.getRecordMetadata().partition();
                    // 消息在分区内的offset
                    long offset = success.getRecordMetadata().offset();
                    System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);
                }, failure -> {
                    System.out.println("发送消息失败:" + failure.getMessage());
                });
    }
    @GetMapping("/kafka/callbackTwo/{message}")
    public void sendMessage3(@PathVariable("message") String callbackMessage) {
        kafkaTemplate.send("topic2", callbackMessage)
                .addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        System.out.println("发送消息失败：" + ex.getMessage());
                    }

                    @Override
                    public void onSuccess(SendResult<String, Object> result) {
                        System.out.println("发送消息成功：" + result.getRecordMetadata().topic() + "-"
                                + result.getRecordMetadata().partition() + "-" + result.getRecordMetadata().offset());
                    }
                });
    }

    /***********************************************/

    /**
     * kafka事务提交
     * 如果在发送消息时需要创建事务，可以使用 KafkaTemplate 的 executeInTransaction 方法来声明事务，
     *
     * spring.kafka.producer.transaction-id-prefix=topic
     * spring.kafka.producer.retries=1
     * spring.kafka.producer.acks=-1
     */
    //@GetMapping("/kafka/transaction/{message}")
    public void sendMessage7(@PathVariable("message") String message) {
        //Kafka生产者在同一个事务内提交到多个分区的消息，要么同时成功，要么同时失败。
        kafkaTemplate.executeInTransaction(
                operations -> {
                    operations.send("topic1","125",message);
                    int i = 10/0;
                    operations.send("topic2","456",message);
                    return true;
                }
        );
    }
}
