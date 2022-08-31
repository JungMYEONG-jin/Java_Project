package com.spring.vol2.chapter1.di.config;

import com.spring.vol2.chapter1.di.AnnotatedHello;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnnotatedHelloConfig {

    @Bean
    public AnnotatedHello annotatedHello2(){
        return new AnnotatedHello();
    }
}
