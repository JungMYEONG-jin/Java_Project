package com.shinhan.review.excel.template.style;

import com.shinhan.review.excel.template.style.color.ExcelColor;
import org.apache.poi.ss.usermodel.CellStyle;

public enum DefaultExcelCellStyle implements ExcelCellStyle{
    ;


    private final ExcelColor bgColor;
    private final DefaultExcelBorders borders;
    private final ExcelAlign align;

    DefaultExcelCellStyle(ExcelColor bgColor, DefaultExcelBorders borders, ExcelAlign align) {
        this.bgColor = bgColor;
        this.borders = borders;
        this.align = align;
    }

    @Override
    public void apply(CellStyle cellStyle) {

    }
}
