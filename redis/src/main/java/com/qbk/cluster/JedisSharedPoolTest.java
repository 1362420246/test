package com.qbk.cluster;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * jedis分布式之 ShardedJedisPool （一致性Hash分片算法）
 */
public class JedisSharedPoolTest {

    private static ShardedJedisPool pool = null;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(5);
        config.setMaxIdle(20);
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);

        List<JedisShardInfo> shards = new ArrayList<>();
        JedisShardInfo infoA = new JedisShardInfo("127.0.0.1", 6379);
        infoA.setPassword("123456");
        JedisShardInfo infoB = new JedisShardInfo("127.0.0.2", 6379);
        infoB.setPassword("123456");

        shards.add(infoA);
        shards.add(infoB);
        //pool = new ShardedJedisPool(config, shards);
        pool = new ShardedJedisPool(config, shards, Hashing.MURMUR_HASH,Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        for (int i = 0; i < 10; i++) {
            String key = "qbk" + i;
            System.out.println(key+":"+jedis.getShard(key).getClient().getHost());
            System.out.println(jedis.set(key,"1"));
        }
    }
}
