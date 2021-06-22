package com.qbk.pattern.proxy.cglib;

import net.sf.cglib.core.DebuggingClassWriter;

/**
 * cglib动态代理
 */
public class Test {
    public static void main(String[] args) {
        //利用 cglib 的代理类可以将内存中的 class 文件写入本地磁盘
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,"C:\\Users\\86186\\Desktop\\proxy\\");

        //代理处理器
        ProxyForCglib proxyForCglib = new ProxyForCglib();
        //生成代理对象
        Object instance = proxyForCglib.getInstance(Target.class);
        //转换目标类 执行方法
        ((Target)instance).fun();
    }
}
