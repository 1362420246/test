package com.qbk.webflux.lambdademo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 函数式接口 格式
 * java.util.function 包下
 * 这些函数是接口 主要关心 参数 和返回值 的类型
 */
public class FunctionFormatDemo {

    @Data
    @AllArgsConstructor
    static class User {
        private int id;

        /**
         * 函数式接口 作为参数
         * Function<T,R> 接受一个输入参数，返回一个结果。
         * 泛型：前面为参数类型，后面为结果类型
         */
        public String fun(Function<Integer,String> function){
            return function.apply(id);
        }
    }

    public static void main(String[] args) {
        User user = new User(3);
        //调用
        String result = user.fun(id -> String.format("id为%d的用户登陆成功", id));
        System.out.println(result);

        //函数式接口 链式编程
        Function<Integer,String> function = id -> String.format("id为%d的用户登陆成功", id);
        Function<Integer, String> newFunction = function.andThen(s -> "结果：" + s);
        String result2 = user.fun(newFunction);
        System.out.println(result2);

        //断言函数接口 判断是否大于0
        Predicate<Integer> predicate = i -> i > 0 ;
        final boolean test = predicate.test(10);
        System.out.println(test);

        //消费函数接口 无返回值 s -> System.out.println(s);
        Consumer<String> consumer = System.out::println;
        consumer.accept("hello word");
    }

}
