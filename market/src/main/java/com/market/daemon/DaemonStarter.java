package com.market.daemon;

import com.market.api.apple.AppleAppId;
import com.market.daemon.dao.MarketInfo;
import com.market.daemon.dto.SendInfo;
import com.market.entity.Market;
import com.market.entity.MarketPropertyEntity;
import com.market.entity.Send;
import com.market.repository.MarketPropertyRepository;
import com.market.repository.MarketRepository;
import com.market.repository.SendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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
		for(AppleAppId value : AppleAppId.values()){
			Market market = new Market();
			market.setAppId(value.name());
			market.setAppPkg(value.getAppPkg());
			market.setOsType(MarketInfo.OS_TYPE_IOS_API);
			marketRepository.save(market);
		}

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
		marketProperty.setPropertyData("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
				"<items>\n" +
				"    <item>\n" +
				"        <market_daemon_sleep_sec>2000</market_daemon_sleep_sec>\n" +
				"        <send_daemon_sleep_sec>1000</send_daemon_sleep_sec>\n" +
				"        <send_daemon_market_send_delay_sec>1000</send_daemon_market_send_delay_sec>\n" +
				"        <file_update_limit_sec>30000</file_update_limit_sec>\n" +
				"        <setting_time_list>\n" +
				"            <time_info>\n" +
				"                <checktime>122400</checktime>\n" +
				"            </time_info>\n" +
				"            <time_info>\n" +
				"                <checktime>133100</checktime>\n" +
				"            </time_info>\n" +
				"        </setting_time_list>\n" +
				"    </item>\n" +
				"</items>");
		marketProperty.setUserId("21111008");
		marketProperty.setIsSetting("N");
		marketPropertyRepository.save(marketProperty);
	}


	/**
	 * 관리자에서 market, send, property 관리함
	 * get 해서 값만 가져올 수 있으면 됨.
	 * 운영에서 ddl-auto  none만 써야됨!!
	 * //		fillSendRepository();
	 * //		updateMarketRepository();
	 * //		setMarketProperty();
	 */
	public void run(){
		fillSendRepository();
		updateMarketRepository();
		setMarketProperty();
		marketDaemon.run();
	}

}
