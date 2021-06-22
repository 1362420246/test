package com.qbk.annotation.service.impl;

import com.qbk.annotation.anno.QbkAnno;
import com.qbk.annotation.service.TestService;
import org.springframework.stereotype.Service;

@Service
@QbkAnno("我是qbk")
public class TestServiceImpl implements TestService {

    @Override
    @QbkAnno("你是qbk")
    public String fun() {
        System.out.println("s");
        return "s";
    }
}
