package sol.com.shinhan.superplatform.util;

import com.sun.org.apache.xerces.internal.parsers.XML11Configuration;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@SuppressWarnings("deprecation")
public class SUPLoggerUtil {

    private static boolean logSessionId = false;
    /**
     * logWrite false 인 경우 supLogger의 option 무시하고 logger
     */
    private static boolean logWrite = false;
    /**
     * nameSapce false 인 경우 nameSpace logger 미생성
     */
    private static boolean nameSapceUse = true;
    /**
     * ip별 추가 로그
     */
    private static boolean ipNameSapceUse = false;

    // namespace use
    public static void supFineLogger(String nameSpace, String errorCode, String msg, String detail, boolean option){
        supSessionLogger(nameSpace, 1, errorCode, msg, detail, option, null);
    }

    public static void supFineLogger(String nameSpace, String msg){
        supSessionLogger(nameSpace, 1, null, msg, null, true, null);
    }

    public static void supInfoLogger(String nameSpace, String errorCode, Object msg, Object detail, boolean option){
        supSessionLogger(nameSpace, 2, errorCode, msg, detail, option, null);
    }

    public static void supExceptionLogger(String nameSpace, String errorCode, Object msg, Object detail, boolean option, Exception e){
        supSessionLogger(nameSpace, 2, errorCode, msg, detail, option, e);
    }

    // namespace not use

    public static void supFineLogger(String errorCode, String msg, String detail, boolean option){
        supSessionLogger(null ,1, errorCode, msg, detail, option, null);
    }

    public static void supFineLogger(String msg){
        supSessionLogger(null, 1, null, msg, null, true, null);
    }

    public static void supInfoLogger(String errorCode, Object msg, Object detail, boolean option){
        supSessionLogger(null, 2, errorCode, msg, detail, option, null);
    }

    public static void supException(String errorCode, Object msg, Object detail, boolean option, Exception e){
        supSessionLogger(null, 3, errorCode, msg, detail, option, e);
    }

    /**
     * level 1 : FINE not use session log
     * level 2 : INFO use session log
     * level 3 : ERROR use error log
     * option : use to create log on admin
     * @param nameSpace
     * @param level
     * @param errorCode
     * @param msg
     * @param detail
     * @param option
     * @param e
     */
    private static void supSessionLogger(String nameSpace, int level, String errorCode, Object msg, Object detail, boolean option, Exception e) {

        HttpServletRequest request = null;
        HttpSession session = null;

        try{
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            request = attr.getRequest();
            session = request.getSession(true);
        }catch (Exception sessionException){
            session = null;
        }

        if(errorCode == null){
            errorCode = "";
        }

        if (msg == null){
            if (e != null)
                msg = e.getMessage();
        }

        if (detail == null){
            if (e != null)
                detail = e.toString();
        }

        try{
            if (level > 1){
                if (session != null){
                    SUPSessionUtil.setUserAttribute(session, "ERROR_CODE", errorCode);
                    SUPSessionUtil.setUserAttribute(session, "ERROR_MSG", msg);
                    SUPSessionUtil.setUserAttribute(session, "ERROR_DETAIL", detail);

                    if (e != null){
                        if (SUPSystemUtil.getSystemGubun().equals("T")){
                            SUPSessionUtil.setUserAttribute(session, "ERROR_EXCEPTION", e);
                        }else{
                            SUPSessionUtil.setUserAttribute(session, "ERROR_EXCEPTION", "");
                        }
                    }



                }
            }

            try{
                String configPath = "";

                configPath = XMLConfiguration.getInstance().get("/proworks/sfg/configPath@path");
                XMLConfiguration config = XMLConfiguration.getInstance(configPath);
                String fineLogDataVal = config.get("/sol/supfineLog/log");

                if (fineLogDataVal.equals("true")){
                    logWrite = true;
                }
            }catch (Exception e1){
                logWrite = false;
            }

            if (logWrite){
                if (option){

                    JSONObject errorJson = new JSONObject();

                    errorJson.put("ERROR_EXCEPTION", e);
                    errorJson.put("ERROR_DETAIL", detail);
                    errorJson.put("ERROR_MSG", msg);
                    errorJson.put("ERROR_CODE", errorCode);

                    if (session != null){
                        int seqStep;

                        if(SUPSessionUtil.getUserAttribute(session, "seqStep") == null){
                            seqStep = 0;
                        }else{
                            seqStep = Integer.parseInt(SUPSessionUtil.getUserAttribute(session, "seqStep").toString()) + 1;
                        }
                        final String remoteHost = request.getRemoteHost().replaceAll("\\.", "_");
                        final String ipLogSpace = "IP_" + remoteHost;

                        SUPSessionUtil.setUserAttribute(session, "seqStep", seqStep);

                        if (logSessionId){
                            if (nameSpace != null && nameSapceUse == true){
                                if (level == 1){
                                    Logger.fine(nameSpace, "session ID : " + session.getId() + ", step : "+seqStep+", "+StringEscapeUtils.unesacpeJson(errorJson.get("ERROR_MSG" ).toString()));
                                }
                                if (level == 2){
                                    Logger.fine(nameSpace, "session ID : " + session.getId() + ", step : "+seqStep+", "+StringEscapeUtils.unesacpeJson(errorJson.toJSONString()));
                                }
                                if (level == 3) {
                                    Logger.fine(nameSpace, "session ID : " + session.getId() + ", step : "+seqStep+", "+StringEscapeUtils.unesacpeJson(errorJson.get("ERROR_MSG" ).toString()), e);
                                }
                            }
                        }else{
                            if (nameSpace != null && nameSapceUse == true){
                                if (level == 1){
                                    Logger.fine(nameSpace,StringEscapeUtils.unesacpeJson(errorJson.get("ERROR_MSG" ).toString()));
                                }
                                if (level == 2){
                                    Logger.fine(nameSpace, StringEscapeUtils.unesacpeJson(errorJson.toJSONString()));
                                }
                                if (level == 3) {
                                    Logger.fine(nameSpace, StringEscapeUtils.unesacpeJson(errorJson.get("ERROR_MSG" ).toString()), e);
                                }
                            }else{
                                if (level == 1){
                                    Logger.fine(StringEscapeUtils.unesacpeJson(errorJson.get("ERROR_MSG" ).toString()));
                                }
                                if (level == 2){
                                    Logger.fine(StringEscapeUtils.unesacpeJson(errorJson.toJSONString()));
                                }
                                if (level == 3) {
                                    Logger.fine(StringEscapeUtils.unesacpeJson(errorJson.get("ERROR_MSG" ).toString()), e);
                                }
                            }

                            if (ipNameSapceUse){
                                if (level == 1){
                                    Logger.fine(ipLogSpace,StringEscapeUtils.unesacpeJson(errorJson.get("ERROR_MSG" ).toString()));
                                }
                                if (level == 2){
                                    Logger.fine(ipLogSpace, StringEscapeUtils.unesacpeJson(errorJson.toJSONString()));
                                }
                                if (level == 3) {
                                    Logger.fine(ipLogSpace, StringEscapeUtils.unesacpeJson(errorJson.get("ERROR_MSG" ).toString()), e);
                                }
                            }
                        }
                    }else{
                        if (nameSpace != null && nameSapceUse == true){
                            if (level == 1){
                                Logger.fine(nameSpace,StringEscapeUtils.unesacpeJson(errorJson.get("ERROR_MSG" ).toString()));
                            }
                            if (level == 2){
                                Logger.fine(nameSpace, StringEscapeUtils.unesacpeJson(errorJson.toJSONString()));
                            }
                            if (level == 3) {
                                Logger.fine(nameSpace, StringEscapeUtils.unesacpeJson(errorJson.get("ERROR_MSG" ).toString()), e);
                            }
                        }else{
                            if (level == 1){
                                Logger.fine(StringEscapeUtils.unesacpeJson(errorJson.get("ERROR_MSG" ).toString()));
                            }
                            if (level == 2){
                                Logger.fine(StringEscapeUtils.unesacpeJson(errorJson.toJSONString()));
                            }
                            if (level == 3) {
                                Logger.fine(StringEscapeUtils.unesacpeJson(errorJson.get("ERROR_MSG" ).toString()), e);
                            }
                        }
                    }
                    }

            }

        }catch (Exception el){
            Logger.fine("exception, ", e);
        }


    }

    public static void initErrorLogger(HttpServletRequest request){
        if (request.getSession() != null){
            SUPSessionUtil.removeUserAttribute(request.getSession(), "ERROR_CODE");
            SUPSessionUtil.removeUserAttribute(request.getSession(), "ERROR_MSG");
            SUPSessionUtil.removeUserAttribute(request.getSession(), "ERROR_DETAIL");
            SUPSessionUtil.removeUserAttribute(request.getSession(), "ERROR_EXCEPTION");
        }
    }

    public static JSONObject getErrorLogger(HttpServletRequest request){
        JSONObject error = null;

        if (request.getSession() != null){
            if (SUPSessionUtil.getUserAttribute(request.getSession(), "ERROR_CODE") != null){
                error = new JSONObject();

                error.put("result", "FAIL");
                error.put("resultCode", SUPSessionUtil.getUserAttribute(request.getSession(), "ERROR_CODE"));
                error.put("resultMsg", SUPSessionUtil.getUserAttribute(request.getSession(), "ERROR_MSG"));
                error.put("resultDetail", SUPSessionUtil.getUserAttribute(request.getSession(), "ERROR_DETAIL"));
                error.put("resultException", SUPSessionUtil.getUserAttribute(request.getSession(), "ERROR_EXCEPTION"));

                return error;
            }else{
                return error;
            }
        }else{
            return error;
        }
    }

    public static boolean setErrorLogger(String errorCode, JSONObject errorMsg, JSONObject errorDetail){
        boolean resultBoolean = true;

        try{
            HttpSession session = null;

            try{
                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                session = attr.getRequest().getSession(true);
            }catch (Exception sessionE){
                session = null;
            }
            SUPSessionUtil.setUserAttribute(session, "ERROR_CODE", errorCode);
            SUPSessionUtil.setUserAttribute(session, "ERROR_MSG", errorMsg);
            SUPSessionUtil.setUserAttribute(session, "ERROR_DETAIL", errorDetail);

        }catch (Exception sessionE){
            resultBoolean = false;
        }

        return resultBoolean;
    }

    public static String getSAStackTrace(Exception e){
        String result = "";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        e.printStackTrace(ps);
        result = out.toString();
        return result;
    }

}
