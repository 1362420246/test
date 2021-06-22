package com.qbk.pattern.proxy.jdk;

/**
 * 目标
 */
public class Target implements ProxyInterfaces {
    @Override
    public void fun() {
        System.out.println("目标类执行方法");
    }
}
