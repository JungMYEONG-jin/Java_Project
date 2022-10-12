package com.shinhan.review.excel.template.style;

import com.shinhan.review.exception.InvalidRgbException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 * 배경색을 지정받아 배경색을 지정하는 class
 */
public class RgbExcelBackgroundColor implements ExcelColor{

    private static final int MIN_RGB = 0;
    private static final int MAX_RGB = 255;

    private byte red;
    private byte blue;
    private byte green;

    private boolean isValid(int color){
        if (color<MIN_RGB || color>MAX_RGB)
            return false;
        return true;
    }

    public RgbExcelBackgroundColor(int red, int blue, int green) {

        if (!isValid(red) || !isValid(blue) || !isValid(green))
            throw new InvalidRgbException(String.format("Wrong RGF format(%s %s %s)", red, green, blue));

        this.red = (byte)red;
        this.blue = (byte) blue;
        this.green = (byte) green;
    }

    // set background color
    @Override
    public void applyBackground(CellStyle cellStyle) {
        XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
        xssfCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{}, new DefaultIndexedColorMap()));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
     }
}
