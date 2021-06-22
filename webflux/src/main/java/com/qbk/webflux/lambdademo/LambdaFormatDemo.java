package com.qbk.webflux.lambdademo;

/**
 * lambda 格式
 */
public class LambdaFormatDemo {

    /**
     * 声明一个函数式接口
     */
    @FunctionalInterface
    interface MyInterface{

        int fun(int i);

        /**
         * 接口的默认方法
         */
        default int add(int a, int b){
            return a + b;
        }
    }

    /**
     * 继承
     */
    @FunctionalInterface
    interface MyInterface2 extends MyInterface{}

    /**
     * 测试
     */
    public static void main(String[] args) {
        //lambda 一般写法
        Runnable runnable = () -> System.out.println(2);

        //lambda 四种写法
        MyInterface2 my1 = (i) -> i * 2 ;
        MyInterface my2 = i -> i * 3 ;
        MyInterface my3 = (int i) -> i * 4 ;
        MyInterface my4 = (int i ) -> {
            return i * 5 ;
        } ;
        //com.qbk.webflux.lambdademo.LambdaFormatDemo$$Lambda$1/97730845@17ed40e0
        System.out.println(my1);
        System.out.println(my1.fun(2));
        System.out.println(my2.fun(2));
        System.out.println(my3.fun(2));
        System.out.println(my4.fun(2));

        //接口的默认方法
        final int add = my1.add(3, 4);
        System.out.println(add);

    }
}
