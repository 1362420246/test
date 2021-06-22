package com.qbk.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * aspectj
 */
@Slf4j
@Aspect
@Component
public class HelloAspect {

    private final ObjectMapper mapper;

    public HelloAspect(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void webLog() {

    }

    /**
     * 前置通知
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        for (Object object : joinPoint.getArgs()) {
            if (object instanceof MultipartFile || object instanceof HttpServletRequest
                    || object instanceof HttpServletResponse) {
                continue;
            }
            if (log.isDebugEnabled()) {
                log.debug(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()
                        + " : request parameter : " + mapper.writeValueAsString(object));
            }
        }
    }

    /**
     * 后置通知
     */
    @AfterReturning(returning = "response", pointcut = "webLog()")
    public void doAfterReturning(Object response) throws Throwable {
        if (response != null) {
            log.info("response parameter : " + mapper.writeValueAsString(response));
        }
    }

    /**
     * 前置通知 并直接配置切入点
     */
    @Before(value = "execution(String com.qbk.aop.HelloController.index())")
    public void before(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        System.out.println("Hello:" + method.getName());
    }
}
