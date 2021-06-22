package com.qbk.caffeinecache.demo;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * caffeine cache
 * api与 guava cache 一致
 */
public class CacheTest {
    public static void main(String[] args) throws Exception {

        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();

        cache.put("hello","world");

        System.out.println(cache.getIfPresent("hello"));

        TimeUnit.SECONDS.sleep(2);

        System.out.println( cache.getIfPresent("hello"));

        LoadingCache<String, String> graphs = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .refreshAfterWrite(1, TimeUnit.MINUTES)
                .build(key ->  key.concat(":value"));

        System.out.println(graphs.get("kk"));
    }

}
