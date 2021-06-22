package com.qbk.timerschedulingconfigurer.confg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ExecutorService;

/**
 * 定时器
 * 实现 SchedulingConfigurer 接口
 */
@Configuration
@EnableScheduling
public abstract class AbstractSchedulingConfigurer implements SchedulingConfigurer {

    @Autowired
    private ExecutorService executorPoolService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //初始化 TaskScheduler
        taskRegistrar.setScheduler(executorPoolService);

        //是否执行
        if(isExecute()){
            //添加任务
            taskRegistrar.addTriggerTask(
                    //执行定时任务
                    this::processTask,
                    //设置触发器
                    triggerContext -> {
                        // 初始化定时任务周期
                        String cron = getCron();
                        CronTrigger trigger = new CronTrigger(cron);
                        return trigger.nextExecutionTime(triggerContext);
                    }
            );
        }
    }

    /**
     * 是否执行
     * 从数据库中查询 定时器状态
     */
    protected abstract boolean isExecute();

    /**
     * 任务的处理函数
     * 本函数需要由派生类根据业务逻辑来实现
     */
    public abstract void processTask();

    /**
     * 获取定时任务周期表达式
     * 本函数由派生类实现，从配置文件，数据库等方式获取参数值
     */
    public abstract String getCron();
}
