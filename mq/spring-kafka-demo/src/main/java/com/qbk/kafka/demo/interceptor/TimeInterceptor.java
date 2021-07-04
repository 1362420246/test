package com.qbk.kafka.demo.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * TimeInterceptor  发送消息之前加上时间戳
 */
public class TimeInterceptor  implements ProducerInterceptor<String,String> {

    /**
     * 发送消息之前加上时间戳
     */
    @Override
    public ProducerRecord<String,String> onSend(ProducerRecord record) {
        return new ProducerRecord<>(record.topic(), (String) record.key(), System.currentTimeMillis()+","+record.value());
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}

