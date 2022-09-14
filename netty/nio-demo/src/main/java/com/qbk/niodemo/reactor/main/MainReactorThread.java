package com.qbk.niodemo.reactor.main;

import java.io.IOException;

/**
 * 主从Reactor多线程模型
 *
 * 客户端测试： telnet 127.0.0.1 8080
 */
public class MainReactorThread {
    public static void main(String[] args) throws IOException {
        new Thread(new MainReactor(8080),"Main-Reactor").start();
    }
}
