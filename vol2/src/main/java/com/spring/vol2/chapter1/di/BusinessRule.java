package com.spring.vol2.chapter1.di;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface BusinessRule {
    String value() default ""; //  빈 아이디 직접 지정할 수 있게 디폴트 엘리먼트 선언
}



