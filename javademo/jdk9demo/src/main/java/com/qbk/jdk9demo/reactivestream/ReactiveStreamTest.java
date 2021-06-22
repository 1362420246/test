package com.qbk.jdk9demo.reactivestream;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

/**
 * Java 9
 * 以下代码简单演示了SubmissionPublisher 和这套发布-订阅框架的基本使用方式
 */
public class ReactiveStreamTest  {

    public static void main(String[] args) throws InterruptedException {

        //1.创建 生产者Publisher JDK9自带的 实现了Publisher接口
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

        //2.创建 订阅者 Subscriber，需要自己去实现内部方法
        Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<>() {

            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                System.out.println("订阅成功。。");
                subscription.request(1);
                System.out.println("订阅方法里请求一个数据");
            }

            @Override
            public void onNext(Integer item) {
//                System.out.println("【onNext 接受到数据 item : " +  item);
//                subscription.request(1);

                /*
                会发现发布者 生成数据到256后就会停止生产，这是因为publisher.submit(i)方法是阻塞的，
                内部有个缓冲数组最大容量就是256，只有当订阅者发送 subscription.request(1);
                请求后，才会从缓冲数组里拿按照顺序拿出数据传给 onNext方法 供订阅者处理，
                当subscription.request(1)这个方法被调用后，发布者发现数组里没有满才会再生产数据，
                这样就防止了生产者一次生成过多的数据把订阅者压垮，从而实现了背压机制
                 */
                System.out.println("【onNext 接受到数据 item : " +  item);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("【onError 出现异常】");
                subscription.cancel();
            }

            @Override
            public void onComplete() {
                System.out.println("【onComplete 所有数据接收完成】");
            }
        };

        //3。发布者和订阅者 建立订阅关系 就是回调订阅者的onSubscribe方法传入订阅合同
        publisher.subscribe(subscriber);

        //4.发布者 生成数据
        for (int i = 1; i <= 1000; i++) {
            System.out.println("【生产数据 :" + i);
            //submit是一个阻塞方法，此时会调用订阅者的onNext方法
            publisher.submit(i);
        }

        //5.发布者 数据都已发布完成后，关闭发送，此时会回调订阅者的onComplete方法
        publisher.close();

        //主线程睡一会
        Thread.currentThread().join(100000);
    }
}

