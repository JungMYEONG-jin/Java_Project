package springbook.hello;

import kakao.getCI.springbook.hello.Hello;
import kakao.getCI.springbook.hello.HelloTarget;
import kakao.getCI.springbook.hello.HelloUppercase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import kakao.getCI.springbook.hello.handler.UppercaseHandler;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = Runnable.class)
class HelloTest {

    @Test
    void simpleProxy()
    {
        HelloTarget helloTarget = new HelloTarget();
        assertThat(helloTarget.sayHello("MJ")).isEqualTo("Hello MJ");
        assertThat(helloTarget.sayHi("MJ")).isEqualTo("Hi MJ");
        assertThat(helloTarget.sayThankYou("MJ")).isEqualTo("Thank you MJ");

        Hello hello = new HelloUppercase(new HelloTarget());
        assertThat(hello.sayHello("MJ")).isEqualTo("HELLO MJ");
        assertThat(hello.sayHi("MJ")).isEqualTo("HI MJ");
        assertThat(hello.sayThankYou("MJ")).isEqualTo("THANK YOU MJ");


    }

    @Test
    void dynamicProxyTest()
    {
        Hello proxyInstance = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Hello.class}, new UppercaseHandler(new HelloTarget()));
        String jj = proxyInstance.sayThankYou("jj");
        System.out.println("jj = " + jj);

        String bye = proxyInstance.byeHello("hh");
        System.out.println("bye = " + bye);
    }

}