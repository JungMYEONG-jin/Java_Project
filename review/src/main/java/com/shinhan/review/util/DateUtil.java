package com.shinhan.review.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    public String getNowFormatToMilliSecond(){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(now);
    }

    public String transformDateYearToDay(String createdDate){
        // yyyymmddhhmmss to yyyy-mm-dd
        return LocalDate.of(Integer.parseInt(createdDate.substring(0, 4)), Integer.parseInt(createdDate.substring(4, 6)), Integer.parseInt(createdDate.substring(6, 8))).toString();
    }

    public LocalDate stringToLocalDate(String createdDate){
        return LocalDate.parse(createdDate, DateTimeFormatter.ISO_DATE);
    }

}
