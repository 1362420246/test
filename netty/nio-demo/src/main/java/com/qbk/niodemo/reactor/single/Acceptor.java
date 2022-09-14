package com.qbk.niodemo.reactor.single;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Acceptor 处理客户端连接事件
 */
public class Acceptor implements Runnable{

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    public Acceptor(Selector selector, ServerSocketChannel serverSocketChannel) {
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void run() {
        try {
            // 接收客户端链接
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("【" + Thread.currentThread().getName() + "】收到一个链接："  + socketChannel.getRemoteAddress());
            socketChannel.configureBlocking(false);
            socketChannel.register(
                    selector,
                    SelectionKey.OP_READ,
                    //添加attachment为 Handler
                    new Handler(socketChannel)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
