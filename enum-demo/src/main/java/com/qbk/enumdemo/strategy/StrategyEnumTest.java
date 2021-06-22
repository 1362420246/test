package com.qbk.enumdemo.strategy;

/**
 * 策略枚举
 */
public class StrategyEnumTest {
    public static void main(String[] args) {
        System.out.println(StrategyEnum.ADD);
        System.out.println( StrategyEnum.ADD.exec(2,4));
        System.out.println( StrategyEnum.SUB.exec(2,4));
        System.out.println( StrategyEnum.MUTI.exec(2,4));
    }
}