package com.qbk.boot.init.postconstruct2;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 多个 PostConstruct 加载顺序测试
 */
@Component
public class PostConstructD {
    public static Object D;
    @PostConstruct
    public void init(){
        D = "d";
        System.out.println("初始化D");
        System.out.println(PostConstructC.C);
    }
}
