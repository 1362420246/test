package com.qbk.niodemo.proactor;

import java.io.IOException;

/**
 * Proactor模型
 *
 * 客户端测试： telnet 127.0.0.1 8080
 */
public class ProactorMain {
    public static void main(String[] args) throws IOException {
        new Thread(new AIOProactor(8080),"Main-Thread").start();
    }
}
