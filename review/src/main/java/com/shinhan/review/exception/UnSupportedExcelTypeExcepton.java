package com.shinhan.review.exception;

public class UnSupportedExcelTypeExcepton extends ExcelException{
    public UnSupportedExcelTypeExcepton() {
        super();
    }

    public UnSupportedExcelTypeExcepton(String message) {
        super(message);
    }

    public UnSupportedExcelTypeExcepton(String message, Throwable cause) {
        super(message, cause);
    }

    public UnSupportedExcelTypeExcepton(Throwable cause) {
        super(cause);
    }

    protected UnSupportedExcelTypeExcepton(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
