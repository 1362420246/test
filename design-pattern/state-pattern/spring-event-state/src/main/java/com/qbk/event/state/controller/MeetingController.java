package com.qbk.event.state.controller;

import com.qbk.event.state.enums.MeetingStateEnum;
import com.qbk.event.state.event.AbstractMeetingStateEvent;
import com.qbk.event.state.pojo.MeetingEntity;
import com.qbk.event.state.pojo.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 会议
 *
 * 状态模式：内部状态转变，触发对应的事件
 */
@RestController
public class MeetingController {

    @Autowired
    private ApplicationContext context;

    /**
     * 创建会议
     */
    @GetMapping("/create")
    public String create(MeetingEntity meetingEntity){
        AbstractMeetingStateEvent event;
        // 0 预约会议 其他 普通会议
        if( 0 == meetingEntity.getType()){
            //会议状态 0 预约 1 进行中 2 结束 3 取消
            meetingEntity.setStatus(0);
            //装换 状态
            event = MeetingStateEnum.SCHEDULE.transitionStatus(null, meetingEntity);
        }else {
            //会议状态 0 预约 1 进行中 2 结束 3 取消
            meetingEntity.setStatus(1);
            //装换 状态
            event = MeetingStateEnum.MEETING.transitionStatus(null, meetingEntity);
        }
        //发送事件
        context.publishEvent(event);
        return "成功";
    }

    /**
     * 结束会议
     */
    @GetMapping("/finish")
    public String finish(Integer id){
        MeetingEntity entity = Source.DATA.get(id);
        if(Objects.isNull(entity)){
            System.out.println("结束失败：无此会议");
            return "结束失败：无此会议";
        }
        //装换 状态
        AbstractMeetingStateEvent event =
                MeetingStateEnum.FINISH.transitionStatus(MeetingStateEnum.get(entity.getStatus()), entity);
        //发送事件
        context.publishEvent(event);
        return "成功";
    }

    /**
     * 开启预约会议
     */
    @GetMapping("/start")
    public String start(Integer id){
        MeetingEntity entity = Source.DATA.get(id);
        if(Objects.isNull(entity)){
            System.out.println("开启失败：无此会议");
            return "开启失败：无此会议";
        }
        //装换 状态
        AbstractMeetingStateEvent event =
                MeetingStateEnum.MEETING.transitionStatus(MeetingStateEnum.get(entity.getStatus()), entity);
        //发送事件
        context.publishEvent(event);
        return "成功";
    }

    /**
     * 取消预约会议
     */
    @GetMapping("/cancel")
    public String cancel(Integer id){
        MeetingEntity entity = Source.DATA.get(id);
        if(Objects.isNull(entity)){
            System.out.println("取消失败：无此会议");
            return "取消失败：无此会议";
        }
        //装换 状态
        AbstractMeetingStateEvent event =
                MeetingStateEnum.CANCEL.transitionStatus(MeetingStateEnum.get(entity.getStatus()), entity);
        //发送事件
        context.publishEvent(event);
        return "成功";
    }
}
