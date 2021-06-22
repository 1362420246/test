package com.qbk.webflux.publisher;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Flux 实现了 Publisher 接口
 * 代表0到N个元素的发布者
 *
 * publisher 就是发布者
 　　subscribe:就是和订阅者产生一个关系
 *
 * subscribe 就是订阅者
 　　onSubscribe:签署一个订阅关系传入subscription
 　　onNext(): 接受到一条数据
 　　onError(): 就是出错
 　　onComplete(): 完成
 *
 * Subscription：发布者与订阅者之间的关系纽带，订阅令牌
 *
 */
public class FluxDemo {
    public static void main(String[] args) {
        //创建一个订阅者
        Subscriber<String> subscriber = new Subscriber<String>() {
            private Subscription subscription;
            @Override
            public void onSubscribe(Subscription subscription) {
                //保存订阅关系，需要用它来给发布者响应
                this.subscription = subscription;
                //请求一个数据
                this.subscription.request(1);
            }
            @Override
            public void onNext(String item) {
                //当有数据到了的时候出发该方法
                //接收到一个数据，处理
                System.out.println("接收到数据："+item);
                //处理完在调用request再请求一个数据
                this.subscription.request(1);
                //或者 已经达到目标，调用cancel告诉发布者不再接收数据了
                //this.subscription.cancel();
            }
            @Override
            public void onError(Throwable throwable) {
                //出错时，触发该方法
                //出现了异常(例如处理数据(onNext中)的时候产生了异常)
                throwable.printStackTrace();
                //异常后还可以告诉发布者，不再接收数据了
                this.subscription.cancel();
            }
            @Override
            public void onComplete() {
                //数据全部处理完了(发布者关闭了)
                //publisher.close()方法后触发该方法
                System.out.println("处理完毕！");
            }
        };

        //通过fromStream 创建一个包含stream的flux
        Flux<String> stringFlux = Flux.fromStream(() -> Stream.of("a", "b"));
        //为这个flux添加订阅者
        stringFlux.subscribe(subscriber);

        Flux.fromStream(()-> Stream.of("a","b")).subscribe(System.out::println);
    }
}
