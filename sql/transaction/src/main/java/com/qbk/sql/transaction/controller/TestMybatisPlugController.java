package com.qbk.sql.transaction.controller;

import com.qbk.sql.transaction.domain.TabUser;
import com.qbk.sql.transaction.service.TabUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 插件测试
 */
@RestController
@RequestMapping("/plug")
public class TestMybatisPlugController {

    @Autowired
    private TabUserService userService;

    @GetMapping("/test")
    public TabUser test(){
        TabUser user = userService.get(1);
        return user;
    }
}
