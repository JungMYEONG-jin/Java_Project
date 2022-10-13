package com.shinhan.review.excel.ver2.single;

import com.shinhan.review.excel.ver2.SXSSFExcelFile;

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


    @Override
    protected void renderExcel(List data) {

    }
}
