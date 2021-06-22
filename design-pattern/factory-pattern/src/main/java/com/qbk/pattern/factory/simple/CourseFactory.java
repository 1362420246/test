package com.qbk.pattern.factory.simple;

/**
 * 工厂
 */
public class CourseFactory {

    /**
     * 利用反射创建
     */
    public static Course create(String className) throws Exception {
        return (Course)Class.forName(className).newInstance();
    }

    /**
     * 利用反射创建
     */
    public static Course create(Class<? extends Course> clazz) throws Exception {
        return clazz.newInstance();
    }
}
