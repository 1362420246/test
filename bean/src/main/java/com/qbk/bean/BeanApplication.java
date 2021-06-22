package com.qbk.bean;

import org.springframework.scheduling.annotation.EnableAsync;
import 循环依赖.异步循环依赖.A4;
import 循环依赖.异步循环依赖.B4;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 测试4种情况的springbean 循环依赖
 * 测试2种aop和无aop情况下getBean获取对象情况
 * 测试多种 Aware、BeanPostProcessor、生命周期 扩展点执行顺序
 */
@EnableAsync
@SpringBootApplication(
//        scanBasePackageClasses = {A4.class, B4.class}
//        scanBasePackageClasses = {A.class,B.class,A2.class ,B2.class}
//        scanBasePackageClasses = {A3.class ,B3.class}
//        scanBasePackageClasses = {A1.class ,B1.class,C1.class}
)
public class BeanApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeanApplication.class, args);
    }

}
