package com.market.service;

import com.market.api.apple.AppleApi;
import com.market.crawling.Crawling;
import com.market.crawling.data.CrawlingResultData;
import com.market.daemon.MarketDaemon;
import com.market.daemon.dao.MarketInfo;
import com.market.daemon.dto.SendInfo;
import com.market.daemon.sender.MarketSender;
import com.market.daemon.service.MarketService;
import com.market.entity.Market;
import com.market.entity.Send;
import com.market.repository.MarketRepository;
import com.market.repository.SendRepository;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.net.MalformedURLException;
import java.util.List;

@SpringBootTest
class MarketJpaServiceTest {

    @Autowired
    MarketRepository marketRepository;

    @Autowired
    SendRepository sendRepository;
    @Autowired
    AppleApi appleApi;


//    @Test
//    void insertTest() throws Exception {
//
////        String keyPath = "static/apple/AuthKey_7JL62P566N.p8";
////        Resource resource = new ClassPathResource(keyPath);
////        System.out.println("resource " + resource.getURI().toString());
//
//        Crawling crawling = new Crawling();
//        List<SendInfo> sendInfos = marketRepository.GET_SEND_INFO_LIST();
//        for (SendInfo sendInfo : sendInfos) {
//            System.out.println("sendInfo = " + sendInfo);
//            CrawlingResultData ret = null;
//            if(sendInfo.getOsType().equals(SendInfo.OS_TYPE_IOS_API)){
//                ret = appleApi.getCrawlingResult(sendInfo.getAppPkg());
//            } else {
//                ret = crawling.crawling(sendInfo);
//            }
//            System.out.println("ret = " + ret);
//        }
//
//
//    }
}