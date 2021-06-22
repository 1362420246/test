package com.qbk.thread;

/**
 * 用join()实现线程按顺序执行
 */
public class ThreadByOrder1 {
    static Thread t1 = new Thread(() -> System.out.println("t1"));

    static Thread t2 = new Thread(() -> {
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("t2");
    });

    static Thread t3 = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t3");
        }
    });

    public static void main(String[] args) {
        t1.start();
        t2.start();
        t3.start();
    }
}
