package com.qbk.deadlock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 活锁?
 * 通过 Jconsole  观察
 * 通过 Jstack  观察
 */
public class Deadlock {

      static AtomicInteger i = new AtomicInteger();

      static int n = 0;

      static {
        //死锁
//        Thread t = new Thread(() -> i.getAndIncrement());

        //死锁
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
//                  n ++ ;
                i.getAndIncrement();
            }
        },"thread-qbk");

        // 正常
//        Thread t = new Thread(i::getAndIncrement);

      /*
        方法引用的是，主线程在执行static代码块的时候，创建线程对象获取全局变量值的时候，从栈里取的，所以没有被阻塞住

        之所以被阻塞，是因为在主线程实例化的过程中，还没有实例化完之前，执行static的时候，在代码块里取的是类里的，
        就是堆里的全局变量，因为还没有实例化完，所以调用join的时候，阻塞到哪儿了
       */
        t.start();
        try {
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("get");
    }
}
