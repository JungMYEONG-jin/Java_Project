package com.market.daemon;


import com.market.daemon.dao.MarketPropertyDao;
import com.market.daemon.sender.MarketSender;
import com.market.daemon.sender.MarketSenderNoThread;
import com.market.daemon.service.MarketService;
import com.market.errorcode.ErrorCode;
import com.market.property.MarketProperty;
import com.market.util.TimeCheker;
import com.market.util.XMLParser;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

@Component
public class MarketDaemonNoThread {

	public static final boolean DEV = true;

	public Logger m_log = Logger.getLogger(getClass());

	private final MarketProperty propertyMarket;
	private final MarketService serviceMarket;
	private final MarketSenderNoThread senderThread;
	MarketPropertyDao propertyDAO = null;

	private LinkedList<TimeCheker> listDateTime = new LinkedList<TimeCheker>();
	private XMLParser xmlParser = new XMLParser();

	private String xmlSettingData;

	public MarketDaemonNoThread(MarketProperty propertyMarket, MarketService serviceMarket, MarketSenderNoThread senderThread) {
		this.propertyMarket = propertyMarket;
		this.serviceMarket = serviceMarket;
		this.senderThread = senderThread;
	}

	public void initialize() {
		m_log.info("Thread initialize OK..");
		senderThread.run();
	}


	public void run() {
		initialize();

		while (true) {

			try {

				// ���� ���� ���� ���� Ȯ��
				if(isChangeSettingInfo()){
					// ���� �����Ͱ� �ִٸ� �������� ����
					m_log.info("init Setting Start");

					initSetting();

					m_log.info("init Setting End");

				}

				if(listDateTime.isEmpty()){
					m_log.info("Reset time start");

					resetDateTime();

					m_log.info("Reset time End");

					if(DEV){
						for(TimeCheker time : listDateTime){
							Calendar cal = time.getDateTime();

							m_log.info("reset time : " + String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)) + String.format("%02d", cal.get(Calendar.MINUTE)) + String.format("%02d",cal.get(Calendar.SECOND)));

						}
					}
				}

				// ��Ŷ�� ������ �ϴ��� Ȯ���Ѵ�.
				boolean isSendPacket = checkSendPacket();

				if(isSendPacket){
					m_log.info("Market Daemon Send Packet Start");

					// ���� ��Ŷ�����Ͱ� �ִ�.
					insertMarketSendInfo();

					m_log.info("Market Daemon Send Packet End");

				}
			} catch (Exception e){
				ErrorCode.LogError(getClass(), "A1006",e);
			}

			try {
				processSleep();
			} catch (InterruptedException e) {
				ErrorCode.LogError(getClass(), "A1004",e);
			}
		}
	}

	private void insertMarketSendInfo() {
		try {
			serviceMarket.insertPeriodMarketSendInfo();
		} catch (Exception e) {
			ErrorCode.LogError(getClass(), "A1003",e);
		}
	}

	private void resetDateTime()  {

		if(listDateTime != null){
			listDateTime.clear();
		} else {
			listDateTime = new LinkedList<TimeCheker>();
		}

		try {
			NodeList nodeList = xmlParser.parseNodeList("//setting_time_list/time_info/checktime");
			for (int idx = 0; idx < nodeList.getLength(); idx++) {
				String endTime = nodeList.item(idx).getTextContent();
				TimeCheker timeChecker = new TimeCheker(endTime);
				if (timeChecker.isVaildate()) {
					listDateTime.add(timeChecker);
				}
			}

		} catch (XPathExpressionException e) {
			ErrorCode.LogError(getClass(), "A1002", e);
		}

		// time ����
		Collections.sort(listDateTime, new CompareDateTime());


		m_log.info("\n\n\n");
		for(TimeCheker time : listDateTime){
			m_log.info(time.toString());
		}
		m_log.info("\n\n\n");
	}

	public class CompareDateTime implements Comparator<TimeCheker>{

		@Override
		public int compare(TimeCheker arg0, TimeCheker arg1) {
			return arg0.getDateTime().compareTo(arg1.getDateTime());
		}

	}

	@SuppressWarnings("unchecked")
	private boolean checkSendPacket() {

		if(listDateTime != null && listDateTime.isEmpty() == false){

			TimeCheker dtchk = listDateTime.getFirst();
			if(dtchk != null){
				try {
					if(dtchk.checkElapseTime()){
						if(listDateTime != null && listDateTime.isEmpty() == false){
							listDateTime.removeFirst();
						}

						return true;
					} else {
					}
				} catch (Exception e) {
					// �ش� ����Ʈ ����.
					ErrorCode.LogError(getClass(), "A1005", e);

					if(listDateTime != null && listDateTime.isEmpty() == false){
						listDateTime.removeFirst();
					}
				}
			}
		}

		return false;
	}


	private void processSleep() throws InterruptedException {
		// Sleep
		Thread.sleep(MarketProperty.MARKET_DAEMON_SLEEP_TIME);
	}

	private void initSetting() {
		// ��ƿ ���� ����
		MarketPropertyDao propertyInfo = getPropertyInfo();
		xmlSettingData = propertyInfo.getPropertyData();

		if(xmlSettingData == null || xmlSettingData.isEmpty()){
			ErrorCode.LogError(getClass(), "A1000");
			System.out.println("property is null");
		}

		try {
			xmlParser.init(xmlSettingData);
		} catch (Exception e) {
			ErrorCode.LogError(getClass(), "A1001", e);
		}

		// Ÿ�Ӽ��� Ŭ����
		if(listDateTime != null){
			listDateTime.clear();
		} else {
			listDateTime = new LinkedList<TimeCheker>();
		}


	}



	public MarketPropertyDao getPropertyInfo() {
		return propertyDAO;
	}

	private boolean isChangeSettingInfo() {
		try {
			MarketPropertyDao newPropertyDAO = serviceMarket.getPropertyInfo();
			if(propertyDAO == null){
				setPropertyInfo(newPropertyDAO);
				return true;

			} else {

				if(propertyDAO.isChangeValue(newPropertyDAO)){
					System.out.println("444 -1 ");
					setPropertyInfo(newPropertyDAO);
					System.out.println("555 - 1");
					return true;
				}
				System.out.println("77777777777777777777");
			}
			System.out.println("88888888888888888888");
		} catch (Exception e) {
			System.out.println("000");
			ErrorCode.LogError(getClass(), "A1007", e);
		}
		System.out.println("99999999999999999999999999");
		return false;
	}

	private void setPropertyInfo(MarketPropertyDao newPropertyDAO) {
		propertyDAO = newPropertyDAO;

		String xmlSettingData = newPropertyDAO.getPropertyData();
		if(xmlSettingData != null && xmlSettingData.isEmpty() == false){
			Document doc = Jsoup.parse(xmlSettingData);

			// marketDaemonSleepTime Set
			int retTime = setSleepTime(doc,"market_daemon_sleep_sec");

			if(retTime == MarketProperty.INIT_VALUE){
				MarketProperty.MARKET_DAEMON_SLEEP_TIME = MarketProperty.DEFAULT_MARKET_DAEMON_SLEEP_TIME;
			} else {
				MarketProperty.MARKET_DAEMON_SLEEP_TIME = retTime;
			}

			retTime = setSleepTime(doc,"send_daemon_sleep_sec");

			if(retTime == MarketProperty.INIT_VALUE){
				MarketProperty.SEND_DAEMON_SLEEP_TIME = MarketProperty.DEFAULT_SEND_DAEMON_SLEEP_TIME;
			} else {
				MarketProperty.SEND_DAEMON_SLEEP_TIME = retTime;
			}

			retTime = setSleepTime(doc,"send_daemon_market_send_delay_sec");

			if(retTime == MarketProperty.INIT_VALUE){
				MarketProperty.SEND_DAEMON_MARKET_SEND_DELAY_SEC = MarketProperty.DEFAULT_SEND_DAEMON_MARKET_SEND_DELAY_SEC;
			} else {
				MarketProperty.SEND_DAEMON_MARKET_SEND_DELAY_SEC = retTime;
			}

			retTime = setSleepTime(doc,"file_update_limit_sec");

			if(retTime == MarketProperty.INIT_VALUE){
				MarketProperty.FILE_UPDATE_LIMIT_SEC = MarketProperty.DEFAULT_FILE_UPDATE_LIMIT_SEC;
			} else {
				MarketProperty.FILE_UPDATE_LIMIT_SEC = retTime;
			}

		} else {
			ErrorCode.LogError(getClass(), "A1008");
		}
	}

	private int setSleepTime(Document doc, String docNode) {
		int marketDaemonSleepSec = MarketProperty.INIT_VALUE;
		try {
			String sec = doc.select(docNode).text();
			marketDaemonSleepSec = Integer.parseInt(sec);
		} catch (Exception e) {
			marketDaemonSleepSec = MarketProperty.INIT_VALUE;
		}

		return marketDaemonSleepSec;
	}
}
