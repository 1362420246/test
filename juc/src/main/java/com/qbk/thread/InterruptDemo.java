package com.qbk.thread;

import java.util.concurrent.TimeUnit;

/**
 * 线程的中断 与复位
 *
 * thread.interrupt() 中断标志
 *
 * Thread.currentThread().isInterrupted() 返回中断标志
 *
 * Thread.interrupted() 复位
 *
 * InterruptedException 复位 并抛出异常
 *
 */
class InterruptDemo {
    private static int i;
    public static void main(String[] args) throws Exception {
        //测试中断
        //testInterrupt();

        //测试复位
        //testInterrupted();

        //测试 InterruptedException 异常
        testInterruptedException();
    }

    /**
     * 测试中断
     */
    private static void testInterrupt() throws InterruptedException {
        Thread thread = new Thread(() -> {
            //默认情况下 isInterrupted 返回 false、通过 thread.interrupt 变成了 true
            while (!Thread.currentThread().isInterrupted()) { //false
                i++;
            }
            System.out.println("Num:" + i);
        }, "interrupt");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        //中断子线程
        thread.interrupt();//true
    }

    /**
     * 测试复位异常
     */
    private static void testInterrupted() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) { //false
                if (Thread.currentThread().isInterrupted()) {//true
                    System.out.println("before:" + Thread.currentThread().isInterrupted());
                    //对线程进行复位，由 true 变成 false
                    Thread.interrupted();
                    System.out.println("after:" + Thread.currentThread().isInterrupted());
                }
            }
        }, "interrupted");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        //中断子线程
        thread.interrupt();//true
    }

    /**
     * 测试 InterruptedException 异常
     */
    private static void testInterruptedException() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) { //false
                try {
                    Thread.sleep(2000000);
                    System.out.println("因为中断。所以这句话不会被执行");
                } catch (InterruptedException e) { //false
                    Thread.currentThread().interrupt();//true
                    System.out.println("再次中断");
                }
            }
        }, "interruptedException");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        //中断子线程
        thread.interrupt();//true
    }
}