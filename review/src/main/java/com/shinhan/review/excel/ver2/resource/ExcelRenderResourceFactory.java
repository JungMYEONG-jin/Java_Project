package com.shinhan.review.excel.ver2.resource;

import com.shinhan.review.excel.annotation.DefaultBodyStyle;
import com.shinhan.review.excel.annotation.DefaultHeaderStyle;
import com.shinhan.review.excel.annotation.ExcelColumn;
import com.shinhan.review.excel.annotation.ExcelColumnStyle;
import com.shinhan.review.excel.template.style.ExcelCellStyle;
import com.shinhan.review.excel.template.style.NoExcelCellStyle;
import com.shinhan.review.excel.ver2.decider.DataFormatDecider;
import com.shinhan.review.exception.InvalidExcelCellStyleException;
import com.shinhan.review.exception.NoExcelColumnAnnotationsException;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.shinhan.review.excel.ver2.util.ClassFieldUtils.*;

public class ExcelRenderResourceFactory {

    public static ExcelRenderResource prepareRenderResource(Class<?> type, Workbook wb, DataFormatDecider dataFormatDecider){
        PreCalculatedCellStyleMap preCalculatedCellStyleMap = new PreCalculatedCellStyleMap(dataFormatDecider);
        Map<String, String> headerNameMap = new LinkedHashMap<>();
        List<String> fieldNames = new ArrayList<>();


        ExcelColumnStyle headerStyle = getHeaderStyle(type);
        ExcelColumnStyle bodyStyle = getBodyStyle(type);


        List<Field> allFields = getAllFields(type);
        for (Field field : allFields) {
            if (field.isAnnotationPresent(ExcelColumn.class)){
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                preCalculatedCellStyleMap.put(String.class, ExcelCellKey.of(field.getName(), ExcelRenderLocation.HEADER),getCellStyle(decideAppliedStyleAnnotation(headerStyle, annotation.headerStyle())),wb);
                Class<?> fieldType = field.getType();
                preCalculatedCellStyleMap.put(fieldType, ExcelCellKey.of(field.getName(), ExcelRenderLocation.BODY), getCellStyle(decideAppliedStyleAnnotation(bodyStyle, annotation.bodyStyle())), wb);
                fieldNames.add(field.getName());
                headerNameMap.put(field.getName(), annotation.headerName());
            }
        }

        if (preCalculatedCellStyleMap.isEmpty()){
            throw new NoExcelColumnAnnotationsException(String.format("Class %s has not @ExcelColumn annotation", type));
        }

        return new ExcelRenderResource(preCalculatedCellStyleMap, headerNameMap, fieldNames);
    }

    private static ExcelColumnStyle getHeaderStyle(Class<?> clazz){
        Annotation annotation = getAnnotation(clazz, DefaultHeaderStyle.class);
        if (annotation == null)
            return null;
        return ((DefaultHeaderStyle) annotation).style();
    }

    private static ExcelColumnStyle getBodyStyle(Class<?> clazz){
        Annotation annotation = getAnnotation(clazz, DefaultBodyStyle.class);
        if (annotation == null)
            return null;
        return ((DefaultBodyStyle) annotation).style();
    }

    private static ExcelColumnStyle decideAppliedStyleAnnotation(ExcelColumnStyle classAnnotation, ExcelColumnStyle fieldAnnotation){
        if (fieldAnnotation.excelCellStyleClass().equals(NoExcelCellStyle.class) && classAnnotation!=null){
            return classAnnotation;
        }
        return fieldAnnotation;
    }

    private static ExcelCellStyle getCellStyle(ExcelColumnStyle excelColumnStyle){
        Class<? extends ExcelCellStyle> cellStyleClass = excelColumnStyle.excelCellStyleClass();
        // enum
        if (cellStyleClass.isEnum()){
            String enumName = excelColumnStyle.enumName();
            return findCellStyle(cellStyleClass, enumName);
        }

        // class
        try{
            return cellStyleClass.newInstance();
        }catch (InstantiationException | IllegalAccessException e){
            throw new InvalidExcelCellStyleException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private static ExcelCellStyle findCellStyle(Class<?> cellStyleClass, String enumName) {
        try{
            return (ExcelCellStyle) Enum.valueOf((Class<Enum>) cellStyleClass, enumName);
        }catch (NullPointerException e){
            throw new InvalidExcelCellStyleException("enumName must not be null", e);
        }catch (IllegalArgumentException e){
            throw new InvalidExcelCellStyleException(String.format("E num %s doest not name %s", cellStyleClass.getName(), enumName));
        }
    }

}
