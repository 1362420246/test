package com.qbk.pattern.factory.abastract;

/**
 * 生成产品等级二级的工厂
 */
public class ConcreteFactory2 implements AbstractFactory{
    @Override
    public AbstractProductA createProductA() {
        return new ProductA2();
    }
    @Override
    public AbstractProductB createProductB() {
        return new ProductB2();
    }
}