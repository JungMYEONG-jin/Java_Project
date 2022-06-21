package kakao.getCI.com.shinhan.security.callback;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public interface SACallbackReg {
    boolean cbSimpleAuthInfoReg(HashMap<String, String> paramHashMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
}
