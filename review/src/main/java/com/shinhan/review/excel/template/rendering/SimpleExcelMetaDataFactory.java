package com.shinhan.review.excel.template.rendering;

import com.shinhan.review.excel.annotation.ExcelColumn;
import com.shinhan.review.exception.NoExcelColumnAnnotationsException;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 렌더링 대상인 DTO의 어노테이션을 파악해 메타데이터를 정리하는 역할
 */
public class SimpleExcelMetaDataFactory<T> {

    private static final SimpleExcelMetaDataFactory factory = new SimpleExcelMetaDataFactory();
    public static SimpleExcelMetaDataFactory getInstance(){
        return factory;
    }

    public SimpleExcelMetaData createMetaData(Class<T> type){
        Map<String, String> headerNameMap = new LinkedHashMap<>();
        for (Field field : getAllFields(type)){
            if (field.isAnnotationPresent(ExcelColumn.class)){
                ExcelColumn columnAnnotation = field.getAnnotation(ExcelColumn.class);
                headerNameMap.put(field.getName(), columnAnnotation.headerName());
            }
        }

        if (headerNameMap.isEmpty()){
            throw new NoExcelColumnAnnotationsException(String.format("Class %s has not @ExcelColumn at all", type));
        }
        return new SimpleExcelMetaData(headerNameMap);

    }


    private Field[] getAllFields(Class<T> type){
        return type.getDeclaredFields();
    }




}
