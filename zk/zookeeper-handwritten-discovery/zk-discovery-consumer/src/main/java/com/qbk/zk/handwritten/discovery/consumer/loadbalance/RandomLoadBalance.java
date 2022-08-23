package com.qbk.zk.handwritten.discovery.consumer.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡
 */
public class RandomLoadBalance implements LoadBalance{
    @Override
    public String select(List<String> urls) {
        int len = urls.size();
        Random random = new Random();
        return urls.get(random.nextInt(len));
    }
}