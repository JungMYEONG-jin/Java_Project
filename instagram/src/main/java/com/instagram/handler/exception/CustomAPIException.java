package com.instagram.handler.exception;


// spring validation 이외 exception
public class CustomAPIException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public CustomAPIException(String msg){
        super(msg);
    }
}
