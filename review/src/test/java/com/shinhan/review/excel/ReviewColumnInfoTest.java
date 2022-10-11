package com.shinhan.review.excel;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
}