package com.ecommerce.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

@Slf4j
@Aspect
public class LogAspect {

    @Before("execution(* com.ecommerce.repository.*..*(..))")
    public void beforeLog(JoinPoint joinPoint){
        MethodSignature method = (MethodSignature) joinPoint.getSignature();
        String name = method.getName();
        log.info("method name {}", name);
    }
}
