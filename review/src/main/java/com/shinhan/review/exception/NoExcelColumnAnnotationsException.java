package com.shinhan.review.exception;

public class NoExcelColumnAnnotationsException extends RuntimeException{
    public NoExcelColumnAnnotationsException() {
        super();
    }

    public NoExcelColumnAnnotationsException(String message) {
        super(message);
    }

    public NoExcelColumnAnnotationsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoExcelColumnAnnotationsException(Throwable cause) {
        super(cause);
    }

    protected NoExcelColumnAnnotationsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
