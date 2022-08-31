package com.spring.vol2.chapter1.di;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HelloTest {

    @DisplayName("bean 수동 등록 테스트")
    @Test
    void beanReg() {
        StaticApplicationContext ac = new StaticApplicationContext();
        ac.registerSingleton("hello1", Hello.class);

        Hello hello1 = ac.getBean("hello1", Hello.class);
        assertThat(hello1).isNotNull();
    }

    @DisplayName("BeanDefinition을 이용한 빈 등록")
    @Test
    void beanReg2(){
        StaticApplicationContext ac = new StaticApplicationContext();
        BeanDefinition beanDefinition = new RootBeanDefinition(Hello.class); // 빈 메타정보를 담은 오브젝트를 만든다.
        beanDefinition.getPropertyValues().addPropertyValue("name", "Toby Spring");
        ac.registerBeanDefinition("hello2", beanDefinition);

        Hello hello2 = ac.getBean("hello2", Hello.class);
        assertThat(hello2).isNotNull();
        assertThat(hello2.sayHello()).isEqualTo("Hello Toby Spring");
        assertThat(ac.getBeanFactory().getBeanDefinitionCount()).isEqualTo(1);
    }

    @Test
    void DITest() {
        StaticApplicationContext ac = new StaticApplicationContext();
        ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));
        BeanDefinition beanDefinition = new RootBeanDefinition(Hello.class);
        beanDefinition.getPropertyValues().addPropertyValue("name", "Toby Spring");
        beanDefinition.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));

        ac.registerBeanDefinition("hello", beanDefinition);

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();
        assertThat(hello.sayHello()).isEqualTo("Hello Toby Spring");
    }

    @Test
    void GenericApplicationContextTest() {
        GenericApplicationContext context = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.loadBeanDefinitions("/bean.xml");

        context.refresh(); // application context clear

        Hello hello = context.getBean("hello", Hello.class);
        hello.print();

        assertThat(context.getBean("printer").toString()).isEqualTo("Hello Toby Spring");
    }

    @Test
    void GenericApplicationContextWithXMLReaderTest() {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext("/bean.xml");
        Hello hello = context.getBean("hello", Hello.class);
        hello.print();

        assertThat(context.getBean("printer").toString()).isEqualTo("Hello Toby Spring");
    }

    @Test
    void parentChildContext() {
        ApplicationContext parent = new GenericXmlApplicationContext("/parentContext.xml");
        GenericApplicationContext child = new GenericApplicationContext(parent);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
        reader.loadBeanDefinitions("/childContext.xml");
        child.refresh(); // reader를 사용해 읽었다면 필히 초기화

        Printer printer = child.getBean("printer", Printer.class);
        Hello hello = child.getBean("hello", Hello.class);
        assertThat(printer).isNotNull();
        assertThat(hello.sayHello()).isEqualTo("Hello Child");
        hello.print();
        assertThat(printer.toString()).isEqualTo("Hello Child");
    }
}