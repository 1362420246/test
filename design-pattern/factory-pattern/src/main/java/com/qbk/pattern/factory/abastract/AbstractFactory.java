package com.qbk.pattern.factory.abastract;

/**
 * 抽象工厂
 */
public interface AbstractFactory {
    /**
     * 创建产品A
     * @return
     */
    public AbstractProductA createProductA();
    /**
     * 创建产品B
     * @return
     */
    public AbstractProductB createProductB();
}