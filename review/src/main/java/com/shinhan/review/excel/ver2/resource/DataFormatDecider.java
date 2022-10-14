package com.shinhan.review.excel.ver2.resource;

import org.apache.poi.ss.usermodel.DataFormat;

public interface DataFormatDecider {

	short getDataFormat(DataFormat dataFormat, Class<?> type);

}
