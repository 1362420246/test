package com.qbk.boot.init.postconstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *  多个 PostConstruct 加载顺序测试
 */
@Component
public class PostConstructB {

    public static Object b;

    @PostConstruct
    public void init(){
        b = "b";
        System.out.println("初始化B");
        System.out.println(PostConstructA.a);
    }
}
