package com.qbk.service;

import com.qbk.properties.TestProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 测试  @Autowired 、 构造方法 、 构造代码块的执行顺序
 */
@Service
//@Lazy(value = false)
public class TestService {

    @Autowired
    private TestProperties testProperties;
    @Autowired
    private ConstructorService constructorService;
    {
        System.out.println(testProperties);
        System.out.println(constructorService);
        System.out.println(1);
    }
    public TestService(){
        System.out.println(testProperties);
        System.out.println(constructorService);
        System.out.println(name);
        System.out.println(3);
    }
    private String name = testProperties == null ? "11" : "22" ;
    {
        System.out.println(name);
        name = "33";
        System.out.println(2);
    }
    @PostConstruct
    private void init(){
        System.out.println(testProperties);
        System.out.println(constructorService);
        System.out.println(4);
    }

    public void into() {
        System.out.println(testProperties);
        System.out.println(name);
        System.out.println("测试自动注入");
    }
}
