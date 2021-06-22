package com.qbk.timerdynamic.task;

import com.qbk.timerdynamic.confg.AbstractSchedulingConfigurer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskDemo1 extends AbstractSchedulingConfigurer {

    public final static String NAME = "task1";

    private MyTriggerTask triggerTask;

    public MyTriggerTask getTriggerTask() {
        return triggerTask;
    }

    @Override
    public String getTaskName() {
        return NAME;
    }

    @Override
    protected void setTriggerTask(MyTriggerTask myTriggerTask) {
        triggerTask = myTriggerTask;
    }

    @Override
    protected boolean isExecute() {
        //从数据库中查询 定时器状态
        return true;
    }

    @Override
    public void processTask() {
        System.out.println("TaskDemo1基于接口SchedulingConfigurer的动态定时任务:"
                + LocalDateTime.now()+"，线程名称："+Thread.currentThread().getName()
                + "线程组："+Thread.currentThread().getThreadGroup()
                + " 线程id："+Thread.currentThread().getId());
    }

    @Override
    public String getCron() {
        return "0/5 * * * * ?";
    }
}
