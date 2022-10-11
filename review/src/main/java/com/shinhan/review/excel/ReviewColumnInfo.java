package com.shinhan.review.excel;

import org.apache.poi.ss.usermodel.Cell;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum ReviewColumnInfo implements ExcelColumnInfo{

    APP("앱이름", 0, 0, ColumnType.TEXT),
    VERSION("버전", 0, 1, ColumnType.NUMBER),
    OS("OS", 0, 2, ColumnType.TEXT),
    DEVICE("디바이스", 0, 3, ColumnType.TEXT),
    NICKNAME("닉네임", 0, 4, ColumnType.TEXT),
    CREATEDDATE("작성일", 0, 5, ColumnType.TEXT),
    RATING("평점", 0, 6, ColumnType.TEXT),
    REVIEW("리뷰", 0, 7, ColumnType.TEXT),
    ANSWEREDDATE("답변일", 0, 8, ColumnType.TEXT),
    RESPONSE("답변", 0, 9, ColumnType.TEXT);


    private final String text;
    private final int row;
    private final int col;
    private final ColumnType columnType;

    public String getText() {
        return text;
    }

    @Override
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    ReviewColumnInfo(String text, int row, int col, ColumnType columnType) {
        this.text = text;
        this.row = row;
        this.col = col;
        this.columnType = columnType;
    }

    // row 그룹화해서 header value 가져오기
    public static Map<Integer, List<ReviewColumnInfo>> getAllColumns(){
        return Arrays.stream(values()).collect(Collectors.groupingBy(ExcelColumnInfo::getRow));
    }

}
