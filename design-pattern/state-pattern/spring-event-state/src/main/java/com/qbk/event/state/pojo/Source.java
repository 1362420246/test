package com.qbk.event.state.pojo;

import java.util.concurrent.ConcurrentHashMap;

/**
 *  数据
 */
public interface Source {
    ConcurrentHashMap<Integer , MeetingEntity> DATA = new ConcurrentHashMap<>();
}
