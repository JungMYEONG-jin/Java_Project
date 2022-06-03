package crawler;

import java.util.HashMap;
import java.util.List;

public interface Crawler {
    HashMap<String, String> getInfo(String packageName);
    List<HashMap<String, String >> getInfoList(String[] packageNames);
}
