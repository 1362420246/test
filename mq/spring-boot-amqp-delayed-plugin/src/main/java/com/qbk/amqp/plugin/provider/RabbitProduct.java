package com.qbk.amqp.plugin.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * rabbitMq生产者类
 */
@Component
@Slf4j
public class RabbitProduct {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(List<Integer> list , long delayTimes) {
        //这里的消息可以是任意对象，无需额外配置，直接传即可
        log.info("===============延时队列生产消息====================");
        log.info("发送时间:{},发送内容:{}", LocalDateTime.now(), list.toString());
        this.rabbitTemplate.convertAndSend(
                "delay_exchange",
                "delay_key",
                list,
                message -> {
                    //注意这里时间可以使long，而且是设置header
                    message.getMessageProperties().setHeader("x-delay", delayTimes);
                    return message;
                }
        );
        log.info("{}ms后执行", delayTimes);
    }
}

