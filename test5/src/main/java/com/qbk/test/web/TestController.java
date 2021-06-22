package com.qbk.test.web;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/test5")
@RestController
public class TestController {

    @GetMapping("/get")
    public Map<String,Object> get(String name){
        System.out.println("get:" + name);
        Map<String,Object> result = new HashMap<>();
        result.put("msg","success");
        return result;
    }

    @PostMapping("/post")
    public Map<String,Object> post(@RequestBody Map<String,Object>  name){
        System.out.println("post:" + name);
        Map<String,Object> result = new HashMap<>();
        result.put("msg","success");
        return result;
    }
}
