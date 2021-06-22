package com.qbk.pattern.template.service;

import org.springframework.stereotype.Service;

/**
 * 临时会议
 */
@Service
public class TemporaryMeetingService extends BaseMeetingService {
    /**
     * 插入数据库
     */
    @Override
    public void insert() {
        System.out.println("插入临时会议成功");
    }
}