package com.market.api.google;

import com.market.api.apple.AppleApi;
import com.market.api.apple.AppleAppId;
import com.market.crawling.data.CrawlingResultData;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@SpringBootTest
class GoogleApiTest {

    GoogleApi controller = new GoogleApi();

    @Test
    void mappingTest() {
        Map<String, String> clientInfo = controller.getClientInfo();
        for (String s : clientInfo.keySet()) {
            System.out.println("key = "+s+" value = "+clientInfo.get(s));
        }
    }

    @Test
    void getTokenTest() {
        String accessToken = controller.getAccessToken();
        System.out.println("accessToken = " + accessToken);
    }


}