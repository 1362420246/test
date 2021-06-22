package com.qbk.pattern.template.service;

import org.springframework.stereotype.Service;

/**
 * 普通会议
 */
@Service
public class GeneralMeetingService extends BaseMeetingService {
    /**
     * 插入数据库
     */
    @Override
    public void insert() {
        System.out.println("插入普通会议成功");
    }
}