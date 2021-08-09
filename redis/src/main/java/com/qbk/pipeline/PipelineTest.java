package com.qbk.pipeline;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis pipeline
 *
 * pipeline是Redis的一个提高吞吐量的机制，适用于多key读写场景，比如同时读取多个key的value，或者更新多个key的value
 */
public class PipelineTest {

    public static void main(String[] args) {
        Jedis redis = new Jedis("127.0.0.1", 6379);
        redis.auth("123456");
        redis.select(8);
        redis.flushDB();

//-------------------插入對比-------------------------------------------------------------------------------
        long start = System.currentTimeMillis();
        Map<String, String> data = new HashMap<>(16);
        for (int i = 0; i < 10000; i++) {
            data.clear();
            data.put("k" + i, "v" + i);
            redis.hmset("key" + i, data);
        }
        long end = System.currentTimeMillis();
        System.out.println("1,未使用PIPE批量设值耗时" + (end - start)  + "毫秒..");
        System.out.println("共插入:[" + redis.dbSize() + "]条 ..");
        System.out.println();

        redis.select(8);
        redis.flushDB();

        //创建 pipeline 的链接对象
        Pipeline pipe = redis.pipelined();
        // 使用pipeline hmset
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            data.clear();
            data.put("k" + i, "v" + i);
            //将值封装到PIPE对象，此时并未执行，还停留在客户端
            pipe.hmset("key" + i, data);
        }
        //将封装后的PIPE一次性发给redis
        pipe.sync();
        end = System.currentTimeMillis();
        System.out.println("2,使用PIPE批量设值耗时" + (end - start)  + "毫秒..");
        System.out.println("PIPE共插入:[" + redis.dbSize() + "]条 ..");
        System.out.println();

//-------------------查詢對比-------------------------------------------------------------------------------
        Set<String> keys = redis.keys("key*");
        start = System.currentTimeMillis();
        Map<String, Map<String, String>> result = new HashMap<>(12000);
        for (String key : keys) {
            //此处keys根据以上的设值结果，共有10000个，循环10000次
            result.put(key, redis.hgetAll(key));
        }
        end = System.currentTimeMillis();
        System.out.println("3,未使用PIPE批量取值耗时 " + (end - start)  + "毫秒..");
        System.out.println("共取值:[" + result.size() + "]条 .. ");
        System.out.println();

        // 使用pipeline hgetall
        start = System.currentTimeMillis();
        for (String key : keys) {
            //使用PIPE封装需要取值的key,此时还停留在客户端，并未真正执行查询请求
            pipe.hgetAll(key);
        }
        //提交到redis进行查询
        List<Object> list = pipe.syncAndReturnAll();
        end = System.currentTimeMillis();
        System.out.println("4,使用PIPE批量取值耗时" + (end - start)  + "毫秒..");
        System.out.println("PIPE共取值:[" + list.size() + "]条 .. ");
        System.out.println();

//-------------------組合命令-------------------------------------------------------------------------------
        redis.select(9);
        redis.flushDB();

        //使用pipeline提交所有操作并返回执行结果：
        //一组需要一起执行的命令放到multi和exec两个命令之间，其中multi代表事务开始，exec代表事务结束
        pipe.multi();
        pipe.set("name", "1");
        pipe.incr("name");
        pipe.get("name");
        pipe.exec();
        // 将不同类型的操作命令合并提交，并将操作操作以list返回
        List<Object> resultList = pipe.syncAndReturnAll();
        System.out.println(resultList);

        // 断开连接，释放资源
        redis.disconnect();
    }
}
