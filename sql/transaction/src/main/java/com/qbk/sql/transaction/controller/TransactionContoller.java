package com.qbk.sql.transaction.controller;

import com.qbk.sql.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  事务 嵌套、捕获异常
 */
@RestController
@RequestMapping("/tran")
public class TransactionContoller {

    @Autowired
    private TransactionService transactionService;
    /**
     * 正常插入
     */
    @GetMapping("/insert")
    public String insertUser(){
        transactionService.insertUser();
        return "正常插入";
    }

    /**
     * 抛出异常
     */
    @GetMapping("/throw")
    public String throwException(){
        transactionService.innerException();
        return "抛出异常";
    }

    /**
     *  捕获内层异常 - 嵌套事务
     */
    @GetMapping("/catch")
    public String catchException(){
        transactionService.catchException();
        return "捕获异常";
    }

    /**
     *  外层异常 - 嵌套事务
     */
    @GetMapping("/outer")
    public String outerException(){
        transactionService.outerException();
        return "外层异常";
    }
}
