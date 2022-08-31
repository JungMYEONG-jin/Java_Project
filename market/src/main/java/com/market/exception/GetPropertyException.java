package com.market.exception;

public class GetPropertyException extends RuntimeException{

    public GetPropertyException() {

    }
    public GetPropertyException(String message) {
        super("[GetPropertyException] "+message);
    }

    public GetPropertyException(String message, Exception e) {
        super("[GetPropertyException] "+message, e);
    }

    public GetPropertyException(Throwable e){
        super(e);
    }
}
