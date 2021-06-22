package com.qbk.lock;

import java.util.concurrent.locks.LockSupport;

/**
 *  阻塞/唤醒
 *  LockSupport.park();
 *  LockSupport.unpark();
 */
public class LockSupportDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("睡觉");
            LockSupport.park(); // 阻塞
            System.out.println("起床");
            System.out.println("是否中断：" + Thread.currentThread().isInterrupted());
        });
        t.setName("A-Name");
        t.start();
        Thread.sleep(3000);
        System.out.println("妈妈喊我起床");
        //t.interrupt(); //中断
        LockSupport.unpark(t); //唤醒
    }
}