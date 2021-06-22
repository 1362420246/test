package com.qbk.timerjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 简单的定时任务
 */
@SpringBootApplication
public class TimerJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(TimerJobApplication.class,args);
    }
}
