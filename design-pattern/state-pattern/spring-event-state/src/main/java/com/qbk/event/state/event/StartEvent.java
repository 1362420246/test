package com.qbk.event.state.event;

import com.qbk.event.state.pojo.MeetingEntity;

/**
 *  开启预约会议事件
 */
public class StartEvent extends AbstractMeetingStateEvent {

    public StartEvent(MeetingEntity meetingEntity) {
        super(meetingEntity);
    }
}
