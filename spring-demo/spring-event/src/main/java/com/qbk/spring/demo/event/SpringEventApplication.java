package com.qbk.spring.demo.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * spring事件
 */
@EnableAsync
@SpringBootApplication
public class SpringEventApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringEventApplication.class,args);
    }
}
