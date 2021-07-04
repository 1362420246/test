package com.qbk.kafka.demo.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaStreamProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @RequestMapping("/streams")
    public String sendMessageStreams(){
        kafkaTemplate.send("Stream","key1","你好1");
        return "success";
    }
}
