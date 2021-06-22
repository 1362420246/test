package com.qbk.thread;

/**
 * 不推荐
 * 用 priority +  yield() /sleep(0)   实现线程按顺序执行
 * priority:更改线程的优先级。
 * yield:放弃当前线程的时间片，并让操作系统调度其它就绪态的线程使用一个时间片。但是如果调用 Yield，只是把当前线程放入到就绪队列中，而不是阻塞队列。
 *       如果没有找到其它就绪态的线程，则当前线程继续运行。
 * sleep 会让 线程进入 TIMED_WAITING 带超时的等待
 * sleep(0):意思是要放弃自己剩下的时间片，但是仍然是就绪状态，其实意思和 Yield 有点类似。
 *          但是 Sleep(0) 只允许那些优先级相等或更高的线程使用当前的CPU，其它线程只能等着挨饿了。如果没有合适的线程，那当前线程会重新使用 CPU 时间片。
 * sleep(1):1作为参数，这会强制当前线程放弃剩下的时间片，并休息 1 毫秒。但因此的好处是，所有其它就绪状态的线程都有机会竞争时间片，而不用在乎优先级。
 */
public class ThreadByOrder2 {
    static Thread t1 = new Thread(() -> System.out.println("t1"));

    static Thread t2 = new Thread(() -> {

         Thread.yield();

//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.println("t2");
    });

    static Thread t3 = new Thread(new Runnable() {
        @Override
        public void run() {

            Thread.yield();

//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            System.out.println("t3");
        }
    });

    public static void main(String[] args) {
        t1.setPriority(8);
        t2.setPriority(5);
        t3.setPriority(3);

        t3.start();
        t2.start();
        t1.start();
    }
}
