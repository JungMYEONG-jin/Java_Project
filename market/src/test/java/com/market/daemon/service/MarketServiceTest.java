package com.market.daemon.service;

import com.market.daemon.dao.MarketInfo;
import com.market.daemon.dao.MarketPropertyDao;
import com.market.daemon.dto.SendInfo;
import com.market.entity.Market;
import com.market.entity.MarketPropertyEntity;
import com.market.entity.Send;
import com.market.exception.GetSendInfoListException;
import com.market.property.MarketProperty;
import com.market.repository.MarketPropertyRepository;
import com.market.repository.MarketRepository;
import com.market.repository.SendHistoryRepository;
import com.market.repository.SendRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MarketServiceTest {

    @Autowired
    MarketService marketService;

    @Autowired
    MarketRepository marketRepository;

    @Autowired
    SendRepository sendRepository;

    @Autowired
    MarketPropertyRepository marketPropertyRepository;

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
    void getSendInfoTest() throws GetSendInfoListException {
        List<SendInfo> sendInfoList = marketService.getSendInfoList();
        for (SendInfo sendInfo : sendInfoList) {
            System.out.println("sendInfo = " + sendInfo);
        }
    }

    @Test
    void getSendMarketInfoList() throws Exception {
        List<MarketInfo> result = marketService.getSendMarketInfoList();
        for (MarketInfo marketInfo : result) {
            System.out.println("marketInfo = " + marketInfo);
        }
    }

    @Test
    void insertSendInfoTest() {
        Market smailvn_ios = Market.builder().appId("sdas").appPkg("1016762804").osType(MarketInfo.OS_TYPE_IOS_API).storeUrl("https://itunes.apple.com/kr/app/id").build();
        Send sbank_ios = Send.builder().appId("32133").sendStatus("0").userId("1111").errorMsg("").build();
        marketRepository.save(smailvn_ios);
        sendRepository.save(sbank_ios);
        SendInfo sendInfo = new SendInfo(smailvn_ios, sbank_ios);
        System.out.println("sendInfo = " + sendInfo);
        marketService.insertSendInfo(sendInfo);

    }

    @Test
    void insertPeriodMarketSendInfoTest() {
        marketService.insertPeriodMarketSendInfo();
        List<Send> all = sendRepository.findAll();
    }

    @Test
    void insertSendHistoryInfoTest() {
        Market smailvn_ios = Market.builder().appId("sdas").appPkg("1016762804").osType(MarketInfo.OS_TYPE_IOS_API).storeUrl("https://itunes.apple.com/kr/app/id").build();
        Send sbank_ios = Send.builder().appId("32133").sendStatus("0").userId("1111").errorMsg("").build();
        marketRepository.save(smailvn_ios);
        sendRepository.save(sbank_ios);
        SendInfo sendInfo = new SendInfo(smailvn_ios, sbank_ios);
        marketService.insertSendHistoryInfo(sendInfo);
    }

    @Test
    void getPropertyInfoTest() throws Exception {
        MarketPropertyDao propertyInfo = marketService.getPropertyInfo();
        System.out.println("propertyInfo = " + propertyInfo);
    }

    @Test
    void insertSendHistArrayTest() {
        marketService.insertSendHistArray(new SendInfo(), "1,2");
    }

    @Test
    void insertSendHistArrayTest2() {
        marketService.insertSendHistArray("1,2");
    }

    @Test
    void deleteSendInfoArrayTest() {
        marketService.deleteSendInfoArray("1,2");
    }

    @Test
    void deleteSendInfoArrayTest2() {
        marketService.deleteSendInfoArray(new SendInfo(),"1,2");
    }

    @Test
    void updateSendInfoTest() {
        SendInfo sendInfo = new SendInfo();
        sendInfo.setSeq("1");
        sendInfo.setSendStatus("1212112");
        sendInfo.setErrorMsg("Error!!!!!!!");
        marketService.updateSendInfo(sendInfo);
    }
}