package com.qbk.jmm;

/**
 * javap 打印汇编指令
 */
public class VolatileDemo {
    /**
     * ACC_VOLATILE
     */
    static volatile int i = 0;
    public static void main(String[] args) {
        int b = i;
        System.out.println(b);
    }
}
