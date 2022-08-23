package com.qbk.zk.handwritten.discovery.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 手写注册中心服务发现-生产端
 */
@SpringBootApplication
public class ZkHandwrittenDiscoveryProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZkHandwrittenDiscoveryProviderApplication.class,args);
    }
}
