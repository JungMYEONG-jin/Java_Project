package sol.com.shinhan.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sol.com.shinhan.service.AccountManagerService;
import sol.com.shinhan.service.AccountService;
import sol.com.shinhan.service.AllAccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/superPlatform/web/account")
@Slf4j
public class AccountController extends DataInitController{

    private static final String className = "AccountController";

    private String REQUEST_PATH = "/api/superPlatform/web/account";
    private long beforeServletCall;
    private long afterServletCall;

    private long serverStartTime;
    private long serverEndTime;

    @RequestMapping(method = RequestMethod.POST, value = {"/list/all", "/manager", "/allaccount", "/accountDetail"})
    public JSONObject newUrl(@RequestBody String param, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
        log.info("account controller newUrl Start");
        super.init();
        if (param == null || param.equals("")){
            log.info(className + "param : " + param);
            return super.noDataException();
        }
        log.info(className + "request.getRequestURI() : " + request.getRequestURI());
        return process(param, request, response, session);
    }

    /**
     * controller process
     * 서비스 분기가 필요한 경우 uri or code 로 분기
     * @param param
     * @param request
     * @param response
     * @param session
     * @return
     */
    private JSONObject process(String param, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        JSONObject headObj = new JSONObject();
        JSONObject bodyObj = new JSONObject();
        JSONObject dataObj = new JSONObject();

        this.beforeServletCall = System.currentTimeMillis();

        if (super.inOutLogData)
            log.info(className + " Request Data : " + param);

        AccountService accountService = null;
        AccountManagerService accountManagerService = null;
        AllAccountService allAccountService = null;

        try{
            headObj.put("result", "SUCCESS");

            String serviceCode = SUPJsonUtil.getJSONStringParserToHeaderString(param, "serviceCode");
            if (request.getRequestURI().equals(REQUEST_PATH + "/manager.sp")){ //간편이체 이체한도
                accountManagerService = new AccountManagerService();
                bodyObj = accountManagerService.getResultData(param, request, response);
            }else if(request.getRequestURI().equals(REQUEST_PATH + "/list/all.sp")) // 계좌이체/입금
            {
                accountService = new AccountService();
                serverStartTime = System.currentTimeMillis();
                bodyObj = accountService.getResultData(param, request, response);
                serverEndTime = System.currentTimeMillis();
            }else if (request.getRequestURI().equals(REQUEST_PATH + "/allaccount.sp")){
                allAccountService = new AllAccountService();
                bodyObj = allAccountService.getResultData(param, request, response);
            }else if (request.getRequestURI().equals(REQUEST_PATH + "/accountDetail.sp")){
                AccountDetailService accountDetailService = new AccountDetailService();
                bodyObj = accountDetailService.getResultData(param, request, response);
            }
        }catch (NoClassDefFoundError e){
            super.dataException(e);
        }catch (Exception e){
            super.dataException(e);
        }finally {
            accountService = null;
            accountManagerService = null;
            allAccountService = null;
        }

        // 에러로그를 세션으로 판단하지 않는 url
        if (request.getRequestURI().equals(REQUEST_PATH + "/allaccount.sp")){
            JSONObject errJson = (JSONObject) bodyObj.get(RES.JSON_ERR_DATA);
            if (errJson != null){
                Map<String, Object> errMsgMap = SUPJsonConvertUtils.getErrorHeader(bodyObj);
                String error_code = errMsgMap.get("ERROR_CODE").toString();
                JSONObject msg = (JSONObject) errMsgMap.get("ERROR_MSG");
                JSONObject detail = (JSONObject) errMsgMap.get("ERROR_DETAIL");

                JSONObject errorHeaderJson = new JSONObject();
                errorHeaderJson.put("result", "FAIL");
                if(error_code != null){
                    errorHeaderJson.put("resultCode", error_code);
                }
                if (msg != null){
                    errorHeaderJson.put("resultMsg", msg);
                }
                if (detail != null){
                    errorHeaderJson.put("resultDetail", detail);
                }

                headObj = errorHeaderJson;
            }
        }else{
            if (SUPLoggerUtil.getErrorLogger(request) != null){
                headObj = SUPLoggerUtil.getErrorLogger(request);
            }
        }

        this.afterServletCall = System.currentTimeMillis();

        bodyObj.put("beforeServletCall", beforeServletCall);
        bodyObj.put("afterServletCall", afterServletCall);
        bodyObj.put("ProcessingTime", (afterServletCall - beforeServletCall));

        bodyObj.put("serverStartTime", serverStartTime);
        bodyObj.put("serverEndTime", serverEndTime);

        dataObj.put("header", headObj);
        dataObj.put("body", bodyObj);

        if(super.inOutLogData)
            log.info(className + " Response Data : " + dataObj);

        return dataObj;


    }

}
