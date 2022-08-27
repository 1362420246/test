package com.qbk.zk.handwritten.discovery.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 手写注册中心服务发现-消费端(服务发现)
 *
 * 服务发现: 使用zk watch 监听服务节点变化
 */
@SpringBootApplication
public class ZkHandwrittenDiscoveryConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZkHandwrittenDiscoveryConsumerApplication.class,args);
    }
}
