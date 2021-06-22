package com.qbk.sql.mybatisplus.controller;

import com.qbk.sql.mybatisplus.domain.TabUser;
import com.qbk.sql.mybatisplus.service.TabUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试事务隔离级别
 */
@RestController
public class TestController {

    @Autowired
    private TabUserService userService;

    /**
     * 测试事务隔离级别
     */
    @GetMapping("/test/transaction")
    public TabUser testTransaction(@RequestParam Integer userId){
        TabUser user = TabUser.builder().userId(userId).userName("卡卡").build();
        user = userService.testTransaction(user);
        return user;
    }

    /**
     * 测试事务嵌套
     */
    @GetMapping("/test/add")
    public String testAdd(){
        userService.testAdd();
        return "s";
    }

    /**
     * 测试 insert ignore 返回行数
     */
    @GetMapping("/test/ignore")
    public String testIgnore(String username){
        int n = userService.testIgnore(username);
        return "count:" + n;
    }
}
