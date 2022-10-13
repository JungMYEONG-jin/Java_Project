package com.shinhan.review.exception;

public class InvalidExcelCellStyleException extends ExcelException{
    public InvalidExcelCellStyleException() {
    }

    public InvalidExcelCellStyleException(String message) {
        super(message);
    }

    public InvalidExcelCellStyleException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidExcelCellStyleException(Throwable cause) {
        super(cause);
    }

    public InvalidExcelCellStyleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
