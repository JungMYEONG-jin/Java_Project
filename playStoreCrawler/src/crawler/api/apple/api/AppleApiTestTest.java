package crawler.api.apple.api;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppleApiTestTest {
    AppleApi controller = new AppleApi();

    @Test
    void test() throws MalformedURLException, NoSuchAlgorithmException {
        List<JSONObject> allReviews = controller.getAllReviews(AppleAppId.O2O.getAppPkg());
        for (JSONObject allReview : allReviews) {
            System.out.println("allReview = " + allReview);

        }
    }
}