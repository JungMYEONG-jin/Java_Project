package com.simpleauthJPA.shinhan;


import com.simpleauthJPA.entity.User;
import com.simpleauthJPA.repository.UserRepository;
import com.simpleauthJPA.shinhan.security.imple.SAProperty;
import com.simpleauthJPA.shinhan.security.imple.SASimpleAuthAction;
import com.simpleauthJPA.shinhan.security.listener.SAListener;
import com.simpleauthJPA.shinhan.security.simpleauth.SAConst;
import com.simpleauthJPA.shinhan.security.simpleauth.exception.SAInvalidPasswordException;
import com.simpleauthJPA.shinhan.security.simpleauth.exception.SASimpleAuthException;
import com.simpleauthJPA.shinhan.security.simpleauth.exception.SASimpleAuthMessageException;
import com.simpleauthJPA.shinhan.security.simpleauth.message.SAErrorMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.message.SAMessageUtil;
import com.simpleauthJPA.shinhan.security.simpleauth.tlv.SAErrsEnum;
import com.simpleauthJPA.shinhan.security.simpleauth.tlv.SATagsEnum;
import com.simpleauthJPA.shinhan.security.simpleauth.util.SAHexUtil;
import com.simpleauthJPA.shinhan.security.simpleauth.util.SAUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class SimpleAuthTask implements SAListener
{

    private final static String service_name = "SA";
    private final static String ses_nm_challenge = "sa_challenge";
    private final static String ses_nm_authkey = "sa_authkey";
    private final static String res_root_node_nm = "<AUTH_LOGIN_RESULT/>";
    private final static String res_tag_node_nm = "tag";
    private final static String res_json_node_nm = "resJson";
    private final static String successCode = "0000";
    private final static String sh_corp_c = "130";
    private final static String successResult = "s";
    private final static String failureResult = "f";
    private static String msg = "";
    private static String osType = "";
    private static String osVersion = "";
    private static String model = "";
    private static String appVersion = "";
    private static String tag = "";
    private static String saveVersion = "";
    private static String type = "";
    private static JSONObject jsonObject = null;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onSetChallengeInSession(String paramString, HttpSession paramHttpSession) {
        paramHttpSession.setAttribute(ses_nm_challenge, paramString);
    }

    @Override
    public boolean onSimpleAuthInfoReg(Object var) throws SASimpleAuthException {
        try{
            if(var!=null)
            {
                User user = (User) var;
                User savedUser = userRepository.save(user);
                List<User> findUser = userRepository.findByIdAndTypeEquals(savedUser.getId(), "999999999");
                int cnt = findUser.size(); // id random 생성하여 user 마다 고유함.
                if(cnt==1)
                    return true;
                else
                    return false;
            }
        } catch (Exception e) {
            throw new SASimpleAuthException(e.toString());
        }
        return false;
    }

    @Override
    public User onSimpleAuthUserSearch(Object var) throws SASimpleAuthException {
        try{
            if(var != null){
                User user = (User) var;
                List<User> findUser = userRepository.findByIdAndTypeEquals(user.getId(), "999999999");
                int cnt = findUser.size();
                if(cnt==1){
                    return findUser.get(0);
                }else{
                    return null;
                }
            }
        }catch (Exception e) {
            throw new SASimpleAuthException(e.toString());
        }
        return null;
    }


    @Override
    public String processInit(String reqJson, HttpSession paramHttpSession) {

        SAProperty.setSA_Log(false);
        SAProperty.setSA_TBName("MBI_SIMPLEAUTH");
        SAProperty.setSA_ColumName("ID", "CUSNO", "PUBKEY", "UUID", "APP_ID", "TYPE", "STATUS", "REG_DTTM", "DROP_DTTM", "LAST_AUTH_DTTM");

        // log, sequence, index 생략

        SAProperty.setSA_DBPoolName("mbbPool");
        SAProperty.setSA_StatusValue("1", "9");

        String result = null;

        jsonObject = SAUtil.strToJSON(reqJson);
        tag = jsonObject.get("tag").toString();
        osType = jsonObject.get("ostype").toString();
        osVersion = jsonObject.get("osversion").toString();
        appVersion = jsonObject.get("appversion").toString();
        model = jsonObject.get("model").toString();
        saveVersion = jsonObject.get("saveversion").toString();
        type = jsonObject.get("type").toString();

        if(SAHexUtil.tagToHex(SATagsEnum.TAG_INIT_REG).equals(tag))
            result = registInit(tag, reqJson, paramHttpSession);
        else if(SAHexUtil.tagToHex(SATagsEnum.TAG_REG).equals(tag))
            result = regist(tag, reqJson, paramHttpSession);
        else if(SAHexUtil.tagToHex(SATagsEnum.TAG_INIT_AUTH).equals(tag))
            result = authorizeInit(tag, reqJson, paramHttpSession);
        else if(SAHexUtil.tagToHex(SATagsEnum.TAG_AUTH).equals(tag))
            result = authorize(tag, reqJson, paramHttpSession);
        else if(SAHexUtil.tagToHex(SATagsEnum.TAG_UNREG).equals(tag))
            result = unregist(tag, reqJson, paramHttpSession);

        return null;
    }

    @Override
    public String registInit(String reqTag, String reqJson, HttpSession paramHttpSession) {
        SASimpleAuthAction action = new SASimpleAuthAction(this);
        String result = null;
        String regInitServerJson = null;

        String cusno = paramHttpSession.getAttribute("cusno").toString();

        try{

            regInitServerJson = action.reg_init_server(reqTag, reqJson, cusno, paramHttpSession);
            System.out.println("junit test reg init message: "+regInitServerJson);
            JSONParser parser = new JSONParser();
            JSONObject obj = null;

            try{
                obj =(JSONObject) parser.parse(regInitServerJson);
                String id = obj.get("id").toString();
            } catch (ParseException e) {
                System.out.println("json exception: "+e.getMessage());
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_PARSE_JSON, SAErrorMessage.ERR_MSG_PARSE_JSON, SAErrorMessage.ERR_CODE_PARSE_JSON);
            }

            if(regInitServerJson!=null)
                return regInitServerJson;

            String strSessionChallenge = paramHttpSession.getAttribute(ses_nm_challenge).toString();

        } catch (SASimpleAuthException e) {
            System.out.println("main exception: "+e.getMessage());
        }finally {
            if(action!=null)
                action=null;
        }


        return result;
    }

    @Override
    public String regist(String reqTag, String reqJson, HttpSession paramHttpSession) {

        String sessionChallenge = paramHttpSession.getAttribute(ses_nm_challenge).toString();
        SASimpleAuthAction action = new SASimpleAuthAction(this);
        String result = null;

        String resultMsgJson = null;

        String cusno = paramHttpSession.getAttribute("cusno").toString();

        try{
            String strSessionChallenge = paramHttpSession.getAttribute(ses_nm_challenge).toString();
            System.out.println("strSessionChallenge = " + strSessionChallenge);

            resultMsgJson = action.reg_server(reqTag, reqJson, cusno, strSessionChallenge, paramHttpSession);
            System.out.println("resultMsgJson = " + resultMsgJson);

            String strID = "";
            String strAppID = "";
            String strUUID = "";
            String strType = "";
            String err_yn = "";
            String strCusno = "";

            JSONObject obj = null;

            obj = SAUtil.strToJSON(resultMsgJson);

            strID = obj.get(SAConst.TAG_ID).toString();
            strAppID = obj.get(SAConst.TAG_APPID).toString();
            strUUID = obj.get(SAConst.TAG_UUID).toString();
            strType = obj.get(SAConst.TAG_TYPE).toString();
            strCusno = obj.get(SAConst.TAG_CUSNO).toString();
            err_yn = obj.get(SAConst.TAG_YN).toString();

            if(err_yn.equals("n"))
                result = resultMsgJson;

            System.out.println("regist auth id = " + strID);

        }catch (SASimpleAuthException e)
        {

        } catch (SAInvalidPasswordException e) {
            e.printStackTrace();
        } finally {
            String strSessionChallenge = paramHttpSession.getAttribute(ses_nm_challenge).toString();
            if(strSessionChallenge!=null || strSessionChallenge.length()>0)
            {
                paramHttpSession.setAttribute(ses_nm_challenge, "");
            }
            if (action!=null)
                action=null;
        }
        return result;

    }

    @Override
    public String authorizeInit(String reqTag, String reqJson, HttpSession paramHttpSession) {

        SASimpleAuthAction action = new SASimpleAuthAction(this);
        String result = null;

        String authInitJson = null;
        String authkey = null;

        try{
            authkey = SAMessageUtil.getAuthKey(reqJson);
            if (authkey!=null)
            {
                paramHttpSession.setAttribute(ses_nm_authkey, authkey);
            }else
                throw new SASimpleAuthException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_ID_NULL, SAErrorMessage.ERR_CODE_ID_NULL);


            authInitJson = action.auth_Init_server(reqTag, reqJson, paramHttpSession);

            JSONObject obj = null;
            String err_YN = "";

            obj = SAUtil.strToJSON(authInitJson);
            err_YN = obj.get(SAConst.TAG_YN).toString();

            if(err_YN.equals("n"))
            {
                result = authInitJson;
            }
        } catch (SASimpleAuthException e) {
            e.printStackTrace();
        } catch (SAInvalidPasswordException e) {
            e.printStackTrace();
        } finally {
            if (action!=null)
                action=null;
        }
        return result;
    }

    @Override
    public String authorize(String reqTag, String reqJson, HttpSession paramHttpSession) {
        SASimpleAuthAction action = new SASimpleAuthAction(this);
        String result = null;
        String authResultJson = null;

        final String authkey = paramHttpSession.getAttribute(ses_nm_authkey).toString();
        System.out.println("authkey = " + authkey);

        try{
            String strSessionChallenge = paramHttpSession.getAttribute(ses_nm_challenge).toString();
            System.out.println("strSessionChallenge = " + strSessionChallenge);
            authResultJson = action.auth_server(reqTag, reqJson, strSessionChallenge, paramHttpSession);
            System.out.println("authResultJson = " + authResultJson);

           JSONObject obj = SAUtil.strToJSON(authResultJson);

            String strID = obj.get(SAConst.TAG_ID).toString();
            String strAppId = obj.get(SAConst.TAG_APPID).toString();
            String strUUID = obj.get(SAConst.TAG_UUID).toString();
            String strType = obj.get(SAConst.TAG_TYPE).toString();
            String cusno = obj.get(SAConst.TAG_CUSNO).toString();
            String err_YN = obj.get(SAConst.TAG_YN).toString();

            if(err_YN.equals("n"))
                result = authResultJson;
        } catch (SASimpleAuthException e) {

        }finally {
            String strSessionChallenge = paramHttpSession.getAttribute(ses_nm_challenge).toString();
            String strAuthKey = paramHttpSession.getAttribute(ses_nm_authkey).toString();

            if(strSessionChallenge!=null || strSessionChallenge.length()>0)
            {
                paramHttpSession.setAttribute(ses_nm_challenge, "");
            }
            if(strAuthKey!=null || strAuthKey.length()>0)
            {
                paramHttpSession.setAttribute(ses_nm_authkey, "");
            }
            if(action!=null)
                action=null;
        }
        return result;
    }

    @Override
    public String unregist(String reqTag, String reqJson, HttpSession paramHttpSession) {
        SASimpleAuthAction action = new SASimpleAuthAction(this);

        String result = null;
        String unregResultJson = null;

        final String cusno = paramHttpSession.getAttribute("cusno").toString();

        try{
            unregResultJson = action.unreg(reqTag, reqJson, paramHttpSession);

            JSONObject obj = null;
            obj = SAUtil.strToJSON(unregResultJson);

            String err_YN = obj.get(SAConst.TAG_YN).toString();

            if (err_YN.equals("n"))
                result = unregResultJson;

        } catch (SASimpleAuthException e) {
            e.printStackTrace();
        }finally {
            if (action!=null)
                action=null;
        }
        return result;


    }

    @Override
    public boolean CheckPasswordValidation(String password, String authType, HttpSession paramHttpSession) {
        return false;
    }


}
