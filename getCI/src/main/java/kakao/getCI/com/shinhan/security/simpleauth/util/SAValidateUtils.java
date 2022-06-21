package kakao.getCI.com.shinhan.security.simpleauth.util;

public class SAValidateUtils {
    private static String _SASimpleAuthAction_ = "_SASimpleAuthAction_ :: ";

    public static boolean isValidTime(long oldtime, int minute) {
        long currentTime = System.currentTimeMillis() / 1000L;
        long diff = currentTime - oldtime;
        long gap = diff / 60L;
        SALogUtil.fine(String.valueOf(_SASimpleAuthAction_) + "isValidTime" + "currentTime=[" + currentTime + "] oldtime=[" + oldtime + "] diff=[" + diff + "] gap=[" + gap + "]");
        if (gap < minute)
            return true;
        return false;
    }

    public static boolean isValidType(String type) {
        if (type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4") || type.equals("5"))
            return true;
        return false;
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.equals(""))
            return true;
        return false;
    }
}
