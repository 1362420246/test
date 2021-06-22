package com.qbk.timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * spring SchedulingConfigurer 动态定时器，也可扩展持久化
 */
@SpringBootApplication
public class TimerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TimerApplication.class,args);
    }
}
