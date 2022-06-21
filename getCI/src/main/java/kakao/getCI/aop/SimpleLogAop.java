package kakao.getCI.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
public class SimpleLogAop {

    // 주요 기능에만 적용
    @Pointcut("execution(* kakao.getCI.com.shinhan.security.imple..*(..))")
    public void cut() {}

    // pointcut에 의해 필터링된 경로로 들어오는 경우 메서드 호출 전에 적용
    @Before("cut()")
    public void beforeLog(JoinPoint joinPoint){
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

    // pointcut 통과후 메서드 리턴에
    @AfterReturning(value = "cut()")
    public void afterReturnLog(JoinPoint joinPoint){
        Method method = getMethodName(joinPoint);
        log.info("===== method {} finished=====", method.getName());
    }


    private Method getMethodName(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
