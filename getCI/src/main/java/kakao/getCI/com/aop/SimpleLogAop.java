package kakao.getCI.com.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Proc;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class SimpleLogAop {

    // 주요 기능에만 적용
//    @Pointcut("kakao.getCI.aop.SimpleLogAop.cut()")
//    @Pointcut("execution(* kakao.getCI.com.shinhan.security.imple.AopTest.test1(..))")
    @Pointcut("execution(* kakao.getCI.com.shinhan.security.imple.*..*(..))")
    public void cut() {}

    // pointcut에 의해 필터링된 경로로 들어오는 경우 메서드 호출 전에 적용
    @Before(value = "cut()")
    public void beforeLog(JoinPoint joinPoint){
        joinPoint.getSignature();
        Method method =  getMethodName(joinPoint);
        log.info("===== method {} start=====", method.getName());
        System.out.println(method.getName());
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

    @Around("cut()")
    public void check(ProceedingJoinPoint joinPoint){
        String name = joinPoint.getSignature().getName();
        log.info("name = {}", name);
    }

    // pointcut 통과후 메서드 리턴에
    @AfterReturning(value = "cut()")
    public void afterReturnLog(JoinPoint joinPoint){
        Method method = getMethodName(joinPoint);
        log.info("===== method {} finished=====", method.getName());
    }


    public Method getMethodName(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
