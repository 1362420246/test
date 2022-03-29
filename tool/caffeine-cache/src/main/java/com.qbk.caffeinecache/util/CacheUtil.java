package com.qbk.caffeinecache.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
/**
 * 通过Cache工具
 */
@Configuration
public class CacheUtil {
    @Bean("cache")
    Cache cache(){
        return Caffeine.newBuilder()
                //设置缓存的最大容量
                .maximumSize(1000)
                //设置缓存在写后失效时间
                .expireAfterWrite(30, TimeUnit.SECONDS)
                //设置缓存在写后，多长时间后，再进行刷新
                //.refreshAfterWrite(10, TimeUnit.SECONDS)
                .build();//加载缓存
    }
    public <K,V> V get(K key , Function<K,V> function){
        //参数类型K 结果类型V
        final Object o = cache().get(key, function);
        return (V)o;
    }
}
