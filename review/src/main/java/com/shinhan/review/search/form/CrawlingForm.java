package com.shinhan.review.search.form;

import com.shinhan.review.crawler.OS;

public class CrawlingForm {

    private OS os;
    private String appId;

    public CrawlingForm() {
    }

    public CrawlingForm(OS os, String appId) {
        this.os = os;
        this.appId = appId;
    }

    public OS getOs() {
        return os;
    }

    public void setOs(OS os) {
        this.os = os;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
