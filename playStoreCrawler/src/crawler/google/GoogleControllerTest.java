package crawler.google;

import com.google.gson.Gson;
import com.sun.javafx.binding.StringFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterInputStream;

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
    void getReviewList() throws MalformedURLException {
        List<JSONObject> res = controller.getReviewList("com.shinhan.o2o");
        for (JSONObject re : res) {
            System.out.println("re.toJSONString() = " + re.toJSONString());
        }
    }

    @Test
    void getReviewTest() throws ParseException, MalformedURLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Map<String, String> map = new HashMap<>();
        map.put("createdDate", "");
        map.put("reviewerNickname", "");
        map.put("rating", "");
        map.put("body", "");
        map.put("responseBody", "");
        map.put("reviewDate", "");
        map.put("appVersion", "");
        map.put("device", "");
        map.put("reviewDate", "");

        JSONObject attr = new JSONObject(map);

        String accessToken = controller.getAccessToken();
        List<JSONObject> reviewDetails = controller.getReviewDetails("com.shinhan.o2o", accessToken);
        System.out.println("reviewDetails = " + reviewDetails.size());

        for (JSONObject reviewDetail : reviewDetails) {
            System.out.println("reviewDetail = " + reviewDetail.toJSONString());
        }
        JSONObject jsonObject = reviewDetails.get(0);
        JSONArray reviews = (JSONArray)jsonObject.get("reviews");
        System.out.println("reviews = " + reviews.toJSONString());



        for (Object review : reviews) {
            JSONObject val = (JSONObject) review;
            String authorName = val.get("authorName").toString();
            attr.put("reviewerNickname", authorName);
            JSONArray comments = (JSONArray)val.get("comments");
            JSONObject comment = (JSONObject)comments.get(0);
            JSONObject userComment = (JSONObject)comment.get("userComment");
            String userText = userComment.get("text").toString();
            attr.put("body", userText);
            JSONObject userLastModified = (JSONObject)userComment.get("lastModified");
            Long userSec = Long.parseLong(userLastModified.get("seconds").toString());
            attr.put("createdDate", dateFormat.format(new Date(userSec*1000)).toString());
            String starRating = userComment.get("starRating").toString();
            attr.put("rating", starRating);
            if (userComment.containsKey("device")){
                String device = userComment.get("device").toString();
                attr.put("device", device);
            }
            if(userComment.containsKey("appVersionName")) {
                String appVersionName = userComment.get("appVersionName").toString();
                attr.put("appVersion", appVersionName);
            }
            if (userComment.containsKey("androidOsVersion")) {
                String androidOsVersion = userComment.get("androidOsVersion").toString();
                attr.put("osVersion", androidOsVersion);
            }
            if (comment.containsKey("developerComment"))
            {
                JSONObject developerComment = (JSONObject)comment.get("developerComment");
                String text = developerComment.get("text").toString();
                attr.put("responseBody", text);
                JSONObject lastModified = (JSONObject)developerComment.get("lastModified");
                Long sec = Long.parseLong(userLastModified.get("seconds").toString());
                attr.put("reviewDate", dateFormat.format(new Date(userSec*1000)).toString());

            }
            attr.clear();
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

