package com.shinhan.review.excel;

import com.shinhan.review.excel.template.ExcelFile;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public abstract class SXSSFExcelFile<T> implements ExcelFile<T> {

    protected static final SpreadsheetVersion supplyExcelVersion = SpreadsheetVersion.EXCEL2007; // excel 2007 기준

    protected SXSSFWorkbook wb;
    protected Sheet sheet;
    protected ExcelRenderResource resource;


    @Override
    public void write(OutputStream stream) throws IOException {

    }

    @Override
    public void addRows(List<T> data) {

    }
}
