package com.qbk.log;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 测试log打印 不重写toString方法
 */
@Setter
@Getter
public class LogVO implements Serializable {
    private static final long serialVersionUID = -3160226312506252613L;
    private String name;
    private int age;
}
