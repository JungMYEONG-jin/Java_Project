package com.shinhan.review.excel.template.style;

import org.apache.poi.ss.usermodel.CellStyle;

public interface ExcelCellStyle {
    void apply(CellStyle cellStyle);
}
