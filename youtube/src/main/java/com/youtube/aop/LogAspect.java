package com.youtube.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class LogAspect {

    @Around("execution(* com.youtube.controller.*..*(..))")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable{

        String className = joinPoint.getClass().getSimpleName();
        log.info("class {}", className);
        log.info("object {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }


}
