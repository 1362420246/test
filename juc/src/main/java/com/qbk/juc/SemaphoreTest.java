package com.qbk.juc;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 信号量
 */
public class SemaphoreTest {

    /**
     * 假若一个工厂有5台机器，但是有8个工人，一台机器同时只能被一个工人使用，只有使用完了，其他工人才能继续使用
     */
    public static void main(String[] args) {
        //工人数
        int n = 8;
        //机器数目
        //参数permits表示许可数目，即同时可以允许多少线程进行访问
        Semaphore semaphore = new Semaphore(5);
        Random random = new Random();
        for(int i=0 ;i < n; i++){
            new Worker(i,semaphore,random).start();
        }
    }
    static class Worker extends Thread{
        private int num;
        private Semaphore semaphore;
        private Random random;
        Worker(int num, Semaphore semaphore, Random random){
            this.num = num;
            this.semaphore = semaphore;
            this.random = random;
        }
        @Override
        public void run() {
            try {
                //执行此方法用于获取执行许可，当总计未释放的许可数不超过permits时,
                //则允许同行，否则线程阻塞等待，直到获取到许可
                semaphore.acquire();
                System.out.println("工人"+this.num+"占用一个机器在生产...");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println("-------------------工人"+this.num+"释放出机器");
                //释放许可
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
