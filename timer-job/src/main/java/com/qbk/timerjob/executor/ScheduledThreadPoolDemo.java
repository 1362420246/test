package com.qbk.timerjob.executor;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ScheduledExecutorService
 * <p>
 * 关于定时线程池，好多人认为设置好频率（比如1Min），它会按照这个间隔按部就班的工作。但是，如果其中一次调度任务卡住的话，不仅这次调度失败，而且整个线程池也会停在这次调度上。
 */
public class ScheduledThreadPoolDemo {

    /**
     * 自定义线程池工厂
     * 【强制】创建线程或线程池时请指定有意义的线程名称，方便出错时回溯。
     * 正例：自定义线程工厂，并且根据外部特征进行分组，比如，来自同一机房的调用，把机房编号赋值给
     */
    static class MyScheduledThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger nextId = new AtomicInteger(1);
        private ThreadGroup threadGroup;

        /**
         * 定义线程组名称，在利用 jstack 来排查问题时，非常有帮助
         */
        MyScheduledThreadFactory(String whatFeatureOfGroup) {
            namePrefix = whatFeatureOfGroup + "-Worker-";
            threadGroup = new ThreadGroup(whatFeatureOfGroup);
        }

        @Override
        public Thread newThread(Runnable task) {
            String name = namePrefix + nextId.getAndIncrement();
            Thread thread = new Thread(threadGroup, task, name, 0);
            return thread;
        }
    }

    /**
     * 线程数
     */
    static final int DEFAULT_THREAD_COUNT;

    static {
        DEFAULT_THREAD_COUNT = Runtime.getRuntime().availableProcessors() > 1 ? Runtime.getRuntime().availableProcessors() / 2 : 1;
    }

    /**
     * ScheduledExecutorService 线程池
     */
    static ScheduledExecutorService scheduled =
            Executors.newScheduledThreadPool(
                    DEFAULT_THREAD_COUNT,
                    new MyScheduledThreadFactory("qbk")
            );


    public static void main(String[] args) {
        /*
           ScheduledExecutorService 四个方法：

           public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit);

           public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);

           public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                  long initialDelay,
                                                  long period,
                                                  TimeUnit unit);

           public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     TimeUnit unit);
         */

//        schedule();
        scheduleAtFixedRate();
//        scheduleWithFixedDelay();

        System.out.println(LocalDateTime.now() + "->    " + Thread.currentThread().getName() + "执行结束");
    }

    /**
     * schedule
     * 只执行一次  的定时任务
     */
    private static void schedule() {
        for (int i = 0; i < 3; i++) {
            ScheduledFuture<String> future = scheduled.schedule(
                    () -> {
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println(LocalDateTime.now() + "->    " + Thread.currentThread().getName() + "执行结束");
                        return "这是执行结果";
                    },
                    3,
                    TimeUnit.SECONDS);

            new Thread(() -> {
                try {
                    //因为获取结果为阻塞 所以放到子线程中
                    final String result = future.get();
                    System.out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //关闭线程
                    future.cancel(false);
                    scheduled.shutdown();
                }
            }).start();
        }
    }

    /**
     * scheduleAtFixedRate  周期性定时任务
     * <p>
     * command：执行线程
     * initialDelay：初始化延时
     * period：两次开始执行最小间隔时间
     * unit：计时单位
     */
    private static void scheduleAtFixedRate() {
        //如果 任务耗时大于 执行间隔。 执行间隔 变成 任务耗时
        scheduled.scheduleAtFixedRate(
                () -> {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(LocalDateTime.now() + "->    " + Thread.currentThread().getName() + "执行结束");
                },
                3,
                3,
                TimeUnit.SECONDS
        );
    }

    /**
     * scheduleWithFixedDelay  周期性定时任务
     * <p>
     * command：执行线程
     * initialDelay：初始化延时
     * period：前一次执行结束到下一次执行开始的间隔时间（间隔执行延迟时间）
     * unit：计时单位
     */
    private static void scheduleWithFixedDelay() {
        //执行间隔 = 执行间隔 + 任务耗时
        scheduled.scheduleWithFixedDelay(
                () -> {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(LocalDateTime.now() + "->    " + Thread.currentThread().getName() + "执行结束");
                },
                3,
                3,
                TimeUnit.SECONDS
        );
    }
}
