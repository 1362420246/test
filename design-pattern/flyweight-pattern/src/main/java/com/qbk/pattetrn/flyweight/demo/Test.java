package com.qbk.pattetrn.flyweight.demo;

/**
 *  享元模式测试
 */
public class Test {
    public static void main(String[] args) {
        //第一次存储
        IFlyweight flyweight1 = FlyweightFactory.getFlyweight("aa");
        IFlyweight flyweight2 = FlyweightFactory.getFlyweight("bb");
        flyweight1.operation("a");
        flyweight2.operation("b");
        //第二次就从缓存池中获取
        IFlyweight flyweight3 = FlyweightFactory.getFlyweight("aa");
        flyweight3.operation("c");
    }
}
