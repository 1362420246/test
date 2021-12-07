package com.qbk.boot.init.postconstruct2;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 多个 PostConstruct 加载顺序测试
 *
 * 如果没有地方注入这个bean ，Lazy会导致 PostConstruct方法都不执行
 */
@Lazy
@Component
public class PostConstructC {

    public static Object C;

    @PostConstruct
    public void init(){
        C = "c";
        System.out.println("初始化C");
        System.out.println(PostConstructD.D);
    }
}
