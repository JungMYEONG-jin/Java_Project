package com.instagram.handler.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.mapping.Join;

@Slf4j
@Aspect
public class LogAspect {

    @Before("com.instagram.handler.aop.Pointcuts.allService()")
    public void logStart(JoinPoint joinPoint){
        log.info("[before] {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "com.instagram.handler.aop.Pointcuts.allService()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result){
        log.info("[after] {}, result = {}", joinPoint.getSignature(), result);
    }
}
