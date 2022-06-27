package com.instagram.handler;

import com.instagram.handler.exception.CustomAPIException;
import com.instagram.handler.exception.CustomException;
import com.instagram.handler.exception.CustomValidationAPIException;
import com.instagram.handler.exception.CustomValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ControllerExceptionHandler {

//    @ExceptionHandler(CustomValidationException.class)
//    public String validationException(CustomValidationException e){
//        if(e.getErrorMap() == null){
//            return Script.back(e.getMessage());
//        }else{
//            return Script.back(e.getErrorMap().toString())
//        }
//    }
//
//    // ajax에서 성공과 실패 구분
//    @ExceptionHandler(CustomValidationAPIException.class)
//    public ResponseEntity<CMRespDto<?>> validationAPIException(CustomValidationAPIException e){
//        return new ResponseEntity<CMRespDto>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
//    }
//
//    // data 리턴 컨트로럴
//    @ExceptionHandler(CustomAPIException.class)
//    public ResponseEntity<CMRespDto<?>> apiException(CustomAPIException e){
//        return new ResponseEntity<>(new CMRespDto(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
//    }
//
//    // html return 컨트롤러
//    @ExceptionHandler(CustomException.class)
//    public String exception(CustomException e){
//        return Script.back(e.getMessage());
//    }



}
