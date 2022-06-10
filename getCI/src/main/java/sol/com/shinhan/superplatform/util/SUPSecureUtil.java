package sol.com.shinhan.superplatform.util;

import java.security.MessageDigest;

public class SUPSecureUtil {

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
