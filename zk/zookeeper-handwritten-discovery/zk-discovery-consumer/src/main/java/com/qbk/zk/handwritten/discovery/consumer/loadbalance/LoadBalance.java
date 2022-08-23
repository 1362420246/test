package com.qbk.zk.handwritten.discovery.consumer.loadbalance;

import java.util.List;

/**
 * 负载均衡
 */
public interface LoadBalance {
    String select(List<String> urls);
}