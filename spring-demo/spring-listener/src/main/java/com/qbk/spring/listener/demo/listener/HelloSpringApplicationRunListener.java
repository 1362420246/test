package com.qbk.spring.listener.demo.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * SpringApplicationRunListener 类是 SpringBoot 中新增的类。
 * SpringApplication 类 中使用它们来间接调用 ApplicationListener。
 * 另外还有一个新增的类是SpringApplicationRunListeners，SpringApplicationRunListeners 中包含了多个 SpringApplicationRunListener。
 */
public class HelloSpringApplicationRunListener implements SpringApplicationRunListener {

    private final SpringApplication application;
    private final String[] args;

    /**
     * 必须有的构造器
     * 必须要在spring.factories注入
     */
    public HelloSpringApplicationRunListener(SpringApplication sa, String[] args) {
        this.application = sa;
        this.args = args;
    }

    /**
     * 当run方法首次启动时立即调用。可用于非常早期的初始化。
     */
    @Override
    public void starting() {
        System.out.println("SpringApplicationRunListener - staring");
    }

    /**
     * 在准备好环境后，但在创建ApplicationContext之前调用。
     */
    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        System.out.println("SpringApplicationRunListener - environmentPrepared");
    }

    /**
     * 在创建和准备好ApplicationContext之后，但在加载源之前调用。
     * @param context
     */
    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener - contextPrepared");
    }

    /**
     * 在加载应用程序上下文后但刷新之前调用
     * @param context
     */
    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener - contextLoaded");
    }

    /**
     * 上下文已刷新，应用程序已启动，但尚未调用commandlinerunner和applicationrunner。
     * @param context
     */
    @Override
    public void started(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener - started");
    }

    /**
     * 在运行方法完成之前立即调用，此时应用程序上下文已刷新，
     * 并且所有commandlinerunner和applicationrunner都已调用。
     * @param context
     */
    @Override
    public void running(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener - running");
    }

    /**
     * 在运行应用程序时发生故障时调用。
     * @param context
     * @param exception
     */
    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("SpringApplicationRunListener - failed");
    }
}
