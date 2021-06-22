package com.qbk.spring.demo.retry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * 重试机制
 */
@EnableRetry
@SpringBootApplication
public class SpringRetryApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringRetryApplication.class,args);
    }
}
