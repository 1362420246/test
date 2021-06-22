package com.qbk.timer.enums;


import com.qbk.timer.annotation.DynamicSchedule;
import com.qbk.timer.model.AddForDynamicDTO;
import com.qbk.timer.model.CancelForDynamicDTO;
import com.qbk.timer.model.UpdateForDynamicDTO;
import com.qbk.timer.task.DynamicScheduledTask;
import com.qbk.timer.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

/**
 * 定时任务操作类型
 **/
@Slf4j
public enum DynamicOperationType implements OperationType {

    /**
     * 枚举式单例
     */
    LIST() {
        @Override
        public List<String> handle(JoinPoint joinPoint,DynamicSchedule dynamicSchedule)throws Exception {
            return dynamicScheduledTask.getTasks();
        }
    },

    ADD(){
        @Override
        public List<String> handle(JoinPoint joinPoint, DynamicSchedule dynamicSchedule) throws Exception {
            String name = dynamicSchedule.scheduleMethod();
            Object[] args = getArgs(joinPoint,name);
            AddForDynamicDTO arg = (AddForDynamicDTO)args[0];
            // invoke
            String runTime = arg.getRunTime();
            String cronTime = runTime.replaceAll(":", "-").replaceAll(" ", "-");
            String[] date = cronTime.split("-");
            String cron = DateUtil.getCron(date);
            String taskid = UUID.randomUUID().toString().replaceAll("-", "");
            dynamicScheduledTask.addTask(cronTime,cron, taskid);
            return new ArrayList<>();
        }
    },
    UPDATE() {
        @Override
        public List<String> handle(JoinPoint joinPoint,DynamicSchedule dynamicSchedule)throws Exception{
            String name = dynamicSchedule.scheduleMethod();
            Object[] args = getArgs(joinPoint,name);
            UpdateForDynamicDTO arg = (UpdateForDynamicDTO)args[0];
            String cronTime = arg.getRunTime().replaceAll(":", "-").replaceAll(" ", "-");
            String cron = DateUtil.getCron(cronTime.split("-"));
            dynamicScheduledTask.resetTask(cronTime, arg.getTaskId(), cron);
            return new ArrayList<>();
        }
    },
    CANCEL() {
        @Override
        public List<String> handle(JoinPoint joinPoint,DynamicSchedule dynamicSchedule)throws Exception{
            String name = dynamicSchedule.scheduleMethod();
            Object[] args = getArgs(joinPoint,name);
            CancelForDynamicDTO arg = (CancelForDynamicDTO)args[0];
            // invoke
            Object res = null;
            String taskId = arg.getTaskId();
            boolean resetTask = dynamicScheduledTask.cancelTask(taskId);
            return new ArrayList<>();
        }
    };

    /**
     * 定时任务调度
     */
    public DynamicScheduledTask dynamicScheduledTask;

    public void setDynamicScheduledTask(DynamicScheduledTask dynamicScheduledTask){
        this.dynamicScheduledTask = dynamicScheduledTask;
    }

    @Component
    public static class DynamicScheduleServiceInjector{
        @Autowired
        private DynamicScheduledTask dynamicScheduledTask;
        @PostConstruct
        public void postConstruct(){
            //遍历枚举 注入属性
            for(DynamicOperationType type: EnumSet.allOf(DynamicOperationType.class)){
                type.setDynamicScheduledTask(dynamicScheduledTask);
            }
        }
    }

    /**
     * 获取参数
     * @param joinPoint 连接点
     * @param name 自定义注解的值
     */
    private static Object[] getArgs(JoinPoint joinPoint,String name) throws Exception {
        Method currentMethod = ((MethodSignature)joinPoint.getSignature()).getMethod();
        Method handleMethod = joinPoint.getTarget().getClass().getDeclaredMethod(name, currentMethod.getParameterTypes());
        handleMethod.setAccessible(true);
        return joinPoint.getArgs();
    }
}
