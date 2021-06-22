package com.qbk.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁
 */
public class SpinLock {

    private AtomicReference<Thread> cas = new AtomicReference<>();

    public void lock() {
        Thread current = Thread.currentThread();
        // 利用CAS
        while (!cas.compareAndSet(current, null)) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "获取自旋锁失败，再次尝试");
        }
        System.out.println(Thread.currentThread().getName() + "获取自旋锁成功");
    }
    public void unlock(Thread thread) {
        cas.compareAndSet(null,thread );
    }

    public static void main(String[] args) throws InterruptedException {
        SpinLock spinLock = new SpinLock();

        final Thread thread = new Thread(() -> {
            spinLock.lock();
        }, "t");

        thread.start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        spinLock.unlock(thread);
    }
}
