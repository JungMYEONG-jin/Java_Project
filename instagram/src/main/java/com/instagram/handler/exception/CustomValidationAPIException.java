package com.instagram.handler.exception;

import java.util.Map;

public class CustomValidationAPIException extends RuntimeException{

    // 객체 구분용
    private static final long serialVersionUID = 1L;

    private Map<String, String> errorMap;

    public CustomValidationAPIException(String message) {
        super(message);
    }

    public CustomValidationAPIException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }
}
