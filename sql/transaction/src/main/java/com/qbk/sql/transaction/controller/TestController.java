package com.qbk.sql.transaction.controller;

import com.qbk.sql.transaction.domain.TabUser;
import com.qbk.sql.transaction.service.TabUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/test/transaction")
    public TabUser testTransaction(@RequestParam Integer userId){
        TabUser user = TabUser.builder().userId(userId).userName("卡卡").build();
        user = userService.testTransaction(user);
        return user;
    }
}
