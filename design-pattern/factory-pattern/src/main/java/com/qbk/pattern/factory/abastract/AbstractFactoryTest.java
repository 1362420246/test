package com.qbk.pattern.factory.abastract;

/**
 * 抽象工厂 是复杂产品的工厂
 */
public class AbstractFactoryTest {
    public static void main(String[] args) {
        //------------- 创建A1-B1
        AbstractFactory f1 = new ConcreteFactory1();
        AbstractProductA productA1 = f1.createProductA();
        AbstractProductB productB1 = f1.createProductB();
        System.out.println(productA1);
        System.out.println(productB1);
        //--------------创建A2-B2
        AbstractFactory f2 = new ConcreteFactory2();
        AbstractProductA productA2 = f2.createProductA();
        AbstractProductB productB2 = f2.createProductB();
        System.out.println(productA2);
        System.out.println(productB2);
    }
}
