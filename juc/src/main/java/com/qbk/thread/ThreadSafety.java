package com.qbk.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程安全问题
 */
public class ThreadSafety {

    public volatile static int i = 0;

//    public volatile static AtomicInteger i = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {

        //ExecutorService executorService = Executors.newFixedThreadPool(50);
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int j = 0; j < 1000; j++) {
//            i++;
            executorService.execute(() -> i++);
//            executorService.execute(() -> i.getAndIncrement());
        }

        TimeUnit.SECONDS.sleep(2);
        System.out.println(i);

        executorService.shutdown();
    }
}
