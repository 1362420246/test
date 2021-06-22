package com.qbk.enumdemo.strategy;

/**
 * 策略枚举
 */
public enum StrategyEnum {
    ADD("+") {
        @Override
        public int exec(int a, int b) {
            return a+b;
        }
    },
    SUB("-") {
        @Override
        public int exec(int a, int b) {
            return a-b;
        }
    },
    MUTI("*") {
        @Override
        public int exec(int a, int b) {
            return a*b;
        }
    };

    private String value;

    StrategyEnum(String value) {
        this.value = value;
    }

    public abstract int exec(int a, int b);

    @Override
    public String toString() {
        return value;
    }
}