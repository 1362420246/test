package com.qbk.webflux.lambdademo;

/**
 * lambad 的 类型推断
 */
public class LambdaTypeInferDemo {

    interface Math{
        int add(int x, int y);
    }
    interface Math2{
        int add(int x, int y);
    }
    public static void main(String[] args) {
        //变量类型定义
        Math math = (x,y) -> x + y ;
        int result = math.add(3, 5);
        System.out.println(result);

        //数组
        Math[] maths = {(x,y) -> x + y};

        //强转
        Object o =(Math) (x,y) -> x + y ;

        //通过返回类型
        Math math2 = createLambda();

        //方法参数
        int result2 = add((x,y) -> x - y);
        System.out.println(result2);
        //当有二义性的时候 使用强转接口解决
        int result3 =test((Math2)(x,y) -> x * y);
        System.out.println(result3);
    }

    public static int test(Math math){
        return math.add(6, 4);
    }
    public static int test(Math2 math){
        return math.add(6, 4);
    }

    public static int add(Math math){
        return math.add(6, 4);
    }
    public static Math createLambda(){
        return  (x,y) -> x + y ;
    }
}
