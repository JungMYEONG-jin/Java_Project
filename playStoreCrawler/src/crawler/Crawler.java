package crawler;

import java.util.HashMap;
import java.util.List;

public interface Crawler {
    HashMap<String, String> getInfos(String packageName);
    List<HashMap<String, String >> getListInfos(String[] packageNames);
}
