package com.qbk.zk.handwritten.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 手写配置中心
 *
 * 配置刷新：
 *   使用 zk watch机制，监听节点值的变化，刷新 Environment
 *   使用 zk watch机制、反射、后置处理器，刷新 @Value
 *
 */
@SpringBootApplication
public class ZkHandwrittenConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZkHandwrittenConfigApplication.class,args);
    }
}
