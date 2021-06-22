package com.qbk.pattern.proxy.cglib;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * cglib代理处理器
 */
public class ProxyForCglib implements MethodInterceptor{

    /**
     * 返回代理对象
     */
    public Object getInstance(Class<?> clazz){
        //相当于Proxy，代理的工具类
        Enhancer enhancer = new Enhancer();
        //用于设置代理对象的父类
        enhancer.setSuperclass(clazz);
        //设置回调
        enhancer.setCallback(this);
        //创建代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        before();
        return proxy.invokeSuper(obj, args);
    }

    private void before(){
        System.out.println("执行增强");
    }
}