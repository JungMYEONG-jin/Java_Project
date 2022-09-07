package crawler.api;

import crawler.api.apple.api.AppleAppId;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrawlerV1Test {

    CrawlerV1 controller = new CrawlerV1();

    @Test
    void googleTest(){
        List<JSONObject> res = controller.getReviews("com.shinhan.o2o", "1");
        for (JSONObject re : res) {
            System.out.println("JSONString() = " + re.toJSONString());
        }
    }

    @Test
    void appleTest(){
        List<JSONObject> res = controller.getReviews(AppleAppId.O2O.getAppPkg(), "2");
        for (JSONObject re : res) {
            System.out.println("JSONString() = " + re.toJSONString());
        }
    }

}