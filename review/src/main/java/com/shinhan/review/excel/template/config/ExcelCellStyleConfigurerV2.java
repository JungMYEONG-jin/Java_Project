package com.shinhan.review.excel.template.config;

import com.shinhan.review.excel.template.style.align.ExcelAlign;
import com.shinhan.review.excel.template.style.align.NoExcelAlign;
import com.shinhan.review.excel.template.style.border.ExcelBorders;
import com.shinhan.review.excel.template.style.border.NoExcelBorders;
import com.shinhan.review.excel.template.style.color.DefaultExcelColor;
import com.shinhan.review.excel.template.style.color.ExcelColor;
import com.shinhan.review.excel.template.style.color.NoExcelColor;
import org.apache.poi.ss.usermodel.CellStyle;

public class ExcelCellStyleConfigurerV2 {
    private ExcelAlign excelAlign = new NoExcelAlign();
    private ExcelColor excelColor = new NoExcelColor();
    private ExcelBorders excelBorders = new NoExcelBorders();

    public ExcelCellStyleConfigurerV2(){}


    public ExcelCellStyleConfigurerV2 excelAlign(ExcelAlign excelAlign){
        this.excelAlign = excelAlign;
        return this;
    }

    public ExcelCellStyleConfigurerV2 foreGroundColor(int red, int blue, int green){
        this.excelColor = DefaultExcelColor.rgb(red, green, blue);
        return this;
    }

    public ExcelCellStyleConfigurerV2 excelBorders(ExcelBorders excelBorders){
        this.excelBorders = excelBorders;
        return this;
    }

    public void configure(CellStyle cellStyle){
        excelBorders.apply(cellStyle);
        excelAlign.apply(cellStyle);
        excelColor.applyForeGround(cellStyle);
    }

}
