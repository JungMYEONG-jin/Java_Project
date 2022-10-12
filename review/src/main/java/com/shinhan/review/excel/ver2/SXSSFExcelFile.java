package com.shinhan.review.excel.ver2;

import com.shinhan.review.excel.ReviewColumnInfo;
import com.shinhan.review.excel.ver2.decider.DataFormatDecider;
import com.shinhan.review.excel.ver2.decider.DefaultDataFormatDecider;
import com.shinhan.review.excel.ver2.resource.ExcelRenderResource;
import com.shinhan.review.exception.ExcelInternalException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class SXSSFExcelFile<T> implements ExcelFile<T> {

    protected static final SpreadsheetVersion supplyExcelVersion = SpreadsheetVersion.EXCEL2007; // excel 2007 기준

    protected SXSSFWorkbook wb;
    protected Sheet sheet;
    protected ExcelRenderResource resource;

    public SXSSFExcelFile(Class<T> type){
        this(Collections.emptyList(), type, new DefaultDataFormatDecider());
    }

    public SXSSFExcelFile(List<T> data, Class<T> type){
        this(data, type, new DefaultDataFormatDecider());
    }

    public SXSSFExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider){
        validateData(data);
        this.wb = new SXSSFWorkbook();

        renderExcel(data);
    }

    protected void validateData(List<T> data){
        int maxRows = supplyExcelVersion.getMaxRows();
        if (data.size() > maxRows)
            throw new IllegalArgumentException(String.format("This Excel Version does not support over %s rows", maxRows));
    }


    protected abstract void renderExcel(List<T> data);

    protected void renderHeaders(org.apache.poi.ss.usermodel.Sheet sheet, int rowIdx){
        Row row = sheet.createRow(rowIdx);
        Map<Integer, List<ReviewColumnInfo>> allColumns = ReviewColumnInfo.getAllColumns();
        List<ReviewColumnInfo> headerColumns = allColumns.get(0); // get header column
        // set header
        headerColumns.forEach(reviewColumnInfo -> {
            Cell cell = row.createCell(reviewColumnInfo.getCol());
            cell.setCellValue(reviewColumnInfo.getText());
        });
    }

    protected void renderBody(Object data, int rowIdx, int colStartIdx){
        Row row = sheet.createRow(rowIdx);
        int colIdx = colStartIdx;
        // 순서대로 enum type 이라 idx ++ 로 가능
        for (String fieldName : resource.getDataFieldNames()){
            Cell cell = row.createCell(colIdx++);
            try{
                getF
            }
        }
    }

    protected void renderCellValue(Cell cell, Object cellValue){
        if (cellValue instanceof Number){
            Number numberValue = (Number) cellValue;
            cell.setCellValue(numberValue.doubleValue());
            return;
        }
        cell.setCellValue(cellValue == null ? "" : cellValue.toString());
    }

    @Override
    public void write(OutputStream stream) throws IOException {
        wb.write(stream);
        wb.close();
        wb.dispose();
        stream.close();
    }

    @Override
    public void addRows(List<T> data) {

    }
}
