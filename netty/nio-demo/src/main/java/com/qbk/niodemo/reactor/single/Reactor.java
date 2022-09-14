package com.qbk.niodemo.reactor.single;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Reactor 负责监听 和 分发事件
 */
public class Reactor implements Runnable {

    Selector selector;

    public Reactor(int port, String threadName) throws IOException {
        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        //把连接事件注册到多路复用器上
        serverSocketChannel.register(
                selector,
                SelectionKey.OP_ACCEPT,
                //添加attachment为 Acceptor
                new Acceptor(selector,serverSocketChannel)
        );
    }

    @Override
    public void run() {
        // 查看线程是否被中断过，只要没有中断，就一直等待客户端过来
        while (!Thread.interrupted()){
            try {
                //1.监听事件，阻塞
                int select = selector.select();
                if(select > 0){
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterable = selectionKeys.iterator();
                    while (iterable.hasNext()){
                        //2.分发事件
                        dispatch(iterable.next());
                        iterable.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 只分发事件，不做处理
     */
    private void dispatch(SelectionKey selectionKey){
        //这里并没有开启线程，所以叫做单Reactor单线程模型
        // 如果是accept，这里的runnable就是Acceptor
        // 如果是read事件，这里的runnable就是handler
        Runnable runnable = (Runnable)selectionKey.attachment();
        if(runnable!=null){
            runnable.run();
        }
    }
}
