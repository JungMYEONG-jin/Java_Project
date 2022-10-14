package com.shinhan.review.excel.ver2.excel.multiplesheet;

import com.shinhan.review.excel.ver2.excel.SXSSFExcelFile;
import com.shinhan.review.excel.ver2.resource.DataFormatDecider;
import org.apache.commons.compress.archivers.zip.Zip64Mode;

import java.util.List;

public final class MultiSheetExcelFile<T> extends SXSSFExcelFile<T> {


//    private static final int maxRows = supplyExcelVersion.getMaxRows()-1;
    private static final int maxRows = 2000;
    private static final int ROW_START_IDX = 0;
    private static final int COL_START_IDX = 0;
    private int currentIdx = ROW_START_IDX;

    public MultiSheetExcelFile(Class<T> type) {
        super(type);
        wb.setZip64Mode(Zip64Mode.Always);
    }

    public MultiSheetExcelFile(List<T> data, Class<T> type) {
        super(data, type);
        wb.setZip64Mode(Zip64Mode.Always);
    }

    public MultiSheetExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider) {
        super(data, type, dataFormatDecider);
        wb.setZip64Mode(Zip64Mode.Always);
    }

    @Override
    public void addRows(List<T> data) {
        for (Object it : data) {
            renderBody(it, currentIdx++, COL_START_IDX);
            if (currentIdx == maxRows){
                currentIdx = 1;
                createNewSheetWithHeader();
            }
        }
    }

    @Override
    protected void renderExcel(List<T> data) {

        if (data.isEmpty())
        {
            createNewSheetWithHeader();
            return;
        }
        createNewSheetWithHeader();
        addRows(data);

    }

    private void createNewSheetWithHeader(){
        sheet = wb.createSheet();
        renderHeadersWithNewSheet(sheet, ROW_START_IDX, COL_START_IDX);
        currentIdx++;
    }
}
