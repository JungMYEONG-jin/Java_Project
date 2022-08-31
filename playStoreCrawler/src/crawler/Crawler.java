package crawler;

import org.json.simple.JSONArray;

import java.util.HashMap;
import java.util.List;

public interface Crawler {
    HashMap<String, String> getInfo(String packageName);
    List<HashMap<String, String >> getInfoList(String[] packageNames);
    JSONArray getReviews(String packageName);
    void saveAppInformationToJSON(String packageName);
}
