package com.qbk.threadpool;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时器线程池
 */
public class ScheduledThreadPoolExecutorDemo {

    static ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(5);

    static Random random = new Random();

    public static void main(String[] args) {
        //5个线程 执行 10个任务
        for (int i = 0; i < 10; i++) {
            //定时 5秒以后 执行。
            scheduled.schedule(()->{
                long time = System.currentTimeMillis();
                LocalDateTime now = LocalDateTime.now();
                System.out.println(Thread.currentThread().getName() + "开始，时间是：" + now);
                int stochastic = random.nextInt(3);
                try {
                    //任务内的耗时
                    TimeUnit.SECONDS.sleep(stochastic);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "结束,随机时间是：" + stochastic + "。用时:" + (System.currentTimeMillis() - time) );
            },5, TimeUnit.SECONDS);
        }
        scheduled.shutdown();
        System.out.println("主线程结束了,时间是：" + LocalDateTime.now());
    }
}
