package com.spring.vol2.chapter1.di;

import lombok.Setter;

@Setter
public class Hello {
    private String name;
    private Printer printer;

    public String sayHello(){
        return "Hello " + name;
    }

    public void print(){
        this.printer.print(sayHello());
    }


}
