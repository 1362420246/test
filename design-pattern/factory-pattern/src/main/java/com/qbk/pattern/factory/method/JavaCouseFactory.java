package com.qbk.pattern.factory.method;

public class JavaCouseFactory implements CourseFactory {
    @Override
    public Course create() {
        return new JavaCouse();
    }
}
