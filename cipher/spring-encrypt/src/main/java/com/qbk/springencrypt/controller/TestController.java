package com.qbk.springencrypt.controller;

import com.qbk.springencrypt.encryption.Encrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Encrypt
    @PostMapping("/post")
    public Map<String,Object> post(@RequestBody HashMap<String,Object> params){
        System.out.println(params);
        return params;
    }
}
