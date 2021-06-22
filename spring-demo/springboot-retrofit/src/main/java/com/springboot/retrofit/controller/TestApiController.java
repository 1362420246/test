package com.springboot.retrofit.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/test/api")
@RestController
public class TestApiController {
    @GetMapping("/get")
    public Map<String,Object> get( Integer id){
        Map<String,Object> result = new HashMap<>();
        result.put("msg","get测试");
        result.put("id",id);
        return result;
    }
    @PostMapping("/post")
    public Map<String,Object> post(@RequestBody Map<String,Object> params){
        params.put("msg","post测试");
        return params;
    }
}
