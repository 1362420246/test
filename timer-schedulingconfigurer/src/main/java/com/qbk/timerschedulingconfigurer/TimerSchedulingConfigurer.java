package com.qbk.timerschedulingconfigurer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 使用 spring 的 SchedulingConfigurer  实现定时器
 *
 * 参考 @EnableScheduling 注释
 */
@SpringBootApplication
public class TimerSchedulingConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(TimerSchedulingConfigurer.class,args);
    }
}
