package com.spring.vol2.chapter1.di;

public class StringPrinter implements Printer{
    private StringBuffer buffer = new StringBuffer();
    @Override
    public void print(String message) {
        this.buffer.append(message);
    }

    public String toString(){
        return buffer.toString();
    }
}
