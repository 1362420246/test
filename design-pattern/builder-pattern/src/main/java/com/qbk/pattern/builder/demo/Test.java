package com.qbk.pattern.builder.demo;

/**
 * 建造者测试
 */
public class Test {

    public static void main(String[] args) {
        Course builder = Course.builder().name("设计模式").ppt("PPT课件").build();
        System.out.println(builder);
    }
}
