package com.qbk.sql.transaction.controller;

import com.qbk.sql.transaction.domain.TabUser;
import com.qbk.sql.transaction.mapper.TabUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BatchController {

    @Autowired
    private TabUserMapper userMapper;

    @GetMapping("/update")
    public String update(){
        List<TabUser> list = new ArrayList<>();
        list.add(new TabUser(1,"zz"));
        list.add(new TabUser(2,"zz2"));
        int i = userMapper.updateBatch(list);
        return i + "";
    }
}
