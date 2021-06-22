package com.qbk.data.algorithm;

/**
 * 两数交换
 */
public class ExchangeDemo {

    public static void main(String[] args) {
        //交换
        int a = 3; //  11
        int b = 5; // 101
        a = a + b ;
        b = a - b;
        a = a - b;
        System.out.println("a : " + a);
        System.out.println("b : " + b);

        //位运算交换
        a = a ^ b ;
        System.out.println(a); // 11 ^ 101 = 110
        b = a ^ b;
        a = a ^ b;
        System.out.println("a : " + a);
        System.out.println("b : " + b);
    }
}
