package com.qbk.service;

import org.springframework.stereotype.Service;

@Service
public class ConstructorService {
    public void into() {
        System.out.println("构造器注入");
    }
}
