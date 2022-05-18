package sol.com.shinhan.superplatform.util;

import org.thymeleaf.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SUPStringUtil {

    final public static String EMPTY = "";

    public static boolean isNull(String str)
    {
        if(str==null || EMPTY.equals(str))
        {
            return true;
        }else
            return false;
    }

    public static boolean isNull(String[] str) {
        if (str == null || str.length == 0) {
            return true;
        }
        for (String s : str) {
            if (isNotNull(s))
                return false;
        }
        return true;
    }

    public static boolean isNull(Object[] str) {
        if (str == null || str.length == 0) {
            return true;
        }
        for (Object o : str) {
            if(o!=null)
                return false;
        }
        return true;
    }

    public static boolean isNullOrEmpty(String str)
    {
        return (str == null || EMPTY.equals(str));
    }

    public static boolean isNotEmpty(String str)
    {
        return !isNullOrEmpty(str);
    }

    public static String getString(String str)
    {
        return getString(str, EMPTY);
    }

    public static String getString(String str, String defualtStr)
    {
        if(isNullOrEmpty(str))
        {
            return defualtStr;
        }else
            return str;
    }

    public static boolean isEquals(String left, String right)
    {
        return left.equals(right);
    }

    /**
     * 1개라도 일치하면 true
     * @param str
     * @param arrStr
     * @return
     */
    public static boolean orEquals(String str, String[] arrStr)
    {
        if(str==null)
            return false;

        for (String s : arrStr) {
            if(str.equals(s))
                return true;
        }
        return false;
    }

    public static boolean orEquals(String str, String str2, String str3)
    {
        return orEquals(str, new String[]{str2, str3});
    }

    public static boolean orEquals(String str, String str2, String str3, String str4)
    {
        return orEquals(str, new String[]{str2, str3, str4});
    }

    public static boolean orEquals(String str, String str2, String str3, String str4, String str5)
    {
        return orEquals(str, new String[]{str2, str3, str4, str5});
    }

    public static String null2white(String s)
    {
        if(s==null)
        {
            s="";
        }
        return s;
    }

    public static String null2StrTrim(String str)
    {
        if(str==null)
            return "";
        return str.trim();
    }

    public static String strNull(Object objInput)
    {
        return (objInput==null) ? "" : objInput.toString();
    }

    /**
     * String 공백 지우기
     */
    public static String removeWhite(String str)
    {
        return str.replaceAll(" ", "");
    }

    /**
     * 2바이트 문자를 2자리로 취급하여 계산
     * char 0~255   까지는 1글자 그 이상은 2글자로 취급한다.
     */
    public static String substring(String source, int size)
    {
        String result = null;
        if(source==null)
        {
            return null;
        }

        if(source.equals(EMPTY))
            return "";

        StringBuffer sb = new StringBuffer();
        int len = 0;
        for(int i=0, length = source.length();i<length;i++)
        {
            char ch = source.charAt(i);
            if(0 <= ch && ch <=255)
            {
                len+=1;
            }else{
                len+=2;
            }
            if(len>size)
            {
                break;
            }
            sb.append(ch);
        }
        result = sb.toString();
        return result;

    }

    /**
     * 제한된 byteLen만큼 반환
     */
    public static String getShortString(String str, int limitLength)
    {
        byte[] byteString = str.getBytes();
        int len = limitLength;
        if(byteString.length <= len)
        {
            return str;
        }else{
            int minusCnt = 0;
            for(int i=0;i<len;i++)
            {
                minusCnt+=(byteString[i]<0)?1:0;
            }
            if(minusCnt % 2 != 0)
            {
                len--;
            }
            return new String(byteString, 0, len);
        }
    }


    /**
     * NumberFormatException 처리
     */
    public static double parseDouble(String str, double dft)
    {
        double result = dft;

        try{
            result = Double.parseDouble(str);
        }catch (Exception e)
        {
            result = dft;
        }
        return result;
    }

    public static boolean isNotNull(String str)
    {
        if(str!=null && EMPTY.equals(str) == false)
            return true;
        else
            return false;
    }

    /**
     * not null 이면 left return else return defaultStr
     * @param paramStr
     * @param defaultStr
     * @return
     */
    public static String nullTo(String paramStr, String defaultStr)
    {
        if(isNotNull(paramStr))
            return paramStr;

        return defaultStr;
    }

    /**
     * bytes to hex string
     */
    public static String bytesToHexString(byte[] bytes)
    {
        String result = "";

        if(bytes!=null && bytes.length>0)
        {
            int len = bytes.length;

            StringBuffer sb = new StringBuffer(2 * len);

            for(int i=0;i<len;i++)
            {
                String strHexNumber = "0"+Integer.toHexString(0xFF & bytes[i]);
                sb.append(strHexNumber.substring(strHexNumber.length()-2));
            }
            result = sb.toString();
        }
        return result;
    }

    /**
     * hexString to byte arr
     */
    public static byte[] hexStrToBytes(String hexStr)
    {
        byte[] bytes = null;

        if(hexStr!=null && hexStr.length()>0)
        {
            bytes = new byte[hexStr.length()/2];

            for(int i=0, len = bytes.length;i<len;i++)
            {
                bytes[i] = (byte) Integer.parseInt(hexStr.substring(2*i, 2*i+2), 16);
            }
        }
        return bytes;
    }

    public static String getPostWord(String str, String firstVal, String secondVal)
    {
        String result = "";
        try{
            char lastStr = str.charAt(str.length()-1);
            if(lastStr<0xAC00 || lastStr>0xD7A3)
            {
                return str;
            }

            int lastCharidx = (lastStr - 0xAC00) % 28;

            if(lastCharidx>0)
            {
                if(firstVal.equals("으로") && lastCharidx==0)
                {
                    result = str+secondVal;
                }else
                {
                    result = str+firstVal;
                }
            }else
            {
                result = str+secondVal;
            }
        }catch (Exception e)
        {
            return str;
        }
        return result;
    }

    /**
     * euckr to utf-8
     */
    public static byte[] euckrToUtf8Buffer(String strEuckr)
    {
        byte[] euckrBytes = strEuckr.getBytes(Charset.forName("EUC-KR"));

        byte[] utf8Bytes = null;

        String decodedFromEuckr = "";
        try{
            decodedFromEuckr = new String(euckrBytes, "EUC-KR");
            utf8Bytes = decodedFromEuckr.getBytes(StandardCharsets.UTF_8);
        }catch (UnsupportedEncodingException e)
        {
            utf8Bytes = euckrBytes;
        }
        return utf8Bytes;
    }

    /**
     * euckr to utf-8
     */
    public static String euckrToUtf8(String strEuckr)
    {
        byte[] utf8Buffer = euckrToUtf8Buffer(strEuckr);

        String strUtf8 = null;
        try {
            strUtf8 = new String(utf8Buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strUtf8;
    }

    /**
     * utf-8 to euckr
     */
    public static String utf8ToEuckr(String utf8String)
    {
        byte[] utf8Buffer = utf8String.getBytes(StandardCharsets.UTF_8);

        String euckrStr = "";
        try {
            euckrStr = new String(utf8Buffer, "euc-kr");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return euckrStr;
    }

    public static int VersionCompTo(String verA, String verB)
    {
        if(StringUtils.isEmpty(verA) || StringUtils.isEmpty(verB))
            return 0;

        String[] partA = verA.split("\\.", -1);
        String[] partB = verB.split("\\.", -1);

        int len = Math.max(partA.length, partB.length);
        for(int i=0;i<len;i++)
        {
            int tmpA = i<partA.length ? Integer.parseInt(partA[i]) : 0;
            int tmpB = i<partB.length ? Integer.parseInt(partB[i]) : 0;
            if(tmpA!=tmpB)
            {
                return Integer.compare(tmpA, tmpB);
            }
        }
        return 0;
    }











}
