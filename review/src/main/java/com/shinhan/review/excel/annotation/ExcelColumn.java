package com.shinhan.review.excel.annotation;

import com.shinhan.review.excel.template.style.ExcelCellStyle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * style 적용하기
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    String headerName() default "";
    ExcelColumnStyle headerStyle() default @ExcelColumnStyle(excelCellStyleClass = ExcelCellStyle.class);
    ExcelColumnStyle bodyStyle() default @ExcelColumnStyle(excelCellStyleClass = ExcelCellStyle.class);
}
