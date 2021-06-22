package com.qbk.thread;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 用 LinkedBlockingQueue  实现线程按顺序执行
 */
public class ThreadByOrder4 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> System.out.println("t1"));
        Thread t2 = new Thread(() -> System.out.println("t2"));
        Thread t3 = new Thread(() -> System.out.println("t3"));

        LinkedBlockingQueue<Thread> threads = new LinkedBlockingQueue<>(3);
        threads.put(t1);
        threads.put(t2);
        threads.put(t3);

        while (threads.size() > 0){
            Thread thread = threads.take();
            thread.start();
        }
    }
}
