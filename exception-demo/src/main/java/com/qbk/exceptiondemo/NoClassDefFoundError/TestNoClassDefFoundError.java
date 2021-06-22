package com.qbk.exceptiondemo.NoClassDefFoundError;

/**
 * NoClassDefFoundError产生的原因有好几种，这里记录静态变量或静态块引起的。具体抛出的异常类似：
 * ********* java.lang.NoClassDefFoundError: Could not initialize class xxx
 *
 * JVM在加载类的时候，会初始化类里的静态变量，或执行静态块，如果这个时候抛出了异常，该类就会加载失败，
 * 那么以后任何使用到这个类的地方，都会抛出NoClassDefFoundError异常，如下面的例子：
 */
public class TestNoClassDefFoundError {
    public static void main(String[] args) throws InterruptedException {
        TestNoClassDefFoundError sample = new TestNoClassDefFoundError();
        sample.getClassWithInitErrors();
    }

    private void getClassWithInitErrors() throws InterruptedException {
        System.out.println("第一次new");
        Thread.sleep(500);
        try {
            //第一次new ClassWithInitErrors类，JVM会加载该类，初始化该类的静态变量或执行静态块
            new ClassWithInitErrors();
        } catch (Throwable t) {
            //因为初始化静态变量失败，所以加载类失败。
            t.printStackTrace();
        }

        Thread.sleep(500);
        System.out.println("-----------------------------------------------------");
        System.out.println("第二次new");
        Thread.sleep(500);
        try {
            //第二次new ClassWithInitErrors类，JVM不会再加载该类，而是抛出NoClassDefFoundError异常
            new ClassWithInitErrors();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        Thread.sleep(500);
        System.out.println("-----------------------------------------------------");
        System.out.println("第三次new");
        Thread.sleep(500);
        try {
            //第三次new ClassWithInitErrors类，JVM不会再加载该类，而是抛出NoClassDefFoundError异常
            new ClassWithInitErrors();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

class ClassWithInitErrors {
    static int data = 1 / 0;
}