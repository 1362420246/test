package com.qbk.niodemo.reactor.main;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Acceptor
 */
public class MainAcceptor {

    private ServerSocketChannel serverSocketChannel;

    private int index = 0;

    Selector[] selectors = new Selector[4];

    SubReactor[] subReactors = new SubReactor[4];

    Thread[] threads = new Thread[4];

    public MainAcceptor(ServerSocketChannel serverSocketChannel) throws IOException {
        this.serverSocketChannel = serverSocketChannel;
        // 初始化子Reactor线程
        for(int i = 0;i < 4;i++){
            selectors[i] = Selector.open();
            subReactors[i] = new SubReactor(selectors[i]);
            threads[i] = new Thread(subReactors[i],"Sub-Reactor" + i);
            threads[i].start();
        }
    }

    /**
     * 处理链接事件
     */
    public void accept() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            System.out.println("【" + Thread.currentThread().getName() + "】收到一个链接：" + socketChannel.getRemoteAddress());

            //获取 子Reactor
            SubReactor subReactor = subReactors[index];
            //把handler放入 当前 子Reactor 的队列中
            subReactor.events.offer(new WorkerHandler(socketChannel));
            //唤醒 当前 子Reactor 的 selector.select() 阻塞
            subReactor.selector.wakeup();

            // 当前 子Reactor 处理了后，下次就给下一个 子Reactor
            if(++index == threads.length){
                index = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
