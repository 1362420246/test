package com.qbk.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Reactor ：将I/O事件发派给对应的Handler
 **/
public class Reactor implements Runnable {

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(
                selector,
                SelectionKey.OP_ACCEPT,
                new Acceptor(selector, serverSocketChannel)
        );
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                // 当注册事件到达时，方法返回，否则该方法会一直阻塞
                selector.select();
                //轮询所有选择器接收到的操作
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    //委派
                    dispatch(iterator.next());
                    // 删除已选的key 以防重负处理
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatch(SelectionKey key) {
        //可能拿到的对象有两个
        // Acceptor
        // Handler
        Runnable runnable = (Runnable) key.attachment();
        if (runnable != null) {
            runnable.run();
        }
    }
}