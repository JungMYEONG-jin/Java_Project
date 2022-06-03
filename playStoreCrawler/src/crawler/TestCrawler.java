package crawler;

import java.util.HashMap;

public class TestCrawler {

    public static void main(String[] args) {
         PlayStoreCrawler crawler = new PlayStoreCrawler();

        HashMap<String, String> infos = crawler.getInfos("com.shinhan.sbanking");


        for (String s : infos.keySet()) {
            System.out.println("key = " + s + " value = "+infos.get(s));
        }
    }
}
