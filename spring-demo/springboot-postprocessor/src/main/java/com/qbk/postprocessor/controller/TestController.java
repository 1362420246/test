package com.qbk.postprocessor.controller;

import com.qbk.postprocessor.handler.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试自定义对象 注册到spring容器
 */
@RestController
public class TestController {

    @Autowired
    @Qualifier("test")
    private Handler handler;

    @Autowired
    @Qualifier("test2")
    private Handler handler2;

    @GetMapping("/get")
    public String get(){
        return handler.getName()+ handler2.getName();
    }

}
