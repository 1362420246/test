package com.qbk.event.state.enums;

import com.qbk.event.state.event.AbstractMeetingStateEvent;
import com.qbk.event.state.pojo.MeetingEntity;

/**
 *  会议状态
 */
public interface MeetingState {

    /**
     * 转换会议状态
     * @param originalState 原来的会议状态
     * @param meetingEntity 会议信息
     * @return 转换状态触发的事件
     */
    AbstractMeetingStateEvent transitionStatus(MeetingStateEnum originalState,MeetingEntity meetingEntity);
}
