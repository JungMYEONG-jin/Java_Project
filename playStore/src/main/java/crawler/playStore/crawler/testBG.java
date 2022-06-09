package crawler.playStore.crawler;

import java.util.HashMap;

public class testBG {
    public static void main(String[] args) {
        Crawler crawler = new PlayStoreCrawler();
        HashMap<String, String> infos = crawler.getInfo("com.shinhan.sbanking");

        for (String s : infos.keySet()) {
            System.out.println("key = " + s + " value = "+infos.get(s));
        }

    }
}
