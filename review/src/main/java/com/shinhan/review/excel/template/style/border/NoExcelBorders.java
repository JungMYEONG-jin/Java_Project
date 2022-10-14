package com.shinhan.review.excel.template.style.border;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * basic setting,, do nothing
 */
public class NoExcelBorders implements ExcelBorders{
    @Override
    public void apply(CellStyle cellStyle) {

    }
}
