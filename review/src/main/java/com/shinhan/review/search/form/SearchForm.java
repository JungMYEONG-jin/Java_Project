package com.shinhan.review.search.form;

import com.shinhan.review.crawler.OS;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class SearchForm {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    private OS os;

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
}
