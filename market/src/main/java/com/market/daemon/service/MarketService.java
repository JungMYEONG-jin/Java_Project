package com.market.daemon.service;


import com.market.daemon.dao.MarketInfo;
import com.market.daemon.dao.MarketPropertyDao;
import com.market.daemon.dao.MarketPropertyDaoMapper;
import com.market.daemon.dto.SendInfo;
import com.market.daemon.dto.SendInfoMapper;
import com.market.entity.Market;
import com.market.entity.MarketPropertyEntity;
import com.market.entity.Send;
import com.market.entity.SendHistory;
import com.market.exception.AppDataException;
import com.market.exception.GetSendInfoListException;
import com.market.property.MarketProperty;
import com.market.repository.MarketPropertyRepository;
import com.market.repository.MarketRepository;
import com.market.repository.SendHistoryRepository;
import com.market.repository.SendRepository;
import com.market.service.MarketJpaService;
import com.market.util.ControllerPropertyBean;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;

@Service
public class MarketService {
	public Logger m_log = Logger.getLogger(getClass());

	private final MarketRepository marketRepository;
	private final SendRepository sendRepository;
	private final SendHistoryRepository sendHistoryRepository;
	private final MarketPropertyRepository marketPropertyRepository;
	private final MarketProperty m_pushProperty;


	int exception_count = 0;

	public MarketService(MarketRepository marketRepository, SendRepository sendRepository, SendHistoryRepository sendHistoryRepository, MarketPropertyRepository marketPropertyRepository, MarketProperty m_pushProperty) {
		this.marketRepository = marketRepository;
		this.sendRepository = sendRepository;
		this.sendHistoryRepository = sendHistoryRepository;
		this.marketPropertyRepository = marketPropertyRepository;
		this.m_pushProperty = m_pushProperty;
	}

	public synchronized void exceptionDBSaveAndAdminappPushSend(Exception e) {
		try {
			// Exception �� ���� �ڵ� �߰� �ʿ�..DB or Message
			m_log.info("EXCEPTION !!! DB MESSAGE at exceptionDBSaveAndAdminappPushSend");
			
		} catch(Exception e1) {
			m_log.error("Excpetion at exceptionDBSaveAndAdminappPushSend.", e1);
		}
	}

	public List<MarketInfo> getSendMarketInfoList() throws Exception {

		List<MarketInfo> appInfoList = new ArrayList<MarketInfo>();
		try {

			List<Market> markets = marketRepository.findAll();
			for (Market market : markets) {
				appInfoList.add(new MarketInfo(market));
			}

		} catch(Exception e) {
			m_log.error("", e);
			System.out.println(e.getMessage());
			throw new AppDataException("SEND_MARKET_INFO LIST SELECT Exception.", e);
		}

		return appInfoList;
	}
	
	public List<SendInfo> getSendInfoList() throws GetSendInfoListException {
		
		List<SendInfo> sendInfoList = null;
		
		try {
			sendInfoList = marketRepository.GET_SEND_INFO_LIST();
		} catch(Exception e) {
			throw new GetSendInfoListException("SEND_INFO LIST SELECT Exception Occurred.", e);
		}
		
		return sendInfoList;		
	}
	
	public synchronized void insertSendInfo(SendInfo sendInfo) {
		try {

			Send mappedSend = new SendInfo().of(sendInfo);
			sendRepository.save(mappedSend);
		} catch(Exception e) {
			m_log.error("SendInfo insert 중 오류가 발생했습니다.", e);
		}
	}




	public synchronized void insertPeriodMarketSendInfo() {
		try {
			Send send = new Send();
			send.setAppId("");
			send.setSendStatus("0");
			send.setErrorMsg("");
			send.setUserId("99999999");
			sendRepository.save(send);
		} catch(Exception e) {
			m_log.error("insertPeriodMarketSendInfo EXCEPTION.", e);
		}
	}

	public synchronized void insertSendHistoryInfo(SendInfo sendInfo) {
		try {
			Map<String,String> whereMap = sendInfo.toInsertMap();
			whereMap.put("REG_DT", sendInfo.getRegDt());
			SendHistory sendHistory = new SendHistory();
			sendHistory.setAppId(sendInfo.getAppId());
			sendHistory.setErrorMsg(sendInfo.getErrorMsg());
			sendHistory.setSendStatus(sendInfo.getSendStatus());
			sendHistory.setUserId(sendInfo.getReqUserId());
			sendHistory.setRegDt(sendInfo.getRegDt());
			sendHistoryRepository.save(sendHistory); // seq 자동 생성,

		} catch(Exception e) {
			m_log.error("insertSendHistoryInfo EXCEPTION", e);
		}
	}



	public MarketPropertyDao getPropertyInfo() throws Exception {

		MarketPropertyDao propertyInfo = null;

		try {
			MarketPropertyEntity property = marketPropertyRepository.findFirstByOrderByRegDt();
			if (property == null){
				throw new RuntimeException("프로퍼티 정보가 존재하지 않습니다.");
			}
			// MarketPropertyDao 로 변환
			propertyInfo = property.of();
		} catch(Exception e) {
			throw new AppDataException("PropertyDAO getPropertyInfo EXCEPTION", e);
		}

		return propertyInfo;
	}

	// 미사용인듯?
	public synchronized void createPushTable() throws AppDataException {
//		simpleJdbcTemplate.update(getQueryString("CARETE_PUSH_TABLE"));

	}

	public synchronized void insertSendHistArray(SendInfo sendInfo, String arraySendSeq) {
		try {

			// to be saved MBM_MARKET_SEND_HISTORY
			// get from MBM_MARKET_SEND_INFO

			// 이게 어떤 형식인지??
			String[] strs = arraySendSeq.split(",");
			List<Long> seqs = new ArrayList<Long>();
			for (String str : strs) {
				seqs.add(Long.parseLong(str));
			}

			List<Send> sendByIds = sendRepository.findAllById(seqs);
			List<SendHistory> sendHistories = new ArrayList<SendHistory>();
			for (Send send : sendByIds) {
				sendHistories.add(send.of());
			}
			sendHistoryRepository.saveAll(sendHistories);
		} catch(Exception e) {
			m_log.error("insertSendHistArray EXCEPTION", e);
		}

	}

	public synchronized void insertSendHistArray(String arraySendSeq) {

		try {
			List<Long> seqs = new ArrayList<Long>();
			String[] split = arraySendSeq.split(",");
			for (String s : split) {
				seqs.add(Long.parseLong(s));
			}
			List<Send> sendByIds = sendRepository.findAllById(seqs);
			List<SendHistory> sendHistories = new ArrayList<SendHistory>();
			for (Send send : sendByIds) {
				sendHistories.add(send.of());
			}
			sendHistoryRepository.saveAll(sendHistories);

		} catch(Exception e) {
			m_log.error("Ǫ�� �߼� ���̺� �߼۰�� ������Ʈ �� Exception�� �߻��߽��ϴ�.", e);
		}
	}


	public synchronized void deleteSendInfoArray(String arraySendSeq) {

		try {
			List<Long> seqs = new ArrayList<Long>();
			String[] split = arraySendSeq.split(",");
			for (String s : split) {
				seqs.add(Long.parseLong(s));
			}
			sendRepository.deleteAllById(seqs); // seq 포함 다 제거
		} catch(Exception e) {
			m_log.error("deleteSendInfoArray EXCEPTION", e);
		}
	}

	public synchronized void deleteSendInfoArray(SendInfo sendInfo, String arraySendSeq) {

		try {
			if(arraySendSeq.isEmpty()){
				sendRepository.deleteById(Long.parseLong(sendInfo.getSeq()));
			} else {
				List<Long> seqs = new ArrayList<Long>();
				String[] split = arraySendSeq.split(",");
				for (String s : split) {
					seqs.add(Long.parseLong(s));
				}
				sendRepository.deleteAllById(seqs); // delete  where seq in seqs
			}

		} catch(Exception e) {
			m_log.error("deleteSendInfoArray EXCEPTION", e);
		}
	}

	// TODO parkyk
	public void updateSendInfo(SendInfo sendInfo) {
		try {

			Send findSendInfo = sendRepository.getById(Long.parseLong(sendInfo.getSeq()));
			findSendInfo.setSendStatus(sendInfo.getSendStatus());
			findSendInfo.setErrorMsg(sendInfo.getErrorMsg());
			sendRepository.save(findSendInfo);
		} catch(Exception e) {
			m_log.error("sendInfo ���̺� �߼۰�� ������Ʈ �� Exception�� �߻��߽��ϴ�.", e);
		}
	}


	//미사용
//	public void insertSendHistArray(String sendSt, String errorMsg, String arraySendSeq) {
//
//	}

	public void testInsertMarketData(String appid, String appPkg){
		try {
			Market market = new Market();
			market.setAppId(appid);
			market.setAppPkg(appPkg);
			market.setOsType("1");
			market.setStoreUrl("https://play.google.com/store/apps/details?id=");
			market.setTitleNode("div.id-app-title");
			market.setVersionNode("div.details-section-contents div.meta-info\n" +
					"\t\t\tdiv[itemprop=softwareVersion]");
			market.setUpdateNode("div.details-section-contents\n" +
					"\t\t\tdiv.meta-info\n" +
					"\t\t\tdiv[itemprop=datePublished]");
			marketRepository.save(market);
		} catch(Exception e) {
			m_log.error("testInsertMarketData EXCEPTION", e);
		}
	}

	public void testInsertMarketDataIOS(String appid, String appPkg){
		try {
			Market market = new Market();
			market.setAppId(appid);
			market.setAppPkg(appPkg);
			market.setOsType("2");
			market.setStoreUrl("https://itunes.apple.com/kr/app/id");
			market.setTitleNode("div#title div.left h1");
			market.setVersionNode("div#left-stack div ul li span[itemprop=softwareVersion]");
			market.setUpdateNode("div#left-stack div ul li.release-date\n" +
					"\t\tspan[itemprop=datePublished]");
			marketRepository.save(market);
		} catch(Exception e) {
			m_log.error("testInsertMarketDataIOS EXCEPTION", e);
		}
	}
	
}
