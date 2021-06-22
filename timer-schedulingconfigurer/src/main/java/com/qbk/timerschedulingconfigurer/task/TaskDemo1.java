package com.qbk.timerschedulingconfigurer.task;

import com.qbk.timerschedulingconfigurer.confg.AbstractSchedulingConfigurer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskDemo1 extends AbstractSchedulingConfigurer {

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
