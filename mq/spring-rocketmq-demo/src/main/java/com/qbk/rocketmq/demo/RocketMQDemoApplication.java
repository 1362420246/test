package com.qbk.rocketmq.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RocketMQ
 *
 * 发送同步消息
 * 发送单向消息
 * 发送批量消费
 * 发送异步消息
 * 发送延时消息
 * 发送顺序消费
 * 发送事务消息
 */
@SpringBootApplication
public class RocketMQDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketMQDemoApplication.class,args);
    }
}
