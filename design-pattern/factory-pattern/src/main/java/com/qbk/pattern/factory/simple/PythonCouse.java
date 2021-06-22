package com.qbk.pattern.factory.simple;


import com.qbk.pattern.factory.simple.Course;

public class PythonCouse implements Course {
    @Override
    public void study() {
        System.out.println("学习python");
    }
}
