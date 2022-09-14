package com.qbk.niodemo.reactor.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 主 Reactor
 * 负责监听ACCEPT事件
 */
public class MainReactor implements Runnable {

    private final Selector selector;

    private final ServerSocketChannel serverSocketChannel;

    public MainReactor(int port) throws IOException {
        //主Reactor负责监听ACCEPT事件
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        //主Reactor只注册 链接事件
        serverSocketChannel.register(
                selector,
                SelectionKey.OP_ACCEPT,
                new MainAcceptor(serverSocketChannel)
        );
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    dispatch(iterator.next());
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatch(SelectionKey key) {
        MainAcceptor acceptor = (MainAcceptor) key.attachment();
        if (acceptor != null) {
            acceptor.accept();
        }
    }
}
