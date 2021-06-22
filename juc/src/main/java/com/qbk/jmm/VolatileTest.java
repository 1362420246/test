package com.qbk.jmm;

import java.io.File;

/**
 *下列操作 可以导致 循环停止
 * 1、volatile 可见性
 *
 * 2、print就可以导致循环结束  活性失败. JIT深度优化
 *
 * 3、Thread.sleep(0) 导致线程切换，线程切换会导致缓存失效从而读取到了新的值。
 *
 * 4、io操作
 *
 * 5、final
 */
public class VolatileTest {

    private static Boolean stop = false;
    private volatile static int i = 1;
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!stop) {
                i++;

                //System.out.println("rs:" + i);

//                try {
//                    Thread.sleep(0);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

               // new File("txt.txt");
            }
        });
        thread.start();
        Thread.sleep(1000);
        stop = true;
        System.out.println("主线程结束" );
    }
}
