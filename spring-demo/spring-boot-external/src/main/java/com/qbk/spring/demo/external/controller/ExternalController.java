package com.qbk.spring.demo.external.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExternalController {

    @Value("${info.name}")
    private String str;

    @RequestMapping("/get")
    public String get(){
        return str;
    }
}
