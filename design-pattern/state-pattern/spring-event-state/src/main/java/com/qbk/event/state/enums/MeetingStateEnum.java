package com.qbk.event.state.enums;

import com.qbk.event.state.event.*;
import com.qbk.event.state.pojo.MeetingEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 会议状态
 */
@Getter
@AllArgsConstructor
public enum MeetingStateEnum implements MeetingState{
    /**
     * 预约
     */
    SCHEDULE(0){
        /**
         * 转换会议状态
         * @param originalState 原来的会议状态
         * @param meetingEntity 会议信息
         * @return 转换状态触发的事件
         */
        @Override
        public AbstractMeetingStateEvent transitionStatus(MeetingStateEnum originalState,
                                                          MeetingEntity meetingEntity) {
            //创建预约会议
            return new CreateEvent(meetingEntity);
        }
    },
    /**
     * 会议中
     */
    MEETING(1) {
        @Override
        public AbstractMeetingStateEvent transitionStatus(MeetingStateEnum originalState,
                                                          MeetingEntity meetingEntity) {
            if(Objects.isNull(originalState)){
                //创建普通会议
                return new CreateEvent(meetingEntity);
            }else if ( SCHEDULE == originalState){
                //开启预约会议
                return new StartEvent(meetingEntity);
            }else {
              throw new RuntimeException("无法转换状态，只有预约状态，或新建普通会议才能转换成会议中状态");
            }
        }
    },
    /**
     * 结束
     */
    FINISH(2) {
        @Override
        public AbstractMeetingStateEvent transitionStatus(MeetingStateEnum originalState,
                                                          MeetingEntity meetingEntity) {
            if ( MEETING == originalState){
                //结束会议
                return new FinishEvent(meetingEntity);
            }else {
                throw new RuntimeException("无法转换状态，只有会议中的状态才能转变成结束状态");
            }
        }
    },
    /**
     * 取消
     */
    CANCEL(3) {
        @Override
        public AbstractMeetingStateEvent transitionStatus(MeetingStateEnum originalState,
                                                          MeetingEntity meetingEntity) {
            if ( SCHEDULE == originalState){
                //取消预约会议
                return new CancelEvent(meetingEntity);
            }else {
                throw new RuntimeException("无法转换状态，只有预约状态才能转变成取消状态");
            }
        }
    };

    /**
     * 会议状态
     */
    private Integer status;

    /**
     * 获取状态
     */
    public static MeetingStateEnum get(Integer status){
        for (MeetingStateEnum stateEnum :MeetingStateEnum.values()) {
            if(status.equals(stateEnum.getStatus())){
                return stateEnum;
            }
        }
        return null;
    }
}
