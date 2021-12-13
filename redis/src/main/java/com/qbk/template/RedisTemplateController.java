package com.qbk.template;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * RedisTemplate
 *
 * execute() 需要 RedisConnection 对象，通过 RedisConnection 操作 Redis 被称为低级抽象（Low-Level Abstractions）
 * opsFor 之类的被称为高级抽象（High-Level Abstractions），是为了提供更友好的模板类，底层还是调用的 execute()，需要 RedisConnection 对象。
 *
 * 将同一类型操作封装为operation接口:
 *  ValueOperations：简单K-V操作
 *  SetOperations：set类型数据操作
 *  ZSetOperations：zset类型数据操作
 *  HashOperations：针对map类型的数据操作
 *  ListOperations：针对list类型的数据操作
 *
 *  提供了对key的“bound”(绑定)便捷化操作API，可以通过bound封装指定的key，然后进行一系列的操作而无须“显式”的再次指定Key，即BoundKeyOperations：
 *  BoundValueOperations
 *  BoundSetOperations
 *  BoundListOperations
 *  BoundSetOperations
 *  BoundHashOperations
 *
 * 针对数据的“序列化/反序列化”，提供了多种可选择策略(RedisSerializer):
 * JdkSerializationRedisSerializer：POJO对象的存取场景，使用JDK本身序列化机制，将pojo类通过ObjectInputStream/ObjectOutputStream进行序列化操作，最终redis-server中将存储字节序列。是目前最常用的序列化策略。
 * StringRedisSerializer：Key或者value为字符串的场景，根据指定的charset对数据的字节序列编码成string，是“new String(bytes, charset)”和“string.getBytes(charset)”的直接封装。是最轻量级和高效的策略。
 * JacksonJsonRedisSerializer：jackson-json工具提供了javabean与json之间的转换能力，可以将pojo实例序列化成json格式存储在redis中，也可以将json格式的数据转换成pojo实例。因为jackson工具在序列化和反序列化时，需要明确指定Class类型，因此此策略封装起来稍微复杂。【需要jackson-mapper-asl工具支持】
 *
 */
@RestController
public class RedisTemplateController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * execute ping / pong
     */
    @GetMapping("/ping")
    public String ping(){
        return redisTemplate.execute(RedisConnection::ping).toString();
    }

    /**
     * string 操作
     */
    @GetMapping("/string")
    public String string(){
        //1、通过redisTemplate设置值
        redisTemplate.boundValueOps("StringKey1").set("StringValue1");
        redisTemplate.boundValueOps("StringKey2").set("StringValue2",1, TimeUnit.MINUTES);

       //2、通过BoundValueOperations设置值
        BoundValueOperations stringKey = redisTemplate.boundValueOps("StringKey3");
        stringKey.set("StringVaule3");
        stringKey.set("StringValue3",1, TimeUnit.MINUTES);

        //3、通过ValueOperations设置值
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set("StringKey4", "StringVaule4");
        ops.set("StringValue5","StringVaule5",1, TimeUnit.MINUTES);

        //1、通过redisTemplate设置值
        String str1 = (String) redisTemplate.boundValueOps("StringKey1").get();
        System.out.println(str1);

        //2、通过BoundValueOperations获取值
        BoundValueOperations stringKey2 = redisTemplate.boundValueOps("StringKey2");
        String str2 = (String) stringKey2.get();
        System.out.println(str2);

        //3、通过ValueOperations获取值
        ValueOperations ops3 = redisTemplate.opsForValue();
        String str3 = (String) ops.get("StringKey3");
        System.out.println(str3);

       // 顺序递增
        redisTemplate.boundValueOps("StringKey6").increment(3L);

        //顺序递减
        redisTemplate.boundValueOps("StringKey6").increment(-3L);

        //判断key是否存在
        final Boolean stringKey6 = redisTemplate.hasKey("StringKey6");
        System.out.println(stringKey6);
        //指定key的失效时间
        redisTemplate.expire("StringKey6",10,TimeUnit.SECONDS);
        //根据key获取过期时间
        Long expire = redisTemplate.getExpire("StringKey6");
        System.out.println(expire);
        //删除多个key
        redisTemplate.delete(Arrays.asList("StringKey4","StringKey1"));
        return "s";
    }

    /**
     * list 操作
     */
    @GetMapping("/list")
    public String list(){
        //1、通过redisTemplate设置值
        redisTemplate.boundListOps("listKey1").leftPush("listLeftValue1");
        redisTemplate.boundListOps("listKey1").rightPush("listRightValue2");

        //2、通过BoundValueOperations设置值
        BoundListOperations listKey = redisTemplate.boundListOps("listKey1");
        listKey.leftPush("listLeftValue3");
        listKey.rightPush("listRightValue4");

        //3、通过ValueOperations设置值
        ListOperations opsList = redisTemplate.opsForList();
        opsList.leftPush("listKey1", "listLeftValue5");
        opsList.rightPush("listKey1", "listRightValue6");

        //将List放入缓存
        ArrayList<String> list = new ArrayList<>();
        list.add("listLeftValue7");
        redisTemplate.boundListOps("listKey1").rightPushAll(list);
        redisTemplate.boundListOps("listKey1").leftPushAll(list);


        //设置过期时间(单独设置)
        redisTemplate.boundValueOps("listKey1").expire(1,TimeUnit.MINUTES);
        redisTemplate.expire("listKey1",1,TimeUnit.MINUTES);


        //获取List缓存的长度
        Long size = redisTemplate.boundListOps("listKey1").size();
        System.out.println(size);

        //获取List缓存全部内容（起始索引，结束索引）
        List listKey1 = redisTemplate.boundListOps("listKey1").range(0, 10);
        System.out.println(listKey1);

        //从左或从右弹出一个元素
        String listKey2 =  redisTemplate.boundListOps("listKey1").leftPop().toString();  //从左侧弹出一个元素
        String listKey3 =  redisTemplate.boundListOps("listKey1").rightPop().toString(); //从右侧弹出一个元素
        System.out.println(listKey2);
        System.out.println(listKey3);

        //根据索引查询元素
        String listKey4 =  redisTemplate.boundListOps("listKey1").index(1).toString();
        System.out.println(listKey4);

        // 根据索引修改List中的某条数据(key，索引，值)
        //redisTemplate.boundListOps("listKey").set(3L,"listLeftValue3");

        // 移除N个值为value(key,移除个数，值)
        redisTemplate.boundListOps("listKey").remove(3L,"value");
        System.out.println();
        return "list";
    }


    /**
     * zset 操作
     */
    @GetMapping("/zset")
    public String zset(){
        //向集合中插入元素，并设置分数
        //1、通过redisTemplate设置值
        redisTemplate.boundZSetOps("zSetKey").add("zSetVaule1", 100D);

        //2、通过BoundValueOperations设置值
        BoundZSetOperations zSetKey = redisTemplate.boundZSetOps("zSetKey");
        zSetKey.add("zSetVaule2", 101);

        //3、通过ValueOperations设置值
        ZSetOperations zSetOps = redisTemplate.opsForZSet();
        zSetOps.add("zSetKey", "zSetVaule3", 99);

        //向集合中插入多个元素,并设置分数
        DefaultTypedTuple<String> p1 = new DefaultTypedTuple<>("zSetVaule4", 2.1D);
        DefaultTypedTuple<String> p2 = new DefaultTypedTuple<>("zSetVaule5", 3.3D);
        redisTemplate.boundZSetOps("zSetKey").add(new HashSet<>(Arrays.asList(p1,p2)));

        //按照排名先后(从小到大)打印指定区间内的元素, -1为打印全部
        Set<String> range = redisTemplate.boundZSetOps("zSetKey").range(0, -1);
        System.out.println(range);

        //获得指定元素的分数
        Double score = redisTemplate.boundZSetOps("zSetKey").score("zSetVaule");
        System.out.println(score);

        //返回集合内的成员个数
        Long size = redisTemplate.boundZSetOps("zSetKey").size();
        System.out.println(size);

        //返回集合内指定分数范围的成员个数（Double类型）
        Long COUNT = redisTemplate.boundZSetOps("zSetKey").count(0D, 2.2D);
        System.out.println(COUNT);

        //返回集合内元素在指定分数范围内的排名（从小到大）
        Set byScore = redisTemplate.boundZSetOps("zSetKey").rangeByScore(0D, 2.2D);
        System.out.println(byScore);

        //带偏移量和个数，(key，起始分数，最大分数，偏移量，个数)
        Set<String> ranking2 = redisTemplate.opsForZSet().rangeByScore("zSetKey", 0D, 2.2D, 1, 3);
        System.out.println(ranking2);

        //返回集合内元素的排名，以及分数（从小到大）
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.boundZSetOps("zSetKey").rangeWithScores(0L, 3L);
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            System.out.println(tuple.getValue() + " : " + tuple.getScore());
        }
        //返回指定成员的排名
        //从小到大
        Long startRank = redisTemplate.boundZSetOps("zSetKey").rank("zSetVaule");
        //从大到小
        Long endRank = redisTemplate.boundZSetOps("zSetKey").reverseRank("zSetVaule");

        //从集合中删除指定元素
        redisTemplate.boundZSetOps("zSetKey").remove("zSetVaule");

        //删除指定索引范围的元素（Long类型）
        redisTemplate.boundZSetOps("zSetKey").removeRange(0L,3L);

        //删除指定分数范围内的元素（Double类型）
        redisTemplate.boundZSetOps("zSetKey").removeRangeByScore(0D,2.2D);

        //为指定元素加分（Double类型）
        Double score1 = redisTemplate.boundZSetOps("zSetKey").incrementScore("zSetVaule",1.1D);
        return "zset";
    }

    @GetMapping("/pipeline")
    public String pipeline(){
        Map<String,String> map = new HashMap<>();
        map.put("a","a");
        map.put("b","b");
        map.put("c","c");
        executePipelined(map,30);
        return "pipeline";
    }

    /**
     * 功能描述: 使用pipelined批量存储
     *
     */
    public void executePipelined(Map<String, String> map, long seconds) {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                map.forEach((key, value) -> {
                    connection.set(serializer.serialize(key), serializer.serialize(JSON.toJSONString(value)), Expiration.seconds(seconds), RedisStringCommands.SetOption.UPSERT);
                });
                return null;
            }
        },serializer);
    }

    /**
     * 功能描述: 使用pipelined批量存储资源文件
     */
    public void executeResourcePipelined(List<String> list, long seconds) {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                list.forEach((value) -> {
                    connection.set(serializer.serialize("k" +value ), serializer.serialize(value),Expiration.seconds(seconds), RedisStringCommands.SetOption.UPSERT);
                });
                return null;
            }
        },serializer);
    }
}
