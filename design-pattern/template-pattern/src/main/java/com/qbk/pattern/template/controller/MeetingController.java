package com.qbk.pattern.template.controller;

import com.qbk.pattern.template.service.GeneralMeetingService;
import com.qbk.pattern.template.service.TemporaryMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 模板模式
 *
 * 会议业务
 */
@RestController
public class MeetingController {

    @Autowired
    private GeneralMeetingService generalMeetingService;

    @Autowired
    private TemporaryMeetingService temporaryMeetingService;

    /**
     * 创建普通会议
     */
    @GetMapping("/create/general")
    public String createGeneralMeeting(String meetingName ,Integer creator){
        boolean result = generalMeetingService.createMeeting(meetingName, creator);
        return result ? "成功" : "失败";
    }

    /**
     * 创建临时会议
     */
    @GetMapping("/create/temporary")
    public String createTemporaryMeeting(String meetingName ,Integer creator){
        boolean result = temporaryMeetingService.createMeeting(meetingName,creator);
        return result ? "成功" : "失败";
    }
}