package com.qbk.reactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Acceptor 处理客户端连接请求
 **/
public class Acceptor implements Runnable{

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    Acceptor(Selector selector, ServerSocketChannel serverSocketChannel) {
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"------Acceptor");
        SocketChannel channel;
        try {
            //得到一个客户端连接
            channel = serverSocketChannel.accept();
            System.out.println(channel.getRemoteAddress()+":收到一个客户端连接");
            channel.configureBlocking(false);
            channel.register(
                    selector,
                    SelectionKey.OP_READ,
                    new Handler(channel)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
