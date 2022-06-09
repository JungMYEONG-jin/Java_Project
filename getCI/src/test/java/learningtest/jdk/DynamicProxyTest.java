package learningtest.jdk;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.boot.test.context.SpringBootTest;
import kakao.getCI.springbook.hello.Hello;
import kakao.getCI.springbook.hello.HelloTarget;
import kakao.getCI.springbook.hello.handler.UppercaseHandler;

import java.lang.reflect.Proxy;

@SpringBootTest(classes = Runnable.class)
public class DynamicProxyTest {

    @Test
    void simpleProxy()
    {
        Hello hello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Hello.class}, new UppercaseHandler(new HelloTarget()));
    }

    @Test
    void proxyFactoryBean()
    {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(new HelloTarget());
        factoryBean.addAdvice(new UpperCaseAdvice());

        Hello proxiedHello = (Hello) factoryBean.getObject();

        Assertions.assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        Assertions.assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");



    }

    @Test
    void pointcutAdvisor()
    {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        factoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice()));

        Hello proxiedHello = (Hello) factoryBean.getObject();

        Assertions.assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        Assertions.assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        Assertions.assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank you Toby");



    }




    static class UpperCaseAdvice implements MethodInterceptor
    {


        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String result = (String) invocation.proceed();
            return result.toUpperCase();
        }
    }

    @Test
    void classNamePointcutAdvisor()
    {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut(){
            public ClassFilter getClassFilter(){
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };

        pointcut.setMappedName("sayH*");

        checkAdviced(new HelloTarget(), pointcut, true);
    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(target);
        factoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice()));
        Hello proxiedHello = (Hello)factoryBean.getObject();

        if(adviced)
        {
            Assertions.assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
            Assertions.assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
            Assertions.assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank you Toby");
        }else{
            Assertions.assertThat(proxiedHello.sayHello("Toby")).isEqualTo("Hello Toby");
            Assertions.assertThat(proxiedHello.sayHi("Toby")).isEqualTo("Hi Toby");
            Assertions.assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank you Toby");
        }
    }
}
