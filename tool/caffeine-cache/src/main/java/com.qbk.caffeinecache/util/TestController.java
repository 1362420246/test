package com.qbk.caffeinecache.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
/**
 * 测试通用Cache工具
 */
@RestController
public class TestController {
    @Autowired
    private CacheUtil cacheUtil;
    @GetMapping("/get")
    public Map<Integer,String> get(Integer id) {
        //参数类型Integer 结果类型Map
        //id = k
        return cacheUtil.get(id,
                k -> {
                    System.out.println("date:" + LocalDateTime.now() + ",调用方法: " + k);
                    Map<Integer,String> map = new HashMap<>();
                    map.put(k,"qbk");
                    return map;
                }
        );
    }
}
