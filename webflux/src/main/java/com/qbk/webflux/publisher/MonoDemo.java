package com.qbk.webflux.publisher;

import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Mono 实现了 Publisher 接口
 * 代表0到1个元素的发布者
 */
public class MonoDemo {

    public static void main(String[] args) {
        System.out.println("开始---------------------");

        //empty()：创建一个不包含任何元素，只发布结束消息的序列
        Mono.empty().subscribe(System.out::println);

        //just()：可以指定序列中包含的全部元素。创建出来的 Mono序列在发布这些元素之后会自动结束。
        Mono.just("我是kk").subscribe(System.out::println);

        //justOrEmpty()：从一个 Optional 对象或可能为 null 的对象中创建 Mono。
        //只有 Optional 对象中包含值或对象不为 null 时，Mono 序列才产生对应的元素。
        Mono.justOrEmpty(null).subscribe(System.out::println);
        Mono.justOrEmpty("I am kk").subscribe(System.out::println);
        Mono.justOrEmpty(Optional.of("kk")).subscribe(System.out::println);

        //error(Throwable error)：创建一个只包含错误消息的序列。
        Mono.error(new RuntimeException("error")).subscribe(System.out::println, System.err::println);

        //never()：创建一个不包含任何消息通知的序列。
        Mono.never().subscribe(System.out::println);

        //通过 create()方法来使用 MonoSink 来创建 Mono。
        Consumer<MonoSink<String>> callback = sink -> sink.success("ka");
        Mono.create(callback).subscribe(System.out::println);

        //通过fromRunnable创建，并实现异常处理
        Mono.fromRunnable(() -> {
            System.out.println("thread run");
            throw new RuntimeException("thread run error");
        }).subscribe(System.out::println, System.err::println);

        //通过fromCallable创建
        Mono.fromCallable(() -> "callable run ").subscribe(System.out::println);

        //通过fromSupplier创建
        Mono.fromSupplier(() -> "create from supplier").subscribe(System.out::println);

        //delay(Duration duration)和 delayMillis(long duration)：创建一个 Mono 序列，在指定的延迟时间之后，产生数字 0 作为唯一值。
        long start = System.currentTimeMillis();
        Disposable disposable =
                Mono.delay(
                        Duration.ofSeconds(2)).subscribe(n -> {
                            System.out.println("生产数据源："+ n);
                            System.out.println(
                                    "当前线程："+ Thread.currentThread().getName() +
                                    ",生产到消费耗时："+ (System.currentTimeMillis() - start)
                            );
        });
        System.out.println("主线程"+ Thread.currentThread().getId() + "耗时："+ (System.currentTimeMillis() - start));
        //阻塞主线程
        while(!disposable.isDisposed()) { }
    }
}
