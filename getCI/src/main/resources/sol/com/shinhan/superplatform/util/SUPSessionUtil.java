package sol.com.shinhan.superplatform.util;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import javax.servlet.http.HttpSession;
import java.util.Vector;

@Slf4j
public class SUPSessionUtil {

    public static long getLastAccessedTime(HttpSession session){
        long lastAccess = System.currentTimeMillis();
        String str = (String) getAttribute(session, "ProWorks.SystemAttribute", "lastAccess");
        try{
            if (str != null && str.equals("")){
                lastAccess = Long.parseLong(str);
            }
        }catch (Exception e){
            log.info("마지막 접근 시간을 구하는중 에러 발생 = {}", e);
        }
        return lastAccess;
    }

    public static void updateLastAccess(HttpSession session){
        setAttribute(session, "ProWorks.SystemAttribute", "lastAccess", System.currentTimeMillis() + "");
    }

    public static Object getSystemAttribute(HttpSession session, String key){
        return getAttribute(session, "ProWorks.SystemAttribute", key);
    }


    public static Object getUserAttribute(HttpSession session, String key) {
        return getAttribute(session, "ProWorks.UserAttribute", key);
    }

    public static Object getTransactionAttribute(HttpSession session, String key) {
        return getAttribute(session, "ProWorks.TransactionAttribute", key);
    }

    public static void setSystemAttribute(HttpSession session, String key, Object value){
        setAttribute(session, "ProWorks.SystemAttribute", key, value);
    }

    public static void setUserAttribute(HttpSession session, String key, Object value){
        setAttribute(session, "ProWorks.UserAttribute", key ,value);
    }

    public static void setTransactionAttribute(HttpSession session, String key, Object value){
        setAttribute(session, "ProWorks.TransactionAttribute", key, value);
    }

    public static void removeUserAttribute(HttpSession session, String key){
        removeAttribute(session, "ProWorks.UserAttribute", key);
    }

    public static void removeTransactionAttribute(HttpSession session, String key){
        removeAttribute(session, "ProWorks.TransactionAttribute", key);
    }

    public static void removeSystemAttribute(HttpSession session, String key){
        removeAttribute(session, "ProWorks.SystemAttribute", key);
    }

    public static void printSession(HttpSession session){
        SessionUtil.printSession(session);
    }

    public static Document getSessionList(Document doc, HttpSession session){
        String getSessionLisStr = XMLUtil.getAttribute(doc, "getSessionList").trim();
        Document result = null;

        if (!getSessionLisStr.equals("")){
            Object obj = null;
            if (getSessionLisStr.startsWith("system:")){
                String id = getSessionLisStr.substring("system:".length());
                obj = SUPSessionUtil.getAttribute(session, "ProWorks.SystemAttribute", id);
            }else if (getSessionLisStr.startsWith("user:")){
                String id = getSessionLisStr.substring("user:".length());
                obj = SUPSessionUtil.getAttribute(session, "ProWorks.UserAttribute", id);
            }else if (getSessionLisStr.indexOf(":") == -1){
                obj = SUPSessionUtil.getAttribute(session, "ProWorks.TransactionAttribute", getSessionLisStr);
            }

            if (obj instanceof Vector){
                Vector vec = (Vector) obj;
                result = XMLUtil.getDocument("<SESSION_ERROR/>");
            }
        }else{
            log.warn("다른 서비스 코드가 실행 되었습니다.");
            result = WarningUtil.makeWarningDoc("인자가 부족합니다.", WarningUtil.MSG, "저장된 메세지를 꺼낼 속성이 지정되지 않음", he is child);
        }
        return result;
    }


}
