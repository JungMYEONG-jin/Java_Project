package crawler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayStoreCrawler implements Crawler{

    private static final String preURL = "https://play.google.com/store/apps/details?id=";
    private static final String postURL = "&hl=ko&gl=US";
    private static final String reviewURL = "&showAllReviews=true";

    private WebDriver getBackGroundDriver(){
//        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        // set background setting
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(chromeOptions);

        return driver;
    }

    private WebDriver getForeGroundDriver(){
//        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        // set foreground setting
        WebDriver driver = new ChromeDriver();

        return driver;
    }


    private HashMap<String, String> doCrawling(WebDriver driver, String packageName){

        HashMap<String, String> appInfo = new HashMap<>();
        appInfo.put("앱 이름",null);
        appInfo.put("버전",null);
        appInfo.put("업데이트 날짜",null);

        String url = preURL + packageName + postURL;

        driver.get(url);

        System.out.println(packageName+" crawling start ");

        WebElement element = driver.findElement(By.xpath("//button[@aria-label='앱 정보 자세히 알아보기']"));
        if (element.isEnabled()){
            System.out.println("클릭이 가능합니다.");

//            element.click();
            element.sendKeys(Keys.ENTER); // 리눅스에서 클릭이 안되는 현상으로 인해 enter로 변경
            System.out.println("로드중입니다...");

            sleep(2000);

            WebElement title = driver.findElement(By.xpath("//*[@id=\"yDmH0d\"]/div[4]/div[2]/div/div/div/div/div[1]/div/div/h5[@class='xzVNx']"));
            System.out.println("앱 이름 = " + title.getText());

            appInfo.put("앱 이름", title.getText());


            WebElement parentAppInfos = driver.findElement(By.xpath("//div[@class='G1zzid']"));
            // 자세한 앱 정보 뽑아내기
            List<WebElement> appInfos = parentAppInfos.findElements(By.xpath("*"));

            System.out.println("자세한 앱 정보 갯수: " + appInfos.size());
            for (WebElement webElement : appInfos) {
                // div class "q078ud" key
                // div class "reAt0" value
                WebElement key = webElement.findElement(By.xpath("div[@class='q078ud']"));
                WebElement value = webElement.findElement(By.xpath("div[@class='reAt0']"));
                String keyText = key.getText();
                String valueText = value.getText();
//                System.out.println("key = " + keyText);
//                System.out.println("value = " + valueText);

                if (keyText.equals("버전") || keyText.equals("업데이트 날짜")){
                    appInfo.put(keyText, valueText);
                }

            }

        }else{
            System.out.println("클릭이 불가능합니다. xpath 확인해주세요.");
        }

        if(appInfo.size()!=3){
            System.out.println("키값이 변경되었습니다. 수정해주세요.");
        }
        return appInfo;
    }


    public void doReviewCrawling(WebDriver driver, String packageName) {

        JSONArray jsonArray = new JSONArray();
        String url = preURL + packageName + postURL + reviewURL;
        driver.get(url);
        System.out.println(packageName + " review crawling start ");

        List<WebElement> reviews = new ArrayList<>();

        // 리뷰 모두 보기 xpath
        WebElement element = driver.findElement(By.xpath("//button[@class='VfPpkd-LgbsSe VfPpkd-LgbsSe-OWXEXe-dgl2Hf ksBjEc lKxP2d qfvgSe aLey0c']"));

        if (element.isEnabled()) {
            System.out.println("클릭이 가능합니다.");

//            element.click();
            element.sendKeys(Keys.ENTER); // 리눅스에서 클릭이 안되는 현상으로 인해 Enter send
            System.out.println("로드중입니다...");

            sleep(2000);

            WebElement title = driver.findElement(By.xpath("//*[@id=\"yDmH0d\"]/div[4]/div[2]/div/div/div/div/div[1]/div/div/h5[@class='xzVNx']"));
            String titleName = title.getText();
            System.out.println("앱 이름 = " + titleName);


//            WebElement parentAppInfos = driver.findElement(By.xpath("//*[@id=\"yDmH0d\"]/div[4]/div[2]/div/div/div/div/div[2]/div/div[1]"));
            WebElement parentAppInfo = driver.findElement(By.xpath("//*[@id=\"yDmH0d\"]/div[4]/div[2]/div/div/div/div/div[2]"));


            int b = -1;
            while(b<20) {

                reviews = driver.findElements(By.xpath("//div[@class='RHo1pe']"));
                System.out.println("리뷰 갯수: " + reviews.size());
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView()", reviews.get(reviews.size()-1)); // 내부 view scroll
                sleep(100);
                b++;
            }

            System.out.println("리뷰 개수 = " + reviews.size());
            for (WebElement webElement : reviews) {
                // div class "q078ud" key
                // div class "reAt0" value
                JSONObject obj = new JSONObject();
                WebElement userElement = webElement.findElement(By.xpath("header/div[1]/div[1]/div[@class='X5PpBb']"));
                WebElement reviewDateElement = webElement.findElement(By.xpath("header/div[2]/span[@class='bp9Aid']"));
                WebElement reviewElement = webElement.findElement(By.xpath("div[@class='h3YV2d']"));

                String userName = userElement.getText();
                String reviewDate = reviewDateElement.getText();
                String review = reviewElement.getText();

                obj.put("이름", titleName);
                obj.put("작성자", userName);
                obj.put("작성일", reviewDate);
                obj.put("리뷰", review);

                jsonArray.add(obj);

            }


            for (Object obj : jsonArray) {
                JSONObject temp = (JSONObject) obj;
                System.out.println("review = " + temp.toJSONString());
            }
        }
    }

    @Override
    public void getReviews(String packageName) {
//        WebDriver driver = getBackGroundDriver();
        WebDriver driver = getForeGroundDriver();
        doReviewCrawling(driver, packageName);
        driver.quit();
    }


    @Override
    public HashMap<String, String> getInfo(String packageName) {



        WebDriver driver = getBackGroundDriver();

        HashMap<String, String> appInfo = doCrawling(driver, packageName);

        driver.quit();

        return appInfo;

    }

    @Override
    public List<HashMap<String, String>> getInfoList(String[] packageNames) {

        List<HashMap<String, String>> infos = new ArrayList<>();

        WebDriver driver = getBackGroundDriver();

        for (String packageName : packageNames) {
            infos.add(doCrawling(driver, packageName));
        }
        driver.quit();

        return infos;
    }


    private void sleep(int millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


}