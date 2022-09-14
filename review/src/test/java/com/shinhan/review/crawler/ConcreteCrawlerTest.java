package com.shinhan.review.crawler;

import com.shinhan.review.crawler.apple.AppleAppId;
import com.shinhan.review.crawler.google.GoogleAppId;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConcreteCrawlerTest {

    @Autowired
    ConcreteCrawler crawler;

    @Description("android")
    @Test
    void andTest() {
        List<JSONObject> reviewList = crawler.getReviewList(GoogleAppId.salimi_android.getAppPkg(), OS.AND.getNumber());
        if (!reviewList.isEmpty()) {
            for (JSONObject jsonObject : reviewList) {
                System.out.println("jsonObject = " + jsonObject);
            }
        }
    }

    @Description("ios")
    @Test
    void appleTest() {
        List<JSONObject> reviewList = crawler.getReviewList(AppleAppId.salimi_ios.getAppPkg(), OS.IOS.getNumber());
        for (JSONObject jsonObject : reviewList) {
            System.out.println("jsonObject = " + jsonObject);
        }
    }

    @Description("존재하지않는 타입")
    @Test
    void failTest() {
        Assertions.assertThatThrownBy(()->{crawler.getReviewList(AppleAppId.salimi_ios.getAppPkg(),"3");}).isInstanceOf(IllegalArgumentException.class);
    }
}