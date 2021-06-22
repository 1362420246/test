package com.qbk.lock;

import org.openjdk.jol.info.ClassLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试 批量撤销
 * 在多线程竞争剧烈的情况下，使用偏向锁将会降低效率，于是乎产生了批量撤销机制。
 *
 * intx BiasedLockingBulkRebiasThreshold   = 20   默认偏向锁批量重偏向阈值
 * intx BiasedLockingBulkRevokeThreshold  = 40   默认偏向锁批量撤销阈值
 */
public class BiasedLockingBulkRevokeThreshold {
    static class A{}
    public static void main(String[] args) throws Exception {
        Thread.sleep(5000);
        List<A> listA = new ArrayList<>();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i <100 ; i++) {
                A a = new A();
                synchronized (a){
                    listA.add(a);
                }
            }
            try {
                Thread.sleep(100000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        Thread.sleep(3000);

        Thread t2 = new Thread(() -> {
            //这里循环了40次。达到了批量撤销的阈值
            for (int i = 0; i < 40; i++) {
                A a =listA.get(i);
                synchronized (a){
                }
            }
            try {
                Thread.sleep(10000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();

        //———————————分割线，前面代码不再赘述——————————————————————————————————————————
        Thread.sleep(3000);
        System.out.println("打印list中第11个对象的对象头：");
        System.out.println((ClassLayout.parseInstance(listA.get(10)).toPrintable()));
        System.out.println("打印list中第26个对象的对象头：");
        System.out.println((ClassLayout.parseInstance(listA.get(25)).toPrintable()));
        System.out.println("打印list中第90个对象的对象头：");
        System.out.println((ClassLayout.parseInstance(listA.get(89)).toPrintable()));

        Thread t3 = new Thread(() -> {
            for (int i = 20; i < 40; i++) {
                A a =listA.get(i);
                synchronized (a){
                    if(i==20||i==22){
                        System.out.println("thread3 第"+ i + "次");
                        System.out.println((ClassLayout.parseInstance(a).toPrintable()));
                    }
                }
            }
        });
        t3.start();

        Thread.sleep(10000);
        System.out.println("重新输出新实例A");
        System.out.println((ClassLayout.parseInstance(new A()).toPrintable()));

        /*
        来看看输出结果，这部分和上面批量偏向结果的大相径庭。重点关注记录的线程ID信息
        前20个对象，并没有触发了批量重偏向机制，线程t2执行释放同步锁后，转变为无锁形态
        第20~40个对象，触发了批量重偏向机制，对象为偏向锁状态，偏向线程t2，线程t2的ID信息为540039429
        而41个对象之后，也没有触发了批量重偏向机制，对象仍偏向线程t1，线程t1的ID信息为540002309

        重头戏来了！线程t3也来竞争锁。因为已经达到了批量撤销的阈值，且对象listA.get(20)和listA.get(22)已经进行过偏向锁的重偏向，并不会再次重偏向线程t3。
        此时触发批量撤销，此时对象锁膨胀变为轻量级锁。

        再来看看最后新生成的对象A。值得注意的是：本应该为可偏向状态偏向锁的新对象，在经历过批量重偏向和批量撤销后直接在实例化后转为无锁。
        如果不历过批量撤销，新实例化的对象应该是匿名偏向锁
         */
    }
}
