package com.qbk.guavademo.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Cache
 */
public class CacheTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // 通过CacheBuilder构建一个缓存实例
        Cache<String, String> cache = CacheBuilder.newBuilder()
                // 设置缓存的最大容量
                .maximumSize(100)
                // 设置缓存在写入一秒后失效
                .expireAfterWrite(1, TimeUnit.SECONDS)
                // 设置并发级别为10
                .concurrencyLevel(10)
                // 开启缓存统计
                .recordStats()
                .build();
        //向缓存中添加 数据 K V 形式
        cache.put("hello","where are you");
        // 获取 缓存
        System.out.println(cache.getIfPresent("hello"));
        // 延迟2 秒
        Thread.sleep(1000 * 2);
        // 获取一个缓存，如果该缓存不存在则返回一个null值
        System.out.println(cache.getIfPresent("hello"));
        // 获取缓存，当缓存不存在时，则通Callable进行加载并返回。该操作是原子
        String getValue = cache.get("hello", new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "ni hao kk";
                    }
                }
        );
        System.out.println(getValue);
        // 回收所有缓存
        cache.invalidateAll();
        System.out.println("--------------------------------------------");
        //LoadingCache 和 cache 的用法差不多，但是可以自定义load方法，当找不到key 时候可以定义如何加载
        LoadingCache<String, String> graphs = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build(
                        new CacheLoader<String, String>() {
                            @Override
                            public String load(String key) {
                                System.out.println("load key ：" + key);
                                return key + " ：value";
                            }
                        }
                );
        System.out.println(graphs.getUnchecked("hello"));
        System.out.println(graphs.getUnchecked("hello"));
    }
}
