package com.qbk.spring.demo.retry.controller;

import com.qbk.spring.demo.retry.service.RetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetryController {

    @Autowired
    private RetryService retryService;

    @RequestMapping("/")
    public String get(@RequestParam(defaultValue = "0") int code) throws Exception {
        return retryService.get(code);
    }

}
