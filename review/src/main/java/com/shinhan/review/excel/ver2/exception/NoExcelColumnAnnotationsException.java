package com.shinhan.review.excel.ver2.exception;

import com.shinhan.review.excel.ver2.ExcelException;

public class NoExcelColumnAnnotationsException extends ExcelException {

	public NoExcelColumnAnnotationsException(String message) {
		super(message, null);
	}

}
