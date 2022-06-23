package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 {

    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder() {} //pointcut signature

    @Pointcut("execution(* hello.aop.order..*(..))")
    private void all2() {}


    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }

    @Around("all2()")
    public Object doLog2(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("[log2] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }

}
