package com.qbk.sortedset;

import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
/**
 * zset 示例2  Redis搭建网上商城的商品相关性分析程序。
 *
 * Redis Zincrby 命令对有序集合中指定成员的分数加上增量 increment
 *
 * 商品的相关性就是某个产品与其他另外某商品同时出现在购物车中的情况。这种数据分析对于电商行业是很重要的，可以用来分析用户购买行为。
 *
 * 例如：
 在某一商品的detail页面，推荐给用户与该商品相关的其他商品；
 在添加购物车成功页面，当用户把一个商品添加到购物车，推荐给用户与之相关的其他商品；
 在货架上将相关性比较高的几个商品摆放在一起。
 *
 * 利用Redis的有序集合，为每种商品构建一个有序集合，集合的成员为和该商品同时出现在购物车中的商品，
 * 成员的score为同时出现的次数。每次A和B商品同时出现在购物车中时，分别更新Redis中A和B对应的有序集合。
 */
public class AliyunShoppingMall {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        try {
            String authString = jedis.auth("123456");
            if (!authString.equals("OK")) {
                System.err.println("AUTH Failed: " + authString);
                return;
            }
            //产品列表
            String key0="产品:啤酒";
            String key1="产品:巧克力";
            String key2="产品:可乐";
            String key3="产品:口香糖";
            String key4="产品:牛肉干";
            String key5="产品:鸡翅";
            final String[] products = new String[]{key0,key1,key2,key3,key4,key5};
            //初始化，清除可能的已有旧数据
            for (int i = 0; i < products.length; i++) {
                jedis.del(products[i]);
            }
            //模拟用户购物
            for (int i = 0; i < 5; i++) {
                //模拟多人次的用户购买行为
                customersShopping(products,i,jedis);
            }
            System.out.println();
            //利用Redis来输出各个商品间的关联关系
            for (int i = 0; i < products.length; i++) {
                System.out.println(">>>>>>>>>>与"+products[i]+"一起被购买的产品有<<<<<<<<<<<<<<<");
                Set<Tuple> relatedList = jedis.zrevrangeWithScores(products[i], 0, -1);
                for (Tuple item : relatedList) {
                    System.out.println("商品名称："+item.getElement()+"， 共同购买次数:"+Double.valueOf(item.getScore()).intValue());
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            jedis.quit();
            jedis.close();
        }
    }

    /**
     * 模拟用户购物
     */
    private static void customersShopping(String[] products, int i, Jedis jedis) {
        //简单模拟3种购买行为，随机选取作为用户的购买选择
        int bought=(int)(Math.random()*3);
        if(bought==1){
            //模拟业务逻辑：用户购买了如下产品
            System.out.println("用户"+i+"购买了"+products[0]+"，"+products[2]+"，"+products[1]);
            //将产品之间的关联情况记录到 Redis的SortSet之中
            jedis.zincrby(products[0], 1, products[1]);
            jedis.zincrby(products[0], 1, products[2]);
            jedis.zincrby(products[1], 1, products[0]);
            jedis.zincrby(products[1], 1, products[2]);
            jedis.zincrby(products[2], 1, products[0]);
            jedis.zincrby(products[2], 1, products[1]);
        }else if(bought==2){
            //模拟业务逻辑：用户购买了如下产品
            System.out.println("用户"+i+"购买了"+products[4]+"，"+products[2]+"，"+products[3]);
            //将产品之间的关联情况记录到 Redis的SortSet之中
            jedis.zincrby(products[4], 1, products[2]);
            jedis.zincrby(products[4], 1, products[3]);
            jedis.zincrby(products[3], 1, products[4]);
            jedis.zincrby(products[3], 1, products[2]);
            jedis.zincrby(products[2], 1, products[4]);
            jedis.zincrby(products[2], 1, products[3]);
        }else if(bought==0){
            //模拟业务逻辑：用户购买了如下产品
            System.out.println("用户"+i+"购买了"+products[1]+","+products[5]);
            //将产品之间的关联情况记录到 Redis的SortSet之中
            jedis.zincrby(products[5], 1, products[1]);
            jedis.zincrby(products[1], 1, products[5]);
        }
    }
}
