package com.market.daemon;

import com.market.api.apple.AppleAppId;
import com.market.daemon.dao.MarketInfo;
import com.market.daemon.dto.SendInfo;
import com.market.entity.Market;
import com.market.entity.MarketPropertyEntity;
import com.market.entity.Send;
import com.market.property.MarketProperty;
import com.market.provider.ApplicationContextProvider;
import com.market.repository.MarketPropertyRepository;
import com.market.repository.MarketRepository;
import com.market.repository.SendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DaemonStarter {

	@Autowired
	private MarketDaemon marketDaemonbean;
	@Autowired
	private SendRepository sendRepositorybean;
	@Autowired
	private MarketRepository marketRepositorybean;
	@Autowired
	private MarketPropertyRepository marketPropertyRepositorybean;


	private static MarketDaemon marketDaemon;
	private static SendRepository sendRepository;
	private static MarketRepository marketRepository;
	private static MarketPropertyRepository marketPropertyRepository;

	@PostConstruct
	private void init(){
		marketDaemon = marketDaemonbean;
		sendRepository = sendRepositorybean;
		marketRepository = marketRepositorybean;
		marketPropertyRepository = marketPropertyRepositorybean;
	}

	@Transactional
	public void fillSendRepository(){
		sendRepository.deleteAll(); // 모두 제거 후 새로 삽입
		for(AppleAppId value : AppleAppId.values()){
			Send send = new Send();
			send.setAppId(value.name());
			send.setSendStatus(SendInfo.SEND_RESULT_OK);
			send.setUserId("1111");
			send.setErrorMsg("");
			sendRepository.save(send);
		}
	}

	@Transactional
	public void updateMarketRepository(){
		List<Market> all = marketRepository.findAll(); //매일 업데이트하기 위해...
		for (Market market : all) {
			market.setUptDt("1"); // 변경을 줌
			// enable 모드라 자동으로 trace하여 다시 수정일 setting 됨.
			marketRepository.save(market);
		}

	}

	@Transactional
	public void setMarketProperty(){
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


	/**
	 * 관리자에서 market, send, property 관리함
	 * get 해서 값만 가져올 수 있으면 됨.
	 */

	public void run(){
		fillSendRepository();
		updateMarketRepository();
//		setMarketProperty();
		marketDaemon.run();
	}

}
