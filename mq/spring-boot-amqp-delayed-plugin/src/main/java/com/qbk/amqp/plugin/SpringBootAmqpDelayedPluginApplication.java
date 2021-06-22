package com.qbk.amqp.plugin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 延时队列插件
 */
@SpringBootApplication
public class SpringBootAmqpDelayedPluginApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAmqpDelayedPluginApplication.class,args);
    }
}
