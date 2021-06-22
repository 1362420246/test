package com.qbk.bean.service.impl;

import com.qbk.bean.service.MyService;
import org.springframework.stereotype.Service;

@Service
public class MyServiceImpl implements MyService {
    @Override
    public void fun() {
        System.out.println("执行方法");
    }
}
