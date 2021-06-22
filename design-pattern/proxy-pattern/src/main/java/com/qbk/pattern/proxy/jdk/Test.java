package com.qbk.pattern.proxy.jdk;

import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;

/**
 * jdk动态代理
 */
public class Test {
    public static void main(String[] args) throws Exception {
        //代理处理器
        ProxyForJdk proxyForJdk = new ProxyForJdk();
        //创建目标类
        Target target = new Target();
        //生成代理对象
        Object instance = proxyForJdk.getInstance(target);
        //转换接口 执行方法
        ((ProxyInterfaces)instance).fun();

        //打印代理类
        byte[] a = ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{ProxyInterfaces.class});
        FileOutputStream fos  = new FileOutputStream("C:\\Users\\86186\\Desktop\\proxy\\$Proxy0.class");
        fos.write(a);
        fos.flush();
    }
}
