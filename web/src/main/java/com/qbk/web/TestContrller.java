package com.qbk.web;

import com.qbk.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试Advice
 */
@RestController
public class TestContrller {

    private TestService testService;

    /**
     * 构造器注入
     */
    public TestContrller(TestService testService){
        this.testService = testService;
    }
    //public TestContrller(){ }

    /**
     * 测试构造器注入
     */
    @GetMapping("/into")
    public Map<String,Object> into(){
        testService.into();
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("name","qbk");
        return resultMap;
    }

    /**
     * 测试 @RestControllerAdvice注解
     */
    @GetMapping("/get")
    public Map<String,Object> get(String name,Integer id){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("name",name);
        resultMap.put("id",id);
        return resultMap;
    }

    /**
     * 测试 RequestBodyAdvice 和 ResponseBodyAdvice
     */
    @PostMapping("/post")
    public Map<String,Object> post( @RequestBody Map<String,Object> resultMap){
        return resultMap;
    }
}
