package com.qbk.smartdocdemo.controller;

import com.qbk.smartdocdemo.entity.SubUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 *
 * @author qbk
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 测试接口
     * @param name 名称
     * @return 用户
     */
    @GetMapping("/get")
    public SubUser get(@RequestParam String name){
        SubUser subUser = new SubUser();
        return subUser;
    }
}
