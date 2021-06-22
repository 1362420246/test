package com.qbk.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用 newSingleThreadExecutor  实现线程按顺序执行
 */
public class ThreadByOrder3 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> System.out.println("t1"));
        Thread t2 = new Thread(() -> System.out.println("t2"));
        Thread t3 = new Thread(() -> System.out.println("t3"));

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(t1);
        executorService.execute(t2);
        executorService.execute(t3);

        executorService.shutdown();

    }
}
