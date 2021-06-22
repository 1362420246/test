package com.qbk.event.state.event;


import com.qbk.event.state.pojo.MeetingEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 基础会议状态事件
 */
@Getter
@Setter
public abstract class AbstractMeetingStateEvent extends ApplicationEvent {

    protected MeetingEntity meetingEntity;

    public AbstractMeetingStateEvent(MeetingEntity meetingEntity) {
        super(meetingEntity);
        this.meetingEntity = meetingEntity;
    }
}
