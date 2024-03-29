package com.shinhan.review.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(ReviewId.class)
public class Review {
    @Column(length = 60)
    private String appVersion;
    @Id
    @Column(length = 14)
    private String createdDate; // 리뷰 작성일
    @Id
    @Column(length = 60)
    private String nickname;
    @Column(length=1)
    private String rating;
    @Column(length = 4000)
    private String body; // 리뷰
    @Column(length = 4000)
    private String responseBody;
    @Column(length=14)
    private String answeredDate; // 답변일
    @Column(length=60)
    private String device;
    @Column(length = 60)
    private String appPkg;
    @Column(length=1)
    private String osType;
    @Column
    private String osVer;

    public Review() {
    }

    public Review(String appVersion, String createdDate, String nickname, String rating, String body, String responseBody, String answeredDate, String device, String appPkg, String osType) {
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

    public Review(String appVersion, String createdDate, String nickname, String rating, String body, String responseBody, String answeredDate, String device, String appPkg, String osType, String osVer) {
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
        this.osVer = osVer;
    }

    public String getAppPkg() {
        return appPkg;
    }

    public String getOsType() {
        return osType;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRating() {
        return rating;
    }

    public String getBody() {
        return body;
    }

    public String getAnsweredDate() {
        return answeredDate;
    }

    public String getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return "Review{" +
                "appVersion='" + appVersion + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", nickname='" + nickname + '\'' +
                ", rating='" + rating + '\'' +
                ", body='" + body + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", answeredDate='" + answeredDate + '\'' +
                ", device='" + device + '\'' +
                ", appPkg='" + appPkg + '\'' +
                ", osType='" + osType + '\'' +
                ", osVer='" + osVer + '\'' +
                '}';
    }

    public String getOsVer() {
        return osVer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(appVersion, review.appVersion) && Objects.equals(createdDate, review.createdDate) && Objects.equals(nickname, review.nickname) && Objects.equals(rating, review.rating) && Objects.equals(body, review.body) && Objects.equals(responseBody, review.responseBody) && Objects.equals(answeredDate, review.answeredDate) && Objects.equals(device, review.device) && Objects.equals(appPkg, review.appPkg) && Objects.equals(osType, review.osType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appVersion, createdDate, nickname, rating, body, responseBody, answeredDate, device, appPkg, osType);
    }
}
