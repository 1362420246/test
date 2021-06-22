package com.qbk.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition 线程通信
 */
public class ConditionDemo {

    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(
                ()->{
                    System.out.println("线程阻塞");
                    lock.lock();
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        lock.unlock();
                    }
                    System.out.println("线程结束");
                }
        );
        Thread t2 = new Thread(
                ()->{
                    lock.lock();
                    try {
                        System.out.println("线程唤醒");
                        condition.signal();
                    } finally {
                        lock.unlock();
                    }
                }
        );
        t1.start();
        TimeUnit.SECONDS.sleep(3);
        t2.start();
    }
}
