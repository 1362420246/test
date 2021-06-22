package com.qbk.sql.transaction.controller;

import com.qbk.sql.transaction.service.TabUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试查询加锁
 */
@RestController
public class ForUpdateController {
    @Autowired
    private TabUserService userService;

    @GetMapping("/for_update")
    public String forUpdate(@RequestParam(defaultValue = "kk") String userName ){
        userService.forUpdate(userName);
        return "s";
    }
}
