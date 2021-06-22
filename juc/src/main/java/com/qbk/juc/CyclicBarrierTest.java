package com.qbk.juc;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 栅栏
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        fun2();
    }

    /**
     * 1.多个线程之间互相等待
     * 每个写入线程执行完写数据操作之后，就在等待其他线程写入操作完毕。
     * 当所有线程线程写入操作完毕之后，所有线程就继续进行后续的操作了。
     */
    private static void fun1(){
        int n = 4;
        //参数parties指让多少个线程或者任务等待至barrier状态；
        CyclicBarrier barrier  = new CyclicBarrier(n);
        Random random = new Random();
        for(int i=0;i<n;i++) {
            new Writer(barrier,random).start();
        }
    }
    /**
     * 2.在所有线程写入操作完之后，进行额外的其他操作可以为CyclicBarrier提供Runnable参数
     */
    private static void fun2(){
        int n = 4;
        //参数parties指让多少个线程或者任务等待至barrier状态；
        //参数barrierAction为当这些线程都达到barrier状态时会执行的内容。
        //当四个线程都到达barrier状态后，会从四个线程中选择一个线程去执行Runnable。
        CyclicBarrier barrier  = new CyclicBarrier(n,
                () -> System.out.println("当前线程"+Thread.currentThread().getName()));
        Random random = new Random();
        for(int i=0;i<n;i++) {
            new Writer(barrier,random).start();
        }
    }

    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;
        private Random random;
        Writer(CyclicBarrier cyclicBarrier, Random random) {
            this.cyclicBarrier = cyclicBarrier;
            this.random = random;
        }
        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                //以睡眠来模拟写入数据操作
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                //当调用await()方法之后，线程就处于barrier了。
                cyclicBarrier.await();
                //超时等待
                // cyclicBarrier.await(1,TimeUnit.SECONDS);
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+":所有线程写入完毕，继续处理其他任务...");
        }
    }



}
