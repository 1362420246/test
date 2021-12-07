package com.qbk.multireactor;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


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