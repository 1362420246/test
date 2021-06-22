package com.qbk.amqp.delayed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ttl + 死信队列
 *
 * 缺点：如果单独设置消息的 TTL，则可能会造成队列中的消息阻塞 : 前一条消息没 有出队（没有被消费），后面的消息无法投递
 */
@SpringBootApplication
public class SpringBootAmqpDelayedApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAmqpDelayedApplication.class,args);
    }
}
