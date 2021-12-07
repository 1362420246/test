package com.qbk.boot.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * spring 初始化方法对比
 *
 * 多個PostConstruct 加載順序
 * PostConstruct和 Lazy 影响
 */
@SpringBootApplication
public class SpringBootInitApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootInitApplication.class,args);
    }
}
