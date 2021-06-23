package com.qbk.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 *
 * 读读共享，写写互斥，读写互斥，写读互斥。
 */
public class ReadWriteLockDemo {
    static Map<String,Object> cacheMap =new HashMap<>();
    static ReentrantReadWriteLock rwl =new ReentrantReadWriteLock();
    static Lock read = rwl.readLock();
    static Lock write = rwl.writeLock();

    public static Object get(String key) {
        System.out.println("开始读取数据");
        read.lock();
        try {
            return cacheMap.get(key);
        }finally {
            read.unlock();
        }
    }
    public static Object put(String key, Object value){
        System.out.println("开始写数据");
        write.lock();
        try{
            return cacheMap.put(key,value);
        }finally {
            write.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final Thread thread1 = new Thread(
                ()->{
                    for (int i = 0; i < 10; i++) {
                        put("a","A");
                    }
                }
        );
        final Thread thread2 = new Thread(
                ()->{
                    for (int i = 0; i < 10; i++) {
                        get("a");
                    }
                }
        );
        final Thread thread3 = new Thread(
                ()->{
                    for (int i = 0; i < 10; i++) {
                        get("a");
                    }
                }
        );
        thread1.start();
        thread2.start();
        thread3.start();
        thread1.join();
        thread2.join();
        thread3.join();
    }
}
