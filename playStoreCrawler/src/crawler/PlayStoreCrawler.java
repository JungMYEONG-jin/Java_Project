package crawler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;

public class PlayStoreCrawler implements Crawler{

    private static final HashMap<String, String> appInfo = new HashMap<>();
    private static final String preURL = "https://play.google.com/store/apps/details?id=";
    private static final String postURL = "&hl=ko&gl=US";
    public PlayStoreCrawler() {
        appInfo.put("title",null);
        appInfo.put("version",null);
        appInfo.put("updatedDate",null);
    }

    @Override
    public HashMap<String, String> getInfos(String packageName) {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        // set background setting
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(chromeOptions);

        String url = preURL + packageName + postURL;

        driver.get(url);

        System.out.println("");








        return null;
    }


}