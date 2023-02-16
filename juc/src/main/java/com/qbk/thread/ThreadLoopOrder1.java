package com.qbk.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 3个线程轮流打印1-10
 */
public class ThreadLoopOrder1 {
    private static Object lock = new Object();
    private volatile static AtomicInteger n = new AtomicInteger(0);
    public static void main(String[] args) throws InterruptedException {
       Runnable runnable = ()->{
           final String name = Thread.currentThread().getName();
           synchronized (lock){
            while (n.get() < 10){
                if(name.equals("a")){
                    if ( n.get()%3 == 0){
                        System.out.println(name + ":" + n.incrementAndGet());
                    }else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else if(name.equals("b")){
                    if (n.get()%3 == 1){
                        System.out.println(name + ":" + n.incrementAndGet());
                    }else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else if(name.equals("c") ){
                    if (n.get()%3 == 2){
                        System.out.println(name + ":" + n.incrementAndGet());
                    }else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //把其他线程换醒（线程：wait -> monitor）
                lock.notifyAll();
            }
           }
       };
       Thread t1 = new Thread(runnable,"a");
       Thread t2 = new Thread(runnable,"b");
       Thread t3 = new Thread(runnable,"c");
       t1.start(); t2.start(); t3.start();
        TimeUnit.SECONDS.sleep(3);
    }
}
