package com.market.daemon.service;


import com.market.daemon.dao.MarketInfo;
import com.market.daemon.dao.MarketPropertyDao;
import com.market.daemon.dao.MarketPropertyDaoMapper;
import com.market.daemon.dto.SendInfo;
import com.market.daemon.dto.SendInfoMapper;
import com.market.entity.Send;
import com.market.exception.AppDataException;
import com.market.exception.GetSendInfoListException;
import com.market.property.MarketProperty;
import com.market.repository.MarketRepository;
import com.market.repository.SendRepository;
import com.market.service.MarketJpaService;
import com.market.util.ControllerPropertyBean;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarketService {
	public Logger m_log = Logger.getLogger(getClass());
//
//	public ControllerPropertyBean commSqlBean;
//	public TransactionTemplate transactionTemplate;
	@Autowired
	MarketRepository marketRepository;
	@Autowired
	SendRepository sendRepository;
	private MarketProperty m_pushProperty;

	int exception_count = 0;
	
//	public void setCommSqlBean(ControllerPropertyBean commSqlBean) {
//		this.commSqlBean = commSqlBean;
//	}
//
//	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
//		this.transactionTemplate = transactionTemplate;
//	}

	public void setPushProperty(MarketProperty pushProperty) {
		this.m_pushProperty = pushProperty;
	}	
	
	public synchronized void exceptionDBSaveAndAdminappPushSend(Exception e) {
		try {
			// Exception �� ���� �ڵ� �߰� �ʿ�..DB or Message
			m_log.info("EXCEPTION !!! DB MESSAGE");
			
		} catch(Exception e1) {
			m_log.error("Excpetion ��� ó�� �߿� ������ �߻��Ͽ����ϴ�.", e1);
		}
	}

//	public List<MarketInfo> getSendMarketInfoList() throws Exception {
//
//		List<MarketInfo> appInfoList = null;
//
//		try {
//			appInfoList = marketRepository.findAll().stream().map(market -> new MarketInfo(marke))
//		} catch(Exception e) {
//			m_log.error("", e);
//			System.out.println(e.getMessage());
//			throw new AppDataException("SEND_MARKET_INFO LIST SELECT�� Exception�� �߻��߽��ϴ�.", e);
//		}
//
//		return appInfoList;
//	}
	
	public List<SendInfo> getSendInfoList() throws GetSendInfoListException {
		
		List<SendInfo> sendInfoList = null;
		
		try {
			Map<String,String> whereMap = new HashMap<String,String>();
			sendInfoList = marketRepository.GET_SEND_INFO_LIST();
		} catch(Exception e) {
			throw new GetSendInfoListException("SEND_INFO LIST SELECT Exception Occurred.", e);
		}
		
		return sendInfoList;		
	}
	
	public synchronized void insertSendInfo(SendInfo sendInfo) {
		try {
			ModelMapper modelMapper = getModelMapper();
			Send mappedSend = modelMapper.map(sendInfo, Send.class);
			sendRepository.save(mappedSend);
		} catch(Exception e) {
			m_log.error("SendInfo insert 중 오류가 발생했습니다.", e);
		}
	}

	private ModelMapper getModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setSkipNullEnabled(true)
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

//	public synchronized void insertPeriodMarketSendInfo() {
//		try {
//			String query = getQueryString("INSERT_MARKET_SEND_INFO_PERIOD");
//			simpleJdbcTemplate.update(query, new HashMap<String, String>());
//		} catch(Exception e) {
//			m_log.error("���� �߼� ���̺� (�ֱ���)�߼۰�� ������Ʈ �� Exception�� �߻��߽��ϴ�.", e);
//		}
//	}
//
//	public synchronized void insertSendHistoryInfo(SendInfo sendInfo) {
//		try {
//			Map<String,String> whereMap = sendInfo.toInsertMap();
//			whereMap.put("REG_DT", sendInfo.getRegDt());
//
//			String query = getQueryString("INSERT_MARKET_SEND_HISTORY");
//			simpleJdbcTemplate.update(query, whereMap);
//		} catch(Exception e) {
//			m_log.error("���� �߼� �����丮 ���̺� �߼۰�� ������Ʈ �� Exception�� �߻��߽��ϴ�.", e);
//		}
//	}
//
//
//
//	public MarketPropertyDao getPropertyInfo() throws Exception {
//
//		MarketPropertyDao propertyInfo = null;
//
//		try {
//			Map<String,String> whereMap = new HashMap<String,String>();
//			propertyInfo = simpleJdbcTemplate.queryForObject(getQueryString("GET_PROPERTY_INFO"), new MarketPropertyDaoMapper(), whereMap);
//		} catch(Exception e) {
//			throw new AppDataException("PropertyDAO SELECT�� Exception�� �߻��߽��ϴ�.", e);
//		}
//
//		return propertyInfo;
//	}
//
//	public synchronized void createPushTable() throws AppDataException {
//		simpleJdbcTemplate.update(getQueryString("CARETE_PUSH_TABLE"));
//	}
//
//	public synchronized void insertSendHistArray(SendInfo sendInfo, String arraySendSeq) {
//		try {
//
//			Map<String,String> whereMap = new HashMap<String,String>();
//			String query = getQueryString("INSERT_MARKET_SEND_INFO_HIS_ARRAY") +"(" + arraySendSeq + "))";
//			m_log.info("query : "+ query);
//
//			simpleJdbcTemplate.update(query, whereMap);
//		} catch(Exception e) {
//			m_log.error("Ǫ�� �߼� ���̺� �߼۰�� ������Ʈ �� Exception�� �߻��߽��ϴ�.", e);
//		}
//
//	}
//
//	public synchronized void insertSendHistArray(String arraySendSeq) {
//
//		try {
//			Map<String,String> whereMap = new HashMap<String,String>();
//			String query = getQueryString("INSERT_MARKET_SEND_INFO_HIS_ARRAY") +"(" + arraySendSeq + "))";
//			m_log.info("query : "+ query);
//
//			simpleJdbcTemplate.update(query, whereMap);
//		} catch(Exception e) {
//			m_log.error("Ǫ�� �߼� ���̺� �߼۰�� ������Ʈ �� Exception�� �߻��߽��ϴ�.", e);
//		}
//	}
//
//
//	public synchronized void deleteSendInfoArray(String arraySendSeq) {
//
//		try {
//			Map<String,String> whereMap = new HashMap<String,String>();
//
//			StringBuffer query = new StringBuffer(getQueryString("DELETE_MARKET_SEND_INFO_ARRAY"));
//			query.append("(").append(arraySendSeq).append(")");
//			m_log.info("query : "+ query);
//
//			simpleJdbcTemplate.update(query.toString(), whereMap);
//		} catch(Exception e) {
//			m_log.error("Ǫ�� �߼� ���̺� �߼۰�� ������Ʈ �� Exception�� �߻��߽��ϴ�.", e);
//		}
//	}
//
//	public synchronized void deleteSendInfoArray(SendInfo sendInfo, String arraySendSeq) {
//
//		try {
//
//			Map<String,String> whereMap = new HashMap<String,String>();
//			StringBuffer query = new StringBuffer(getQueryString("DELETE_MARKET_SEND_INFO_ARRAY"));
//
//			if(arraySendSeq.isEmpty()){
//				query.append("(").append(sendInfo.getSeq()).append(")");
//			} else {
//				query.append("(").append(arraySendSeq).append(")");
//			}
//
//			m_log.info("query : "+ query);
//
//			simpleJdbcTemplate.update(query.toString(), whereMap);
//		} catch(Exception e) {
//			m_log.error("Ǫ�� �߼� ���̺� �߼۰�� ������Ʈ �� Exception�� �߻��߽��ϴ�.", e);
//		}
//	}
//
//	public String getQueryString(String QueryString) {
//		String returnString = commSqlBean.getProps().get(QueryString);
//		return returnString;
//	}

	// TODO parkyk
	public void updateSendInfo(SendInfo sendInfo) {
		try {
			Map<String,String> whereMap = sendInfo.toInsertMap();
			whereMap.put("SEND_STATUS", sendInfo.getSendStatus());
			String errmsg = sendInfo.getErrorMsg();
			if(errmsg == null){
				errmsg = "";
			}
			whereMap.put("ERROR_MSG", errmsg);
			whereMap.put("SEQ", sendInfo.getSeq());
									
			m_log.info("whereMap.toString(): "+ whereMap.toString());

			ModelMapper modelMapper = getModelMapper();
			Send send = modelMapper.map(sendInfo, Send.class);
			sendRepository.save(send);

		} catch(Exception e) {
			m_log.error("sendInfo update Exception ", e);
		}			
	}
	
//	public void insertSendHistArray(String sendSt, String errorMsg, String arraySendSeq) {
//		
//	}		
	
//	public void testInsertMarketData(String appid, String appPkg){
//		try {
//			Map<String,String> whereMap = new HashMap<String, String>();
//			String query = getQueryString("TEST_INSERT_MARKET_INFO_LIST");
//			whereMap.put("APP_ID", appid);
//			whereMap.put("APP_PACKAGE", appPkg);
//			simpleJdbcTemplate.update(query, whereMap);
//		} catch(Exception e) {
//			m_log.error("���� �߼� ���̺� �߼۰�� ������Ʈ �� Exception�� �߻��߽��ϴ�.", e);
//		}
//	}
//
//	public void testInsertMarketDataIOS(String appid, String appPkg){
//		try {
//			Map<String,String> whereMap = new HashMap<String, String>();
//			String query = getQueryString("TEST_INSERT_MARKET_INFO_LIST_IOS");
//			whereMap.put("APP_ID", appid);
//			whereMap.put("APP_PACKAGE", appPkg);
//			simpleJdbcTemplate.update(query, whereMap);
//		} catch(Exception e) {
//			m_log.error("���� �߼� ���̺� �߼۰�� ������Ʈ �� Exception�� �߻��߽��ϴ�.", e);
//		}
//	}
	
}
