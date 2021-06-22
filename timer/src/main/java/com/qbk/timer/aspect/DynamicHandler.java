package com.qbk.timer.aspect;

import com.qbk.timer.annotation.DynamicSchedule;
import com.qbk.timer.enums.DynamicOperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.List;


/**
 * 切面处理类
 **/
@Aspect
@Component
@Order(0)
public class DynamicHandler {

    @Pointcut("@annotation(dynamicSchedule)")
    public void dynamicPointCut(DynamicSchedule dynamicSchedule) {

    }
    @Before("@annotation(dynamicSchedule)")
    public void doBefore(JoinPoint joinPoint,DynamicSchedule dynamicSchedule) throws Throwable {
        if(!StringUtils.isEmpty(dynamicSchedule.scheduleMethod())) {
            dynamicSchedule.dynamicOperationType().handle(joinPoint, dynamicSchedule);
        }
    }
    @Around(value = "dynamicPointCut(dynamicSchedule)", argNames = "joinPoint,dynamicSchedule")
    public Object doAround(ProceedingJoinPoint joinPoint,DynamicSchedule dynamicSchedule) throws Throwable {
        Object ob = joinPoint.proceed();
        if(StringUtils.isEmpty(ob) && DynamicOperationType.LIST.name().equals(dynamicSchedule.dynamicOperationType().name())){
            ob = dynamicSchedule.dynamicOperationType().handle(joinPoint, dynamicSchedule);
        }
        return ob;
    }
}
