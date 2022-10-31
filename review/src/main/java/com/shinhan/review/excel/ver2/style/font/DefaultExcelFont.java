package com.shinhan.review.excel.ver2.style.font;

import org.apache.poi.ss.usermodel.Font;

public enum DefaultExcelFont implements ExcelFont{

    DEFAULT_HEADER_NOT_BOLD((short)9, false),
    DEFAULT_HEADER_BOLD((short)9, true),
    DEFAULT_BODY_NOT_BOLD((short)10, false),
    DEFAULT_BODY_BOLD((short)10, true)
    ;

    private final short fontSize;
    private final boolean isBold;

    DefaultExcelFont(short fontSize, boolean isBold) {
        this.fontSize = fontSize;
        this.isBold = isBold;
    }

    @Override
    public void apply(Font font) {
        font.setFontHeightInPoints(fontSize);
        font.setBold(isBold);
    }

    public short getFontSize() {
        return fontSize;
    }

    public boolean isBold() {
        return isBold;
    }
}
