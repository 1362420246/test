package com.qbk.niodemo.proactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Acceptor
 */
public class AIOAcceptor implements CompletionHandler<AsynchronousSocketChannel, AIOProactor> {

    @Override
    public void completed(AsynchronousSocketChannel channel, AIOProactor proactor){
        try {
            System.out.println("【" + Thread.currentThread().getName() + "】收到一个链接：" + channel.getRemoteAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 每接收一个连接之后，再执行一次异步连接请求，这样就能一直处理多个连接
        proactor.serverSocketChannel.accept(proactor,this);
        //创建新的Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 异步读取,第三个参数为接收消息回调的业务Handler
        channel.read(byteBuffer,byteBuffer,new ReadHandler(channel));
    }

    @Override
    public void failed(Throwable exc, AIOProactor attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
