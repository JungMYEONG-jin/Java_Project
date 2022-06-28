package com.instagram.handler.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.instagram.service.*..*(..))")
    public void allService(){}

}
