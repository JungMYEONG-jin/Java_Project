package com.shinhan.review.custom.validator;

import com.shinhan.review.custom.annotation.ValidDateRange;
import com.shinhan.review.entity.DateEntity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, DateEntity> {
    @Override
    public boolean isValid(DateEntity value, ConstraintValidatorContext context) {
        if (value.getStart()!=null && value.getEnd()!=null){
            return value.getStart().isBefore(value.getEnd());
        }
        return true;
    }
}
