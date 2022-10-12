package com.shinhan.review.excel.template.style.custom.decider;

import org.apache.poi.ss.usermodel.DataFormat;

import java.util.Arrays;
import java.util.List;

public class DefaultDataFormatDecider implements DataFormatDecider{

    private static final String CURRENT_FORMAT = "#,##0";
    private static final String FLOAT_2_DECIMAL_FORMAT = "#,##0.00";
    private static final String DEFAULT_FORMAT = "";

    @Override
    public short getDataFormat(DataFormat dataFormat, Class<?> type) {

        if (isFloat(type))
        {
            return dataFormat.getFormat(FLOAT_2_DECIMAL_FORMAT);
        }

        if (isInteger(type)){
            return dataFormat.getFormat(CURRENT_FORMAT);
        }
        return dataFormat.getFormat(DEFAULT_FORMAT);
    }

    private boolean isFloat(Class<?> type){
        List<Class<?>> floatTypes = Arrays.asList(Float.class, float.class, Double.class, double.class);
        return floatTypes.contains(type);
    }

    private boolean isInteger(Class<?> type){
        List<Class<?>> integerTypes = Arrays.asList(Integer.class, int.class, Long.class, long.class, Short.class, short.class, Byte.class, byte.class);
        return integerTypes.contains(type);
    }

}
