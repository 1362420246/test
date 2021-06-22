package com.qbk.pattern.factory.simple;


import com.qbk.pattern.factory.simple.Course;

public class JavaCouse implements Course {
    @Override
    public void study() {
        System.out.println("学习java");
    }
}
