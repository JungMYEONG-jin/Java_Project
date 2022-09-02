package crawler.google;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GoogleControllerTest {

    GoogleController controller = new GoogleController();

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

    @Test
    void getReviewTest() throws ParseException, MalformedURLException {
        String accessToken = controller.getAccessToken();
        List<JSONObject> reviewDetails = controller.getReviewDetails("com.shinhan.o2o", accessToken);
        System.out.println("reviewDetails = " + reviewDetails.size());

        for (JSONObject reviewDetail : reviewDetails) {
            System.out.println("reviewDetail = " + reviewDetail.toJSONString());
        }


//        System.out.println("reviewDetails = " + reviewDetails);
//
//        JSONParser parser = new JSONParser();
//        JSONObject parse = (JSONObject)parser.parse(reviewDetails);
//        JSONObject next = (JSONObject)parse.get("tokenPagination");
//        String nextPageToken = next.get("nextPageToken").toString();
//        System.out.println("nextPageToken = " + nextPageToken);
//        String details = controller.getReviewDetails("com.shinhan.o2o", nextPageToken);
//        System.out.println("details = " + details);


    }
}

