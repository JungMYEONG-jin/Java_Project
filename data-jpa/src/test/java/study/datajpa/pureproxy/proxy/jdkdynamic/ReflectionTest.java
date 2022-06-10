package study.datajpa.pureproxy.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    @Test
    void reflection0(){
        Hello target = new Hello();
        log.info("start");
        String result = target.callA();
        log.info("result={}", result);
        // common logic
        log.info("start");
        String result2 = target.callB();
        log.info("result={}", result2);

    }

    @Test
    void reflection1() throws Exception{
        Class classHello = Class.forName("study.datajpa.pureproxy.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target);
        log.info("result1={}",result1);

        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2={}", result2);
    }

    @Test
    void relfection2() throws Exception {
        Class classHello = Class.forName("study.datajpa.pureproxy.proxy.jdkdynamic.ReflectionTest$Hello");
        Hello target = new Hello();

        Method callA = classHello.getMethod("callA");
        dynamicCall(callA, target);

        Method callB = classHello.getMethod("callB");
        dynamicCall(callB, target);
    }

    static class Hello{

        public String callA(){
            log.info("callA");
            return "A";
        }

        public String callB(){
            log.info("callB");
            return "B";
        }
    }

    private void dynamicCall(Method method, Object target) throws Exception{
        log.info("start");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }
}
