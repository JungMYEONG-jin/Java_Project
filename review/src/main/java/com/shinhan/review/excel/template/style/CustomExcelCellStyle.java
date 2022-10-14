package com.shinhan.review.excel.template.style;

import com.shinhan.review.excel.template.config.ExcelCellStyleConfigurerV2;
import com.shinhan.review.excel.template.style.ExcelCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;

public abstract class CustomExcelCellStyle implements ExcelCellStyle {

    private ExcelCellStyleConfigurerV2 configurer = new ExcelCellStyleConfigurerV2();

    public CustomExcelCellStyle() {
        configure(configurer);
    }

    public abstract void configure(ExcelCellStyleConfigurerV2 configurer);

    @Override
    public void apply(CellStyle cellStyle) {
        configurer.configure(cellStyle);
    }
}
