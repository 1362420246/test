package com.qbk.niodemo.reactor.single;

import java.io.IOException;

/**
 * 单Reactor单线程模型
 *
 * 客户端测试： telnet 127.0.0.1 8080
 */
public class SingleMain {
    public static void main(String[] args) throws IOException {
        new Thread(new Reactor(8080,"single-main")).start();
    }
}
