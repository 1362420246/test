package com.qbk.boot.init.postconstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 多个 PostConstruct 加载顺序测试
 */
@Component
public class PostConstructA {

    public static Object a;

    /**
     * 注入b 可导致 b的初始化早于 a ，从而达到 b的PostConstruct 方法早于 a的PostConstruct的效果。
     * 去掉注入 按照顺序加载 a初始化早于b  所以 a的PostConstruct 早于 b的PostConstruct
     */
    @Autowired
    private PostConstructB postConstructB;

    @PostConstruct
    public void init(){
        a = "a";
        System.out.println("初始化A");
        System.out.println(PostConstructB.b);
    }
}
