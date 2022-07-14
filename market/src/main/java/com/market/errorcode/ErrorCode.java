package com.market.errorcode;

import com.shinhan.market.util.UtilString;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class ErrorCode {
	
	private final static String[][] errorCode = {

		{"A1000","PropertyInfo�� XML Data�� �������� �ʽ��ϴ�."},
		{"A1001","XML�Ľ� �ʱ�ȭ�� ������ �߻��Ͽ����ϴ�."},
		{"A1002","CheckTime ������ �Ľ̿� �����Ͽ����ϴ�."},
		{"A1003","(�ֱ���) ���� ���� �۽ſ� �����Ͽ����ϴ�. "},
		{"A1004","������ Sleep ó���� ������ �߻��߽��ϴ�."},
		{"A1005","��Ŷ ���� ���� üũ �� ������ �߻��Ͽ����ϴ�."},
		{"A1006","���ϵ��� ó���� ������ �߻��Ͽ����ϴ�."},
		{"A1007","�������� ���� ���� ó�� �� ������ �߻��Ͽ����ϴ�."},
		{"A1008","��������  -> ������ �Ľ��� ������ �߻��Ͽ����ϴ�."},
		
		{"B1000","�ǵ� ���� �ʴ� �������� Exception �߻����� ���� ���࿡ ������ �߻��Ͽ����ϴ�. ������ Ȯ���� �ּ���."},
		{"B1001","market ���� ó���� ������ �߻��߽��ϴ�."},
		{"B1002","Crawling���� �����͸� �������� �� ������ �߻��Ͽ����ϴ�."},
		{"B1003","�߼� ������ Sleep ó���� ������ �߻��߽��ϴ�"},
		/*
		 * Crawling���� ����
		 * */
		{"C1000","CrawlingData�� ���� �����ϴ�."},
		{"C1001","����� URL�� �����ϴ�. ����� URL�� Ȯ�����ּ���."},
		{"C1002","JSoup Document �Ľ��� ������ �߻��Ͽ����ϴ�."},
		{"C1003","Crawling�� ������ �߻��Ͽ����ϴ�."},
		
		
		{"D1000","XML ������ ������ �߻��Ͽ����ϴ�."},
		{"D1001","XML ���� ���� �� ������ �߻� �Ͽ����ϴ�."},

//		{"",""},
//		{"",""},
//		{"",""},
//		{"",""},
//		{"",""},
//		{"",""},
//		{"",""},
//		{"",""},		
//		{"",""},
//		{"",""},
//		{"",""},
//		{"",""},
//		{"",""},
//		{"",""},
	};

	private final static String ERROR_FORMAT = "[%s] %s";
	public static HashMap<String, String> errorCodeMap = new HashMap<String, String>();

	static {
		for(String[] errorCode : errorCode){
			if(UtilString.isNotEmpty(errorCode[0], errorCode[1])) {
				errorCodeMap.put(errorCode[0], errorCode[1]);				
			}
		}		
	}

	public static String LogInfo(Class<?> clazz, String code){
		try {
			String data = get(code);
//			Logger.getLogger(clazz).info(data);
			return data;
			
		} catch (Exception e) {
			return String.format(ERROR_FORMAT, "Code���� �������� �ʽ��ϴ�.", "Value���� ���� ���� �ʽ��ϴ�.");
			
		}
	}

	public static String LogError(Class<?> clazz, String code){
		try {
			String data = get(code);
			Logger.getLogger(clazz).error(data);
			return data;
			
		} catch (Exception e) {
			return String.format(ERROR_FORMAT, "Code���� �������� �ʽ��ϴ�.", "Value���� ���� ���� �ʽ��ϴ�.");
			
		}
	}
	
	public static String LogError(Class<?> clazz, String code, Exception e){
		try {
			String data = get(code);
			Logger.getLogger(clazz).error(data + " Exception : "+ e.getMessage());
			
			return data;
			
		} catch (Exception ex) {
			return String.format(ERROR_FORMAT, "Code���� �������� �ʽ��ϴ�.", "Value���� ���� ���� �ʽ��ϴ�. Exception : "+ ex.getMessage());
			
		}
	}

	public static String get(String code){
		try {
			if (UtilString.isNotEmpty(code)) {
				String val = errorCodeMap.get(code);
				if (UtilString.isNotEmpty(val)) {
					return String.format(ERROR_FORMAT, code, val);
				} else {
					return String.format(ERROR_FORMAT, code, "Value���� ���� ���� �ʽ��ϴ�."); // ���� ����. 
				}
			} else {
				return String.format(ERROR_FORMAT, "Code���� �������� �ʽ��ϴ�.", "Value���� ���� ���� �ʽ��ϴ�."); // �ڵ� �̻�
			}
		} catch (Exception e) {
			return String.format(ERROR_FORMAT, "Code���� �������� �ʽ��ϴ�.", "Value���� ���� ���� �ʽ��ϴ�.");
		}
	}
}
