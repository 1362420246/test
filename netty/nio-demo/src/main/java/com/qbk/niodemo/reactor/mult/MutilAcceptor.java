package com.qbk.niodemo.reactor.mult;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class MutilAcceptor implements Runnable{

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    public MutilAcceptor(Selector selector, ServerSocketChannel serverSocketChannel) {
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;

    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("【" + Thread.currentThread().getName() + "】收到一个链接：" + socketChannel.getRemoteAddress());
            socketChannel.configureBlocking(false);
            socketChannel.register(selector,SelectionKey.OP_READ,new MutilDispatchHandler(socketChannel));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
