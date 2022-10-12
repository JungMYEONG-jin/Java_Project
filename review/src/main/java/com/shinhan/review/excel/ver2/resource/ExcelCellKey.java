package com.shinhan.review.excel.ver2.resource;

import java.util.Objects;

public final class ExcelCellKey {

    private final String dataFieldName;
    private final ExcelRenderLocation excelRenderLocation;

    public ExcelCellKey(String dataFieldName, ExcelRenderLocation excelRenderLocation) {
        this.dataFieldName = dataFieldName;
        this.excelRenderLocation = excelRenderLocation;
    }

    public static ExcelCellKey of(String fieldName, ExcelRenderLocation excelRenderLocation){
        assert excelRenderLocation != null;
        return new ExcelCellKey(fieldName, excelRenderLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataFieldName, excelRenderLocation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ExcelCellKey it = (ExcelCellKey) obj;
        return Objects.equals(dataFieldName, it.dataFieldName) && excelRenderLocation == it.excelRenderLocation;
    }
}
