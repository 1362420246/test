package com.qbk.pattern.factory.method;

/**
 * Created by 86186 on 2020/5/5.
 */
public class PythonCouseFactory implements CourseFactory {
    @Override
    public Course create() {
        return new PythonCouse();
    }
}
