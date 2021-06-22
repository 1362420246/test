package com.qbk.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 如果不使用spring-boot-maven-plugin
 * 那么就可以不写主函数类
 */
@SpringBootApplication
public class QbkSpringStarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(QbkSpringStarterApplication.class,args);
    }
}
