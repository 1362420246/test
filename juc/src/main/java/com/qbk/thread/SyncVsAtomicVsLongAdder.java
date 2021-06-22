package com.qbk.thread;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * sync+long、Atomic、LongAdder性能比较
 * synchronized—重量级锁；Atomic—自旋锁(CAS)；LongAdder—分段自旋锁。因此该实验其实就是三种锁在不同场景下的性能对比。
 * 竞争不激烈，建议使用自旋锁。
 * 竞争激烈，建议使用重量级锁。
 * 循环次数越高，分段自旋锁的性能优势越明显。
 */
public class SyncVsAtomicVsLongAdder {
    private static long count1 = 0;
    private static AtomicLong count2 = new AtomicLong(0);
    private static LongAdder count3 = new LongAdder();

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10000];// 控制线程数
        int times = 100000;// 控制循环次数
        // sync+long
        Object lock = new Object();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    synchronized (lock) {
                        count1++;
                    }
                }
            });
        }
        long start = System.currentTimeMillis();
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        long end = System.currentTimeMillis();
        System.out.println("sync+long:" + count1 + ",time:" + (end - start));
        // AtomicLong
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    count2.incrementAndGet();
                }
            });
        }
        start = System.currentTimeMillis();
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        end = System.currentTimeMillis();
        System.out.println("AtomicLong:" + count2 + ",time:" + (end - start));
        // LongAdder
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    count3.increment();
                }
            });
        }
        start = System.currentTimeMillis();
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        end = System.currentTimeMillis();
        System.out.println("LongAdder:" + count3 + ",time:" + (end - start));
    }
}
