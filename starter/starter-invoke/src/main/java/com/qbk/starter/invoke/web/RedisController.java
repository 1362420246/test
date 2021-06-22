package com.qbk.starter.invoke.web;

import com.qbk.starter.utils.RedisUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/redis")
@RestController
public class RedisController {

    @GetMapping("/")
    public String get(){
        RedisUtils.set("qbk","这是一个redis的测试");
        return (String) RedisUtils.get("qbk");
    }
}
