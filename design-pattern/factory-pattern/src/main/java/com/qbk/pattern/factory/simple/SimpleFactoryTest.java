package com.qbk.pattern.factory.simple;

/**
 * 简单工厂 是产品的工厂
 */
public class SimpleFactoryTest {
    public static void main(String[] args) throws Exception {

        Course course = CourseFactory.create("com.qbk.pattern.factory.simple.JavaCouse");
        course.study();

        Course course2 = CourseFactory.create(PythonCouse.class);
        course2.study();
    }
}
