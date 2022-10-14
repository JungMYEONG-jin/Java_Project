package com.shinhan.review.excel.template.style.color;

import com.shinhan.review.exception.UnSupportedExcelTypeExcepton;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class DefaultExcelColor implements ExcelColor{

    private static final int MIN_RGB = 0;
    private static final int MAX_RGB = 255;

    private byte red;
    private byte green;
    private byte blue;

    public DefaultExcelColor(byte red, byte green, byte blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public static boolean isValidRGB(int value){
        if (value < MIN_RGB || value > MAX_RGB)
            return false;
        return true;
    }

    public static DefaultExcelColor rgb(int red, int green, int blue){
        if (!isValidRGB(red) || !isValidRGB(green) || !isValidRGB(blue))
            throw new IllegalArgumentException(String.format("Wrong RGB(%s, %s, %s)", red, green, blue));
        return new DefaultExcelColor((byte) red, (byte) green ,(byte) blue);
    }


    @Override
    public void applyForeGround(CellStyle cellStyle) {
        try {
            XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
            xssfCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{red, green, blue}, new DefaultIndexedColorMap()));
        }catch (Exception e){
            throw new UnSupportedExcelTypeExcepton(String.format("Excel Type %s is not supported", cellStyle.getClass()));
        }
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }


}
