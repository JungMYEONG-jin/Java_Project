package com.shinhan.review.excel.template.config;

import com.shinhan.review.excel.template.style.ExcelCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;

public abstract class CustomExcelCellStyle implements ExcelCellStyle {

    private ExcelCellStyleConfigurer configurer = new ExcelCellStyleConfigurer();

    public CustomExcelCellStyle(ExcelCellStyleConfigurer configurer) {
        configure(configurer);
    }

    public CustomExcelCellStyle() {
    }

    public abstract void configure(ExcelCellStyleConfigurer configurer);

    @Override
    public void apply(CellStyle cellStyle) {
        configurer.configure(cellStyle);
    }
}
