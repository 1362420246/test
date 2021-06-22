package com.qbk.lockweb;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 并发测试
 */
public class Test {
    public static void main(String[] args)  {
        try {
            long start = System.currentTimeMillis();
            ConcurrentExecutor.execute(() -> {
                Map<String,Object> params = new HashMap<>();
                params.put("name",Thread.currentThread().getName());
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8098/syn/get" +
                        "?name={name}",String.class,params);
                String body = responseEntity.getBody();
                System.out.println(Thread.currentThread().getName() + ":" + body);
            }, 2,2);
            long end = System.currentTimeMillis();
            System.out.println("总耗时：" + (end - start) + " ms.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}