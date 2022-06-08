package crawler.playStore.crawling;

import crawler.Crawler;
import crawler.playStore.crawler.PlayStoreCrawler;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestCrawlerTest {

    Crawler crawler = new PlayStoreCrawler();

    @Test
    public void singleTest(){


        HashMap<String, String> infos = crawler.getInfo("com.shinhan.sbanking");

        for (String s : infos.keySet()) {
            System.out.println("key = " + s + " value = "+infos.get(s));
        }

        Assertions.assertThat(infos.size()).isEqualTo(3);


    }

    @Test
    public void listTest(){
        String[] arr = {"com.shinhan.sbanking", "com.shinhan.smartcaremgr", "com.shinhan.sbankmini"};

        List<HashMap<String, String>> listInfos = crawler.getInfoList(arr);
        for (HashMap<String, String> listInfo : listInfos) {
            System.out.println("===========================\n");

            for (String s : listInfo.keySet()) {
                System.out.println("key = " + s + " value = "+listInfo.get(s));
            }

            System.out.println("===========================\n");
        }
    }

    @Test
    public void reviewTest(){
        crawler.getReviews("com.shinhan.sbanking");
    }

    @Test
    public void integratedTest(){
        crawler.saveAppInformationToJSON("com.shinhan.sbanking");
    }










}