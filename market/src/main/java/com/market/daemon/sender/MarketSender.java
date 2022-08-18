package com.market.daemon.sender;

import com.market.api.apple.AppleApi;
import com.market.crawling.Crawling;
import com.market.crawling.data.CrawlingResultData;
import com.market.daemon.UserRejectHandler;
import com.market.daemon.UserThreadFactory;
import com.market.daemon.dto.SendInfo;
import com.market.daemon.service.MarketService;
import com.market.errorcode.ErrorCode;
import com.market.exception.CrawlingException;
import com.market.exception.CreateFileException;
import com.market.exception.GetSendInfoListException;
import com.market.exception.SendInfoListException;
import com.market.property.MarketProperty;
import org.apache.log4j.Logger;
import org.h2.engine.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author parkyk
 * FILE UPDATE LIMIT MIN���� ������ �߰��� �����ٸ� ���Ϸ� ����.
 */
public class MarketSender extends Thread {

	public Logger m_log = Logger.getLogger(getClass());

	private MarketProperty propertyMarket;

	private MarketService serviceMarket;

	private AppleApi appleApi;

	private Crawling crawling;

	private long regTime = MarketProperty.INIT_VALUE;

	private int m_nMaxArraySendSeqCnt = 500;

	private HashMap<String, CrawlingResultData> mapResCrawling = new HashMap<String, CrawlingResultData>();

	// for thread
	private SendInfo mySendInfo;
	private String myArraySeq = "";
	private int myArraySeqCount;

	private int mCheckCnt;

	private boolean isExist = false;

	public void setExist(boolean exist) {
		isExist = exist;
	}

	public boolean isExist() {
		return isExist;
	}

	public MarketSender(MarketService marketService, MarketProperty marketProperty){
		this.propertyMarket = marketProperty;
		this.serviceMarket = marketService;
		initialize();
	}

	private void initialize() {
		m_log.info("PushDBSender Thread initialize OK..");
	}

	public void run() {

		try {
			while (true) {
				System.out.println("start");

				if(mCheckCnt > 60){
					mCheckCnt = 0;
					m_log.info("================ SEND_DAEMON ================");
				}

				try {
					List<SendInfo> sendInfoList = serviceMarket.getSendInfoList();
					processSendInfoList(sendInfoList);
					isCheckCreateFile();

				} catch (GetSendInfoListException e){
					System.out.println("GetSendInfoListException");
					m_log.info("GetSendInfoListException : " + e.getMessage());
				} catch (SendInfoListException e) {
					System.out.println("SendInfoListException");
					m_log.info("SendInfoListException : " + e.getMessage());
				} catch (CreateFileException e) {
					System.out.println("CreateFileException");
					m_log.info("CreateFileException : " + e.getMessage());
				} catch (Exception e) {
					System.out.println("EXCEPTION");
					m_log.info("Send Message Exception : "+ e.getMessage());
				}

				try {
					processSleep();
				} catch (InterruptedException e) {
					ErrorCode.LogError(getClass(), "B1003", e);
				}

//				if(isExist)
//					break;

				mCheckCnt++;
			}

		} catch(Exception ex) {
			System.out.println("B1000 error");
			ErrorCode.LogError(getClass(), "B1000", ex);
			serviceMarket.exceptionDBSaveAndAdminappPushSend(ex);
		} finally { //최후 에러 상황
			System.out.println("create file...");
			if(mapResCrawling != null && mapResCrawling.isEmpty() == false){
				createFile(mapResCrawling);
			}
		}
	}

	private void isCheckCreateFile() throws CreateFileException {
		try {
			if (regTime != MarketProperty.INIT_VALUE) {
				long diffTIme = ((System.currentTimeMillis() - regTime));

				m_log.info("DiffTime : " + diffTIme + " limitTime : "
						+ MarketProperty.FILE_UPDATE_LIMIT_SEC);

				// 원래 FILE_UPDATE_LIMIT_SEC임
				if (diffTIme > MarketProperty.FILE_UPDATE_LIMIT_SEC) {
					createFile(mapResCrawling);

					// reset
					regTime = MarketProperty.INIT_VALUE;

					if (mapResCrawling != null) {
						mapResCrawling.clear();
					} else {
						mapResCrawling = new HashMap<String, CrawlingResultData>();
					}
				}
			}
		} catch (Exception e) {
			throw new CreateFileException("XML CreateFileException.", e);
		}
	}

	private void doCrawling(SendInfo sendInfo){
		try {
			if (sendInfo != null) {
				System.out.println("sendInfo = " + sendInfo);
				// set ��Ͻð�
				regTime = System.currentTimeMillis();
				// 이 부분이 크롤링 동작 인듯
				CrawlingResultData ret = null;
				if(sendInfo.getOsType().equals(SendInfo.OS_TYPE_IOS_API)){
					ret = appleApi.getCrawlingResult(sendInfo.getAppPkg());
				} else {
					ret = crawling.crawling(sendInfo);
				}

				System.out.println("ret = " + ret);
				if (ret != null) {
					m_log.info("Crawling 시작 : "
							+ ret.toString());
					mapResCrawling.put(ret.getAppId(), ret);

					// Max Seqence

					// TODO parkyk
					if (myArraySeqCount == 0) {
						myArraySeq = myArraySeq
								+ sendInfo.getSeq();
					} else {
						myArraySeq = myArraySeq + ","
								+ sendInfo.getSeq();
					}

					++myArraySeqCount;

					if (myArraySeqCount >= m_nMaxArraySendSeqCnt) {

						updateSendInfoArray(myArraySeq);
						myArraySeqCount = 0;
						myArraySeq = "";
					}

				} else {
					m_log.info("Crawling FAILED");
					sendInfo.setSendStatus(SendInfo.SEND_RESULT_CRAWLING_FAIL);
					updateSendInfoError(sendInfo,
							sendInfo.getSeq());
				}

			} else {
				m_log.info("Market Entity is null.");
			}

		} catch (CrawlingException e) {
			ErrorCode.LogError(getClass(), "B1002", e);
			sendInfo.setErrorMsg(e.getMessage());
			sendInfo.setSendStatus(SendInfo.SEND_RESULT_CRAWLING_FAIL);
			updateSendInfoError(sendInfo, sendInfo.getSeq());

		} catch (Exception e) {
			ErrorCode.LogError(getClass(), "B1001", e);
			sendInfo.setErrorMsg(e.getMessage());
			sendInfo.setSendStatus(SendInfo.SEND_RESULT_ERROR);
			updateSendInfoError(sendInfo, sendInfo.getSeq());
		}
	}

	public void processSendInfoList(List<SendInfo> sendInfoList) throws SendInfoListException {

		try {
			// 여기 synchronized(sendInfoList)
				if (sendInfoList.isEmpty() == false) {
					Crawling crawling = getCrawling();
					AppleApi appleApi = getAppleApi();
					Date date = new Date(System.currentTimeMillis());
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMddHHmmSS");
					String startTime = sdf.format(date);

					m_log.info("ProcessSendInfo Start Time : " + startTime);

					BlockingQueue queue = new LinkedBlockingQueue(1000);
					UserThreadFactory factory = new UserThreadFactory("MJ");
					UserRejectHandler handler = new UserRejectHandler();
					ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 100, 60, TimeUnit.SECONDS, queue, factory, handler);
					/**
					 * java6에서는 람다가 안됨.. 따라서 전역변수로 myArraySeq, myArraySeqCount를 관리해야함.
					 */
					myArraySeq = "";
					myArraySeqCount = 0;

					m_log.info("Crawling Start");
					System.out.println("Crawling Start");
					int threadSize = sendInfoList.size();
					final CountDownLatch countDownLatch = new CountDownLatch(threadSize);
					for (SendInfo sendInfo : sendInfoList) {
						mySendInfo = sendInfo;
						threadPoolExecutor.execute(new Runnable() {
							@Override
							public void run() {
								doCrawling(mySendInfo);
								countDownLatch.countDown();
							}
						});

						try {
							Thread.sleep(MarketProperty.SEND_DAEMON_MARKET_SEND_DELAY_SEC);
						} catch (InterruptedException e) {
							try {
								Thread.sleep(MarketProperty.DEFAULT_SEND_DAEMON_MARKET_SEND_DELAY_SEC);
							} catch (InterruptedException e1) {
							}
						}
					}

					try{
						countDownLatch.await(); // 크롤링 다 돌때까지 대기
					}catch (InterruptedException e){
						e.printStackTrace();
					}
					threadPoolExecutor.shutdown(); // 모두 돌면 종료
					m_log.info("Crawling End");

					processSleep();

					if (myArraySeqCount > 0 && !myArraySeq.equals("")) {
						updateSendInfoArray(myArraySeq);
						sendInfoList.clear();
					}


				}

		} catch (Exception e) {

			throw new SendInfoListException("processSendInfoList EXCEPTION", e);
		}
	}
	
	private void updateSendInfoError(SendInfo sendInfo, String arraySendSeq) {
		// TODO 
		if (sendInfo != null) {
			serviceMarket.updateSendInfo(sendInfo);
		}			
		
		// TODO
		serviceMarket.insertSendHistArray(sendInfo, arraySendSeq);
		serviceMarket.deleteSendInfoArray(arraySendSeq);
	}

	private boolean createFile(HashMap<String, CrawlingResultData> mapResCrawling) {
		
		m_log.info("Create File Start");
		
		Document doc = null;
		try {
			doc = createDocumentOutputXML(mapResCrawling);
		} catch (ParserConfigurationException e1) {
			ErrorCode.LogError(getClass(), "D1000");
		}
		
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trfomer = tf.newTransformer();
			trfomer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			trfomer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);
			// Console��¿�
//			StreamResult res= new StreamResult(System.out);
			
			// FILE ����
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String dateString = sdf.format(date);
			String path = propertyMarket.getOutput_xml_path();
			String fileName = String.format(propertyMarket.getOutput_xml_file_name(), dateString);
			m_log.info("XML Output File Path : " + path + fileName);
			StreamResult res = new StreamResult(new FileOutputStream(new File(path, fileName)));
			trfomer.transform(source, res);
			
		} catch (Exception e) {
			ErrorCode.LogError(getClass(), "D1001", e);
		}
		
		m_log.info("Create File End");
//		isExist = true;
		return false;
	}

	public void updateSendInfoArray(String arraySendSeq) {
		serviceMarket.insertSendHistArray(arraySendSeq);
		serviceMarket.deleteSendInfoArray(arraySendSeq);
	}

	public void deleteSendInfoArray(String arraySendSeq) {
		m_log.debug("arraySendSeq ==> "+arraySendSeq);
		serviceMarket.deleteSendInfoArray(arraySendSeq);
	}
	
	private Crawling getCrawling() {
		if(crawling == null){
			crawling = new Crawling();
		}
		return crawling;
	}

	private AppleApi getAppleApi(){
		if(appleApi == null)
			appleApi = new AppleApi();
		return appleApi;
	}
	
	private static Document createDocumentOutputXML(HashMap<String, CrawlingResultData> mapResult) throws ParserConfigurationException {
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc =docBuilder.newDocument();
		
		Element rootElement = doc.createElement("items");
		Element item = doc.createElement("item");
		Element public_app_list = doc.createElement("public_app_list");

		if(mapResult != null){
			for(String key : mapResult.keySet()){
				if(key == null || key.isEmpty()){
					continue;
				}
				
				CrawlingResultData data = mapResult.get(key);
				
				if(data == null){
					continue;
				}
				
				Element app_info = doc.createElement("app_info");
				
				addChild(doc, app_info, "app_id", data.getAppId());
				addChild(doc, app_info, "app_version", data.getAppVersion());
				addChild(doc, app_info, "update_date", data.getUpdate());
				
				public_app_list.appendChild(app_info);				
			}
		}
		
		item.appendChild(public_app_list);
		doc.appendChild(rootElement);
		rootElement.appendChild(item);
		return doc;
	}

	private static void addChild(Document doc, Element app_info, String key, String value) {
		if(value != null && value.isEmpty() == false){
			Element elUptDate = doc.createElement(key);
			elUptDate.appendChild(doc.createTextNode(value));
			
			app_info.appendChild(elUptDate);
		}
	}
	
	/**
	 * @author parkyk
	 * @param
	 * 
	 * ���� üũ
	 * @throws InterruptedException 
	 * 
	 */
	private void processSleep() throws InterruptedException {
		// Sleep 
		Thread.sleep(MarketProperty.SEND_DAEMON_SLEEP_TIME);
	}

}
