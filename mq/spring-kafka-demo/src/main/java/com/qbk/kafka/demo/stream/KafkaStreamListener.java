package com.qbk.kafka.demo.stream;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamListener {

    @KafkaListener(topics = "Stream")
    public void onMessage2(String message) {
        // 处理
        System.out.println("Stream处理之前的"+message);
    }

    @KafkaListener(topics = "AnotherTopic")
    public void onMessage1(String message) {
        // 处理
        System.out.println("Stream处理之后的"+message);
    }
}
