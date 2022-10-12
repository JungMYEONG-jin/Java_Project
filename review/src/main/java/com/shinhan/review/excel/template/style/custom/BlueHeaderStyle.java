package com.shinhan.review.excel.template.style.custom;

import com.shinhan.review.excel.template.config.CustomExcelCellStyle;
import com.shinhan.review.excel.template.config.ExcelCellStyleConfigurer;

/**
 * 손쉽게 만든 사용자 지정 색
 */
public class BlueHeaderStyle extends CustomExcelCellStyle {
    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.backgroundColor(223, 235, 246);
    }
}
