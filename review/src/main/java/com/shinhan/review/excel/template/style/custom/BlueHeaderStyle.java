package com.shinhan.review.excel.template.style.custom;

import com.shinhan.review.excel.template.style.CustomExcelCellStyle;
import com.shinhan.review.excel.template.config.ExcelCellStyleConfigurerV2;
import com.shinhan.review.excel.template.style.align.DefaultExcelAlign;
import com.shinhan.review.excel.template.style.border.DefaultExcelBorders;
import com.shinhan.review.excel.template.style.border.ExcelBorderStyle;

/**
 * 손쉽게 만든 사용자 지정 색
 */
public class BlueHeaderStyle extends CustomExcelCellStyle {
    @Override
    public void configure(ExcelCellStyleConfigurerV2 configurer) {
        configurer.foreGroundColor(223, 235, 246).excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
                .excelAlign(DefaultExcelAlign.CENTER_CENTER);
    }
}
