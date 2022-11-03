package com.shinhan.review.custom.annotation;

import com.shinhan.review.custom.validator.DateRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default  "종료일은 시작일보다 커야합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
