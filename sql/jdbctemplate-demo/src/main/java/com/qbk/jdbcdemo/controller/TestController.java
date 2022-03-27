package com.qbk.jdbcdemo.controller;

import com.qbk.jdbcdemo.service.MixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 86186 on 2022/2/19.
 */
@RestController
public class TestController {

    @Autowired
    private MixService mixService;

    /**
     * 混合查询
     */
    @GetMapping("/mix/query")
    public String mixQuery(){
        mixService.mixQuery();
        return "mix";
    }

    /**
     * 混合插入
     */
    @GetMapping("/mix/insert")
    public String mixInsert(){
        mixService.mixInsert();
        return "mix";
    }

    /**
     * 混合\嵌套事务
     */
    @GetMapping("/mix/nested")
    public String mixNested(){
        mixService.mixNested();
        return "mix";
    }
}
