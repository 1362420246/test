package com.qbk.enumdemo.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
//@ToString(of = {"value"})
public enum SexEnum {
    MEN("男"),
    WOMEN("女");
    private String value;

    /**
     * 序列化 可以搭配 spring.jackson.serialization.WRITE_ENUMS_USING_TO_STRING=true
     */
    @Override
    public String toString() {
        return value;
    }
}
