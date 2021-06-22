package com.qbk.timerjob.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定时器堆积: 同一个周期任务，如果任务执行时间 大于 执行频率 。会造成 下一次任务执行的时间延后，导致执行频率变化。
 * 已知存在问题的定时器： ScheduledExecutorService 、 @Scheduled 、 xxl-job
 *
 * 测试 定时器 堆积
 * 如果 定时器 执行频率 2s ,但是任务耗时 5s ,为了不影响 执行频率 , 在执行器内部 使用线程池 异步执行任务
 * 问题: 如果一直执行下去 会不会 把线程池 用满
 * 结论: 只会使用 5/2 = 3个线程
 */
@Slf4j
@Component
public class TestJob {

    /**
     * 线程数
     */
    private final static int n = 3;

    /**
     *  自定义线程池工厂
     * 【强制】创建线程或线程池时请指定有意义的线程名称，方便出错时回溯。
     * 正例：自定义线程工厂，并且根据外部特征进行分组，比如，来自同一机房的调用，把机房编号赋值给
     */
    static class UserThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger nextId = new AtomicInteger(1);

        // 定义线程组名称，在利用 jstack 来排查问题时，非常有帮助
        UserThreadFactory(String whatFeatureOfGroup) {
            namePrefix = "From UserThreadFactory's " + whatFeatureOfGroup + "-Worker-";
        }

        @Override
        public Thread newThread(Runnable task) {
            String name = namePrefix + nextId.getAndIncrement();
            Thread thread = new Thread(  null ,task, name, 0);
            System.out.println(thread.getName());
            return thread;
        }
    }

    /**
     * 自定义线程拒绝策略
     * 默认策略 ThreadPoolExecutor.AbortPolicy()  该策略是线程池的默认策略。使用该策略时，如果线程池队列满了丢掉这个任务并且抛出RejectedExecutionException异常。
     */
    private static class MyRejectPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.info("触发拒绝策略 .Task: " + r.toString() +
                    " rejected from: " +
                    executor.toString());
        }
    }

    /**
     * 自定义线程池
     * SynchronousQueue没有容量
     */
    private ExecutorService executor = new ThreadPoolExecutor(
            n, //核心线程数量
            n, //最大线程数
            0L,  //超时时间,超出核心线程数量以外的线程空余存活时间
            TimeUnit.MILLISECONDS,  //存活时间单位
            new SynchronousQueue<>(), //保存执行任务的队列
            new UserThreadFactory("qbk"), //创建新线程使用的工厂
            new MyRejectPolicy()  //拒绝策略
    );

    /**
     * 定时任务
     */
    @Scheduled(cron = "*/2 * * * * ?")
    public void test(){
        LocalDateTime startTime = LocalDateTime.now();
        log.info("任务开始时间:{}" ,startTime);
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        executor.submit(
                ()->{
                    LocalDateTime startTimeSub = LocalDateTime.now();
                    log.info("子线程开始时间:{}" ,startTimeSub);
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
