package com.qbk.threadlocal;

/**
 *  3.InheritableThreadLocal
 *
 * 如果线程A创建了线程B，那么B就是A的子线程。
 *
 * InheritableThreadLocal是ThreadLocal的子类。该类扩展了 ThreadLocal，为子线程提供从父线程那里继承的值：
 * 在创建子线程时，子线程会接收所有可继承的线程局部变量的初始值，以获得父线程所具有的值。
 * 通常，子线程的值与父线程的值是一致的；但是，通过重写这个类中的 childValue 方法，子线程的值可以作为父线程值的一个任意函数。
 * 当必须将变量（如用户 ID 和 事务 ID）中维护的每线程属性（per-thread-attribute）自动传送给创建的所有子线程时，
 * 应尽可能地采用可继承的线程局部变量，而不是采用普通的线程局部变量。
 *
 * 缺点
 * 这样的方式解决了创建线程时的 ThreadLocal 传值的问题，但不可能一直创建新的线程，那实在耗费资源。
 * 因此通用做法是线程复用，比如线程池。但是，递交异步任务是相应的 ThreadLocal 的值就无法传递过去了。
 */
public class InheritableThreadLocalDemo {

    private static ThreadLocal<Integer> integerThreadLocal = new ThreadLocal<>();
    private static InheritableThreadLocal<Integer> inheritableThreadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {

        integerThreadLocal.set(1001);
        inheritableThreadLocal.set(1002);

        new Thread(
                () -> System.out.println(
                        Thread.currentThread().getName() + ":"
                                + integerThreadLocal.get() + "/"
                                + inheritableThreadLocal.get()
                )
        ).start();
    }
}
