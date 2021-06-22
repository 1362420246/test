package com.qbk.pattern.factory.method;

/**
 * 工厂方法 是工厂的工厂
 */
public class FatoryMethodTest {
    public static void main(String[] args) {
        CourseFactory factory = new JavaCouseFactory();
        Course course = factory.create();
        course.study();
    }
}
