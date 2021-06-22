package com.qbk.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 *  4.TransmittableThreadLocal
 *
 * 解决 InheritableThreadLocal 在线程池中 无法传递值的问题
 */
public class TransmitTableThreadLocalTest {

    //alibaba 的 ThreadLocal
    static TransmittableThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();
    static ExecutorService pool = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(5));

    //如果采用jdk的threadLocal 难以保证在线程池执行的时候变量的传递
//    static ThreadLocal<String> threadLocal = new ThreadLocal<>();
//    static ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
//    static ExecutorService pool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws InterruptedException {
        //用来获取线程池里面的线程  然后拿到线程 阻塞/唤醒
        ConcurrentHashMap<String,Thread> threadMap = new ConcurrentHashMap<>();

        //保证线程池里面的所有线程 都是已经初始化好的
        //不然新创建的线程 使用InheritableThreadLocal 也可以传递
        //保证 传递threadlocal时候  都是已经都是线程池里面存在的线程
        for (int i = 0; i < 5; i++) {
            pool.execute(()-> System.out.println(Thread.currentThread().getName()));
        }
        Thread.sleep(1000);
        System.out.println("线程池中的线程全部初始化结束!");

        pool.execute(new Runnable() {
            @Override
            public void run() {
                //父线程 传递值
                threadLocal.set("qbk");

                //父线程 获取值
                System.out.println(Thread.currentThread().getName() + ":父线程:" + threadLocal.get());

                //子线程
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        //子线程 获取值
                        System.out.println(Thread.currentThread().getName() + ":子线程:" + threadLocal.get());

                        threadMap.put("son",Thread.currentThread());
                        LockSupport.park();
                    }
                });

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                LockSupport.unpark(threadMap.get("son"));
            }
        });

        Thread.sleep(100);

        //非父子线程 无法 继承 传递
        pool.execute(new Runnable() {
                         @Override
                         public void run() {
                             //获取值
                             System.out.println(Thread.currentThread().getName() + ":" + threadLocal.get());
                         }
                     }
        );

        Thread.sleep(1000);
        pool.shutdown();
    }
}
