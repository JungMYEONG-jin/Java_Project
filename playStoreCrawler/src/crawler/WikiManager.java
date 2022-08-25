package crawler;

import io.opentelemetry.exporter.logging.SystemOutLogExporter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WikiManager {

    private static final String preURL = "https://play.google.com/store/apps/details?id=";
    private static final String postURL = "&hl=ko&gl=US";
    private static final String reviewURL = "&showAllReviews=true";
    private static final String managerURL = "https://mobwiki.shinhan.com/pages/viewpage.action?pageId=";

    private WebDriver getBackGroundDriver(){
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
//        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        // set background setting
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        return driver;
    }

    private WebDriver getForeGroundDriver(){
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
//        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        // set foreground setting
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        return driver;
    }

    private void dododo(){
        List<AppInfo> result = new ArrayList<>();
        result.add(new AppInfo("nFilter", "보안키패드", "NSHC"));
        result.add(new AppInfo("OnePass", "FIDO", "라온시큐어"));
        result.add(new AppInfo("Orchestra", "하이브리드프레임웍", "핑거"));
        result.add(new AppInfo("MobilianWeb", "공동인증서", "이니텍"));
        result.add(new AppInfo("IDCard", "신분증인식", "인지소프트"));
        result.add(new AppInfo("MOTP", "모바일OTP", "아톤"));
        result.add(new AppInfo("Allatori", "소스난독화", "NSHC"));
        result.add(new AppInfo("CodeGuard", "앱위변조방지", "비티웍스"));
        result.add(new AppInfo("SplunkMint", "앱모니터링", "유클릭"));
        result.add(new AppInfo("NetFUNNEL", "유량제어", "에스티씨랩"));
        result.add(new AppInfo("ClipReport", "PDF전자서명", "클립소프트"));
        result.add(new AppInfo("SafeOn", "PUSH", "마이스스포트"));
        result.add(new AppInfo("S-Pass", "비대면영상인증", "신한DS"));
        result.add(new AppInfo("QRCode", "인증서복사", "라온시큐어"));
        result.add(new AppInfo("UBIKey", "공동인증서", "인포바인"));
        result.add(new AppInfo("GA360", "앱정보수집", "Golden Planet"));
        result.add(new AppInfo("UniSign", "전자세금계산서", "한국전자인증"));
        result.add(new AppInfo("WebSquare", "웹프레임웍", "인스웨이브"));
        result.add(new AppInfo("My-Id", "본인인증", "아이콘루프"));
        result.add(new AppInfo("Openface", "안면인증", "네오시큐"));
        result.add(new AppInfo("MMP", "광고플랫폼", "애드브릭스"));
        result.add(new AppInfo("IniPass", "금융인증서", "이니텍"));
        result.add(new AppInfo("AppSuit", "앱소스난독화/위변조방지", "스틸리언"));
        result.add(new AppInfo("BankId", "간편로그인", "금융결제원"));
        result.add(new AppInfo("ShinhanSign", "신한인증서", "아톤"));
        result.add(new AppInfo("ChattingPlus", "SMS기반이체연동서비스", "SKT"));
        result.add(new AppInfo("MetsaFR", "안면인증", "시큐젠"));
        result.add(new AppInfo("BankPay", "제로페이", "금융결제원"));
    }


    public void sleep(int millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getNumSize(int num){
        int n = 0;
        while(num>0){
            num/=10;
            n++;
        }
        return n;
    }

    /**
     * 메인 아이디 -> 각 솔루션 아이디 -> 각 솔루션 마다 Android iOS 등록
     * @param mainID
     */
    public void addMiniCategory(String mainID) throws InterruptedException {
        String url = managerURL+mainID;//22216948
        WebDriver driver = getBackGroundDriver();
        driver.get(url);
        WebElement idBox = driver.findElement(By.xpath("//*[@id=\"os_username\"]"));
        idBox.sendKeys("21111008");
        WebElement pwBox = driver.findElement(By.xpath("//*[@id=\"os_password\"]"));
        pwBox.sendKeys("audwls##2");
        pwBox.sendKeys(Keys.ENTER);
        // 잠시 대기
        // 현재 솔루션 리스트 갯수 구하기
        List<WebElement> childList = driver.findElements(By.xpath("//*[@id=\"child_ul"+mainID+"-0\"]/li"));
        Map<String, String> pageList = new HashMap<>();
        System.out.println("childList = " + childList.size());

        // get PageList and number
        for (WebElement webElement : childList) {
            String text = webElement.getText();
            List<WebElement> divs = webElement.findElements(By.xpath("div"));
            // 3번째에 id
            WebElement idDiv = divs.get(2);
            String dirtyID = idDiv.getAttribute("id");
            String[] split = dirtyID.split("-");
            String id = split[0];
            String pageID = id.replaceAll("[a-zA-Z]", "");
            String number = text.substring(0,text.indexOf('.'));
            String solutionURL = "https://mobwiki.shinhan.com/pages/viewpage.action?pageId="+pageID;
            pageList.put(solutionURL, number);
        }

        int cnt = 0;
        // 등록
        for (Map.Entry<String, String> entry : pageList.entrySet()) {
            String key = entry.getKey(); // page
            String value = entry.getValue(); // number
            driver.get(key);
            cnt++;
            List<WebElement> cateList = driver.findElements(By.xpath("//*[@id=\"child_ul"+key.substring(key.indexOf('=')+1)+"-0\"]/li"));
            if (cateList.size()==2) //and ios 존재시
                continue;
            System.out.println(key+" "+value+" "+cateList.size()+" "+cnt);
            addOSType(driver, value+"."+"Android");
            driver.get(key);
            addOSType(driver, value+"."+"iOS");
        }

        driver.quit();
    }

    private void addOSType(WebDriver driver, String OSType) {
        WebElement createButton = driver.findElement(By.xpath("//*[@id=\"quick-create-page-button\"]"));
        createButton.click();

        // text
        WebElement textBox = driver.findElement(By.xpath("//*[@id=\"content-title\"]"));
        textBox.sendKeys(OSType);

        //submit
        WebElement submitButton = driver.findElement(By.xpath("//*[@id=\"rte-button-publish\"]"));
        if(submitButton.isEnabled())
            submitButton.click();
    }


    public void addSolution(String mainID, List<AppInfo> solutionList){
        String url = managerURL+mainID;//22216948
        WebDriver driver = getBackGroundDriver();
        driver.get(url);
        WebElement idBox = driver.findElement(By.xpath("//*[@id=\"os_username\"]"));
        idBox.sendKeys("21111008");
        WebElement pwBox = driver.findElement(By.xpath("//*[@id=\"os_password\"]"));
        pwBox.sendKeys("audwls##2");
        pwBox.sendKeys(Keys.ENTER);
        // 잠시 대기
        sleep(1000);
        // 현재 솔루션 리스트 갯수 구하기
        List<WebElement> childList = driver.findElements(By.xpath("//*[@id=\"child_ul"+mainID+"-0\"]/li"));
        int num = childList.size()+1; // 현재 갯수 + 1 에서 시작해야함

        for (AppInfo appInfo : solutionList) {
            // 만들기 버튼 클릭
            WebElement createButton = driver.findElement(By.xpath("//*[@id=\"quick-create-page-button\"]"));
            if (createButton.isEnabled())
                createButton.click();

            int numSize = getNumSize(num);
            // 제목 형식
            String result_user_no = String.format("%0"+String.valueOf(numSize)+"d", num++);
            // 제목 박스
            WebElement textBox = driver.findElement(By.xpath("//*[@id=\"content-title\"]"));
            sleep(1000);
            // 입력
            textBox.sendKeys(result_user_no+"."+appInfo.getTitle());
            sleep(1000);
            // 제출
            WebElement submitButton = driver.findElement(By.xpath("//*[@id=\"rte-button-publish\"]"));
            if (submitButton.isEnabled())
                submitButton.click();

            sleep(1000);
            driver.get(url);

        }
        driver.quit();
    }

}
