package com.shinhan.review.excel.ver2.exception;

import com.shinhan.review.excel.ver2.ExcelException;

public class UnSupportedExcelTypeException extends ExcelException {

	public UnSupportedExcelTypeException(String message) {
		super(message, null);
	}

}
