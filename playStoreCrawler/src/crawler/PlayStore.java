package crawler;

import com.beust.ah.A;
import crawler.apple.api.AppleApi;
import crawler.apple.api.AppleAppId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class PlayStore {

    public static void main(String[] args) {
//        WikiManager wikiManager = new WikiManager();
//        List<AppInfo> res = new ArrayList<>();
//        String mainID = "22216948";
//        res = wikiManager.getBasedList();
//        wikiManager.addSolution("22216948", res);
//        wikiManager.addMiniCategory(mainID);
        AppleApi api = new AppleApi();
        String jwt = api.createJWT();
        try {
            long start = new Date().getTime();
            List<JSONObject> allReviews = api.getAllReviews(jwt, AppleAppId.sbankmini_ios.getAppPkg());
            for (JSONObject allReview : allReviews) {
                System.out.println("allReview = " + allReview);
            }
            long end = new Date().getTime();
            System.out.println((end - start)/1000); // sec
            System.out.println("allReviews = " + allReviews.size());


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }





    //AIzaSyBQrXTIJL4XfXUuy7cMp7pqVu5zW8kWr8M google key
    //5722814114791747414 dev id
    //118014375029-3lfevtf7okr9mqrn7l8p41p4g3dus4ah.apps.googleusercontent.com client id
    //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 client password


    private static void sleep(int millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    public void test(){
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(chromeOptions);

//        String naver = "https://www.naver.com";

        String solURL = "https://play.google.com/store/apps/details?id=com.shinhan.sbanking&hl=ko&gl=US";

        driver.get(solURL);

//        List<WebElement> elements = driver.findElements(By.xpath("//div[@class='h3YV2d']"));

        WebElement element = driver.findElement(By.xpath("//button[@aria-label='앱 정보 자세히 알아보기']"));
        if (element.isEnabled()){
            System.out.println("클릭이 가능합니다.");

            element.click();

            sleep(1000);


            WebElement title = driver.findElement(By.xpath("//*[@id=\"yDmH0d\"]/div[4]/div[2]/div/div/div/div/div[1]/div/div/h5[@class='xzVNx']"));
            System.out.println("title.getText() = " + title.getText());

            WebElement parentAppInfos = driver.findElement(By.xpath("//div[@class='G1zzid']"));
            List<WebElement> appInfos = parentAppInfos.findElements(By.xpath("*"));

            System.out.println("appInfos.size() = " + appInfos.size());
            for (WebElement webElement : appInfos) {
                // div class "q078ud" key
                // div class "reAt0" value
                WebElement key = webElement.findElement(By.xpath("div[@class='q078ud']"));
                WebElement value = webElement.findElement(By.xpath("div[@class='reAt0']"));
                System.out.println("key.getText() = " + key.getText());
                System.out.println("value.getText() = "+value.getText());

            }

        }else{
            System.out.println("클릭이 불가능합니다. xpath 확인해주세요.");
        }



        driver.quit();
    }
}
