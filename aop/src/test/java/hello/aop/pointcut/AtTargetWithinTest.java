package hello.aop.pointcut;

import hello.aop.member.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({AtTargetWithinTest.Config.class})
@SpringBootTest
public class AtTargetWithinTest {

    @Autowired
    Child child;

    @Test
    void success() {
        log.info("child proxy = {}", child.getClass());
        child.ChildMethod();
        child.parentMethod();
    }

    static class Config{
        @Bean
        public Parent parent() {return new Parent();}

        @Bean
        public Child child() {return new Child();}

        @Bean
        public AtTargetWithinAspect atTargetWithinAspect() {return new AtTargetWithinAspect();}
    }



    static class Parent{
        public void parentMethod(){}
    }

    @ClassAop
    static class Child extends Parent{
        public void ChildMethod(){}
    }

    @Slf4j
    @Aspect
    static class AtTargetWithinAspect{
        // 애는 해당 타겟의 위아래 전부
        @Around("execution(* hello.aop..*(..)) && @target(hello.aop.member.annotation.ClassAop)")
        public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable{
            log.info("[@target] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // within은 안에만
        @Around("execution(* hello.aop..*(..)) && @within(hello.aop.member.annotation.ClassAop)")
        public Object atWithin(ProceedingJoinPoint joinPoint) throws Throwable{
            log.info("[@within] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
