package com.qbk.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  2.threadlocal 内存泄露 测试
 *
 * -Xmx50m -XX:+PrintGCDetails
 *
 * 当调用testUseThread()时，系统在运行时执行了大量YGC，但始终稳定回收，最后正常执行，
 * 但是执行testUseThreadPool()时，经历的频繁的Full GC，内存却没有降下去，最终发生了OOM。
 *
 * 分析：在使用new Thread()的时候，当线程执行完毕时，随着线程的终止，那个这个Thread对象的生命周期也就结束了，
 * 此时该线程下的成员变量，ThreadLocalMap是GC Root不可达的，同理，下面的Entry、里面的key、value都会在下一次gc时被回收；
 * 而使用线程池后，由于线程执行完一个任务后，不会被回收，而是被放回线程池以便执行后续任务，自然其成员变量ThreadLocalMap不会被回收，最终引起内存泄露直至OOM。
 *
 * 至于怎么避免出现内存泄露，就是在使用线程完成任务后，如果保存在ThreadLocalMap中的数据不必留给之后的任务重复使用，
 * 就要及时调用ThreadLocal的remove()，这个方法会把ThreadLocalMap中的相关key和value分别置为null，就能在下次GC时回收了。
 *
 */
public class TestThreadLocalLeak {
    final static ThreadLocal<byte[]> LOCAL = new ThreadLocal();
    final static int _1M = 1024 * 1024;

    public static void main(String[] args) {
        //1、new thread
//        testUseThread();

        //2、thread pool
        testUseThreadPool();
    }

    /**
     * 使用线程
     */
    private static void testUseThread() {
        for (int i = 0; i < 100; i++) {
            new Thread(() ->
                    LOCAL.set(new byte[_1M])
            ).start();
        }
    }

    /**
     * 使用线程池
     */
    private static void testUseThreadPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() ->
                    LOCAL.set(new byte[_1M])
            );
        }
        executorService.shutdown();
    }
}