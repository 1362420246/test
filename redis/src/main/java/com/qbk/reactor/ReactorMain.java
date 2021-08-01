package com.qbk.reactor;

import java.io.IOException;

/**
 * 测试 Reactor模型
 **/
public class ReactorMain {
    public static void main(String[] args) throws IOException {
        new Thread(new Reactor(8080),"Main-Thread").start();
    }
}
