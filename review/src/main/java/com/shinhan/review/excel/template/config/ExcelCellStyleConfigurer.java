package com.shinhan.review.excel.template.config;

import com.shinhan.review.excel.template.style.ExcelBackgroundColor;
import com.shinhan.review.excel.template.style.NoExcelBackgroundColor;
import com.shinhan.review.excel.template.style.RgbExcelBackgroundColor;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * excel cell style config
 */
public class ExcelCellStyleConfigurer {

    private ExcelBackgroundColor backgroundColor = new NoExcelBackgroundColor();

    public ExcelCellStyleConfigurer backgroundColor(int red, int green, int blue) {
        this.backgroundColor = new RgbExcelBackgroundColor(red, green, blue);
        return this;
    }

    public void configure(CellStyle cellStyle){
        backgroundColor.applyBackground(cellStyle);
    }

}
