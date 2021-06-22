package com.qbk.event.state.event;

import com.qbk.event.state.pojo.MeetingEntity;

/**
 * 结束会议事件
 */
public class FinishEvent extends AbstractMeetingStateEvent {

    public FinishEvent(MeetingEntity meetingEntity) {
        super(meetingEntity);
    }
}
