package com.qbk.annotation.aop;

import com.qbk.annotation.anno.QbkAnno;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {

    @Around("@annotation(qbkAnno)")
    public Object arround(ProceedingJoinPoint point, QbkAnno qbkAnno)throws Throwable{
        System.out.println("aop:" + qbkAnno.value());
        Object proceed = point.proceed();
        return proceed;
    }

    @Pointcut("execution(* com.qbk.annotation.service.*.*(..))")
    private void myPointcut(){}

    @Before("myPointcut()")
    public void validateAccount(JoinPoint joinPoint){
        final Object target = joinPoint.getTarget();
        System.out.println("aop:" + target);
    }
}
