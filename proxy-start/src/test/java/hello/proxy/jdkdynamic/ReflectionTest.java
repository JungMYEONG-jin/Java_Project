package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    @Test
    void reflection0(){
        Hello target = new Hello();

        log.info("start");
        String result = target.callA();
        log.info("result={}", result);


        log.info("start");
        String result2 = target.callB();
        log.info("result={}",result2);
    }

    @Test
    void reflection1() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //class info
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        Method callA = classHello.getMethod("callA");
        Object result = callA.invoke(target); // target의 callA 호출
        log.info("result1={}", result);

        Method callB = classHello.getMethod("callB");
        Object result2 = callB.invoke(target); // target의 callA 호출
        log.info("result1={}", result2);
    }

    @Test
    void reflection2() throws Exception {
        //class info
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        Method callA = classHello.getMethod("callA");
        dynamicCall(callA, target);

        Method callB = classHello.getMethod("callB");
        dynamicCall(callB, target);


    }

    private void dynamicCall(Method method, Object object) throws InvocationTargetException, IllegalAccessException {
        log.info("start");
        Object result = method.invoke(object);
        log.info("result={}", result);
    }


    @Slf4j
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


}
