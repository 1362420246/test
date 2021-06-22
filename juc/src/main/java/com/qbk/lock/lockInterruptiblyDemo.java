package com.qbk.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁可中断
 * lockInterruptibly()
 * 可以中断等待锁的线程
 *
 * lock.isHeldByCurrentThread() lock锁是否被当前线程所持有
 */
public class lockInterruptiblyDemo {

    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock(true);

    public static void main(String[] args) throws InterruptedException {

        final Thread thread1 = new Thread(() -> {
            try {
                lock1.lockInterruptibly();

                Thread.sleep(1000);

                lock2.lockInterruptibly();
                System.out.println(Thread.currentThread().getName() + "，完成。");
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(Thread.currentThread().getName() + "，失败。");
            }finally {
                // 查询当前线程是否保持此锁。
                if (lock1.isHeldByCurrentThread()) {
                    lock1.unlock();
                }
                if (lock2.isHeldByCurrentThread()) {
                    lock2.unlock();
                }
                System.out.println(Thread.currentThread().getName() + "，退出。");
            }
        }, "thread1");

        final Thread thread2 = new Thread(() -> {
            try {
                lock2.lockInterruptibly();

                Thread.sleep(1000);

                lock1.lockInterruptibly();
                System.out.println(Thread.currentThread().getName() + "，完成。");
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(Thread.currentThread().getName() + "，失败。");
            }finally {
                if (lock2.isHeldByCurrentThread()) {
                    lock2.unlock();
                }
                if (lock1.isHeldByCurrentThread()) {
                    lock1.unlock();
                }
                System.out.println(Thread.currentThread().getName() + "，退出。");
            }
        },"thread2");


        thread1.start();
        thread2.start();

        Thread.sleep(3000);
        thread2.interrupt(); // 中断线程2

        Thread.sleep(1000);
        System.out.println("结束");
    }
}
