package com.ecommerce.common.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity RuntimeExceptionHandler(RuntimeException e){
        HashMap<String, String> map = new HashMap<>();
        map.put("errorMessage", e.getMessage());
        return ResponseEntity.status(400).body(map);
    }
}
