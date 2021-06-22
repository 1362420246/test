package com.qbk.thread;

import java.util.concurrent.*;

/**
 * 线程的创建
 */
public class ThreadCreate {
    public static void main(String[] args) {

        //1、 Thread
        new Thread("qbk-1"){
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        }.start();

        //2、 Runnable
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        },"qbk-2").start();

        //3、 Callable + FutureTask
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName());
                return Thread.currentThread().getName();
            }
        };
        //class FutureTask<V> implements RunnableFuture<V>
        //interface RunnableFuture<V> extends Runnable, Future<V>
        FutureTask<String> futureTask = new FutureTask<String>(callable);
        new Thread(futureTask,"qbk-3").start();
        try {
            final String s = futureTask.get();
            System.out.println(s);
            final boolean cancel = futureTask.cancel(true);
            System.out.println(cancel);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        ExecutorService executor = Executors.newFixedThreadPool(5);

        //4、ExecutorService + Runnable
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        });

        //5、ExecutorService + Runnable + Future
        Future<?> future =  executor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        });
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //6、ExecutorService + Callable + Future
        Future<String> future2 =  executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName());
                return Thread.currentThread().getName();
            }
        });
        try {
            System.out.println(future2.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName());

        executor.shutdown();
    }
}
