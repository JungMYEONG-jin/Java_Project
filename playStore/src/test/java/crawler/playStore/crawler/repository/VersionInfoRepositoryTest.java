package crawler.playStore.crawler.repository;

import crawler.playStore.crawler.Crawler;
import crawler.playStore.crawler.PlayStoreCrawler;
import crawler.playStore.crawler.entity.VersionInfo;
import crawler.playStore.crawler.entity.compositekey.VersionId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class VersionInfoRepositoryTest {

    @Autowired
    VersionInfoRepository versionInfoRepository;

    Crawler crawler = new PlayStoreCrawler();

    @Test
    @Transactional
    @Rollback(value = false)
    public void versionTest(){
        String[] arr = {"com.shinhan.sbanking", "com.shinhan.smartcaremgr", "com.shinhan.sbankmini"};

        List<HashMap<String, String>> listInfos = crawler.getInfoList(arr);
        for (HashMap<String, String> infos : listInfos) {
            System.out.println("===========================\n");

            String appName = infos.get("앱 이름");
            String version = infos.get("버전");
            String updatedDate = infos.get("업데이트 날짜");
            updatedDate = updatedDate.replaceAll(" ","");
            updatedDate = updatedDate.substring(0, updatedDate.length()-1);
            VersionId versionId = new VersionId(appName, version);
            versionInfoRepository.save(new VersionInfo(versionId, updatedDate));
            System.out.println("===========================\n");
        }
    }

}