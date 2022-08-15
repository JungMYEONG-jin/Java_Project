package com.spring.vol2.chapter1.di;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class SingleTonTest {

    @Test
    void singletonScope() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class, SingletonClientBean.class);
        Set<SingletonBean> beans = new HashSet<>();

        //DL check
        beans.add(ac.getBean(SingletonBean.class));
        beans.add(ac.getBean(SingletonBean.class));
        assertThat(beans.size()).isEqualTo(1);

        //DI check
        beans.add(ac.getBean(SingletonClientBean.class).bean1);
        beans.add(ac.getBean(SingletonClientBean.class).bean2);
        assertThat(beans.size()).isEqualTo(1);

    }

    @Test
    void prototypeScope() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, PrototypeClientBean.class);
        Set<PrototypeBean> beans = new HashSet<>();

        //DL check
        beans.add(ac.getBean(PrototypeBean.class));
        beans.add(ac.getBean(PrototypeBean.class));
        assertThat(beans.size()).isEqualTo(2);

        //DI check
        beans.add(ac.getBean(PrototypeClientBean.class).bean1);
        beans.add(ac.getBean(PrototypeClientBean.class).bean2);
        assertThat(beans.size()).isEqualTo(4);

    }

    static class SingletonBean{}
    static class SingletonClientBean{
        @Autowired SingletonBean bean1;
        @Autowired SingletonBean bean2;
    }

    @Scope("prototype")
    static class PrototypeBean{}

    static class PrototypeClientBean{
        @Autowired PrototypeBean bean1;
        @Autowired PrototypeBean bean2;
    }
}
