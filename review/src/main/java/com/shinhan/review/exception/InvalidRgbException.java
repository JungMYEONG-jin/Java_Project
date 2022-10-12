package com.shinhan.review.exception;

public class InvalidRgbException extends RuntimeException{
    public InvalidRgbException() {
    }

    public InvalidRgbException(String message) {
        super(message);
    }

    public InvalidRgbException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRgbException(Throwable cause) {
        super(cause);
    }

    public InvalidRgbException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
