package com.qbk.pattern.chain.demo;

/**
 * b处理器
 */
public class ConcreteHandlerB extends Handler {

    /**
     * 处理请求
     */
    @Override
    public void handleRequest(String request) {
        //判断是否是b请求，如果不是由下一个处理器处理
        if ("requestB".equals(request)) {
            System.out.println(this.getClass().getSimpleName() + "处理请求: " + request);
            return;
        }
        if (this.nextHandler != null) {
            this.nextHandler.handleRequest(request);
        }
    }
}