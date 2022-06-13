package sol.com.shinhan.superplatform.util;

import java.security.MessageDigest;

public class SUPSecureUtil {

    private static final String className = "SUPSecureUtil";

    private static String KNC_KEY = "SH_KNC_MOBILE_SUP_CO_KR_201801"; // aes enc.dec knc_key
    private static String KEY = "SOL_SHINHAN_2018";

    private static String AUTION_KEY = "SHAUTIONKEY20180";
    private static int AUTION_KEY_LENGTH = 16;

    private static String NOFACE_KEY = "shinhan_noface16"; // 비대면 aes128 key

    // 보안 취약점 XSS 방지
    public static String XSSFilter(String str){
        if (str == null || str.equals("")){
            return "";
        }

        String result = str.trim();

        result = result.replaceAll("&", "&amp;");
        result = result.replaceAll("<", "&lt;");
        result = result.replaceAll(">", "&gt;");
        result = result.replaceAll("\"", "&quot;");
        result = result.replaceAll("\'", "&#039");
        result = result.replaceAll("\\n", "");
        result = result.replaceAll("\\r", "");

        return result;
    }

    // XSS 취약점 검사하여 발생하지 않도록 변환된 값 반환
    public static String invalidXSS(String str){
        if (str == null || str.equals("")){
            return "";
        }

        String result  = str.trim();
        result = result.replaceAll("\\n", "&#x0A");
        result = result.replace("\\r","");
        result = result.replace("\r\n","");

        return result;
    }



    public static byte[] hashSHA256(String data)
    {
        if(data==null)
            return null;

        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(data.getBytes());
            byte[] digest = sh.digest();
            return digest;
        }catch (Exception e)
        {

        }
        return null;
    }
}
