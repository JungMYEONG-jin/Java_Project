package com.simpleauthJPA.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Slf4j
@Aspect
public class SALogAspect {

    @Pointcut("execution(* com.simpleauthJPA.repository.*..*(..))")
    public void doLog(){}

    @Before(value = "doLog()")
    public void beforeLog(JoinPoint joinPoint){
        String methodName = getMethodName(joinPoint);
        log.info("===== method {} start=====", methodName);
        Object[] args = joinPoint.getArgs();
        if(args.length == 0)
        {
            log.info("no parameter");
            return;
        }
        for (Object arg : args) {
            log.info("parameter type = {}", args.getClass().getSimpleName());
            log.info("parameter value = {}", arg);
        }
    }

    @AfterReturning(value = "doLog()", returning = "result")
    public void afterLog(JoinPoint joinPoint, Object result){
        String methodName = getMethodName(joinPoint);
        log.info("===== method {} finished=====", methodName);
        log.info("===== result {} ======", result);
    }



    private String getMethodName(JoinPoint joinPoint){
        MethodSignature method  = (MethodSignature) joinPoint.getSignature();
        return method.getName();
    }
}
