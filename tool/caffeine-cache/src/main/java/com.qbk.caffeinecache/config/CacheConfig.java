package com.qbk.caffeinecache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * caffeine使用
 *
 * @author qbk
 */
@Slf4j
@RestController
@Configuration
public class CacheConfig {

    @Bean("userCache")
    public LoadingCache<Integer, Map<String, String>> userCache() {
        return Caffeine.newBuilder()
                .maximumSize(10)//设置缓存的最大容量
                //.recordStats()//开启缓存统计
                .expireAfterWrite(10, TimeUnit.SECONDS)//设置缓存在写后失效时间
                //.refreshAfterWrite(10, TimeUnit.SECONDS)//设置缓存在写后，多长时间后，再进行刷新
                .build(this::getUserByID);//加载缓存
    }

    /**
     * 加载缓存数据
     */
    private Map<String, String> getUserByID(Integer id) {
        //模拟数据来源
        Map<Integer, Map<String, String>> datasource = new HashMap<>();
        for (int i = 1; i < 11; i++) {
            String username = "qbk" + i;
            datasource.put(i, new HashMap<String, String>() {
                {
                    put("name", username);
                }
            });
        }
        return datasource.getOrDefault(id, Collections.emptyMap());
    }

    @Autowired
    @Qualifier("userCache")
    private LoadingCache<Integer, Map<String, String>> userCache;

    @GetMapping("/user/get")
    public Map<String, String> getUser(Integer id) {
        //获取缓存
        final Map<String, String> user = userCache.get(id);
        log.info("查询数据:" + user);
        return user;
    }
}