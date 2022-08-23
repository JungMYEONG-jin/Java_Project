package com.market.property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MarketProperty {
	
	public static final int INIT_VALUE = -1;

	public static final int DEFAULT_MARKET_DAEMON_SLEEP_TIME = 1000;
	public static final int DEFAULT_SEND_DAEMON_SLEEP_TIME = 1000;
	public static final int DEFAULT_SEND_DAEMON_MARKET_SEND_DELAY_SEC = 1000;
	public static final int DEFAULT_FILE_UPDATE_LIMIT_SEC = 1000 * 60 * 5;
	
	public static int MARKET_DAEMON_SLEEP_TIME = DEFAULT_MARKET_DAEMON_SLEEP_TIME;
	public static int SEND_DAEMON_SLEEP_TIME = DEFAULT_SEND_DAEMON_SLEEP_TIME;
	public static int SEND_DAEMON_MARKET_SEND_DELAY_SEC = DEFAULT_SEND_DAEMON_MARKET_SEND_DELAY_SEC;
	public static int FILE_UPDATE_LIMIT_SEC = DEFAULT_FILE_UPDATE_LIMIT_SEC;
	public static int FILE_UPDATE_LIMIT_SEC_FOR_TEST = 1000 * 30 ;
	
	public Logger log = LoggerFactory.getLogger(getClass());

	@Value("${bReal}")
	public boolean bReal = false;
	@Value("${daemon_code}")
	public String daemon_code = null;
	@Value("${output_xml_path}")// Daemon ��
	public String output_xml_path = null;
	@Value("${output_xml_file_name}")// ���� ��� �ð�
	public String output_xml_file_name = null;
	
	/******************* Setter/Getter **********************/

	public void setbReal(boolean bReal) {
		this.bReal = bReal;
	}

	public void setDaemon_code(String daemon_code) {
		this.daemon_code = daemon_code;
	}
	
	public String getOutput_xml_path() {
		return output_xml_path;
	}

	public void setOutput_xml_path(String output_xml_path) {
		this.output_xml_path = output_xml_path;
	}
	
	public String getOutput_xml_file_name() {
		return output_xml_file_name;
	}

	public void setOutput_xml_file_name(String output_xml_file_name) {
		this.output_xml_file_name = output_xml_file_name;
	}
}
