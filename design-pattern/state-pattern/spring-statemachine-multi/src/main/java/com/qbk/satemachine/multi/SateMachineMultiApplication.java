package com.qbk.satemachine.multi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 状态机
 *
 * spring-statemachine 多个状态机共存
 */
@SpringBootApplication
public class SateMachineMultiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SateMachineMultiApplication.class,args);
    }
}
