package com.qbk.timerjob.netty;

import io.netty.util.HashedWheelTimer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * HashedWheelTimer
 * 任务的执行都是通过worker单线程执行，如过时间格子长度设置过小而任务执行时间较长，则会导致执行的时间不精确。因此可以将任务异步的执行可以加速任务的遍历。
 */
public class HashedWheelTimerTest {

    public static void main(String[] args) throws Exception {
        test2();
    }

    private static void test1() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        /*
          ThreadFactory threadFactory, // 用来创建worker线程
          long tickDuration, // tick的时长，也就是指针多久转一格
          TimeUnit unit, // tickDuration的时间单位
          int ticksPerWheel, // 一圈有几格
          boolean leakDetection // 是否开启内存泄露检测
         */
        HashedWheelTimer hashedWheelTimer =
                new HashedWheelTimer(Executors.defaultThreadFactory(),1, TimeUnit.SECONDS,6);
        System.out.println("start:" + LocalDateTime.now().format(formatter));

        hashedWheelTimer.newTimeout(timeout -> {
            System.out.println("task :" + LocalDateTime.now().format(formatter));
        }, 3, TimeUnit.SECONDS);

        hashedWheelTimer.newTimeout(timeout -> {
            System.out.println("task :" + LocalDateTime.now().format(formatter));
        }, 5, TimeUnit.SECONDS);

        Thread.sleep(8000);

        hashedWheelTimer.stop();
    }

    private static void test2() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS);
        System.out.println("start:" + LocalDateTime.now().format(formatter));

        hashedWheelTimer.newTimeout(timeout -> {
            Thread.sleep(3000);
            System.out.println("task1:" + LocalDateTime.now().format(formatter));
        }, 3, TimeUnit.SECONDS);

        //当前一个任务执行时间过长的时候，会影响后续任务的到期执行时间的。也就是说其中的任务是串行执行的。所以，要求里面的任务都要短平快。
        hashedWheelTimer.newTimeout(timeout -> System.out.println("task2:" + LocalDateTime.now().format(
                formatter)), 4, TimeUnit.SECONDS);

        Thread.sleep(10000);

        hashedWheelTimer.stop();
    }
}
