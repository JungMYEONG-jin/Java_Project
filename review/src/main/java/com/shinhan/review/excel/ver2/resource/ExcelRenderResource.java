package com.shinhan.review.excel.ver2.resource;

import org.apache.poi.ss.usermodel.CellStyle;

import java.util.List;
import java.util.Map;

public class ExcelRenderResource {

    private PreCalculatedCellStyleMap calculatedCellStyleMap;

    // data field name to excel header name
    private Map<String, String > headerNames;
    private List<String> dataFieldNames;


    public ExcelRenderResource(PreCalculatedCellStyleMap calculatedCellStyleMap, Map<String, String> headerNames, List<String> dataFieldNames) {
        this.calculatedCellStyleMap = calculatedCellStyleMap;
        this.headerNames = headerNames;
        this.dataFieldNames = dataFieldNames;
    }

    public CellStyle getCellStyle(String fieldName, ExcelRenderLocation excelRenderLocation) {
        return calculatedCellStyleMap.get(ExcelCellKey.of(fieldName, excelRenderLocation));
    }

    public String getHeaderName(String fieldName) {
        return headerNames.get(fieldName);
    }

    public List<String> getDataFieldNames() {
        return dataFieldNames;
    }
}
