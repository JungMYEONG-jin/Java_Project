package crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestCrawlerTest {

    PlayStoreCrawler crawler = new PlayStoreCrawler();

    @Test
    public void singleTest(){


        HashMap<String, String> infos = crawler.getInfos("com.shinhan.sbanking");

        for (String s : infos.keySet()) {
            System.out.println("key = " + s + " value = "+infos.get(s));
        }

        Assertions.assertTrue(infos.size() == 3);
    }

    @Test
    public void listTest(){
        String[] arr = {"com.shinhan.sbanking", "com.shinhan.smartcaremgr", "com.shinhan.sbankmini"};

        List<HashMap<String, String>> listInfos = crawler.getListInfos(arr);
        for (HashMap<String, String> listInfo : listInfos) {
            System.out.println("===========================\n\n\n");

            for (String s : listInfo.keySet()) {
                System.out.println("key = " + s + " value = "+listInfo.get(s));
            }

            System.out.println("===========================\n\n\n");
        }


    }




}