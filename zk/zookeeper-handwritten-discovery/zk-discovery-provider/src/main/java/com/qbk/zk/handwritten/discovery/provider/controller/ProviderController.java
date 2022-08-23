package com.qbk.zk.handwritten.discovery.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/query")
    public String query(){
        System.out.println("port:" + port);
        return "port:" + port;
    }
}