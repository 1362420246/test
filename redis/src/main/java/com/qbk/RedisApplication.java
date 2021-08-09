package com.qbk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 布隆过滤器
 * jedis 分片
 * Redisson 分布式锁
 * lua脚本
 * 自定义redis客户端
 * pipeline
 * jedis 发布订阅
 * Reactor模型
 */
@SpringBootApplication
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

}
