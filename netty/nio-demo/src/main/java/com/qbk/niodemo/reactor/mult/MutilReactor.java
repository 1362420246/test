package com.qbk.niodemo.reactor.mult;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MutilReactor implements Runnable {

    Selector selector;

    public MutilReactor(int port, String threadName) throws IOException {
        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT,new MutilAcceptor(selector,serverSocketChannel));
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            try {
                Thread.sleep(10);
                int select = selector.select();
                if(select > 0){
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterable = selectionKeys.iterator();
                    while (iterable.hasNext()){
                        dispatch(iterable.next());
                        iterable.remove();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void dispatch(SelectionKey selectionKey){
        // 如果是accept，这里的runnable就是Acceptor
        // 如果是read事件，这里的runnable就是handler
        Runnable runnable = (Runnable)selectionKey.attachment();
        if(runnable!=null){
            runnable.run();
        }
    }
}
