package com.shinhan.review.excel.template.style;

import com.shinhan.review.excel.template.style.align.DefaultExcelAlign;
import com.shinhan.review.excel.template.style.align.ExcelAlign;
import com.shinhan.review.excel.template.style.border.DefaultExcelBorders;
import com.shinhan.review.excel.template.style.border.ExcelBorderStyle;
import com.shinhan.review.excel.template.style.color.DefaultExcelColor;
import com.shinhan.review.excel.template.style.color.ExcelColor;
import org.apache.poi.ss.usermodel.CellStyle;

public enum DefaultExcelCellStyle implements ExcelCellStyle{
    GREY_HEADER(DefaultExcelColor.rgb(217, 217, 217),
            DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.CENTER_CENTER),
    BLUE_HEADER(DefaultExcelColor.rgb(223, 235, 246),
            DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.CENTER_CENTER),
    BODY(DefaultExcelColor.rgb(255, 255, 255),
            DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.RIGHT_CENTER);


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
        bgColor.applyForeGround(cellStyle);
        borders.apply(cellStyle);
        align.apply(cellStyle);
    }
}
