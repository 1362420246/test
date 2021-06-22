package com.qbk.data.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实现阻塞队列
 */
public class MyBlockingQueue {

    /**
     * 表示阻塞队列中的容器
     */
    private List<String> items;

    /**
     * 元素个数（表示已经添加的元素个数）
     */
    private volatile int size;

    /**
     * 数组的容量
     */
    private volatile int count;

    private ReentrantLock lock = new ReentrantLock();

    /**
     * take方法阻塞
     */
    private Condition notEmpty = lock.newCondition();

    /**
     * add方法阻塞
     */
    private Condition notFull = lock.newCondition();

    public MyBlockingQueue(int count){
        this.count = count;
        this.items = new ArrayList<>(count);
    }

    public void put(String item) throws InterruptedException {
        lock.lock();
        try {
            if(size >= count){
                System.out.println("队列满了，需要先等一会");
                notFull.await();
            }
            ++size;
            items.add(item);
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
    }

    public String take() throws InterruptedException {
        lock.lock();
        try {
            if(size==0){
                System.out.println("阻塞队列空了，先等一会");
                notEmpty.await();
            }
            --size;
            final String item = items.remove(0);
            notFull.signal();
            return item;
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyBlockingQueue queue = new MyBlockingQueue(5);
        Thread t1 = new Thread(
                ()->{
                    Random random = new Random();
                    for (int i = 0; i < 1000; i++) {
                        String item = "item-"+i;
                        try {
                            queue.put(item);
                            System.out.println("生产一个元素："+item);
                            Thread.sleep(random.nextInt(10));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        t1.start();
        Thread.sleep(100);
        Thread t2=new Thread(()->{
            Random random = new Random();
            for (;;) {
                try {
                    String item = queue.take();
                    System.out.println("消费者线程消费一个元素："+item);
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();
    }
}
