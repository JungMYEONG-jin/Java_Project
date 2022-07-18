package com.market.daemon.sender;

import com.market.daemon.MarketDaemon;
import com.market.daemon.dao.MarketInfo;
import com.market.daemon.dto.SendInfo;
import com.market.daemon.service.MarketService;
import com.market.entity.Market;
import com.market.entity.MarketPropertyEntity;
import com.market.entity.Send;
import com.market.exception.GetSendInfoListException;
import com.market.exception.SendInfoListException;
import com.market.property.MarketProperty;
import com.market.provider.ApplicationContextProvider;
import com.market.repository.MarketPropertyRepository;
import com.market.repository.MarketRepository;
import com.market.repository.SendRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
class MarketSenderTest {




    @Autowired
    MarketRepository marketRepository;
    @Autowired
    MarketDaemon daemon;
    @Autowired
    SendRepository sendRepository;
    @Autowired
    MarketPropertyRepository marketPropertyRepository;
    @Autowired
    MarketService marketService;
//    @Autowired
//    MarketProperty marketProperty;

    @BeforeEach
    void init(){
        Market smailvn_ios = Market.builder().appId("smailvn_ios").appPkg("1016762804").osType(MarketInfo.OS_TYPE_IOS_API).storeUrl("https://itunes.apple.com/kr/app/id").build();
        Market sbank = Market.builder().appId("sbank_ios").appPkg("357484932").osType(MarketInfo.OS_TYPE_IOS_API).storeUrl("https://itunes.apple.com/kr/app/id").build();
        marketRepository.save(sbank);
        marketRepository.save(smailvn_ios);
        Send sbank_ios = Send.builder().appId("sbank_ios").sendStatus("0").userId("1111").errorMsg("").build();
        Send smailvn_ios_send = Send.builder().appId("smailvn_ios").sendStatus("0").userId("1111").errorMsg("").build();
        sendRepository.save(sbank_ios);
        sendRepository.save(smailvn_ios_send);
        MarketPropertyEntity marketProperty = new MarketPropertyEntity();
        marketProperty.setPropertyVersion("1.0.1");
        marketProperty.setPropertyStatus("0");
        marketProperty.setDataType("1");
        marketProperty.setPropertyData("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
        marketProperty.setUserId("21111008");
        marketProperty.setIsSetting("N");
        marketPropertyRepository.save(marketProperty);
    }



    @Test
    void daemonTest() throws Exception {

        daemon.run();

//        daemon.setMarketProperty(marketProperty);
//        daemon.setMarketDBService(marketService);
//        Thread threadDaemon = new Thread(daemon);
//        threadDaemon.start();
    }

}