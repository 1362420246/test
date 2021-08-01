package com.qbk.multireactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Reactor ：将I/O事件发派给对应的Handler
 **/
public class Reactor implements Runnable{

    private final Selector selector;

    private ConcurrentLinkedQueue<AsyncHandler> events = new ConcurrentLinkedQueue<>();

    Reactor() throws IOException {
        this.selector = Selector.open();
    }

    public Selector getSelector() {
        return selector;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()){
            AsyncHandler handler;
            try {
                while((handler = events.poll()) != null){
                    handler.getChannel().configureBlocking(false);
                    SelectionKey selectionKey = handler.getChannel().register(selector,SelectionKey.OP_READ);
                    selectionKey.attach(handler);
                    handler.setSk(selectionKey);
                }
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    //得到Acceptor / Handler 实例
                    Runnable runnable=(Runnable) key.attachment();
                    if(runnable!=null){
                        runnable.run();
                    }
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void register(AsyncHandler handler){
        //有一个事件注册
        events.offer(handler);
        selector.wakeup();
    }
}
