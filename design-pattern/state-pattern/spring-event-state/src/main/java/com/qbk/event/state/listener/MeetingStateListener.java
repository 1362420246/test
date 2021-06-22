package com.qbk.event.state.listener;

import com.qbk.event.state.event.CancelEvent;
import com.qbk.event.state.event.CreateEvent;
import com.qbk.event.state.event.FinishEvent;
import com.qbk.event.state.event.StartEvent;
import com.qbk.event.state.pojo.MeetingEntity;
import com.qbk.event.state.pojo.Source;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  会议状态监听
 */
@Component
public class MeetingStateListener {

    /**
     * 创建
     */
    @EventListener
    public void handleCreate (CreateEvent event){
        final MeetingEntity meetingEntity = event.getMeetingEntity();
        // 0 预约会议 其他 普通会议
        if(0 == meetingEntity.getType()){
            System.out.println("创建预约会议：" + meetingEntity.toString());
        }else {
            System.out.println("创建普通会议：" + meetingEntity.toString());
        }
        Source.DATA.put(meetingEntity.getId(),meetingEntity);
    }

    /**
     * 结束
     */
    @EventListener
    public void handleFinish (FinishEvent event){
        MeetingEntity meetingEntity = event.getMeetingEntity();
        if(Objects.isNull(meetingEntity)){
            System.out.println("结束会议失败，无此会议");
        }else {
            System.out.println("结束会议：" + meetingEntity.toString());
            Source.DATA.remove(meetingEntity.getId());
        }
    }

    /**
     * 开启
     */
    @EventListener
    public void handleStart (StartEvent event){
        MeetingEntity meetingEntity = event.getMeetingEntity();
        meetingEntity = Source.DATA.get(meetingEntity.getId());
        // 0 预约会议 其他 普通会议
        if(Objects.isNull(meetingEntity) || 0 != meetingEntity.getType() ){
            System.out.println("开启会议失败，无此会议");
        }else {
            // 会议状态 0 预约 1 进行中 2 结束 3 取消
            meetingEntity.setStatus(1);
            System.out.println("开启会议：" + meetingEntity.toString());
        }
    }

    /**
     * 取消
     */
    @EventListener
    public void handleCancel (CancelEvent event){
        MeetingEntity meetingEntity = event.getMeetingEntity();
        meetingEntity = Source.DATA.get(meetingEntity.getId());
        // 0 预约会议 其他 普通会议
        if(Objects.isNull(meetingEntity) || 0 != meetingEntity.getType() ){
            System.out.println("取消会议失败，无此会议");
        }else {
            System.out.println("取消会议：" + meetingEntity.toString());
            Source.DATA.remove(meetingEntity.getId());
        }
    }
}
