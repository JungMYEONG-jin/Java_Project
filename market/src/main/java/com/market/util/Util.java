package com.market.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * @author �赿��
 * 
 *         ��Ŭ������ [����/WAR �� ���������� ���� ��ƿ��Ƽ �޼ҵ带 ����] Ŭ���� �Դϴ�.
 */
public class Util {
	/**
	 * SHA ��ȣȭ
	 * 
	 * @param inputText
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String makeSHA(String inputText)
			throws NoSuchAlgorithmException {
		String test = inputText;
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(test.getBytes());
		byte[] digest = md.digest();

//		System.out.println(md.getAlgorithm());
//		System.out.println(digest.length);

		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
//			System.out.print(Integer.toHexString(b & 0xff) + "");
			sb.append(Integer.toHexString(b & 0xff));
		}

//		System.out.println("\n\nReturn String : " + sb.toString());
		return sb.toString();
	}

	/**
	 * �־��� ���ڿ��� ���� ���ڸ����� ���ڸ������� ������ ���ΰ�.. ( aaaaaa -> aaa...)
	 * 
	 * @param s
	 *            ��ȯ�� ����
	 * @param len
	 *            ������ �ڸ���
	 * @param tails
	 *            �ڸ� ���ڿ� �� ���
	 * @return ��ȯ�� ����
	 */
	public static String fixString(String s, int len, String tails) {
		String retstr = null;
		int totlen = s.length();

		if (s == null || s.equals("")) {
			retstr = "";
		} else if (totlen > len) {
			retstr = s.substring(0, len);
			retstr += tails;
		} else {
			retstr = s;
		}

		return retstr;
	}

	/**
	 * ����Ʈ ����ŭ ���ڿ� �ڸ���
	 * 
	 * @param str
	 *            �ڸ� ���ڿ�
	 * @param sz
	 *            ������ �ڸ���
	 * @param tail
	 *            �ڸ� ���ڿ� �� �������� ���ڿ�
	 * @return ��ȯ�� ����
	 */
	public static String getByteCut(String str, int sz, String tail) // throws
																		// UnsupportedEncodingException
	{
		String returnStr = "";
		StringCharacterIterator iter = null;
		if ((str.getBytes()).length > sz) {
			iter = new StringCharacterIterator(
					new String(str.getBytes(), 0, sz));
			int type = Character.getType(iter.last());
			if (type == Character.OTHER_SYMBOL)
				sz--;
			if (type == Character.UNASSIGNED)
				sz--;

			if (str.length() > sz) {
				// ��˻�
				iter.setText(str.substring(0, sz));
				type = Character.getType(iter.last());
				if (type == Character.OTHER_SYMBOL)
					sz += 2;
			}

			// ���ڸ� �ٽ� �߶� ����
			returnStr = (new String(str.getBytes(), 0, sz)) + tail;
		} else {
			returnStr = str;
		}
		return returnStr;
	}

	/**
	 * ���� ���ڸ� ��¥�ϰ�� �տ� 0�� �ٿ� �����Ѵ�.
	 * 
	 * @param tempInt
	 *            ��, �Ǵ� ��
	 * @return ���ڸ� �� �Ǵ� ��
	 */
	public static String dateTwoConvert(int tempInt) {
		String s = null;
		if (tempInt < 10) {
			s = "0" + tempInt;
		} else {
			s = "" + tempInt;
		}
		return s;
	}

	/**
	 * �ѱ� ��ȯ
	 * 
	 * @param Break_String
	 *            �ѱ۷� ��ȯ�� String
	 * @return �ѱ۷� ��ȯ�� String
	 */
	public static String toKor(String str) {
		String tmp = "";
		if (str == null || str.equals(""))
			return "";

		try {
			tmp = new String(str.getBytes("8859_1"), "KSC5601");
		} catch (Exception ex) {
			tmp = "";
		}
		return tmp;
	}

	public static String nullToBlank(String str) {
		if (str == null || str.equals(""))
			return "";
		return str;
	}

	public static String getStringFromObject(Object obj1) {
		if (obj1 == null)
			return "";
		return (String) obj1;
	}

	public static String nullToString(String str, String str2) {
		if (str == null || str.equals(""))
			return str2;
		return str;
	}

	public static String getSelectedStr(String str, String checkStr) {
		if (str.equals(checkStr)) {
			return "selected";
		} else {
			return "";
		}
	}
public static void main(String[] args) {
	Calendar calendar = Calendar.getInstance();
	SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmssSS");
	
	String currentTime = dateFormat.format(calendar.getTime());
//	System.out.println("����ð�==>"+currentTime);
//	System.out.println("����ð�==>"+currentTime.substring(0,6));
}
	/**
	 * Calendar Ŭ������ YYYYMMDD ������ ��Ʈ������ ��ȯ�Ѵ�.
	 * 
	 * @param cal
	 *            Calendar ��ü
	 * @return YYYYMMDD ������ ��Ʈ��
	 */
	public static String getCalYYYYMMDD(Calendar cal) {
		String sDate = null;

		sDate = cal.get(Calendar.YEAR)
				+ Util.dateTwoConvert((cal.get(Calendar.MONTH)
						- Calendar.JANUARY + 1))
				+ Util.dateTwoConvert(cal.get(Calendar.DATE));

		return sDate;
	}
	
	/**
	 * Calendar Ŭ������ YYYYMMDD ������ ��Ʈ������ ��ȯ�Ѵ�.
	 * 
	 * @param cal
	 *            Calendar ��ü
	 * @return YYYYMMDD ������ ��Ʈ��
	 */
	public static String getCalYYYY_MM_DD(Calendar cal) {
		String sDate = null;

		sDate = cal.get(Calendar.YEAR)
				+ "-"
				+ Util.dateTwoConvert((cal.get(Calendar.MONTH)
						- Calendar.JANUARY + 1)) + "-"
				+ Util.dateTwoConvert(cal.get(Calendar.DATE));

		return sDate;
	}

	/**
	 * ���� ��ǻ�� �� IP �������� �޼ҵ�
	 * 
	 * @return ����IP
	 */
	public static String getServerIp() {
		String serverIp = null;

		InetAddress inetAddr;
		try {
			inetAddr = InetAddress.getLocalHost();
			serverIp = inetAddr.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return serverIp;
	}

	/**
	 * ���ڿ��� ���̿�, ���ڷ� �Ѱܹ��� �Ѱ���ڸ� ���Ͽ� �۰ų� ������ true �� �����Ѵ�. DB TABLE �ڸ����� �°�
	 * Insert �ϱ� ���� �޼ҵ�
	 * 
	 * @param string
	 *            �˻��� String
	 * @param limit
	 *            �˻��� String �� �ִ� �ڸ���
	 * @return �˻���
	 */
	public static boolean tableLimitCheck(String string, int limit) {
		boolean bTrue = false;

		if (string.getBytes().length <= limit) {
			bTrue = true;
		}

		return bTrue;
	}

	/**
	 * YYYYMMDD ������ ��¥�� ��, ��, �� �� �и��Ѵ�.
	 * 
	 * @param day
	 *            YYYYMMDD ������ ��¥
	 * @return ��, ��, �� �� �и��Ǿ� ����� String[]
	 */
	public static String[] dayDivide(String day) {
		String[] days = new String[3];

		days[0] = day.substring(0, 4);
		days[1] = day.substring(4, 6);
		days[2] = day.substring(6, 8);

		return days;
	}

	public static String[] dayDivide2(String day) {
		String[] days = new String[3];

		days[0] = day.substring(0, 4);
		days[1] = day.substring(5, 7);
		days[2] = day.substring(8, 10);

		return days;
	}

	public static String getStrYYYY_MM_DD(String day) {
		String days = "";

		days = day.substring(0, 4) + "-" + day.substring(4, 6) + "-"
				+ day.substring(6, 8);

		return days;
	}

	/**
	 * 2004/01/01 ���� ��¥ ������ ���ϴ� ��, ��, �� �� �ϳ��� �����ü� �ִ� �޼ҵ�
	 * 
	 * @param date
	 * @param delimeter
	 *            ������
	 * @param cnt
	 *            ��/��/�� �� ������ �ڸ���
	 * @return ��/��/�� �� ����� ��
	 */
	public static String dayDivide(String date, String delimeter, int cnt) {
		String day = null;
		String[] str = null;

		StringTokenizer st = new StringTokenizer(date, delimeter);

		if (st.countTokens() > 0) {
			str = new String[st.countTokens()];
			int i = 0;
			while (st.hasMoreTokens()) {
				str[i] = st.nextToken();
				i++;
			}
		}

		day = str[cnt];
		return day;
	}

	/**
	 * �и��ϰ� ���� ��Ʈ���� �޾� �и��Ͽ�, ��Ʈ�� �迭�� ��´�.
	 * 
	 * @param string
	 *            �и��� ��Ʈ��
	 * @param delimeter
	 *            �и� ������ ������
	 * @return �и��� �迭�� ������ ��ü
	 */
	public static String[] stringDivide(String string, String delimeter) {
		String[] strings = null;

		StringTokenizer st = new StringTokenizer(string, delimeter);

		if (st.countTokens() > 0) {
			strings = new String[st.countTokens()];
			int i = 0;
			while (st.hasMoreTokens()) {
				strings[i] = st.nextToken();
				i++;
			}
		}

		return strings;
	}

	/**
	 * �Ѱܹ��� ��Ʈ���� ��������, �ѱ����� �˻��ϴ� �޼ҵ�
	 * 
	 * @param string
	 *            �˻��� ����
	 * @return �˻� ���
	 */
	public static boolean isEnglish(String string) {
		boolean result = true;

		for (int i = 0; i < string.length(); i++) {
			String ch = string.charAt(i) + "";
			if (ch.getBytes().length == 2) {
				result = false;
				break;
			}
		}

		return result;
	}

	public static String printStackTraceToString(Throwable e) {
		StringBuilder sb = new StringBuilder();
		sb.append(e.toString());
		sb.append("\n");
		StackTraceElement element[] = e.getStackTrace();
		for (int idx = 0; idx < element.length; idx++) {
			sb.append("\tat ");
			sb.append(element[idx].toString());
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * YYYYMMDD ������ ��Ʈ���� Calendar ��ü�� �ٲ۴�.
	 * 
	 * @param sDate
	 * @return Calendar
	 */
	public static Calendar getStrCalendar(String sDate) {
		Calendar date = Calendar.getInstance();
		String setDate[] = Util.dayDivide(sDate);
		date.set(Integer.parseInt(setDate[0]),
				Integer.parseInt(setDate[1]) - 1, Integer.parseInt(setDate[2]),
				0, 0, 0);

		return date;
	}

	public static Calendar getStrCalendar2(String sDate) {
		Calendar date = Calendar.getInstance();
		String setDate[] = Util.dayDivide2(sDate);
		date.set(Integer.parseInt(setDate[0]),
				Integer.parseInt(setDate[1]) - 1, Integer.parseInt(setDate[2]),
				0, 0, 0);

		return date;
	}

	public static String getCalYYYYMMDDhhmmss(Calendar cal) throws Exception {
		String sDate = null;
		// sDate = cal.get(Calendar.YEAR) + "" +
		// dateTwoConvert((cal.get(Calendar.MONTH) - Calendar.JANUARY+1)) + "" +
		// dateTwoConvert(cal.get(Calendar.DATE)) + "" +
		// dateTwoConvert(cal.get(Calendar.HOUR)) + "" +
		// dateTwoConvert(cal.get(Calendar.MINUTE)) + "" +
		// dateTwoConvert(cal.get(Calendar.SECOND));
		sDate = cal.get(Calendar.YEAR)
				+ ""
				+ dateTwoConvert((cal.get(Calendar.MONTH) - Calendar.JANUARY + 1))
				+ "" + dateTwoConvert(cal.get(Calendar.DATE)) + ""
				+ dateTwoConvert(cal.get(Calendar.HOUR_OF_DAY)) + ""
				+ dateTwoConvert(cal.get(Calendar.MINUTE)) + ""
				+ dateTwoConvert(cal.get(Calendar.SECOND));
		// System.out.println("sDate ["+sDate+"]");

		return sDate;
	}

	public static String getCalhhmmss(Calendar cal) throws Exception {
		String sDate = null;
		// sDate = cal.get(Calendar.YEAR) + "" +
		// dateTwoConvert((cal.get(Calendar.MONTH) - Calendar.JANUARY+1)) + "" +
		// dateTwoConvert(cal.get(Calendar.DATE)) + "" +
		// dateTwoConvert(cal.get(Calendar.HOUR)) + "" +
		// dateTwoConvert(cal.get(Calendar.MINUTE)) + "" +
		// dateTwoConvert(cal.get(Calendar.SECOND));
		sDate = dateTwoConvert(cal.get(Calendar.HOUR_OF_DAY)) + ""
				+ dateTwoConvert(cal.get(Calendar.MINUTE)) + ""
				+ dateTwoConvert(cal.get(Calendar.SECOND));
		// System.out.println("sDate ["+sDate+"]");

		return sDate;
	}

	public static String getCalYYYY_MM_DD_hh_mm_ss(Calendar cal)
			throws Exception {
		String sDate = null;
		// sDate = cal.get(Calendar.YEAR) + "" +
		// dateTwoConvert((cal.get(Calendar.MONTH) - Calendar.JANUARY+1)) + "" +
		// dateTwoConvert(cal.get(Calendar.DATE)) + "" +
		// dateTwoConvert(cal.get(Calendar.HOUR)) + "" +
		// dateTwoConvert(cal.get(Calendar.MINUTE)) + "" +
		// dateTwoConvert(cal.get(Calendar.SECOND));
		sDate = cal.get(Calendar.YEAR)
				+ "-"
				+ dateTwoConvert((cal.get(Calendar.MONTH) - Calendar.JANUARY + 1))
				+ "-" + dateTwoConvert(cal.get(Calendar.DATE)) + " "
				+ dateTwoConvert(cal.get(Calendar.HOUR_OF_DAY)) + ":"
				+ dateTwoConvert(cal.get(Calendar.MINUTE)) + ":"
				+ dateTwoConvert(cal.get(Calendar.SECOND));
		// System.out.println("sDate ["+sDate+"]");

		return sDate;
	}

	/**
	 * ���ڷ� ���� ��Ʈ����, i ���� ���� �����Ѵ�.
	 * 
	 * @param startDate
	 * @return ������
	 */
	public static String getDayMove(String startDate, int i) {
		Calendar date = Util.getStrCalendar(startDate);
		date.add(Calendar.DATE, i);

		return Util.getCalYYYYMMDD(date);
	}

	public static String getLastDay(String startDate) {
		String[] s_strings = Util.stringDivide(startDate, "-");
		Calendar static_cal = Util.getStrCalendar(s_strings[0] + s_strings[1]
				+ s_strings[2]);
		Calendar dynamic_cal = Util.getStrCalendar(s_strings[0] + s_strings[1]
				+ s_strings[2]);

		int cnt = 0;
		while (true) {
			if (!Util.dateTwoConvert(
					(dynamic_cal.get(Calendar.MONTH) - Calendar.JANUARY + 1))
					.equals(Util.dateTwoConvert((static_cal.get(Calendar.MONTH)
							- Calendar.JANUARY + 1)))) {
				break;
			}
			cnt++;
			// System.out.println(cnt+" "+Util.getCalYYYYMMDD(dynamic_cal));
			dynamic_cal.add(Calendar.DATE, 1);
		}

		return cnt + "";
	}

	/**
	 * ���ϵ� ���ڷ� ��ȯ�Ͽ� �����ϱ�
	 * 
	 * @param string
	 *            ���� ���ڿ�
	 * @return ���ϵ� ����
	 */
	public static String getWildChar(String string) {
		String wild = "";

		for (int i = 0; i < string.length(); i++) {
			wild += "*";
		}

		return wild;
	}

	/**
	 * fileCopdy �޼ҵ�
	 * 
	 * @param mother
	 *            �θ� ����
	 * @param clone
	 *            ������ ����
	 * @return ���� ���
	 */
	public static boolean fileCody(String mother, String clone) {
		boolean result = false;

		// File ��ü�� ���α׷� ������� ���ڸ� �̿��Ͽ� �����Ѵ�.
		// ���� ���, java CopyFile test.txt output.txt
		// test.txt -> args[0]
		// output.txt -> args[1]
		File inputFile = new File(mother);
		File outputFile = new File(clone);

		// FileReader, FileWriter Ŭ���� ��ü�� ����
		try {
			FileReader in = new FileReader(inputFile);
			FileWriter out = new FileWriter(outputFile);

			int c;
			// FileReader Ŭ���� ��ü���� ������ ������ �о
			// FileWriter Ŭ������ ���ش�.
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			in.close();
			out.close();
			result = true;
		} catch (IOException e) {
			e.getMessage();
		} finally {
		}

		return result;
	}

	/**
	 * String�� int������ ��ȯ (������ Ư�� int�� ����)
	 * 
	 * @param s
	 *            int�� �ٲ� ���ڿ�
	 * @param i
	 *            ������ ���ϰ�
	 * @return ��ȯ�� int
	 */
	// String�� int������ ��ȯ
	// s(���������� �ٲ� ���ڿ�),i(��ȯ�۾��� ������ �߻��ϸ� �⺻��)
	// return(s�� �ٲ� int)
	public static int toInteger(String s, int i) {
		int j;
		try {
			if (s == null || s == "")
				return i;
			j = Integer.parseInt(s);
		} catch (Exception exception) {
			j = i;
		}
		return j;
	}

	/**
	 * EnterŰ���� html�±׷� ��ȯ
	 * 
	 * @param s
	 *            ��ȯ�� ����
	 * @return ��ȯ�� ����
	 */
	// EnterŰ���� html�±׷� ��ȯ
	public static String convLFtoBR(String s) {
		int i = s.length();
		String s1 = "";
		for (int j = 0; j < i; j++) {
			char c = s.charAt(j);
			if (c == '\r') {
				if (j < i - 1)
					if (s.charAt(j + 1) == '\n')
						j++;
				s1 = s1 + "<BR>";
			} else if (c == '\n') {
				if (j < i - 1)
					if (s.charAt(j + 1) == '\r')
						j++;
				s1 = s1 + "<BR>";

			} else {
				s1 = s1 + c;
			}

		}
		return s1;
	}

	/**
	 * EnterŰ���� html�±׷� ��ȯ
	 * 
	 * @param s
	 *            ��ȯ�� ����
	 * @return ��ȯ�� ����
	 */
	// EnterŰ���� html�±׷� ��ȯ
	public static String convLFtoBR1(String s) {
		int i = s.length();
		String s1 = "";
		for (int j = 0; j < i; j++) {
			char c = s.charAt(j);
			if (c == '\r') {
				if (j < i - 1)
					if (s.charAt(j + 1) == '\n')
						j++;
				s1 = s1 + "<BR>";
			} else if (c == '\n') {
				if (j < i - 1)
					if (s.charAt(j + 1) == '\r')
						j++;
				s1 = s1 + "<BR>";

			} else if (c == ' ') {
				s1 = s1 + "&nbsp;";
			} else {
				s1 = s1 + c;
			}

		}
		return s1;
	}

	public static String cutString(String s, int c) {
		if (s.length() <= c) {
			return s;
		} else {
			return s.substring(0, c);
		}
	}

	public static String cutStringByte(String s, int cutlen) {
		if (s == null)
			return null; // return ""; �� ������ ��...

		byte[] ab = s.getBytes();
		int i, slen, cnt;

		slen = ab.length;

		if (slen <= cutlen)
			return s;

		cnt = 0;
		for (i = 0; i < cutlen; i++) {
			if ((((int) ab[i]) & 0xff) > 0x80)
				cnt++;
		}
		if ((cnt % 2) == 1)
			i--;

		return new String(ab, 0, i);
	}

	public static String cropByte(String str, int i, String trail) {
		if (str == null)
			return "";
		String tmp = str;
		int slen = 0, blen = 0;
		char c;
		if (tmp.getBytes().length > i) {
			while (blen + 1 < i) {
				c = tmp.charAt(slen);
				blen++;
				slen++;
				if (c > 127)
					blen++; // 2-byte character..
			}
			tmp = tmp.substring(0, slen) + trail;
		}
		return tmp;
	}

	// MD5 hash�� ���ϱ�
	public static final String encodeMD5(String strInput) {
		String strOut = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(strInput.getBytes());
			byte[] hash = digest.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				String strHash = Integer.toHexString(0xff & (char) hash[i]);
				sb.append(strHash.length() == 1 ? "0" : "").append(strHash);
			}
			strOut = sb.toString();
		} catch (NoSuchAlgorithmException nsae) {
			strOut = strInput;
		}
		return strOut;
	}

	/**
	 * Ư���Ⱓ �����ȿ� �ִ��� üũ (��: �ֱ� 5�Ͼȿ� �ֳ�?)
	 * 
	 * @param strDate
	 *            ��¥ (ex: 2004-06-19)
	 * @param sd
	 *            �ϼ�(������¥��)
	 * @return
	 */
	public static boolean isRecent(String strDate, int sd) {
		boolean boolResult = false;
		try {
			if (strDate != null && strDate.length() >= 10) {
				String yyyy = strDate.substring(0, 4);
				String mm = strDate.substring(5, 7);
				String dd = strDate.substring(8, 10);

				Calendar currentCal = Calendar.getInstance();
				Date currentDate = currentCal.getTime();

				int intYyyy = Integer.parseInt(yyyy);
				int intMm = Integer.parseInt(mm);
				int intDd = Integer.parseInt(dd);
				Calendar scal = Calendar.getInstance();
				scal.set(intYyyy, intMm - 1, intDd);
				Date sdate = scal.getTime();

				long millisDiff = currentDate.getTime() - sdate.getTime();
				int intResult = (int) (millisDiff / (1000 * 60 * 60 * 24));

				if (Math.abs(intResult) <= sd) {
					boolResult = true;
				}
			}

		} catch (Exception e) {
		}

		return boolResult;
	}
	
	public String getPushDataCut(String sendMsg, String key, int maxLength) throws Exception {
		
		String pushData = sendMsg;
		int totalMsgLength = 0;
		
		byte[] byteSendMsg = sendMsg.getBytes();
		int sendMsgLength = byteSendMsg.length;
		
		byte[] byteKey = key.getBytes();
		int keyLength = byteKey.length;
		
		totalMsgLength = sendMsgLength + keyLength;
		
		if (totalMsgLength > maxLength) {
			pushData = cropByte(sendMsg, maxLength - 2 - keyLength, "..");
		}
		return pushData;
	}	
}
