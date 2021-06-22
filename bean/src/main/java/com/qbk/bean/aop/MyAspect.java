package com.qbk.bean.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
@Aspect
public class MyAspect {

    @Pointcut("execution(* com.qbk.bean.service.*.*(..))")
    private void myPointcut(){}

    @Before("myPointcut()")
    public void validateAccount(JoinPoint joinPoint){
        final Object target = joinPoint.getTarget();
        System.out.println("aop:" + target);
    }
}
