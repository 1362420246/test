package com.qbk.zk.handwritten.discovery.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 手写注册中心服务发现-生产端(服务注册)
 *
 * 服务注册: 用zk创建临时子节点进行服务注册
 */
@SpringBootApplication
public class ZkHandwrittenDiscoveryProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZkHandwrittenDiscoveryProviderApplication.class,args);
    }
}
