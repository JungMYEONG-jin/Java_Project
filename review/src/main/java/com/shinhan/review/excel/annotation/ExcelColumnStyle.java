package com.shinhan.review.excel.annotation;

import com.shinhan.review.excel.template.style.ExcelCellStyle;

public @interface ExcelColumnStyle {
    Class<? extends ExcelCellStyle> excelCellStyleClass();
    String enumName() default "";
}


