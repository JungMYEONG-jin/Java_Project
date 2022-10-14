package com.shinhan.review.excel.ver2.style.border;

import org.apache.poi.ss.usermodel.CellStyle;

public interface ExcelBorder {

	void applyTop(CellStyle cellStyle);

	void applyRight(CellStyle cellStyle);

	void applyBottom(CellStyle cellStyle);

	void applyLeft(CellStyle cellStyle);

}
