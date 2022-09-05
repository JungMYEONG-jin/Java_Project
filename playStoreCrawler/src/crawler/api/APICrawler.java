package crawler.api;

import com.beust.ah.A;
import crawler.api.apple.api.AppleApi;
import org.json.simple.JSONObject;

import java.util.List;

public interface APICrawler {
    List<JSONObject> getReviews(String packageName, String osType);
}
