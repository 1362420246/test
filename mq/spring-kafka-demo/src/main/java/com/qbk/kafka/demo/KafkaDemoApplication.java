package com.qbk.kafka.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 生产者:
 * 1.简单发送消息
 * 2.带回调的发送消息
 * 3.发送事务消息
 * 4.自定义分区器
 *
 * 消费者:
 * 1.简单消费
 * 2.指定topic、partition、offset消费
 * 3.批量消费
 * 4.消费异常处理器
 * 5.消息过滤器
 * 6.消息转发
 */
@SpringBootApplication
public class KafkaDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaDemoApplication.class,args);
    }
}
