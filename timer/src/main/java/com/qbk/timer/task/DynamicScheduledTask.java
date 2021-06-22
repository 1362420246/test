package com.qbk.timer.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务调度
 **/
@Slf4j
@Component
public class DynamicScheduledTask implements SchedulingConfigurer {

    /**
     * 注入任务调度器
     */
    @Autowired
    private TaskScheduler scheduler;

    /**
     * 把定时任务的id 和 定时任务的结果 做绑定
     */
    private static Map<String, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<>();

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        //TODO 可以提前加载持久化的定时任务
        addTask("*-*-* *:*:*/5","*/5 * * * * ?","qbk");
    }

    /**
     * 添加定时任务
     * @param runTime 运行时间
     * @param cron cron 表达式
     * @param taskId task 任务唯一标识
     */
    public void addTask(String runTime,String cron,String taskId) {
        ScheduledFuture<?> scheduledFuture = scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                //子线程里执行任务
                String curName = Thread.currentThread().getName() ;
                System.out.println("当前时间:"+ LocalDateTime.now()+",任务对应的线程名: "+curName);
            }
        }, new Trigger() {
            /**
             * 根据给定的触发器上下文确定下一次执行时间
             */
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger cronTrigger = new CronTrigger(cron);
                return cronTrigger.nextExecutionTime(triggerContext);
            }
        });
        taskFutures.put(
                runTime + "-" + (taskId.contains("-") ? taskId.substring(taskId.lastIndexOf("-") + 1):taskId),
                scheduledFuture);
    }

    /**
     * 获取正在执行的任务列表
     */
    public List<String> getTasks(){
        List<String> tasks = new ArrayList<>();
        Set<Map.Entry<String, ScheduledFuture<?>>> entries = taskFutures.entrySet();
        for (Map.Entry<String, ScheduledFuture<?>> next : entries) {
            String key = next.getKey();
            tasks.add(key);
        }
        return tasks;
    }

    /**
     * 取消指定任务
     * @param taskId 任务唯一标识
     * @return boolean
     */
    public boolean cancelTask(String taskId) {
        ScheduledFuture<?> offFuture = taskFutures.get(taskId);
        if(null != offFuture){
            boolean cancel = offFuture.cancel(true);
            boolean cancelled = offFuture.isCancelled();
            //将这个 taskId 的任务从 map 里面移除
            taskFutures.remove(taskId);
            log.info("取消taskId:{}，取消结果:{}，是否取消?:{}",taskId,cancel,cancelled);
            return cancel;
        }else{
            log.error("taskId: {} nonexistent",taskId);
            return false;
        }
    }

    /**
     * 重置指定任务
     * @param runTime 任务运行时间
     * @param taskId taskId 任务唯一标识
     * @param cron cron 表达式
     */
    public void resetTask(String runTime,String taskId,String cron) {
        boolean cancel = cancelTask(taskId);
        if(cancel){
            addTask(runTime,cron,taskId);
        }
    }
}
