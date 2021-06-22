package com.qbk.pubsub;

import redis.clients.jedis.Jedis;

/**
 * 监听的客户端
 */
public class ListenTest {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        final MyListener listener = new MyListener();
        // 使用模式匹配的方式设置频道
        // 会阻塞
        jedis.psubscribe(listener, new String[]{"qbk-*"});
    }
}
