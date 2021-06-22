package com.qbk.timer.enums;

import com.qbk.timer.annotation.DynamicSchedule;
import org.aspectj.lang.JoinPoint;

import java.util.List;

/**
 * 操作类型接口
 **/
public interface OperationType {
    List<String> handle(JoinPoint joinPoint, DynamicSchedule dynamicSchedule) throws Exception;
}
