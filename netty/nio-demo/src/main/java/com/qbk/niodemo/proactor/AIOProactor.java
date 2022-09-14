package com.qbk.niodemo.proactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * Proactor
 */
public class AIOProactor implements Runnable {

    public CountDownLatch latch;

    public AsynchronousServerSocketChannel serverSocketChannel;

    public AIOProactor(int prot) throws IOException {
        //创建一个Group，类似于一个线程池，用于处理IO完成事件
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(), 4);
        serverSocketChannel = AsynchronousServerSocketChannel.open(group);
        serverSocketChannel.bind(new InetSocketAddress(prot));
        System.out.println("【" + Thread.currentThread().getName() + "】 服务段初始化完成,端口:" + prot);
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        //注册Accept事件由AIOAccept处理
        serverSocketChannel.accept(this,new AIOAcceptor());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
