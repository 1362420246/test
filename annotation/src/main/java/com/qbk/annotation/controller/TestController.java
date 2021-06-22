package com.qbk.annotation.controller;

import com.qbk.annotation.anno.QbkAnno;
import com.qbk.annotation.service.TestService;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/")
    public String get(){

        //$Proxy64
        //TestServiceImpl$$EnhancerBySpringCGLIB$$333fdd8b
        System.out.println(testService.getClass().getSimpleName());

        testService.fun();

        //反射工具类
        Reflections reflections = new Reflections("com.qbk.annotation.service");
        // 反射出子类
        Set<Class<? extends TestService>> subTypes = reflections.getSubTypesOf(TestService.class);
        for (Class<?> clazz : subTypes) {
            //获取注解值
            QbkAnno annotation = clazz.getAnnotation(QbkAnno.class);
            return annotation.value();
        }

        return "s";
    }
}
