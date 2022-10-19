package com.shinhan.review.crawler.google;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.lang.model.SourceVersion;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class GoogleApiForMinuetTest {

    GoogleApiForMinuet googleApiForMinuet = new GoogleApiForMinuet();

    @Test
    void getClientInfoTest() {
        Map<String, String> clientInfo = googleApiForMinuet.getClientInfo();
        assertThat(clientInfo.isEmpty()).isFalse();
        for (String s : clientInfo.keySet()) {
            System.out.println(String.format("%s value %s", s, clientInfo.get(s)));
        }
    }

    @Test
    void getReviewTest() throws MalformedURLException {
        List<JSONObject> reviewList = googleApiForMinuet.getReviewList(GoogleAppId.sbank.getAppPkg());
        System.out.println("reviewList = " + reviewList.size());
        for (JSONObject jsonObject : reviewList) {
            System.out.println("jsonObject = " + jsonObject.toJSONString());
        }
    }
}