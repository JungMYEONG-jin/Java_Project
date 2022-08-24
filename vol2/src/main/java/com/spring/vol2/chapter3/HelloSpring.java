package com.spring.vol2.chapter3;

import org.springframework.stereotype.Component;

@Component
public class HelloSpring {
    public String sayHello(String name){
        return "Hello " + name;
    }
}
