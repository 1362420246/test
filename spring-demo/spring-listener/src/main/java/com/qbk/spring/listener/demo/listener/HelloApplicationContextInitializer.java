package com.qbk.spring.listener.demo.listener;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * ApplicationContextInitializer是Spring框架原有的东西，
 * 这个类的主要作用就是在ConfigurableApplicationContext类型(或者子类型)的ApplicationContext做refresh之前，
 * 允许我们对ConfiurableApplicationContext的实例做进一步的设置和处理。
 *
 * ApplicationContextInitializer接口是在spring容器刷新之前执行的一个回调函数。
 * 是在ConfigurableApplicationContext#refresh() 之前调用（当spring框架内部执行 ConfigurableApplicationContext#refresh() 方法的时候
 * 或者在SpringBoot的run()执行时），作用是初始化Spring ConfigurableApplicationContext的回调接口。
 *
 * ApplicationContextInitializer是Spring框架原有的概念, 这个类的主要目的就是在ConfigurableApplicationContext类型（或者子类型）
 * 的ApplicationContext做refresh之前，允许我们对ConfigurableApplicationContext的实例做进一步的设置或者处理。
 * 通常用于需要对应用程序上下文进行编程初始化的web应用程序中。例如，根据上下文环境注册属性源或激活概要文件。
 *
 * 参考ContextLoader和FrameworkServlet中支持定义contextInitializerClasses作为context-param或定义init-param。
 * ApplicationContextInitializer支持Order注解，表示执行顺序，越小越早执行；
 */
public class HelloApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("ApplicationContextInitializer~~~");
    }
}
