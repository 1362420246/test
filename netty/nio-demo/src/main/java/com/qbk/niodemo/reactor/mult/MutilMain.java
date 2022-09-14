package com.qbk.niodemo.reactor.mult;

import java.io.IOException;

/**
 * 单Reactor多线程模型
 *
 * 多线程模式 和 单线程模式 的区别在于 hander 加入线程池异步处理
 *
 * 客户端测试： telnet 127.0.0.1 8080
 */
public class MutilMain {
    public static void main(String[] args) throws IOException {
        new Thread(new MutilReactor(8080,"mutil-main")).start();
    }
}
