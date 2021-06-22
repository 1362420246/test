package com.qbk.enumdemo.paramconver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * jackson 序列化
 * 序列化的结构时 使用(JSON) Object
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
@ToString(of = {"code","value"})
public enum VipEnum {
    HUANG(1, "黄钻"),
    HONG(2, "红钻");

    private Integer code;
    private String value;

    /**
     * jackson 反序列化
     * 使用@JsonCreator注解加在工厂静态方法上，可以使用静态工厂函数反序列化构造对象
     * JsonCreator只能用在静态方法或者构造方法，实例方法是不行的。因为还没构造出对象。
     */
    @JsonCreator
    public static VipEnum findstr(int code){
        for (VipEnum item : VipEnum.values()) {
            if (item.getCode().equals(code) ) {
                return item;
            }
        }
        return HUANG;
    }
}
