package com.shinhan.review.entity.dto;

import com.shinhan.review.entity.Review;
import com.shinhan.review.excel.annotation.ExcelColumn;
import org.json.simple.JSONObject;

public class ReviewDto {
    @ExcelColumn(headerName = "버전")
    public String appVersion;
    @ExcelColumn(headerName = "작성일")
    public String createdDate; // 리뷰 작성일
    @ExcelColumn(headerName = "닉네임")
    public String nickname;
    @ExcelColumn(headerName = "평점")
    public String rating;
    @ExcelColumn(headerName = "리뷰")
    public String body; // 리뷰
    @ExcelColumn(headerName = "답변")
    public String responseBody;
    @ExcelColumn(headerName = "답변일")
    public String answeredDate; // 답변일
    @ExcelColumn(headerName = "디바이스")
    public String device;
    @ExcelColumn(headerName = "앱이름")
    public String appPkg;
    @ExcelColumn(headerName = "OS")
    public String osType;

    public ReviewDto() {
    }

    public ReviewDto(String appVersion, String createdDate, String nickname, String rating, String body, String responseBody, String answeredDate, String device, String appPkg, String osType) {
        this.appVersion = appVersion;
        this.createdDate = createdDate;
        this.nickname = nickname;
        this.rating = rating;
        this.body = body;
        this.responseBody = responseBody;
        this.answeredDate = answeredDate;
        this.device = device;
        this.appPkg = appPkg;
        this.osType = osType;
    }

    public Review toEntity(){
        return new Review(appVersion, createdDate, nickname, rating, body,responseBody, answeredDate, device, appPkg, osType);
    }

    public ReviewDto(JSONObject jsonObject){
        if (jsonObject!=null){
            if (jsonObject.containsKey("appVersion"))
                appVersion = jsonObject.get("appVersion").toString();
            if (jsonObject.containsKey("createdDate"))
                createdDate = jsonObject.get("createdDate").toString();
            if (jsonObject.containsKey("nickName"))
                nickname = jsonObject.get("nickName").toString();
            if (jsonObject.containsKey("rating"))
                rating = jsonObject.get("rating").toString();
            if (jsonObject.containsKey("body"))
                body = jsonObject.get("body").toString();
            if (jsonObject.containsKey("responseBody"))
                responseBody = jsonObject.get("responseBody").toString();
            if (jsonObject.containsKey("answeredDate"))
                answeredDate = jsonObject.get("answeredDate").toString();
            if (jsonObject.containsKey("device"))
                device = jsonObject.get("device").toString();
        }
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getAppPkg() {
        return appPkg;
    }

    public void setAppPkg(String appPkg) {
        this.appPkg = appPkg;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAnsweredDate() {
        return answeredDate;
    }

    public void setAnsweredDate(String answeredDate) {
        this.answeredDate = answeredDate;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
