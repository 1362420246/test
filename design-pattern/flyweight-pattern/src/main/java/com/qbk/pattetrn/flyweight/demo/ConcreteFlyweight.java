package com.qbk.pattetrn.flyweight.demo;

/**
 * 具体享元角色
 */
public class ConcreteFlyweight implements IFlyweight {

    /**
     * 内部状态
     */
    private String intrinsicState;

    public ConcreteFlyweight(String intrinsicState) {
        this.intrinsicState = intrinsicState;
    }

    @Override
    public void operation(String extrinsicState) {
        System.out.println("Object address: " + System.identityHashCode(this));
        System.out.println("内部状态: " + this.intrinsicState);
        System.out.println("外部状态: " + extrinsicState);
    }
}