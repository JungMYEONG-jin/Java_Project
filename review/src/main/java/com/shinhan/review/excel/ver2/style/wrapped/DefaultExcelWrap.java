package com.shinhan.review.excel.ver2.style.wrapped;

import org.apache.poi.ss.usermodel.CellStyle;

public class DefaultExcelWrap implements ExcelWrap{

    public static DefaultExcelWrap wrappedTrue(){
        return new DefaultExcelWrap(true);
    }

    public static DefaultExcelWrap wrappedFalse(){
        return new DefaultExcelWrap(false);
    }

    public DefaultExcelWrap(boolean isWrap) {
        this.isWrap = isWrap;
    }

    private final boolean isWrap;

    @Override
    public void apply(CellStyle cellStyle) {
        cellStyle.setWrapText(isWrap); // 줄바꿈 여부 결정
    }
}
