package com.shinhan.review.excel.template.style;

import org.apache.poi.ss.usermodel.CellStyle;

public class NoExcelBackgroundColor implements ExcelColor{
    @Override
    public void applyBackground(CellStyle cellStyle) {
        // nothing
    }
}
