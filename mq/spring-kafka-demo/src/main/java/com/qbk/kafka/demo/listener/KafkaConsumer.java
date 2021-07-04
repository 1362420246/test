package com.qbk.kafka.demo.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaConsumer {

    /**
     * 消费监听
     */
    @KafkaListener(topics = {"topic1"})
    public void onMessage1(ConsumerRecord<?, ?> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("简单消费：topic:" + record.topic() + "|partition:" + record.partition() + "|offset:" + record.offset() + "|value:" + record.value());
    }

    /**
     * 指定topic、partition、offset消费
     * 同时监听topic1和topic2，监听topic1的0号分区、topic2的 "0号和1号" 分区，指向0号分区的offset初始值为3
     *
     * ① id：消费者ID；
     * ② groupId：消费组ID；
     * ③ topics：监听的topic，可监听多个；
     * ④ topicPartitions：可配置更加详细的监听信息，可指定topic、parition、offset监听。
     *
     * 注意：topics和topicPartitions不能同时使用；
     **/
    @KafkaListener(
            id = "consumer1",
            groupId = "qbk-group",
            topicPartitions = {
                    @TopicPartition(topic = "topic2", partitions = {"0"}),
                    @TopicPartition(
                            topic = "topic1",
                            //partitions = "0",
                            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "3")
                    )
            })
    public void onMessage2(ConsumerRecord<?, ?> record) {
        System.out.println("topic:" + record.topic() + "|partition:" + record.partition() + "|offset:" + record.offset() + "|value:" + record.value());
    }

    /**
     * 批量消费
     *
     *  # 设置批量消费
     *  spring.kafka.listener.type=batch
     *  # 批量消费每次最多消费多少条消息
     *  spring.kafka.consumer.max-poll-records=50
     */
   // @KafkaListener(id = "consumer2",groupId = "qbk-group", topics = "topic1")
    public void onMessage3(List<ConsumerRecord<?, ?>> records) {
        System.out.println(">>>批量消费一次，records.size()=" + records.size());
        for (ConsumerRecord<?, ?> record : records) {
            System.out.println(record.value());
        }
    }
}
