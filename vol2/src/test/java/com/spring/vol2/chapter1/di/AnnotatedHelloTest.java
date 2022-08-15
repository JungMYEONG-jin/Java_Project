package com.spring.vol2.chapter1.di;

import com.spring.vol2.chapter1.di.config.AnnotatedHelloConfig;
import com.spring.vol2.chapter1.di.config.HelloConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AnnotatedHelloTest {

    @DisplayName("빈스캐닝 성공 테스트")
    @Test
    void simpleBeanScanningTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.spring.vol2.chapter1.di");
        AnnotatedHello hello = context.getBean("annotatedHello", AnnotatedHello.class);
        assertThat(hello).isNotNull();
    }

    @DisplayName("빈스캐닝 실패 테스트")
    @Test
    void simpleBeanScanningTestFail() {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.spring.vol2.chapter1.di");
        assertThatThrownBy(()->context.getBean("AnnotatedHello", AnnotatedHello.class)).isInstanceOf(NoSuchBeanDefinitionException.class);
    }

    @DisplayName("Configuration으로 등록한 빈")
    @Test
    void configBeanTest(){
        ApplicationContext context = new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);
        AnnotatedHello hello = context.getBean("annotatedHello2", AnnotatedHello.class);
        assertThat(hello).isNotNull();
    }

    @DisplayName("Configuration도 @Component로 인해 빈으로 등록됨")
    @Test
    public void configSelfRegisteredBySpring() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);
        AnnotatedHelloConfig annotatedHelloConfig = context.getBean("annotatedHelloConfig", AnnotatedHelloConfig.class);
        assertThat(annotatedHelloConfig).isNotNull();
    }

    @Test
    void arrBeanTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(HelloConfig.class);
        ArrBean arrBean = context.getBean("arrBean", ArrBean.class);
        int[] arr = arrBean.getArr();
        for (int i : arr) {
            System.out.println(i);
        }
    }
}