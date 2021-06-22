package com.qbk.source.controller;

import com.qbk.source.domain.TabUser;
import com.qbk.source.service.ReadWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 读写分离控制器
 */
@RestController
public class ReadWriteController {

    @Autowired
    private ReadWriteService readWriteService;

    /**
     * 测试读
     */
    @GetMapping("/read")
    public List<TabUser> read(){
        return readWriteService.read();
    }

    /**
     * 测试写
     */
    @GetMapping("/write")
    public List<TabUser> write(){
        TabUser tabUser = new TabUser();
        tabUser.setUserName("qbk");
        return readWriteService.write(tabUser);
    }

    /**
     * 测试切换
     */
    @GetMapping("/slave")
    public List<TabUser> slave(){
        return readWriteService.slave();
    }

    /**
     * 测试事务
     */
    @GetMapping("/transaction")
    public String transaction(){
        readWriteService.transaction();
        return "S";
    }
}
