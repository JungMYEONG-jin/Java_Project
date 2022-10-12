package com.shinhan.review.excel;

import com.shinhan.review.entity.dto.ReviewDto;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

class ReviewColumnInfoTest {
    @Test
    void name() {
        Map<Integer, List<ReviewColumnInfo>> allColumns = ReviewColumnInfo.getAllColumns();
        for (Integer integer : allColumns.keySet()) {
            System.out.println("integer = " + integer);
            System.out.println("allColumns = " + allColumns.get(integer));
            List<ReviewColumnInfo> reviewColumnInfos = allColumns.get(integer);
        }
    }

    @Test
    void fieldTest() throws IllegalAccessException {

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setAppVersion("1.0.1");
        reviewDto.setAppPkg("os.noface");

        Class<? extends ReviewDto> aClass = reviewDto.getClass();

        ReviewColumnInfo[] values = ReviewColumnInfo.values();
        for (ReviewColumnInfo value : values) {
            Field field = getField(aClass, value.name());
            field.setAccessible(true);
            System.out.println("field name = " + field.getName() +"  field.get(reviewDto) = " + field.get(reviewDto));
        }

    }

    private Field getField(Class<?> object, String fieldName){
        Field field = null;
        try {
            field = object.getField(fieldName);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}