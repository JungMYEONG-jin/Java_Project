package com.market.daemon.sender;

import com.market.api.apple.AppleAppId;
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

import java.util.ArrayList;
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
        insertMarketList();
        insertSendList();

//        Market sbank_android = Market.builder().appId("sbank_android").appPkg("com.shinhan.sbanking").osType(MarketInfo.OS_TYPE_AND).storeUrl("https://play.google.com/store/apps/details?id=").titleNode("[first://]div[class=sIskre] c-wiz[jsrenderer=vVnOi]")
//                .versionNode("div[class=xyOfqd] div[class=hAyfc]:nth-child(4)").updateNode("[first://]div[class=xyOfqd] div[class=hAyfc]:nth-child(1) div:nth-child(2)").build();
//        marketRepository.save(sbank_android);
//        Send sbank_android1 = Send.builder().appId("sbank_android").sendStatus("0").userId("1111").errorMsg("").build();
//        sendRepository.save(sbank_android1);


        // property setting for xml save
        setMarketProperty();
    }


    @Test
    void daemonTest() throws Exception {
        daemon.run();
    }

    private void insertMarketList(){
        List<Market> marketList = new ArrayList<Market>();
        for(AppleAppId value : AppleAppId.values()){
            marketList.add(Market.builder().appId(value.name()).appPkg(value.getAppPkg()).osType(MarketInfo.OS_TYPE_IOS_API).storeUrl("https://itunes.apple.com/kr/app/id").build());
        }
        marketRepository.saveAll(marketList);
    }

    private void insertSendList(){
        List<Send> sendList = new ArrayList<Send>();
        for(AppleAppId value : AppleAppId.values()){
            sendList.add(Send.builder().appId(value.name()).sendStatus("0").userId("1111").errorMsg("").build());
        }
        sendRepository.saveAll(sendList);
    }

    private void setMarketProperty() {
        MarketPropertyEntity marketProperty = new MarketPropertyEntity();
        marketProperty.setPropertyVersion("1.0.1");
        marketProperty.setPropertyStatus("0");
        marketProperty.setDataType("1");
        marketProperty.setPropertyData("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<items>\n" +
                "\t<item>\n" +
                "\t\t<setting_time_list>\n" +
                "\t\t\t<time_info>\n" +
                "\t\t\t\t<checktime>070000</checktime>\n" +
                "\t\t\t</time_info>\n" +
                "\t\t\t<time_info>\n" +
                "\t\t\t\t<checktime>150000</checktime>\n" +
                "\t\t\t</time_info>\n" +
                "\t\t</setting_time_list>\n" +
                "\t</item>\n" +
                "</items>");
        marketProperty.setUserId("21111008");
        marketProperty.setIsSetting("N");
        marketPropertyRepository.save(marketProperty);
    }


}