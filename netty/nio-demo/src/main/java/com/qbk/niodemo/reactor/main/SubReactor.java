package com.qbk.niodemo.reactor.main;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 子 Reactor
 * 负责监听IO事件
 */
public class SubReactor implements Runnable{

    public Selector selector;

    public ConcurrentLinkedQueue<WorkerHandler> events = new ConcurrentLinkedQueue<>();

    public SubReactor(Selector selector){
        this.selector = selector;
    }

    @Override
    public void run() {
        while (true){
            try {
                WorkerHandler handler;
                while((handler = events.poll()) != null){
                    SocketChannel channel = handler.getChannel();
                    channel.configureBlocking(false);
                    //子Reactor只注册IO事件
                    channel.register(
                            selector,
                            SelectionKey.OP_READ,
                            handler
                    );
                }
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    dispatch(key);
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatch(SelectionKey selectionKey){
        WorkerHandler handler = (WorkerHandler)selectionKey.attachment();
        if(handler != null){
            handler.read();
        }
    }
}
