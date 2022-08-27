package com.qbk.zk.handwritten.config.controller;

import com.qbk.zk.handwritten.config.refresh.QBKRefreshScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@QBKRefreshScope
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private Environment environment;

    @RequestMapping("/remote-config-env")
    public String remoteConfig(){
        return "[env] id: "+this.environment.getProperty("id")+
                ", name: "+this.environment.getProperty("name");
    }

    @Value("${id}")
    private Integer id;

    @Value("${name}")
    private String name;

    @RequestMapping("/config-value")
    public String config(){
        return "id: "+this.id+", name: "+this.name;
    }
}
