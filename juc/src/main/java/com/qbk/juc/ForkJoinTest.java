package com.qbk.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;


/**
 * Fork/Join  invokeAll方法 批量发送消息
 */
public class ForkJoinTest {

    /**
     * RecursiveAction  一个递归无结果的ForkJoinTask（没有返回值）
     * RecursiveAction -> ForkJoinTask -> Future
     */
    class SendMsgTask extends RecursiveAction {

        private final int THRESHOLD = 10;

        private int start;
        private int end;
        private List<String> list;

        public SendMsgTask(int start, int end, List<String> list) {
            this.start = start;
            this.end = end;
            this.list = list;
        }

        @Override
        protected void compute() {
            // 如果条件成立，说明这个任务所需要计算的数值分为足够小了
            if ((end - start) <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    System.out.println(Thread.currentThread().getName() + ": " + list.get(i));
                }
            }else {
                // 否则再进行任务拆分，拆分成两个任务
                //去中间值
                int middle = (start + end) / 2;
                //派生给定的任务
                //public static void invokeAll(ForkJoinTask<?> t1, ForkJoinTask<?> t2)
                ForkJoinTask.invokeAll(new SendMsgTask(start, middle, list), new SendMsgTask(middle, end, list));
            }

        }

    }

    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 233; i++) {
            list.add("消息"+i);
        }

        System.out.println("Java虚拟机可用的处理器数量:" + Runtime.getRuntime().availableProcessors());
        //一个运行ForkJoinTask的ExecutorService。 初始化默认线程数为 Java虚拟机可用的处理器数量 4
        //ForkJoinPool pool = new ForkJoinPool();

        //指定线程数
        ForkJoinPool pool = new ForkJoinPool(6);
        //提交一个forkjoint请求执行。
        pool.submit(new ForkJoinTest().new SendMsgTask(0, list.size(), list));
        //等待终止
        pool.awaitTermination(10, TimeUnit.SECONDS);
        pool.shutdown();
    }

}
