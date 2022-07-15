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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.net.MalformedURLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MarketJpaServiceTest {

    @Autowired
    MarketRepository marketRepository;

    @Autowired
    SendRepository sendRepository;
    @Autowired
    AppleApi appleApi;
    @BeforeEach
    void init() {
        Market smailvn_ios = Market.builder().appId("smailvn_ios").appPkg("1016762804").osType(MarketInfo.OS_TYPE_IOS_API).storeUrl("https://itunes.apple.com/kr/app/id").build();
        Market sbank = Market.builder().appId("sbank_ios").appPkg("357484932").osType(MarketInfo.OS_TYPE_IOS_API).storeUrl("https://itunes.apple.com/kr/app/id").build();
        marketRepository.save(sbank);
        marketRepository.save(smailvn_ios);
        Send sbank_ios = Send.builder().appId("sbank_ios").sendStatus("0").userId("1111").errorMsg("").build();
        Send smailvn_ios_send = Send.builder().appId("smailvn_ios").sendStatus("0").userId("1111").errorMsg("").build();
        sendRepository.save(sbank_ios);
        sendRepository.save(smailvn_ios_send);

    }

    @Test
    void insertTest() throws Exception {

//        String keyPath = "static/apple/AuthKey_7JL62P566N.p8";
//        Resource resource = new ClassPathResource(keyPath);
//        System.out.println("resource " + resource.getURI().toString());

        Crawling crawling = new Crawling();
        List<SendInfo> sendInfos = marketRepository.GET_SEND_INFO_LIST();
        for (SendInfo sendInfo : sendInfos) {
            System.out.println("sendInfo = " + sendInfo);
            CrawlingResultData ret = null;
            if(sendInfo.getOsType().equals(SendInfo.OS_TYPE_IOS_API)){
                ret = appleApi.getCrawlingResult(sendInfo.getAppPkg());
            } else {
                ret = crawling.crawling(sendInfo);
            }
            System.out.println("ret = " + ret);
        }


    }
}