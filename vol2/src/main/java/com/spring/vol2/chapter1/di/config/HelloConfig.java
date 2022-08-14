package com.spring.vol2.chapter1.di.config;

import com.spring.vol2.chapter1.di.Hello;
import com.spring.vol2.chapter1.di.Printer;
import com.spring.vol2.chapter1.di.StringPrinter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloConfig {
    @Bean
    public Hello hello2(){
        Hello hello = new Hello();
        hello.setName("Toby");
        hello.setPrinter(printer());
        return hello;
    }

    @Bean
    public Hello hello3(){
        Hello hello = new Hello();
        hello.setName("Spring");
        hello.setPrinter(printer());
        return hello;
    }

    @Bean
    public Printer printer() {
        return new StringPrinter();
    }
}
