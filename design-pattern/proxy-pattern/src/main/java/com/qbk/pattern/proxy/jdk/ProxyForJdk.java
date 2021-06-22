package com.qbk.pattern.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk代理处理器
 */
public class ProxyForJdk implements InvocationHandler {
    /**
     * 目标类
     */
    private Object target;

    /**
     * 生成代理类
     */
    public Object getInstance(Object target){
        this.target = target;
        Class clazz = target.getClass();
        ClassLoader classLoader = clazz.getClassLoader();
        Class [] interfaces = clazz.getInterfaces();
        /*
            返回指定接口的代理类的实例
            @param loader  定义代理类的类加载器
            @param interfaces 代理类要实现的接口列表
            @param h 将方法调用分派到的调用处理程序
         */
        return Proxy.newProxyInstance(classLoader, interfaces, this);
    }

    /**
     *
     * 处理代理实例上的方法调用并返回结果。该方法将在调用处理程序上调用在代理实例上调用方法时有关联。
     * @param proxy  调用该方法的代理实例
     * @param method 实例对应于在代理实例上调用的接口方法
     * @param args 包含的值的对象数组,在代理实例上的方法调用中传递的参数
     * @return 从方法调用返回的值
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //增加前置通知
        before();
        //用原目标类 执行方法
        return method.invoke(this.target, args);
    }

    private void before(){
        System.out.println("执行增强");
    }

}
