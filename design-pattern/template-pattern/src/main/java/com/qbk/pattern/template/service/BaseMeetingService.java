package com.qbk.pattern.template.service;

import java.util.Arrays;
import java.util.List;

/**
 * 模板模式（Template Pattern）中，一个抽象类公开定义了执行它的方法的方式/模板。
 * 它的子类可以按需要重写方法实现，但调用将以抽象类中定义的方式进行。这种类型的设计模式属于行为型模式。
 *
 * 会议基类
 */
public abstract class BaseMeetingService {

    private final List<String> meetingNames = Arrays.asList("会议1","会议2","会议3");

    private final Integer creator = 1;

    /**
     * 创建会议
     */
    public boolean createMeeting(String meetingName ,Integer creator){
        //校验会议名
        if(meetingNames.contains(meetingName)){
            System.out.println("会议名已存在");
            return false;
        }
        //校验创建者
        if(!this.creator.equals(creator)){
            System.out.println("创建者不存在");
            return false;
        }
        this.insert();
        return true;
    }

    /**
     * 插入数据库
     */
    public abstract void insert();
}