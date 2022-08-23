package com.qbk.zk.handwritten.discovery.consumer.discovery;

import java.util.List;

/**
 * 服务发现接口
 */
public interface ServiceDiscovery {

    List<String> discovery(String serviceName);

    void registerWatch(String serviceNamePath);
}