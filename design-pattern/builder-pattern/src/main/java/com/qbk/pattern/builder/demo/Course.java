package com.qbk.pattern.builder.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  目标类
 */
@Data
@AllArgsConstructor
public class Course {

    private String name;
    private String ppt;

    public static Course.CourseBuilder builder() {
        return new Course.CourseBuilder();
    }

    /**
     * 构建者
     */
    public static class CourseBuilder {
        private String name;
        private String ppt;

        CourseBuilder() {}

        public Course.CourseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Course.CourseBuilder ppt(String ppt) {
            this.ppt = ppt;
            return this;
        }

        public Course build() {
            return new Course(this.name, this.ppt);
        }
    }
}
