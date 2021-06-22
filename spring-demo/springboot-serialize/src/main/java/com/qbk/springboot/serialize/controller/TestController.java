package com.qbk.springboot.serialize.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @PostMapping("/post")
    public Map<String,Object> post(@RequestBody Map<String,Object> map){
        System.out.println(map);
        Map<String,Object> result = new HashMap<>();
        result.put("code",200);
        return result;
    }
}
