package com.qbk.webflux.streamdemo;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 并行流demo
 */
public class ParallelStreamDemo {

    public static void main(String[] args) {
        //1.串行流 1-9  每个元素都调用一个函数，最后得出总数
//        long count = IntStream.range(1, 10).peek(ParallelStreamDemo::debug).count();
//        System.out.println(count);

        //2.并行流 parallel
        //使用的是 ForkJoinPool.commonPool 线程池
        //默认的并发线程数是 当前机器的cpu个数
        //使用这个属性可以修改默认的线程数
//        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism","8");
//        long count2 = IntStream.range(1, 50).parallel().peek(ParallelStreamDemo::debug).count();
//        System.out.println(count2);

        //3.使用自己的线程池，不适用默认线程池，防止任务阻塞
        ForkJoinPool pool = new ForkJoinPool(20);
        pool.submit(()->IntStream.range(1, 50).parallel().peek(ParallelStreamDemo::debug).count());
        pool.shutdown();
        //等待下
        synchronized (pool){
            try {
                pool.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void debug(int i) {
        System.out.println(Thread.currentThread().getName() + " : debug:"+i);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
