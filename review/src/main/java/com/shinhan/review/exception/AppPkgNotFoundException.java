package com.shinhan.review.exception;

public class AppPkgNotFoundException extends RuntimeException{

    public AppPkgNotFoundException() {
        super();
    }

    public AppPkgNotFoundException(String message) {
        super(message);
    }

    public AppPkgNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppPkgNotFoundException(Throwable cause) {
        super(cause);
    }

    protected AppPkgNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
