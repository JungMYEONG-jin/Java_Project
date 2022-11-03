package com.shinhan.review.search.form;

import com.shinhan.review.crawler.OS;
import com.shinhan.review.custom.annotation.ValidDateRange;
import com.shinhan.review.entity.DateEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class SearchForm2 {

    @ValidDateRange
    private DateEntity dateEntity;

    private OS os;
    private String appPkg;

    public SearchForm2() {
    }

    public DateEntity getDateEntity() {
        return dateEntity;
    }

    public void setDateEntity(DateEntity dateEntity) {
        this.dateEntity = dateEntity;
    }

    public OS getOs() {
        return os;
    }

    public void setOs(OS os) {
        this.os = os;
    }

    public String getAppPkg() {
        return appPkg;
    }

    public void setAppPkg(String appPkg) {
        this.appPkg = appPkg;
    }

    public void clear(){
        dateEntity = null;
        os = null;
        appPkg = "";
    }
}
