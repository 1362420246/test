package com.qbk.lockweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 把请求任务分多个子线程并发处理
 */
@RestController
public class SeparatelyController {

    ExecutorService exec = Executors.newCachedThreadPool();

    /**
     * 0、Future get
     */
    @GetMapping("/get")
    public List<String> get() throws Exception {
        long start = System.currentTimeMillis();

        List<Callable<String>> tasks = new ArrayList<>();
        Callable<String> task1 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.SECONDS.sleep(2);
                return "result1";
            }
        };
        Callable<String> task2 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.SECONDS.sleep(5);
                return "result2";
            }
        };
        Callable<String> task3 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.SECONDS.sleep(3);
                return "result3";
            }
        };
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        List<Future<String>> futures = new ArrayList<>();
        for (Callable<String> task : tasks) {
            //submit
            Future<String> future = exec.submit(task);
            futures.add(future);
        }

        List<String> results = futures.stream().map(future -> {
                    try {
                        // get
                        return future.get();
                    } catch (Exception e) {
                        return e.getMessage();
                    }
                }
        ).collect(Collectors.toList());

        long end = System.currentTimeMillis();
        System.out.println("Future get 总耗时:"+( end - start));
        return results;
    }

    /**
     * 1、线程池的 invokeAll方法
     */
    @GetMapping("/invokeAll")
    public List<String> invokeAll() throws Exception {
        long start = System.currentTimeMillis();

        List<Callable<String>> tasks = new ArrayList<>();
        Callable<String> task1 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.SECONDS.sleep(2);
                return "result1";
            }
        };
        Callable<String> task2 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.SECONDS.sleep(5);
                return "result2";
            }
        };
        Callable<String> task3 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.SECONDS.sleep(3);
                return "result3";
            }
        };
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        //invokeAll 方法
        List<Future<String>> futures = exec.invokeAll(tasks);
        List<String> results = futures.stream().map(future -> {
                    try {
                        //get
                        return future.get();
                    } catch (Exception e) {
                        return e.getMessage();
                    }
                }
        ).collect(Collectors.toList());
        long end = System.currentTimeMillis();
        System.out.println("invokeAll 总耗时:"+( end - start));
        return results;
    }

    /**
     * 2、CountDownLatch 闭锁
     */
    @GetMapping("/countDownLatch")
    public List<String> countDownLatch() throws Exception {
        long start = System.currentTimeMillis();

        //并发安全list
        List<String> results = new CopyOnWriteArrayList<>();

        //闭锁
        final CountDownLatch countDownLatch = new CountDownLatch(3);

        List<Runnable> tasks = new ArrayList<>();
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                results.add("result1");
                countDownLatch.countDown();
            }
        };
        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                results.add("result2");
                countDownLatch.countDown();
            }
        };
        Runnable task3 = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                results.add("result3");
                countDownLatch.countDown();
            }
        };
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        for (Runnable task : tasks) {
            //submit
            exec.submit(task);
        }

        //countDownLatch
        countDownLatch.await();

        long end = System.currentTimeMillis();
        System.out.println("countDownLatch 总耗时:"+( end - start));
        return results;
    }

    /**
     * 3、CyclicBarrier 栅栏/线程计数器
     */
    @GetMapping("/cyclicBarrier")
    public List<String> cyclicBarrier() throws Exception {
        long start = System.currentTimeMillis();

        //并发安全list
        List<String> results = new CopyOnWriteArrayList<>();

        //线程计数器
        CyclicBarrier barrier  = new CyclicBarrier(3 + 1);

        List<Runnable> tasks = new ArrayList<>();
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    results.add("result1");
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    results.add("result2");
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable task3 = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    results.add("result3");
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        for (Runnable task : tasks) {
            //execute
            exec.execute(task);
        }

        //CyclicBarrier
        barrier.await();

        long end = System.currentTimeMillis();
        System.out.println("cyclicBarrier 总耗时:"+( end - start));
        return results;
    }

    /**
     * 4、CompletableFuture 异步回调
     */
    @GetMapping("/completableFuture")
    public List<String> completableFuture() throws Exception {
        long start = System.currentTimeMillis();

        AtomicInteger state = new AtomicInteger(0);

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(
                new Supplier<String>() {
                    @Override
                    public String get() {
                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "result1";
                    }
                }
        );
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(
                new Supplier<String>() {
                    @Override
                    public String get() {
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "result2";
                    }
                }
        );
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(
                new Supplier<String>() {
                    @Override
                    public String get() {
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "result3";
                    }
                }
        );

        //并发安全 list
        List<String> results = new CopyOnWriteArrayList<>();
        future1.whenComplete(
                (t,u)->{
                    results.add(t);
                    state.addAndGet(1);
                }
        );
        future2.whenComplete(
                (t,u)->{
                    results.add(t);
                    state.addAndGet(1);
                }
        );
        future3.whenComplete(
                (t,u)->{
                    results.add(t);
                    state.addAndGet(1);
                }
        );

        Thread mainThread = Thread.currentThread();

        new Thread(
                ()->{
                    while (true){
                        if(state.get() == 3){
                            LockSupport.unpark(mainThread);
                            break;
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();

        if(state.get() != 3){
            LockSupport.park();
        }
        long end = System.currentTimeMillis();
        System.out.println("completableFuture 总耗时:"+( end - start));
        return results;
    }
}
