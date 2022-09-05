package crawler.api;

import crawler.api.apple.api.AppleApi;
import crawler.api.google.GoogleApi;
import org.json.simple.JSONObject;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class CrawlerV1 implements APICrawler{

    private final static AppleApi apple = new AppleApi();
    private final static GoogleApi google = new GoogleApi();
    private final static String TypeAnd = "1";
    private final static String TypeIOS = "2";

    @Override
    public List<JSONObject> getReviews(String packageName, String osType) {
        if(osType.equals(TypeAnd)) {
            try {
                return google.getReviewList(packageName);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        else if(osType.equals(TypeIOS)) {
            try {
                return apple.getAllReviews(packageName);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
