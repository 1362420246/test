package com.qbk.source.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DbKeyEnum {

    /**
     * 主
     */
    MASTER("master"),
    /**
     * 从
     */
    SLAVE_1("slave-1"),
    SLAVE_2("slave-2");

    private String key;
}
