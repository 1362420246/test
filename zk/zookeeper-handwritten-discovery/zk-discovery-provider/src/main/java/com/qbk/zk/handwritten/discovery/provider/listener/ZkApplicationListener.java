package com.qbk.zk.handwritten.discovery.provider.listener;

import com.qbk.zk.handwritten.discovery.provider.registry.ServiceRegistry;
import com.qbk.zk.handwritten.discovery.provider.registry.ZookeeperServiceRegistry;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 通过监听器 在refresh执行后 向zk注册服务
 */
@Component
public class ZkApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * 当SpringBoot执行refresh方法到最后的finishRefresh的时候，就会发布ContextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("事件监听机制的回调...");
        // 获取配置属性
        Environment environment = event.getApplicationContext().getEnvironment();
        String serviceName = environment.getProperty("zk.service-name");
        String ip = environment.getProperty("zk.ip");
        String port = environment.getProperty("server.port");
        String zkServer = environment.getProperty("zk.server");
        // 服务注册
        ServiceRegistry zookeeperServiceRegistry = new ZookeeperServiceRegistry(serviceName,ip,port,zkServer);
        zookeeperServiceRegistry.register();
    }
}
