package com.qbk.niodemo.reactor.mult;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 多线程模式 和 单线程模式 的区别在于 hander 加入线程池异步处理
 */
public class MutilDispatchHandler  implements Runnable{

    private Executor executor = Executors.newFixedThreadPool(10);

    SocketChannel socketChannel;

    public MutilDispatchHandler(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        //多线程模式的体现
        executor.execute(new ReaderHandler(socketChannel));
    }

    /**
     * 执行的任务就是之前handler要做的事情，定义一个静态的内部类ReaderHandler，实现Runnable接口
     */
    static class ReaderHandler implements  Runnable{
        SocketChannel socketChannel;

        public ReaderHandler(SocketChannel socketChannel){
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            if(!socketChannel.isOpen()){
                return;
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int length;
            StringBuilder message = new StringBuilder();
            try {
                do {
                    length = socketChannel.read(byteBuffer);
                    message.append(new String(byteBuffer.array()),0,byteBuffer.position());
                }while (length > byteBuffer.capacity());
                if(length == -1){
                    socketChannel.close();
                }else {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(socketChannel != null){
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
