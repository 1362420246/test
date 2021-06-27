package com.qbk.jpa.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString(of = "value" )
public enum Sex {

    MAN("男"),
    WOMAN("女");

    private String value;

}