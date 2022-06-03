package crawler;

import java.util.HashMap;

public interface Crawler {

    HashMap<String, String> getInfos(String packageName);
}
