package crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.List;

public class PlayStoreCrawler implements Crawler{

    private static final HashMap<String, String> appInfo = new HashMap<>();
    private static final String preURL = "https://play.google.com/store/apps/details?id=";
    private static final String postURL = "&hl=ko&gl=US";
    public PlayStoreCrawler() {
        appInfo.put("앱 이름",null);
        appInfo.put("버전",null);
        appInfo.put("업데이트 날짜",null);
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

        System.out.println(packageName+" crawling start ");

        WebElement element = driver.findElement(By.xpath("//button[@aria-label='앱 정보 자세히 알아보기']"));
        if (element.isEnabled()){
            System.out.println("클릭이 가능합니다.");

            element.click();

            System.out.println("로드중입니다...");

            sleep(1000);

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
                System.out.println("key = " + keyText);
                System.out.println("value = " + valueText);

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



        driver.quit();

        return appInfo;
    }

    private void sleep(int millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


}