package com.shinhan.review.entity;

import javax.persistence.*;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appVersion;
    private String createdDate; // 리뷰 작성일
    private String nickname;
    private String rating;
    @Column(length = 4000)
    private String body; // 리뷰
    private String answeredDate; // 답변일
    private String device;

    public Review() {
    }

    public Review(Long id, String appVersion, String createdDate, String nickname, String rating, String body, String answeredDate, String device) {
        this.id = id;
        this.appVersion = appVersion;
        this.createdDate = createdDate;
        this.nickname = nickname;
        this.rating = rating;
        this.body = body;
        this.answeredDate = answeredDate;
        this.device = device;
    }

    public Long getId() {
        return id;
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
}
