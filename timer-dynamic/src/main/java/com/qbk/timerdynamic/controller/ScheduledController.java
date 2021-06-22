package com.qbk.timerdynamic.controller;

import com.qbk.timerdynamic.task.MyTriggerTask;
import com.qbk.timerdynamic.task.TaskDemo2;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.Task;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * 无法使用 SchedulingConfigurer 使用做动态定时器
 * 原因 无法删减ScheduledTaskRegistrar 中的scheduledTasks列表
 *
 * 需要自己管理 ScheduledFuture 列表
 */
@RestController
public class ScheduledController {

    @Autowired
    private TaskDemo2 taskDemo2;

    private final Map<String,ScheduledFuture<?>> futures = new HashMap<>();

    @GetMapping("/cancel")
    public String cancel(){
        Set<ScheduledTask> scheduledTasks = taskDemo2.getTaskRegistrar().getScheduledTasks();
        for (ScheduledTask scheduledTask : scheduledTasks) {
            if(scheduledTask.getTask().equals(taskDemo2.getTriggerTask())){
                scheduledTask.cancel();
            }
        }
        futures.forEach(
                (name , future) -> {
                    if(name.equals(taskDemo2.getTaskName())){
                        future.cancel(true);
                    }
                }
        );
        return "s";
    }

    @GetMapping("/add")
    public String add(){
        Runnable task = () -> taskDemo2.processTask();
        Trigger trigger = new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                String cron = taskDemo2.getCron();
                CronTrigger trigger = new CronTrigger(cron);
                return trigger.nextExecutionTime(triggerContext);
            }
        };
        MyTriggerTask triggerTask = new MyTriggerTask(task,trigger,TaskDemo2.NAME);


        TaskScheduler scheduler = taskDemo2.getTaskRegistrar().getScheduler();
        ScheduledFuture<?> future = scheduler.schedule(task, trigger);
        futures.put(taskDemo2.getTaskName(),future);

//        taskDemo2.getTaskRegistrar().scheduleTriggerTask(triggerTask);
//        taskDemo2.getTaskRegistrar().afterPropertiesSet();
        return "s";
    }

}
