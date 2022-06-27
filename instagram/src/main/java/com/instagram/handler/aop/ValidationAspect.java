package com.instagram.handler.aop;

import com.instagram.handler.exception.CustomValidationAPIException;
import com.instagram.handler.exception.CustomValidationException;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class ValidationAspect {

    // validation instance면 aop 적용
    // 클라이언트 호출을 가로챘으므로 proceed 해줘야 함.
    @Around("execution(* com.instagram.controller.api.*Controller.*(..))")
    public Object doAPICheck(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult){
                log.info("유효성 검사를 하는 method 입니다. {}", arg);
                BindingResult bindingResult = (BindingResult) arg;

                // if has error throw exception
                if (bindingResult.hasErrors()){
                    Map<String, String> errorMap = new HashMap<>();
                    for(FieldError error : bindingResult.getFieldErrors()){
                        errorMap.put(error.getField(), error.getDefaultMessage());
                    }
                    throw new CustomValidationAPIException("유효성 검사 살패", errorMap);
                }
            }
        }
        return joinPoint.proceed();
    }

    @Around("execution(* com.instagram.controller.*Controller.*(..))")
    public Object doCheck(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult){
                log.info("유효성 검사를 하는 method 입니다. {}", arg);
                BindingResult bindingResult = (BindingResult) arg;

                // if has error throw exception
                if (bindingResult.hasErrors()){
                    Map<String, String> errorMap = new HashMap<>();
                    for(FieldError error : bindingResult.getFieldErrors()){
                        errorMap.put(error.getField(), error.getDefaultMessage());
                    }
                    throw new CustomValidationException("유효성 검사 살패", errorMap);
                }
            }
        }
        return joinPoint.proceed();
    }
}
