package com.shinhan.review.excel.ver2.single;

import com.shinhan.review.excel.ver2.SXSSFExcelFile;
import com.shinhan.review.excel.ver2.decider.DataFormatDecider;

import java.util.List;

public class SingleExcelFile<T> extends SXSSFExcelFile<T> {

    private static final int ROW_START_IDX = 0;
    private static final int COL_START_IDX = 0;
    private int currentRow = ROW_START_IDX;

    public SingleExcelFile(Class<T> type){
        super(type);
    }

    public SingleExcelFile(List<T> data, Class<T> type){
        super(data, type);
    }

    public SingleExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider){
        super(data, type, dataFormatDecider);
    }

    @Override
    protected void validateData(List<T> data) {
        int maxRows = supplyExcelVersion.getMaxRows();
        if (data.size() > maxRows)
            throw new IllegalArgumentException(String.format("This Excel Version does not support over %s rows", maxRows));
    }


    @Override
    protected void renderExcel(List<T> data) {
        sheet = wb.createSheet();
        renderHeadersWithNewSheet(sheet, currentRow++, COL_START_IDX);

        if (data.isEmpty())
            return;

        // render body
        for (Object render : data){
            renderBody(render, currentRow++, COL_START_IDX);
        }
    }

    @Override
    public void addRows(List<T> data) {
        renderBody(data, currentRow++, COL_START_IDX);
    }
}
