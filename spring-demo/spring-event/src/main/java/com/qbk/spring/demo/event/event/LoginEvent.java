package com.qbk.spring.demo.event.event;

import lombok.Getter;

import java.util.Map;

/**
 * 登陆事件
 */
@Getter
public class LoginEvent extends AbstractEvent {

    private Map<String,String> source ;

    public LoginEvent(Map<String,String> source) {
        super(source);
        this.source = source;
    }
}
