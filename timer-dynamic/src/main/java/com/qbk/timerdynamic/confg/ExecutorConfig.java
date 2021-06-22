package com.qbk.timerdynamic.confg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ExecutorConfig {

    private final AtomicInteger nextId = new AtomicInteger(1);

    @Bean(destroyMethod="shutdown")
    public ExecutorService executorPoolService(){
        System.out.println("初始化线程池");
        //创建线程池
        return Executors.newScheduledThreadPool(5, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable task) {
                ThreadGroup threadGroup = new ThreadGroup("qbk-");
                String name = "test-" + nextId.getAndIncrement();;
                return new Thread(threadGroup, task, name, 0);
            }
        });
    }
}
