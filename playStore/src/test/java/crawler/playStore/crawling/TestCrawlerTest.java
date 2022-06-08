package crawler.playStore.crawling;

import crawler.Crawler;
import crawler.playStore.crawler.PlayStoreCrawler;
import crawler.playStore.crawler.entity.App;
import crawler.playStore.crawler.entity.AppRepository;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCrawlerTest {

    @Autowired AppRepository em;

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
        JSONArray reviews = crawler.getReviews("com.shinhan.sbanking");
    }

    @Test
    public void integratedTest(){
        crawler.saveAppInformationToJSON("com.shinhan.sbanking");
    }


    /**
     *  obj.put("작성자", userName);
     *  obj.put("작성일", reviewDate);
     *  obj.put("리뷰", review);
     *  appInfo.put("앱 이름",null);
     *  appInfo.put("버전",null);
     *  appInfo.put("업데이트 날짜",null);
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void jpaTest(){
        HashMap<String, String> infos = crawler.getInfo("com.shinhan.sbanking");


        String appName = infos.get("앱 이름");
        String version = infos.get("버전");
        String updatedDate = infos.get("업데이트 날짜");

        JSONArray reviews = crawler.getReviews("com.shinhan.sbanking");
        for (Object review : reviews) {
            JSONObject obj = (JSONObject) review;
            String writer = obj.get("작성자").toString();
            String createdDate = obj.get("작성일").toString();
            String comment = obj.get("리뷰").toString();

            System.out.println("comment = " + comment);
//            App app = new App(appName, updatedDate, version, writer, createdDate, comment);
            App app = new App();
            app.setName(appName);
            app.setReview(comment);
            app.setReviewDate(createdDate);
            app.setUpdateDate(updatedDate);
            app.setVersion(version);
            app.setWriter(writer);
            em.save(app);
//            em.save(new App(appName, updatedDate, version, writer, createdDate, comment));

        }


    }









}