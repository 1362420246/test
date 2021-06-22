package com.qbk.webflux.lambdademo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

/**
 * 方法引用 格式
 */
public class MethodsFormatDemo {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User{
        private Integer id;

        public Integer fun(int n){
            return this.id + n;
        }
        public static String fun2(int id){
            return "这个id是" + id;
        }

        /**
         * 编译器会默认给所有非静态方法添加一个参数this，并且是在形参的第一位，可以在字节码种查看到
         * 就是这个参数 加不加 效果都一样
         */
        public void fun3(User this){
            System.out.println("id是" + this.id);
        }
    }

    public static void main(String[] args) {
        //1.构造方法的 方法引用 语法 Class::new
        //函数接口  无参数，返回一个结果
        Supplier<User> supplier = User::new;
        final User user = supplier.get();
        //带参构造方法
        Function<Integer,User> function = User::new;
        User user1 = function.apply(3);

        //2.对象方法的 方法引用 语法 instance::method
        //函数接口  输入一个参数,无返回值
        Consumer<Integer> consumer = user::setId;
        //set方法
        consumer.accept(5);
        //get方法
        Supplier<Integer> supplier2 = user::getId;
        Integer id = supplier2.get();

        //函数式接口 输入一个参数，返回一个同类型结果
        UnaryOperator<Integer> unaryOperator = user::fun;
        //链式编程  把方法fun执行结果 后面添加东西 并修改类型
        String result = unaryOperator.andThen(i -> i + "天").apply(3);
        System.out.println(result);

        //3静态方法的 方法引用 语法 Class::static_method
        //函数接口  输入一个参数，返回一个接口
        Function<Integer, String> fun2 = User::fun2;
        final String str = fun2.apply(id);
        System.out.println(str);

        //4.类型方法的 方法引用 语法 Class::method
        //这里为什么用一个参数无返回值函数接口呢？
        Consumer<User> consumer2 = User::fun3;
        //因为 jdk 默认会给所有非静态方法添加 一个this参数 在形参第一位
        consumer2.accept(user1);
        //同等效果
        user.fun3();

        //类型方法的 方法引用 例子2
        // 两个参数 一个返回，其中第一个参数是this
        BiFunction<User,Integer,Integer> fun = User::fun;
        Integer apply = fun.apply(user, 2233);
        System.out.println(apply);

        //类型方法的 方法引用 例子3
        List<User> list = new ArrayList<User>(){
            {
                add(new User(2));
                add(new User(3));
            }
        };
        list.forEach(User::fun3);

    }

}
