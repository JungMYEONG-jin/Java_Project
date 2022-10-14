package com.shinhan.review.excel.template.style.border;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * basic setting,, do nothing
 */
public class NoExcelBorders implements ExcelBorder{
    @Override
    public void applyTop(CellStyle cellStyle) {

    }

    @Override
    public void applyBottom(CellStyle cellStyle) {

    }

    @Override
    public void applyRight(CellStyle cellStyle) {

    }

    @Override
    public void applyLeft(CellStyle cellStyle) {

    }
}
