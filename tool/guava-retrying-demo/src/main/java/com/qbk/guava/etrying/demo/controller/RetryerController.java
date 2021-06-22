package com.qbk.guava.etrying.demo.controller;

import com.qbk.guava.etrying.demo.service.RetryerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetryerController {

    @Autowired
    private RetryerService retryerService;

    @GetMapping("/")
    public Boolean get() throws Exception {
        return retryerService.test();
    }
}
