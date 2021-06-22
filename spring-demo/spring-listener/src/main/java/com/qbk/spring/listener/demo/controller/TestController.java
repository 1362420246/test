package com.qbk.spring.listener.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("testController")
public class TestController {

    @GetMapping("/test")
    public String get(){
        return "t";
    }
}
