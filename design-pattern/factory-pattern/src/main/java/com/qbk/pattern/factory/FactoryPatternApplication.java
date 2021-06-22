package com.qbk.pattern.factory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 简单工厂 是产品的工厂
 * 工厂方法 是工厂的工厂
 * 抽象工厂 是复杂产品的工厂
 */
@SpringBootApplication
public class FactoryPatternApplication {
    public static void main(String[] args) {
        SpringApplication.run(FactoryPatternApplication.class,args);
    }
}
