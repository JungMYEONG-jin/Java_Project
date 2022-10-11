package com.shinhan.review.excel;

import java.awt.*;

public enum ColumnType {

    TEXT(Color.cyan),
    NUMBER(Color.green);

    public Color colr;

    ColumnType(Color colr) {
        this.colr = colr;
    }
}
