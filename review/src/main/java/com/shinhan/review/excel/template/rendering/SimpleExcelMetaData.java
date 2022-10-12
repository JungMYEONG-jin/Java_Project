package com.shinhan.review.excel.template.rendering;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SimpleExcelMetaData {

    public String appVersion;
    public String createdDate; // 리뷰 작성일
    public String nickname;
    public String rating;
    public String body; // 리뷰
    public String responseBody;
    public String answeredDate; // 답변일
    public String device;
    public String appPkg;
    public String osType;


    public SimpleExcelMetaData() {
    }

    public SimpleExcelMetaData(Class<?> type) {
        Field[] declaredFields = type.getDeclaredFields();
        Field[] thisClassFields = this.getClass().getDeclaredFields();
        Map<String, Boolean> map = new HashMap<>();

        Arrays.stream(thisClassFields).forEach(field -> map.put(field.getName(), false));
        Arrays.stream(declaredFields).forEach(field -> {
            if (map.containsKey(field.getName()))
                map.put(field.getName(), true);
            else
                throw new IllegalArgumentException("기준 메타 필드와 받아온 데이터가 일치하지 않습니다...");
        });

    }

    public SimpleExcelMetaData(Map<String, String> headerNameMap) {
    }
}
