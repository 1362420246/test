package com.qbk.spring.demo.event.event;

import org.springframework.context.ApplicationEvent;

/**
 * 事件基类
 */
public abstract class AbstractEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AbstractEvent(Object source) {
        super(source);
    }
}
