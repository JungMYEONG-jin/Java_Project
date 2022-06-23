package hello.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    // 해당 패키지와 하위 패키지 전부
    @Pointcut("execution(* hello.aop.order..*(..))")
    public void allOrder(){}

    // 패턴 service
    @Pointcut("execution(* *..*Service.*(..))")
    public void allService(){}

    @Pointcut("allOrder() && allService()")
    public void orderAndService(){}
}
