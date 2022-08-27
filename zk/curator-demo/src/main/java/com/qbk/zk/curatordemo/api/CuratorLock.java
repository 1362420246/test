package com.qbk.zk.curatordemo.api;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Curator客户端的InterProcessMutex可重入锁
 */
public class CuratorLock {

    public static void main(String[] args)  {
        String url = "101.43.76.164:2181";
        //认证
        String scheme = "digest";
        byte[] auth = "qbk:123456".getBytes();
        //Curator
        CuratorFramework zkClient = CuratorFrameworkFactory
                .builder()
                .connectionTimeoutMs(20000)
                .connectString(url)
                .authorization(scheme, auth)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        zkClient.start();

        // InterProcessMutex
        String lockPath = "/lock";
        InterProcessMutex lock = new InterProcessMutex(zkClient, lockPath);

        //模拟100个线程抢锁
        for (int i = 0; i < 100; i++) {
            new Thread(new TestThread(i, lock)).start();
        }
    }

    static class TestThread implements Runnable {
        private Integer threadFlag;
        private InterProcessMutex lock;

        public TestThread(Integer threadFlag, InterProcessMutex lock) {
            this.threadFlag = threadFlag;
            this.lock = lock;
        }

        @Override
        public void run() {
            try {
                //获取锁，若失败则阻塞等待直到成功
                lock.acquire();
                System.out.println("第"+threadFlag+"线程获取到了锁");
                //等到1秒后释放锁
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    //释放锁
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
