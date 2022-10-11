package com.shinhan.review.excel.template;

import com.shinhan.review.excel.ReviewColumnInfo;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;
import java.util.Map;

public class SimpleExcelFile<T> {

    private static final SpreadsheetVersion supplyExcelVersion = SpreadsheetVersion.EXCEL2007;
    private static final int ROW_START_IDX = 0;
    private static final int COL_START_IDX = 0;

    private SXSSFWorkbook wb;
    private Sheet sheet;
//    private SimpleExcelMetaData excelMetaData;

    public SimpleExcelFile(List<T> data, Class<T> type){
        validateMaxRow(data);
        this.wb = new SXSSFWorkbook();
        renderExcel(data);
    }

    private void validateMaxRow(List<T> data){
        int maxRows = supplyExcelVersion.getMaxRows();
        if (data.size() > maxRows)
            throw new IllegalArgumentException(String.format("This Excel Version does not support over %s rows", maxRows));
    }

    private void renderExcel(List<T> data){
        // Create sheet and render headers
        sheet = wb.createSheet();

        if (data.isEmpty())
            return;

        // render body
        int rowIdx = ROW_START_IDX + 1;
        for (Object renderData : data) {
            renderBody(renderData, rowIdx++, COL_START_IDX);
        }
    }

    private void renderHeaders(Sheet sheet, int rowIdx){
        Row row = sheet.createRow(rowIdx);
        Map<Integer, List<ReviewColumnInfo>> allColumns = ReviewColumnInfo.getAllColumns();
        List<ReviewColumnInfo> headerColumns = allColumns.get(0); // get header column
        // set header
        headerColumns.forEach(reviewColumnInfo -> {
            Cell cell = row.createCell(reviewColumnInfo.getCol());
            cell.setCellValue(reviewColumnInfo.getText());
        });
    }

    private void renderBody(Object data, int rowIdx, int colStartIdx){
        Row row = sheet.createRow(rowIdx);
        int colIdx = colStartIdx;
        ReviewColumnInfo[] values = ReviewColumnInfo.values();
        for (ReviewColumnInfo value : values) {
            Cell cell = row.createCell(colIdx++);
            try{

            }catch (Exception e){
                throw new ExcelInternalException(e.getMessage(), e);
            }
        }
    }

    private void renderCellValue(Cell cell, Object cellValue){
        if (cellValue instanceof Number){
            Number numberValue = (Number) cellValue;
            cell.setCellValue(numberValue.doubleValue());
            return;
        }
        cell.setCellValue(cellValue == null ? "" : cellValue.toString());
    }

}
