package com.shinhan.review.excel.template.style.custom.decider;

import org.apache.poi.ss.usermodel.DataFormat;

public interface DataFormatDecider {
    short getDataFormat(DataFormat dataFormat, Class<?> type);
}
