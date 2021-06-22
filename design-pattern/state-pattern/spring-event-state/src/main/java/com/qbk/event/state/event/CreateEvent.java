package com.qbk.event.state.event;

import com.qbk.event.state.pojo.MeetingEntity;

/**
 * 创建会议事件
 */
public class CreateEvent extends AbstractMeetingStateEvent {

    public CreateEvent(MeetingEntity meetingEntity) {
        super(meetingEntity);
    }
}
