package com.qbk.zk.handwritten.discovery.consumer.config;

import com.qbk.zk.handwritten.discovery.consumer.discovery.ServiceDiscovery;
import com.qbk.zk.handwritten.discovery.consumer.discovery.ServiceDiscoveryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * 将ServiceDiscoveryImpl交给Spring IoC容器管理
 */
@Configuration
public class ZookeeperDiscoveryAutoConfiguration {

    @Resource
    private Environment environment;

    @Bean
    public ServiceDiscovery serviceDiscovery(){
        ServiceDiscovery serviceDiscovery =
                new ServiceDiscoveryImpl(environment.getProperty("zk.server"));
        // 添加对节点的监听
        serviceDiscovery.registerWatch("/qbk-registry/discovery-provider");
        return serviceDiscovery;
    }
}










