package com.qbk.pattern.factory.abastract;

/**
 * 生成产品等级一级的工厂
 */
public class ConcreteFactory1 implements AbstractFactory{
    @Override
    public AbstractProductA createProductA() {
        return new ProductA1();
    }
    @Override
    public AbstractProductB createProductB() {
        return new ProductB1();
    }
}
