package com.qbk.event.state.event;

import com.qbk.event.state.pojo.MeetingEntity;

/**
 *  取消预约会议事件
 */
public class CancelEvent extends AbstractMeetingStateEvent {

    public CancelEvent(MeetingEntity meetingEntity) {
        super(meetingEntity);
    }
}
