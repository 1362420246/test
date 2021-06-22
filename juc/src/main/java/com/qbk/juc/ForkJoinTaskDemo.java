package com.qbk.juc;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 *  Fork/Join 求和
 *
 *  fork方法 + join方法
 */
public class ForkJoinTaskDemo {

    /**
     * 一个递归有结果的ForkJoinTask（有返回值）
     * RecursiveTask -> ForkJoinTask -> Future
     */
    private class SumTask extends RecursiveTask<Integer> {

        private static final int THRESHOLD = 20;

        private int arr[];
        private int start;
        private int end;

        public SumTask(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        /**
         * 小计
         */
        private Integer subtotal() {
            Integer sum = 0;
            for (int i = start; i < end; i++) {
                sum += arr[i];
            }
            System.out.println(Thread.currentThread().getName() + ": ∑(" + start + "~" + end + ")=" + sum);
            return sum;
        }

        @Override
        protected Integer compute() {
            // 如果条件成立，说明这个任务所需要计算的数值分为足够小了
            if ((end - start) <= THRESHOLD) {
                return subtotal();
            }else {
                // 否则再进行任务拆分，拆分成两个任务
                int middle = (start + end) / 2;
                SumTask left = new SumTask(arr, start, middle);
                SumTask right = new SumTask(arr, middle, end);
                //在当前线程运行的线程池中安排一个异步执行。简单的理解就是再创建一个子任务。
                left.fork();
                right.fork();
                //当任务完成的时候返回计算结果。
                return left.join() + right.join();
            }
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int arrLenth = 1000;
        int[] arr = new int[arrLenth];
        for (int i = 0; i < arrLenth; i++) {
            arr[i] = i + 1;
        }
        //一个运行ForkJoinTask的ExecutorService。 初始化默认线程数为 Java虚拟机可用的处理器数量 4
        //ForkJoinPool pool = new ForkJoinPool();

        /**
         * @parallelism 并发数
         * @factory ForkJoinWorkerThread}的工厂。
         * @handler 内部工作线程的处理程序
         * @asyncMode 异步模式（任务队列出队模式 异步：先进先出，同步：后进先出）
         */
        ForkJoinPool pool = new ForkJoinPool(5,ForkJoinPool.defaultForkJoinWorkerThreadFactory,null,true);

        //提交一个forkjoint请求执行。
        ForkJoinTask<Integer> result = pool.submit(new ForkJoinTaskDemo().new SumTask(arr, 0, arr.length));
        //返回结果
        System.out.println("最终计算结果: " + result.invoke());
        pool.shutdown();
    }

}