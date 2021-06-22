package com.qbk.lock;

import java.util.concurrent.TimeUnit;

/**
 * synchronized 与 interrupt 中断
 *
 * 只有获取到锁之后才能中断，等待锁时不可中断。
 * 不可中断的意思是等待获取锁的时候不可中断，拿到锁之后可中断，没获取到锁的情况下，中断操作一直不会生效
 */
public class SynAndInterrup {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        Thread t1 = new Thread(
                ()-> {
                    synchronized (lock){
                        System.out.println("进入临界区");
                        try {
                            TimeUnit.SECONDS.sleep(5);
                            //lock.wait();
                            System.out.println("线程没有被中断");
                        } catch (InterruptedException e) {
                            System.out.println("线程被中断了");
                        }
                    }
                    System.out.println("已经出了临界区");
                }
        );
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("中断线程");
        t1.interrupt();
    }
}
