package com.qbk.timer.annotation;


import com.qbk.timer.enums.DynamicOperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务注解
 **/
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DynamicSchedule {

    /**
     * 定时任务方法
     * @return string
     */
    String scheduleMethod() default "";

    /**
     * 定时任务操作类型
     * @return DynamicOperationType
     */
    DynamicOperationType dynamicOperationType();

}
