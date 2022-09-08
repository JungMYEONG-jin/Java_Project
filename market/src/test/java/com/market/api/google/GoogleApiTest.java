package com.market.api.google;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class GoogleApiTest {

    @Autowired
    GoogleApi controller;

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