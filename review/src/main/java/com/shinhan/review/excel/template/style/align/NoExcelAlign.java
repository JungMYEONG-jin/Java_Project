package com.shinhan.review.excel.template.style.align;

import org.apache.poi.ss.usermodel.CellStyle;

public class NoExcelAlign implements ExcelAlign{
    @Override
    public void apply(CellStyle cellStyle) {
        // Nothing
    }
}
