package com.qbk.guavademo.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * guava 事件机制
 */
public class Test {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        //消息总线
        EventBus eb = new AsyncEventBus(executorService);

        GuavaListener gl = new GuavaListener();

        //注册观察者;
        eb.register(gl);
        //发布者发布消息;
        eb.post("Hello world!");

        executorService.shutdown();
    }
}
