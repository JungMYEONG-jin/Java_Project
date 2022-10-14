package com.shinhan.review.excel.ver2.style;

import org.apache.poi.ss.usermodel.CellStyle;

public class NoExcelCellStyle implements ExcelCellStyle {

	@Override
	public void apply(CellStyle cellStyle) {
		// Do nothing
	}

}
