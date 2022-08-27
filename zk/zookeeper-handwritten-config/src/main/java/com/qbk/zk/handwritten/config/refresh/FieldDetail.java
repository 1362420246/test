package com.qbk.zk.handwritten.config.refresh;

import java.lang.reflect.Field;

/**
 * 存储 @Value 属性的字段值
 */
public class FieldDetail {
    /**
     * 字段
     */
    private Field field;
    /**
     * 字段属于的实例
     */
    private Object instance;

    public FieldDetail(Field field, Object instance) {
        this.field = field;
        this.instance = instance;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}