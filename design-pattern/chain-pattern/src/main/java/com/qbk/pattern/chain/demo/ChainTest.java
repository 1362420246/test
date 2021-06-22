package com.qbk.pattern.chain.demo;

/**
 * 责任链
 */
public class ChainTest {
    public static void main(String[] args) {
        Handler handlerA = new ConcreteHandlerA();
        Handler handlerB = new ConcreteHandlerB();
        //设置b是a的下一个处理器
        handlerA.setNextHanlder(handlerB);
        //处理请求b
        handlerA.handleRequest("requestB");
    }
}
