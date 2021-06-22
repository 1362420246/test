package com.qbk.juc;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 *  CompletableFuture jdk1.8 异步回调
 *
 * Future虽然可以实现获取异步执行结果的需求，但是它没有提供通知的机制，我们无法得知Future什么时候完成。
 * 要么使用阻塞，在future.get()的地方等待future返回的结果，这时又变成同步操作。
 * 要么使用isDone()轮询地判断Future是否完成，这样会耗费CPU的资源。
 *
 * CompletableFuture能够将回调放到与任务不同的线程中执行，也能将回调作为继续执行的同步函数，在与任务相同的线程中执行。
 * 它避免了传统回调最大的问题，那就是能够将控制流分离到不同的事件处理器中。
 *
 * CompletableFuture弥补了Future模式的缺点。在异步的任务完成后，需要用其结果继续操作时，无需等待。
 * 可以直接通过thenAccept、thenApply、thenCompose等方式将前面异步处理的结果交给另外一个异步事件处理线程来处理。
 *
 * completedFuture是一个静态辅助方法，用来返回一个已经计算好的CompletableFuture。
 *
 * 静态方法：
 * runAsync(Runnable runnable)	使用ForkJoinPool.commonPool()作为它的线程池执行异步代码。
 * runAsync(Runnable runnable, Executor executor)	使用指定的thread pool执行异步代码。
 * supplyAsync(Supplier<U> supplier) 使用ForkJoinPool.commonPool()作为它的线程池执行异步代码，异步操作有返回值
 * supplyAsync(Supplier<U> supplier, Executor executor)	使用指定的thread pool执行异步代码，异步操作有返回值
 *
 * runAsync将Runnable作为输入参数并返回CompletableFuture<Void>，这意味着它不返回任何结果
 * suppyAsync将Supplier作为参数并返回CompletableFuture<U>with结果值，这意味着它不接受任何输入参数，而是将result作为输出返回。
 */
public class CompletableFutureDemo {

    public static void main(String[] args) {
        //返回一个新的异步完成的CompletableFuture
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName());
                    }
                }
        );
        try {
            future1.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //异步回调
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(
                new Supplier<String>() {
                    @Override
                    public String get() {
                        int i = 10/0;
                        return "qbk";
                    }
                }
        );

        try {
            String result = future2.whenComplete(
                    (t,u)->{
                        System.out.println(t);
                        System.out.println(Objects.isNull(u) ? "null!": u.getMessage());
                    }
            ).exceptionally(
                    (e) ->{
                        System.out.println(e.getMessage());
                        return "error";
                    }
            ).get(); //future.get()在等待执行结果时，程序会一直block，如果此时调用complete(T t)会立即执行。
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        future2.whenComplete(
                (t,u)->{
                    System.out.println(t);
                    System.out.println(Objects.isNull(u) ? "null!": u.getMessage());
                }
        );

        //通过thenAccept、thenApply、thenCompose等方式将前面异步处理的结果交给另外一个异步事件处理线程来处理
        CompletableFuture.supplyAsync(()->"qbk1").thenAccept(System.out::println);

        CompletableFuture.supplyAsync(()->"qbk2").thenApply( result ->{
            System.out.println(result);
            return result + "kk2";
        }).thenAccept(System.out::println) ;

        //thenCompose（）用来连接两个CompletableFuture，是生成一个新的CompletableFuture
        CompletableFuture.supplyAsync(()->"qbk3").thenCompose(result -> CompletableFuture.supplyAsync(
                ()->{
                    return result + "kk3";
                }
        )).thenAccept(System.out::println) ;

        //completedFuture是一个静态辅助方法，用来返回一个已经计算好的CompletableFuture。
        CompletableFuture<String> cf = CompletableFuture.completedFuture("qbk4");
        cf.thenAccept(System.out::println);

    }
}
