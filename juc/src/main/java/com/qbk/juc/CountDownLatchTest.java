package com.qbk.juc;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 同步计数器
 */
public class CountDownLatchTest {

    public static void main (String[] args) throws ExecutionException, InterruptedException {
        int count = 10 ;
        final ExecutorService executorService = Executors.newCachedThreadPool();
        //同步计数器
        final CountDownLatch countDownLatch = new CountDownLatch(count);
        Random random = new Random();
        //执行10个任务线程
        for (int i = 0; i< count ;i++) {
            final Future<String> submit = executorService.submit(() -> {
                //模拟不同的耗时
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(Thread.currentThread().getName() + "执行完成");
                //计数减一
                countDownLatch.countDown();
                return "s" ;
            });
        }
        //一个监测线程
        new Thread(() -> {
            try {
                //阻塞等待，计数器为0时才往下执行
                countDownLatch.await();
                //使当前线程在计数器倒计时至0前一直等待，除非被中断或超时。
                //countDownLatch.await(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "等待所有线程执行完后再执行");
            //关闭线程池
            executorService.shutdown();
        }).start();
    }
}
