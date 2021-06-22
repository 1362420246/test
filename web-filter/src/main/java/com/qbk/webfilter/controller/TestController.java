package com.qbk.webfilter.controller;

import com.qbk.webfilter.resp.Result;
import com.qbk.webfilter.resp.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("/get")
    public Result<?> get(String name){
        System.out.println(name);
        return ResultUtil.ok();
    }

    @GetMapping("/error")
    public Result<?> error(String name){
        System.out.println(name);
        int i = 10/0;
        return ResultUtil.ok();
    }
}
