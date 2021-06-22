package com.qbk.pattern.chain.demo;

/**
 * 抽象处理器
 */
public abstract class Handler {

    /**
     * 下一个处理器
     */
    protected Handler nextHandler;

    /**
     * 设置下一个处理器
     */
    public void setNextHanlder(Handler successor) {
        this.nextHandler = successor;
    }

    /**
     * 处理请求
     */
    public abstract void handleRequest(String request);

}