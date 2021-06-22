package com.qbk.collection;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ArrayList 并发安全问题
 */
public class ArrayListConcurrent {

    public static void main(String[] args) {
        //List<String> list = new ArrayList<>();
        //List<String> list = new Vector<>();
        //List<String> list = Collections.synchronizedList(new ArrayList<>());
        List<String> list = new CopyOnWriteArrayList<>();

        for (int i = 0; i <= 30 ; i++) {
           new Thread(() -> {
               list.add(UUID.randomUUID().toString());
               System.out.println(list);
           },String.valueOf(i)).start();
        }

        /*问题
         不安全的list会报 java.util.ConcurrentModificationException
         */

        /*原因
        线程A和线程B获取的size都是9，线程A先插入元素e，这个时候elementData数组的大小为10，
        是正常情况下下次应该是要扩容的，但是线程B获取的size=9而不是10，在线程B中没有进行扩容，
        而是报出数组index越界异常。
        */

       /*解决
       从add方法知道：CopyOnWriteArrayList底层数组的扩容方式是一个一个地增加，
       而且每次把原来的元素通过Arrays.copy()方法copy到新数组中，然后在尾部加上新元素e.
       它的底层并发安全的保证是通过ReentrantLock进行保证的，
       CopyOnWriteArrayList和SynchronizedList的底层实现方式是不一样的，
       前者是通过Lock机制进行加锁，而后者是通过Synchronized进行加锁。
        */
    }
}
