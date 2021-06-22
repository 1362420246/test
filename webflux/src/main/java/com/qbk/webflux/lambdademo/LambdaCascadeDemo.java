package com.qbk.webflux.lambdademo;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * lambda 的级联
 * 其实就是 套娃
 *
 * 柯里化是把接受多个参数的函数变换成接受一个单一参数(最初函数的第一个参数)的函数，并且返回接受余下的参数且返回结果的新函数的技术
 * 柯里化的目的：函数标准化
 */
public class LambdaCascadeDemo {

    public static void main(String[] args) {
        //级联  外侧函数的参数x是int 外层函数的返回值 是内层函数
        //内层函数的参数x是int  内层函数的返回值 是int
        Function<Integer, Function<Integer, Integer>> function = x -> y -> x + y;
        //外层函数 执行返回 是内存函数  然后接着 内层函数 执行 返回 int
        Integer result = function.apply(3).apply(2);
        System.out.println(result);

        //add(2)(3)(4)=9
        Function<Integer, Function<Integer, Function<Integer, Integer>>> function2 = x -> y -> z -> x + y +z;
        Integer result2 = function2.apply(2).apply(3).apply(4);
        System.out.println(result2);

        // add(2,3)(4)=9
        BiFunction<Integer,Integer, Function<Integer, Integer>> function3 = (x , y)-> z -> x + y +z;
        Integer result3 = function3.apply(2,3).apply(4);
        System.out.println(result3);

        //add(2)(3,4)=9 
        Function<Integer, BiFunction<Integer,Integer,Integer>> function4 = x -> (y , z) -> x + y +z;
        Integer result4 = function4.apply(2).apply(3,4);
        System.out.println(result4);
    }
}
