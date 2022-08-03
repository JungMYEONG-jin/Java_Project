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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Test
    void daemonTest() throws Exception {
        daemon.run();
    }

    @Test
    void marketRepoTest() {
        List<Market> all = marketRepository.findAll(); //매일 업데이트하기 위해...
        for (Market market : all) {
            market.setUptDt("1"); // 변경을 줌
            // enable 모드라 자동으로 trace하여 다시 수정일 setting 됨.
            marketRepository.save(market);
        }

    }

    @Test
    void timeTest(){
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = formatter.format(now);
        System.out.println("formattedDate = " + formattedDate);
    }

    private void insertMarketList(){
        for(AppleAppId value : AppleAppId.values()){
            Market market = new Market();
            market.setAppId(value.name());
            market.setAppPkg(value.getAppPkg());
            market.setOsType(MarketInfo.OS_TYPE_IOS_API);
            market.setStoreUrl("https://itunes.apple.com/kr/app/id");
            marketRepository.save(market);
        }

    }

    private void insertSendList(){
        sendRepository.deleteAll(); // 모두 제거 후 새로 삽입
        List<Send> sendList = new ArrayList<Send>();
        for(AppleAppId value : AppleAppId.values()){
            Send send = new Send();
            send.setAppId(value.name());
            send.setSendStatus("0");
            send.setUserId("1111");
            send.setErrorMsg("");
            sendList.add(send);
        }
        sendRepository.save(sendList);
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