package com.qbk.niodemo.reactor.single;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Handler 处理读、写的I/O事件
 */
public class Handler implements Runnable{

    SocketChannel socketChannel;

    public Handler(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int length;
        StringBuilder message = new StringBuilder();
        try {
            do {
                length = socketChannel.read(byteBuffer);
                message.append(new String(byteBuffer.array()),0,byteBuffer.position());
                // 就是判断数据是否有没有读完
            }while (length > byteBuffer.capacity());
            System.out.println("【" + Thread.currentThread().getName() + "】收到一个消息：" + message);
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
