package com.qbk.pubsub;

import redis.clients.jedis.Jedis;

/**
 * 发布的客户端
 */
public class PublishTest {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.publish("qbk-123", "666");
        jedis.publish("qbk-abc", "test");
    }
}
