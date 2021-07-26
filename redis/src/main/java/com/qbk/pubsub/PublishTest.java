package com.qbk.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 发布的客户端
 */
public class PublishTest {

    private static JedisPool pool;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(50);
        config.setMaxWaitMillis(3000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        pool = new JedisPool(config,"127.0.0.1", 6379,6000,"123456");
    }

    public static void main(String[] args) {
        //Jedis jedis = new Jedis("127.0.0.1", 6379);

        Jedis jedis = pool.getResource();
        jedis.publish("qbk-123", "666");
        jedis.publish("qbk-abc", "test");
    }
}
