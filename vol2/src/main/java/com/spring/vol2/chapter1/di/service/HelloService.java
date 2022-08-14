package com.spring.vol2.chapter1.di.service;

import com.spring.vol2.chapter1.di.Hello;
import com.spring.vol2.chapter1.di.Printer;
import com.spring.vol2.chapter1.di.StringPrinter;
import org.springframework.context.annotation.Bean;

public class HelloService {

    private Printer printer;

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    @Bean
    public Hello hello2(){
        Hello hello = new Hello();
        hello.setName("Toby");
        hello.setPrinter(this.printer);
        return hello;
    }

    @Bean
    public Hello hello3(){
        Hello hello = new Hello();
        hello.setName("Spring");
        hello.setPrinter(this.printer);
        return hello;
    }

    @Bean
    private Printer printer(){
        return new StringPrinter();
    }
}
