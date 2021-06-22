package com.qbk.lock;

import org.openjdk.jol.info.ClassLayout;

/**
 * 通过打印加锁类来查看对象头
 *
 * 001 无锁
 * 101 无线程id 匿名偏向锁
 * 101 有线程id 偏向锁
 * 000 轻量级锁
 * 010 重量级锁
 */
public class BiasedLockingDemo {
    public static void main(String[] args) throws InterruptedException {
        // 需要sleep一段时间，因为java对于偏向锁的启动是在启动几秒之后才激活。
        // 因为jvm启动的过程中会有大量的同步块，且这些同步块都有竞争，如果一启动就启动
        // 偏向锁，会出现很多没有必要的锁撤销
        Thread.sleep(5000);
        BiasedLockingDemo a = new BiasedLockingDemo();
        // 未出现任何获取锁的时候
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
        synchronized (a){
            // 获取一次锁之后
            System.out.println(ClassLayout.parseInstance(a).toPrintable());
        }
        // 输出hashcode
        System.out.println(a.hashCode());
        // 计算了hashcode之后
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
        synchronized (a){
            // 再次获取锁
            System.out.println(ClassLayout.parseInstance(a).toPrintable());
        }
        //新对象还是匿名偏向
        System.out.println(ClassLayout.parseInstance(new BiasedLockingDemo() ).toPrintable());

        //第一次：
        //05 00 00 00 (00000101 00000000 00000000 00000000) (5)
        //测试环境是windows，所以数据的存储是以小端模式，101可以看出这时候的锁状态是偏向锁，
        // 但是并没有偏向的threadId，因为还没有线程获取过锁。此时是匿名偏向锁。

        //第二次：
        //05 48 cf 02 (00000101 01001000 11001111 00000010) (47138821)
        //这时候出线程获取了一次锁，threadId出现了值

        //hashcod：1188753216 转换16进制：46daef40

        //第三次：
        //01 40 ef da (00000001 01000000 11101111 11011010) (-621854719)
        //计算了hascode之后，大家可以计算一下输出的hashcode与存储的值是否一致，
        // 注意，小端模式。这时候发现偏向锁的标志位已经为0了，虽然锁标志位还是01，
        // 但是他进行不能作为偏向锁了，此时是无锁

        //第四次：
        //c0 f0 b6 02 (11000000 11110000 10110110 00000010) (45543616)
        //代码中虽然没有任何的线程竞争，但是我们的锁还是变成了轻量级锁，就是因为hashcode带来的锁膨胀的问题

        // 05 00 00 00 (00000101 00000000 00000000 00000000) (5)
        //新对象还是匿名偏向
    }
}
