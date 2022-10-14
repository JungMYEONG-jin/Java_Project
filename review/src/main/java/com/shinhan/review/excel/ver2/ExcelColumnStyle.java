package com.shinhan.review.excel.ver2;

import com.shinhan.review.excel.ver2.style.ExcelCellStyle;

public @interface ExcelColumnStyle {

	/**
	 * Enum implements {@link ExcelCellStyle}
	 * Also, can use just class.
	 * If not use Enum, enumName will be ignored
	 * @see com.lannstark.style.DefaultExcelCellStyle
	 * @see com.lannstark.style.CustomExcelCellStyle
	 */
	Class<? extends ExcelCellStyle> excelCellStyleClass();

	/**
	 * name of Enum implements {@link ExcelCellStyle}
	 * if not use Enum, enumName will be ignored
	 */
	String enumName() default "";

}
