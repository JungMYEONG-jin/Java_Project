package com.shinhan.review.excel.ver2.style.configurer;

import com.shinhan.review.excel.ver2.style.align.ExcelAlign;
import com.shinhan.review.excel.ver2.style.align.NoExcelAlign;
import com.shinhan.review.excel.ver2.style.border.ExcelBorders;
import com.shinhan.review.excel.ver2.style.border.NoExcelBorders;
import com.shinhan.review.excel.ver2.style.color.DefaultExcelColor;
import com.shinhan.review.excel.ver2.style.color.ExcelColor;
import com.shinhan.review.excel.ver2.style.color.NoExcelColor;
import org.apache.poi.ss.usermodel.CellStyle;

public class ExcelCellStyleConfigurer {

	private ExcelAlign excelAlign = new NoExcelAlign();
	private ExcelColor foregroundColor = new NoExcelColor();
	private ExcelBorders excelBorders = new NoExcelBorders();

	public ExcelCellStyleConfigurer() {

	}

	public ExcelCellStyleConfigurer excelAlign(ExcelAlign excelAlign) {
		this.excelAlign = excelAlign;
		return this;
	}

	public ExcelCellStyleConfigurer foregroundColor(int red, int blue, int green) {
		this.foregroundColor = DefaultExcelColor.rgb(red, blue, green);
		return this;
	}

	public ExcelCellStyleConfigurer excelBorders(ExcelBorders excelBorders) {
		this.excelBorders = excelBorders;
		return this;
	}

	public void configure(CellStyle cellStyle) {
		excelAlign.apply(cellStyle);
		foregroundColor.applyForeground(cellStyle);
		excelBorders.apply(cellStyle);
	}

}
