package com.shinhan.review.search.form;

import com.shinhan.review.crawler.OS;
import com.shinhan.review.custom.annotation.ValidDateRange;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

//@ValidDateRange
public class SearchForm {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;
    private OS os;
    private String appPkg;

    // 12자리 review createdTime 과 비교하기 위해
    private String strStart;
    private String strEnd;

    public String getStrStart() {
        return strStart;
    }

    public void setStrStart(String strStart) {
        this.strStart = strStart;
    }

    public String getStrEnd() {
        return strEnd;
    }

    public void setStrEnd(String strEnd) {
        this.strEnd = strEnd;
    }

    public SearchForm() {
    }

    public SearchForm(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public SearchForm(LocalDate start, LocalDate end, OS os) {
        this.start = start;
        this.end = end;
        this.os = os;
    }

    public SearchForm(LocalDate start, LocalDate end, OS os, String appPkg) {
        this.start = start;
        this.end = end;
        this.os = os;
        this.appPkg = appPkg;
    }

    public String getAppPkg() {
        return appPkg;
    }

    public void setAppPkg(String appPkg) {
        this.appPkg = appPkg;
    }

    public OS getOs() {
        return os;
    }

    public void setOs(OS os) {
        this.os = os;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public void clear(){
        start = null;
        end = null;
        os = null;
        appPkg = "";
    }

}
