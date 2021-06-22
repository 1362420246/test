package com.qbk.spring.demo.event.event;

import lombok.Getter;

import java.util.Map;

/**
 * ip事件
 */
@Getter
public class IpEvent extends AbstractEvent {

    private Map<String,String> source ;

    public IpEvent(Map<String,String> source) {
        super(source);
        this.source = source;
    }
}
