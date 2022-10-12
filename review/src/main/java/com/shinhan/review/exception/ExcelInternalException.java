package com.shinhan.review.exception;

public class ExcelInternalException extends RuntimeException {
    public ExcelInternalException() {
        super();
    }

    public ExcelInternalException(String message) {
        super(message);
    }

    public ExcelInternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelInternalException(Throwable cause) {
        super(cause);
    }

    protected ExcelInternalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
