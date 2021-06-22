package com.qbk.sql.transaction.controller;

import com.qbk.sql.transaction.service.SynAndTranstactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 锁与事务
 */
@RestController
public class SynAndTranstactionController {

    @Autowired
    private SynAndTranstactionService synAndTranstactionService;

    /**
     * 测试方法锁  能不能锁住事务
     * 场景 先查询 再插入。 是否能插入多条
     */
    @GetMapping("/synAndTrans")
    public String selectAndInsert(){
        System.out.println("断点处");
        String result = null;
        //锁在事务外面 是线程安全的。
        //synchronized(Object.class){
            result = synAndTranstactionService.selectAndInsert();
        //}
        return result;
    }
}
