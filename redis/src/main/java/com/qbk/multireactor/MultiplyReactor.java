package com.qbk.multireactor;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 多Reactor模型 (也是主从Reactor模型)
 *
 * 客户端测试： telnet 127.0.0.1 8080
 */
public class MultiplyReactor {

    Executor mainReactorExecutor= Executors.newFixedThreadPool(10);

    /**
     * main Reactor
     */
    private Reactor mainReactor;

    private int port;

    public MultiplyReactor(int port) throws IOException {
        this.port = port;
        mainReactor = new Reactor();
    }

    public void start() throws IOException {
        new Acceptor(mainReactor.getSelector(),port);
        mainReactorExecutor.execute(mainReactor);
    }

    public static void main(String[] args) throws IOException {
        new MultiplyReactor(8080).start();
    }
}
