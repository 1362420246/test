package com.qbk.pubsub;

import redis.clients.jedis.*;

/**
 * 监听的客户端
 */
public class ListenTest {

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
        Jedis jedis = pool.getResource();
        final MyListener listener = new MyListener();
        // 使用模式匹配的方式设置频道
        // 会阻塞
        jedis.psubscribe(listener, "qbk-*");
    }
}
