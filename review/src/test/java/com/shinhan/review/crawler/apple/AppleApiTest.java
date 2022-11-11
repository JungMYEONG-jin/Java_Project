package com.shinhan.review.crawler.apple;

import org.assertj.core.api.Assertions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Profile;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * provisioning 파일로 쓰려면
 * content 내용을 base64 decode 후 .mobileprovision 저장해야됨
 */

@SpringBootTest
class AppleApiTest {

    AppleApi controller = new AppleApi();

    @Test
    void createJWT() {

        String jwt = controller.createJWT();
        System.out.println("jwt = " + jwt);
    }

    @Test
    void review() throws MalformedURLException, NoSuchAlgorithmException {
        List<JSONObject> allReviews = controller.getAllReviews(AppleAppId.O2O.getAppPkg());
        for (JSONObject allReview : allReviews) {
            System.out.println("allReview = " + allReview);
        }
    }

    /**
     * id : appid
     * name : provisioning file name
     * type : provisioning type  etc.. IOS_APP_STORE, IOS_APP_ADHOC
     * bundleId : bundleId
     * bundleType : bundleType
     * certificateId : certificateId
     * certificateType : certificateType
     * device : deviceArr matches appId
     * @throws MalformedURLException
     * @throws NoSuchAlgorithmException
     */
    @Test
    void getProfileInfo() throws MalformedURLException, NoSuchAlgorithmException {
        String jwt = controller.createJWT();
        JSONArray profileInfo = controller.getProfileInfo(jwt);
        for (Object o : profileInfo) {
            JSONObject obj = (JSONObject) o;
            System.out.println("obj = " + obj);
        }
    }


    @Test
    void getDeviceInfoTest() throws MalformedURLException, NoSuchAlgorithmException, ParseException {
        JSONArray profileInfo = controller.getDeviceInfoFromProfile(controller.createJWT(), "7DPLA3U3AP");
        for (Object o : profileInfo) {
            JSONObject obj = (JSONObject) o;
            System.out.println("obj = " + obj);
        }
    }
//XS9V8NVWX7
    @Description("새 appid와 이전id certificate 정보 일치 테스트")
    @Test
    void getCertificates() throws MalformedURLException, NoSuchAlgorithmException {
        JSONObject certificateInfo = controller.getCertificateFromProfile(controller.createJWT(), "7DPLA3U3AP");
        JSONObject certificateInfo2 = controller.getCertificateFromProfile(controller.createJWT(), "L7CQXPB4DR");
        System.out.println("certificateInfo = " + certificateInfo);
        Assertions.assertThat(certificateInfo).isEqualTo(certificateInfo2);
    }

    @Test
    void getBundleIdTest() throws MalformedURLException, NoSuchAlgorithmException {
        JSONObject bundleInfo = controller.getBundleIdFromProfile(controller.createJWT(), "7DPLA3U3AP");
        JSONObject bundleInfo2 = controller.getBundleIdFromProfile(controller.createJWT(), "L7CQXPB4DR");
        System.out.println("bundleInfo = " + bundleInfo);
        Assertions.assertThat(bundleInfo).isEqualTo(bundleInfo2);
    }

    @Test
    void deleteTest() throws IOException {
        String jwt = controller.createJWT();
        int resCode = controller.deleteProfile(jwt, "L7CQXPB4DR");
        Assertions.assertThat(resCode).isEqualTo(204);
    }

    /**
     * 204 is normal
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @Test
    void updateTest() throws IOException, NoSuchAlgorithmException {
        String jwt = controller.createJWT();
        String name = "poney_distribution_20231110";
        int i = controller.updateProfile(jwt, name);
        Assertions.assertThat(i).isEqualTo(204);
    }
}

/**
 * id는 getProfileInfo 해서 나오는 계정임
 */