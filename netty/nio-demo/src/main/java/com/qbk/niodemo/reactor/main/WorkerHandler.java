package com.qbk.niodemo.reactor.main;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * Handler
 */
public class WorkerHandler{

    private SocketChannel socketChannel;

    public WorkerHandler(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }

    public SocketChannel getChannel() {
        return socketChannel;
    }

    public void read() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            socketChannel.read(byteBuffer);
            String message = new String(byteBuffer.array(), StandardCharsets.UTF_8);
            System.out.println("【" + Thread.currentThread().getName() + "】收到一个消息：" +message);
            socketChannel.write(ByteBuffer.wrap("\r\nserver received!\r\n".getBytes()));
        }catch (IOException e){
            System.out.println("退出");
            try {
                socketChannel.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
