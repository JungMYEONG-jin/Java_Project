package kakao.getCI.com.shinhan.security.imple;

import kakao.getCI.com.shinhan.security.simpleauth.crypto.SACryptoUtil;
import kakao.getCI.com.shinhan.security.simpleauth.exception.SAInvalidPasswordException;
import kakao.getCI.com.shinhan.security.simpleauth.exception.SASimpleAuthCryptoException;
import kakao.getCI.com.shinhan.security.simpleauth.exception.SASimpleAuthException;
import kakao.getCI.com.shinhan.security.simpleauth.exception.SASimpleAuthMessageException;
import kakao.getCI.com.shinhan.security.simpleauth.message.SAErrorMessage;
import kakao.getCI.com.shinhan.security.simpleauth.message.SAMessageUtil;
import kakao.getCI.com.shinhan.security.simpleauth.message.auth.SAAuthClientMessage;
import kakao.getCI.com.shinhan.security.simpleauth.message.auth.SAAuthInitClientMessage;
import kakao.getCI.com.shinhan.security.simpleauth.message.auth.SAAuthInitServerMessage;
import kakao.getCI.com.shinhan.security.simpleauth.message.reg.SARegClientMessage;
import kakao.getCI.com.shinhan.security.simpleauth.message.reg.SARegInitClientMessage;
import kakao.getCI.com.shinhan.security.simpleauth.message.reg.SARegInitServerMessage;
import kakao.getCI.com.shinhan.security.simpleauth.message.result.SAResultMessage;
import kakao.getCI.com.shinhan.security.simpleauth.message.sign.SAAuthPlainTextMessage;
import kakao.getCI.com.shinhan.security.simpleauth.message.sign.SARegPlainTextMessage;
import kakao.getCI.com.shinhan.security.simpleauth.message.unreg.SAUnRegClientMessage;
import kakao.getCI.com.shinhan.security.simpleauth.tlv.SAErrsEnum;
import kakao.getCI.com.shinhan.security.simpleauth.tlv.SATagsEnum;
import kakao.getCI.com.shinhan.security.simpleauth.util.SAHashUtil;
import kakao.getCI.com.shinhan.security.simpleauth.util.SAHexUtil;
import kakao.getCI.com.shinhan.security.simpleauth.util.SALogUtil;
import kakao.getCI.com.shinhan.security.simpleauth.util.SAUtil;
import kakao.getCI.com.shinhan.security.callback.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PublicKey;
import java.util.HashMap;

@Component
public class SASimpleAuthTask {
    private static String _SASimpleAuthTask_ = "_SASimpleAuthTask_ :: ";


    public static String reg_init_server(String strReqTag, String reqJson, String cusno, HttpServletRequest request, HttpServletResponse response, SACallbackChallenge callBack) throws SASimpleAuthException {
        SALogUtil.fine("reg_init_server :: start");
        String resultJson = null;
        SARegInitClientMessage objRegInitClientMessage = new SARegInitClientMessage();
        PublicKey tmpPubKeyFromClient = null;
        try {
            SAMessageUtil.parseJSONData(reqJson, objRegInitClientMessage);
            SALogUtil.fine(String.valueOf(_SASimpleAuthTask_) + "reg_init_server client (JSON) :: " + SAMessageUtil.toJSON(objRegInitClientMessage));
            if (isEmpty(cusno))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_SERVER, SAErrorMessage.ERR_MSG_CUSNO_NULL, SAErrorMessage.ERR_CODE_CUSNO_NULL);
            if (isEmpty(objRegInitClientMessage.tag))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_SERVER, SAErrorMessage.ERR_MSG_TAG_NULL, SAErrorMessage.ERR_CODE_TAG_NULL);
            if (isEmpty(objRegInitClientMessage.tmppub))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_SERVER, SAErrorMessage.ERR_MSG_PUBKEY_NULL, SAErrorMessage.ERR_CODE_PUBKEY_NULL);
            tmpPubKeyFromClient = SACryptoUtil.byteToPublicKey(SAHexUtil.hexStrToByteArr(objRegInitClientMessage.tmppub));
            SARegInitServerMessage objRegInitServerMessage = new SARegInitServerMessage();
            objRegInitServerMessage.tag = SAHexUtil.tagToHex(SATagsEnum.TAG_INIT_REG);
            String strChallenge = SACryptoUtil.getChallengeValue512();
            objRegInitServerMessage.challenge = SACryptoUtil.rsaEncrypt(strChallenge, tmpPubKeyFromClient);
            String id = SACryptoUtil.getIDValue(cusno);
            objRegInitServerMessage.id = SACryptoUtil.rsaEncrypt(id, tmpPubKeyFromClient);
            SAEventReg reg = new SAEventReg(callBack);
            reg.doSetChallengeInSession(strChallenge);
            long currentTime = System.currentTimeMillis() / 1000L;
            String strTime = String.valueOf(currentTime);
            objRegInitServerMessage.time = SACryptoUtil.rsaEncrypt(strTime, tmpPubKeyFromClient);
            objRegInitServerMessage.cusno = SACryptoUtil.rsaEncrypt(cusno, tmpPubKeyFromClient);
            objRegInitServerMessage.erroryn = "n";
            resultJson = SAMessageUtil.toJSON(objRegInitServerMessage);
            SALogUtil.fine(String.valueOf(_SASimpleAuthTask_) + " reginit server (JSON) :: " + resultJson);
        } catch (SASimpleAuthException e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_SERVER, e.getMsg(), e.getCode());
        } catch (Exception e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_SERVER, e.toString());
        }
        SALogUtil.fine("reg_init_server :: end");
        return resultJson;
    }

    public static String reg_server(String strReqTag, String reqJson, String cusno, String challenge, HttpServletRequest request, HttpServletResponse response, SACallbackReg callBack) throws SASimpleAuthException, SAInvalidPasswordException {
            return reg_server(strReqTag, reqJson, cusno, challenge, request, response, callBack, null);
    }

    public static String reg_server(String strReqTag, String reqJson, String cusno, String challenge, HttpServletRequest request, HttpServletResponse response, SACallbackReg callBack, SACallbackPasswordCheck saCallbackPasswordCheck) throws SASimpleAuthException, SAInvalidPasswordException {
        SALogUtil.fine("reg_server :: start");
        String resultJson = null;
        boolean isSuccess = false;
        SARegClientMessage objRegClientMessage = new SARegClientMessage();
        try {
            SALogUtil.fine("reg_client_json :: " + reqJson);
            SAMessageUtil.parseJSONData(reqJson, objRegClientMessage);
            if (isEmpty(cusno))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_CUSNO_NULL, SAErrorMessage.ERR_CODE_CUSNO_NULL);
            if (isEmpty(challenge))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_SS_CHALLENGE_NULL, SAErrorMessage.ERR_CODE_SS_CHALLENGE_NULL);
            if (isEmpty(objRegClientMessage.tag))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_TAG_NULL, SAErrorMessage.ERR_CODE_TAG_NULL);
            if (isEmpty(objRegClientMessage.rd))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_RD_NULL, SAErrorMessage.ERR_CODE_RD_NULL);
            if (isEmpty(objRegClientMessage.signdata))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_SIGNDATA_NULL, SAErrorMessage.ERR_CODE_SIGNDATA_NULL);
            String rd = objRegClientMessage.rd;
            SALogUtil.fine("rd :: " + rd);
            String strSessionChallenge = challenge;
            String challenge_kdf_server = SACryptoUtil.kdf512(SAHexUtil.hexStrToByteArr(strSessionChallenge));
            SALogUtil.fine("challenge_kdf_server ::" + challenge_kdf_server);
            String aesKey = challenge_kdf_server.substring(0, 32);
            SALogUtil.fine("aesKey :: " + aesKey);
            String aesIV = challenge_kdf_server.substring(32, 64);
            SALogUtil.fine("aesIV :: " + aesIV);
            String decryptedRd = SACryptoUtil.aesDecrypt(aesKey, aesIV, rd);
            SALogUtil.fine("decryptedRd :: " + decryptedRd);
            String sha256RegiData = SAHexUtil.byteArrToHexString(SAHashUtil.sha256(decryptedRd));
            SALogUtil.fine("sha256RegiData :: " + sha256RegiData);
            SARegPlainTextMessage sign_plain_text_msg = new SARegPlainTextMessage();
            SAMessageUtil.parseJSONData(decryptedRd, sign_plain_text_msg);
            if (isEmpty(sign_plain_text_msg.id))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_ID_NULL, SAErrorMessage.ERR_CODE_ID_NULL);
            if (isEmpty(sign_plain_text_msg.appid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_APPID_NULL, SAErrorMessage.ERR_CODE_APPID_NULL);
            if (isEmpty(sign_plain_text_msg.challenge))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_CHALLENGE_NULL, SAErrorMessage.ERR_CODE_CHALLENGE_NULL);
            if (isEmpty(sign_plain_text_msg.uuid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_UUID_NULL, SAErrorMessage.ERR_CODE_UUID_NULL);
            if (isEmpty(sign_plain_text_msg.time))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_TIME_NULL, SAErrorMessage.ERR_CODE_TIME_NULL);
            if (isEmpty(sign_plain_text_msg.pubkey))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_PUBKEY_NULL, SAErrorMessage.ERR_CODE_PUBKEY_NULL);
            if (!isValidType(sign_plain_text_msg.type))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_TYPE_INVALID, SAErrorMessage.ERR_CODE_TYPE_INVALID);
            PublicKey publicKey = SACryptoUtil.byteToPublicKey(SAHexUtil.hexStrToByteArr(sign_plain_text_msg.pubkey));
            String deSigndata = SACryptoUtil.rsaDecrypt(objRegClientMessage.signdata, publicKey);
            SALogUtil.fine("deSigndata :: " + deSigndata);

            if(saCallbackPasswordCheck!=null)
            {
                SAEventReg reg = new SAEventReg(saCallbackPasswordCheck);
                HashMap<String, String> passwordInfo = SAMessageUtil.getPasswordInfo(decryptedRd);
                boolean isNewer = false;
                if(passwordInfo!=null) {
                    try {
                        String isContain = passwordInfo.get("isContain");

                        if (isContain != null && isContain.equals("true")) {
                            isNewer = true;
                        }

                    } catch (Exception e) {
                        isNewer = false;
                    }

                    SALogUtil.fine("isNewer: " + isNewer);

                    if (isNewer) {
                        String clientPasswd = passwordInfo.get("clientPasswd");
                        if (isEmpty(clientPasswd)) {
                            throw new SAInvalidPasswordException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_PASSWORD_NULL, SAErrorMessage.ERR_CODE_PASSWORD_NULL);
                        }

                        SALogUtil.fine("Password: " + clientPasswd);

                        boolean isValidPassword = reg.doPasswordValidationCheck(clientPasswd, sign_plain_text_msg.type);
                        if (!isValidPassword) {
                            SALogUtil.fine("Password is invalid! : "+clientPasswd);
                            throw new SAInvalidPasswordException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_PASSWORD_INVALID, SAErrorMessage.ERR_CODE_PASSWORD_INVALID);
                        }
                    }
                }else
                {
                    SALogUtil.fine("passwordInfo is null");
                }

            }


            if (isEmpty(deSigndata))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_DESIGNDATA_NULL, SAErrorMessage.ERR_CODE_DESIGNDATA_NULL);
            if (isEmpty(sha256RegiData))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_SHA256REGDATA_NULL, SAErrorMessage.ERR_CODE_SHA256REGDATA_NULL);
            if (!deSigndata.toUpperCase().equals(sha256RegiData.toUpperCase()))
                throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_SIGN_VARIFICATION, SAErrorMessage.ERR_CODE_SIGN_VARIFICATION);
            String challenge_kdf_client = sign_plain_text_msg.challenge;
            if (challenge_kdf_client.toUpperCase().equals(challenge_kdf_server.toUpperCase())) {
                long time = SAUtil.parseLong(sign_plain_text_msg.time);
                boolean isValidTime = isValidTime(time, SAProperty.TIMEOUT);
                if (isValidTime) {
                    SALogUtil.fine("id :: " + sign_plain_text_msg.id);
                    SALogUtil.fine("appid :: " + sign_plain_text_msg.appid);
                    SALogUtil.fine("challenge :: " + sign_plain_text_msg.challenge);
                    SALogUtil.fine("uuid :: " + sign_plain_text_msg.uuid);
                    SALogUtil.fine("pubkey :: " + sign_plain_text_msg.pubkey);
                    SALogUtil.fine("type :: " + sign_plain_text_msg.type);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(SAProperty.COL_NM_ID, sign_plain_text_msg.id);
                    map.put(SAProperty.COL_NM_APPID, sign_plain_text_msg.appid);
                    map.put(SAProperty.COL_NM_UUID, sign_plain_text_msg.uuid);
                    map.put(SAProperty.COL_NM_PUBKEY, sign_plain_text_msg.pubkey);
                    map.put(SAProperty.COL_NM_TYPE, sign_plain_text_msg.type);
                    map.put(SAProperty.COL_NM_STATUS, SAProperty.STATUS_Y);
                    map.put(SAProperty.COL_NM_CUSNO, cusno);
                    SAEventReg reg = new SAEventReg(callBack);
                    isSuccess = reg.doSimpleAuthInfoReg(map, request, response);
                    SAResultMessage objRegServerMessage = new SAResultMessage();
                    if (isSuccess) {
                        objRegServerMessage.tag = SAHexUtil.tagToHex(SATagsEnum.TAG_REG);
                        objRegServerMessage.erroryn = "n";
                        objRegServerMessage.id = sign_plain_text_msg.id;
                        objRegServerMessage.cusno = cusno;
                        objRegServerMessage.appid = sign_plain_text_msg.appid;
                        objRegServerMessage.type = sign_plain_text_msg.type;
                        objRegServerMessage.uuid = sign_plain_text_msg.uuid;
                        objRegServerMessage.status = SAProperty.STATUS_Y;
                        resultJson = SAMessageUtil.toJSON(objRegServerMessage);
                    } else {
                        throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_DB_INSERT, SAErrorMessage.ERR_CODE_DB_INSERT);
                    }
                    SALogUtil.fine(String.valueOf(_SASimpleAuthTask_) + "[reg_server_sign_plaintext](:: " + SAMessageUtil.getFieldInfo(sign_plain_text_msg));
                    SALogUtil.fine(String.valueOf(_SASimpleAuthTask_) + "[reg_server_sign_plaintext](JSON) :: " + SAMessageUtil.toJSON(sign_plain_text_msg));
                    SALogUtil.fine("_SASimpleAuthTask_+[reg_server](:: " + SAMessageUtil.getFieldInfo(objRegServerMessage));
                    SALogUtil.fine(String.valueOf(_SASimpleAuthTask_) + "[reg_server](JSON) :: " + SAMessageUtil.toJSON(objRegServerMessage));
                } else {
                    throw new SASimpleAuthException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_TIME_INVALID, SAErrorMessage.ERR_CODE_TIME_INVALID);
                }
            } else {
                throw new SASimpleAuthException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_CHALLENGE_VARIFICATION, SAErrorMessage.ERR_CODE_CHALLENGE_VARIFICATION);
            }
        } catch (SASimpleAuthException e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, e.getMsg(), e.getCode());
        } catch (Exception e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, e.toString());
        }
        SALogUtil.fine("reg_server :: end");
//        clearListener();
        return resultJson;
    }

    public static String auth_Init_server(String strReqTag, String reqJson, HttpServletRequest request, HttpServletResponse response, SACallbackSearch callBack1, SACallbackChallenge callBack2) throws SASimpleAuthException, SAInvalidPasswordException{
            return auth_Init_server(strReqTag, reqJson, request, response, callBack1, callBack2, null);
    }

    public static String auth_Init_server(String strReqTag, String reqJson, HttpServletRequest request, HttpServletResponse response, SACallbackSearch callBack1, SACallbackChallenge callBack2, SACallbackPasswordCheck saCallbackPasswordCheck) throws SASimpleAuthException, SAInvalidPasswordException {
        SALogUtil.fine("auth_Init_server :: start");
        String resultJson = null;
        SAAuthInitClientMessage objAuthInitClientMessage = new SAAuthInitClientMessage();
        try {
            SAMessageUtil.parseJSONData(reqJson, objAuthInitClientMessage);
            if (isEmpty(objAuthInitClientMessage.tag))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_TAG_NULL, SAErrorMessage.ERR_CODE_TAG_NULL);
            if (isEmpty(objAuthInitClientMessage.uuid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_UUID_NULL, SAErrorMessage.ERR_CODE_UUID_NULL);
            if (isEmpty(objAuthInitClientMessage.id))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_ID_NULL, SAErrorMessage.ERR_CODE_ID_NULL);
            if (isEmpty(objAuthInitClientMessage.appid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_APPID_NULL, SAErrorMessage.ERR_CODE_APPID_NULL);
            if(!isValidType(objAuthInitClientMessage.type)) // type add
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_TYPE_INVALID, SAErrorMessage.ERR_CODE_TYPE_INVALID);

            String strId = objAuthInitClientMessage.id;
            String strAppId = objAuthInitClientMessage.appid;
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(SAProperty.COL_NM_ID, strId);
            map.put(SAProperty.COL_NM_APPID, strAppId);
            map.put(SAProperty.COL_NM_STATUS, SAProperty.STATUS_Y);
            HashMap<String, String> reqMap = new HashMap<String, String>();
            SAEventReg reg1 = new SAEventReg(callBack1);
            reqMap = reg1.doSimpleAuthInfoSearch(map, request, response);
            if (reqMap == null || reqMap.size() < 1)
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_ANOTHER_DEVICE_REG, SAErrorMessage.ERR_CODE_ANOTHER_DEVICE_REG);
            SALogUtil.fine("map result [id search ] :: " + reqMap.toString());
            String id_db = reqMap.get(SAProperty.COL_NM_ID);
            String pubkey_db = reqMap.get(SAProperty.COL_NM_PUBKEY);
            String uuid_db = reqMap.get(SAProperty.COL_NM_UUID);
            String appid_db = reqMap.get(SAProperty.COL_NM_APPID);
            // type add
            String type_db = reqMap.get(SAProperty.COL_NM_TYPE);



            if (isEmpty(id_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_ID_NULL, SAErrorMessage.ERR_CODE_SERVER_ID_NULL);
            if (isEmpty(pubkey_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_PUBKEY_NULL, SAErrorMessage.ERR_CODE_SERVER_PUBKEY_NULL);
            if (isEmpty(uuid_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_UUID_NULL, SAErrorMessage.ERR_CODE_SERVER_UUID_NULL);
            if (isEmpty(appid_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_APPID_NULL, SAErrorMessage.ERR_CODE_SERVER_APPID_NULL);
            if(!isValidType(type_db)) // type add
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_TYPE_INVALID, SAErrorMessage.ERR_CODE_TYPE_INVALID);
            if (!strId.toUpperCase().equals(id_db.toUpperCase()) && !strId.equals(id_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_ID_NOT_MATCH, SAErrorMessage.ERR_CODE_ID_NOT_MATCH);
            PublicKey publicKey = SACryptoUtil.byteToPublicKey(SAHexUtil.hexStrToByteArr(pubkey_db));
            String strClientUuid = SACryptoUtil.rsaDecrypt(objAuthInitClientMessage.uuid, publicKey);
            if (!strClientUuid.equals(uuid_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_UUID_NOT_MATCH, SAErrorMessage.ERR_CODE_UUID_NOT_MATCH);
            if (!strAppId.equals(appid_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_APPID_NOT_MATCH, SAErrorMessage.ERR_CODE_APPID_NOT_MATCH);

            if(saCallbackPasswordCheck!=null)
            {
                HashMap<String, String> passwordInfo = SAMessageUtil.getPasswordInfo(reqJson);
                boolean isNewer = false;

                if(passwordInfo!=null)
                {
                    try {
                        String isContain = passwordInfo.get("isContain");

                        if (isContain != null && isContain.equals("true")) {
                            isNewer = true;
                        }
                    }catch (Exception e)
                    {
                        isNewer = false;
                    }

                    SALogUtil.fine("isNewer: "+isNewer);

                    /**
                     * fingerprint 1
                     * pin 2
                     * pattern 3
                     * face_id 4
                     * auto 5
                     */
                    if(isNewer && (type_db.equals("1") || type_db.equals("4")))
                    {
                        String clientPasswd = passwordInfo.get("clientPasswd");
                        SALogUtil.fine("RSA(Client Sha(id+Sha(uuid)+type)): "+clientPasswd);
                        if(isEmpty(clientPasswd))
                        {
                            throw new SAInvalidPasswordException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_PASSWORD_NULL, SAErrorMessage.ERR_CODE_PASSWORD_NULL);
                        }
                        String rawClientPasswd = SACryptoUtil.rsaDecrypt(clientPasswd, publicKey);

                        SALogUtil.fine("Client Sha(id+Sha(uuid)+type): "+rawClientPasswd);
                        SALogUtil.fine("ClientId: "+objAuthInitClientMessage.id);
                        SALogUtil.fine("ClientType: "+objAuthInitClientMessage.type);
                        SALogUtil.fine("ClientUUID: "+objAuthInitClientMessage.uuid);


                        String sid = id_db.toUpperCase();
                        String stype = type_db.toUpperCase();
                        String suuid = uuid_db.toUpperCase();

                        SALogUtil.fine("DB Id: "+sid);
                        SALogUtil.fine("DB Type: "+stype);
                        SALogUtil.fine("DB UUID: "+suuid);

                        String sha256uuid = SAHexUtil.byteArrToHexString(SAHashUtil.sha256(suuid)).toUpperCase();
                        SALogUtil.fine("DB SHA UUID: "+sha256uuid);
                        String serverPasswd = SAHexUtil.byteArrToHexString(SAHashUtil.sha256(sid+sha256uuid+stype));
                        SALogUtil.fine("Server Sha(id+Sha(uuid)+type): "+serverPasswd);

                        if(!rawClientPasswd.toUpperCase().equals(serverPasswd.toUpperCase()) && !rawClientPasswd.equals(serverPasswd))
                        {
                            SALogUtil.fine("authInit - 비밀번호가 다릅니다.");
                            throw new SAInvalidPasswordException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_PASSWORD_NOT_MATCH, SAErrorMessage.ERR_CODE_PASSWORD_NOT_MATCH);
                        }
                    }

                }else
                {
                    SALogUtil.fine("passwordInfo is null");
                }

            }





            String strChallenge = SACryptoUtil.getChallengeValue512();
            SAAuthInitServerMessage objAuthInitServerMessage = new SAAuthInitServerMessage();
            objAuthInitServerMessage.tag = SAHexUtil.tagToHex(SATagsEnum.TAG_INIT_AUTH);
            objAuthInitServerMessage.challenge = SACryptoUtil.rsaEncrypt(strChallenge, publicKey);
            long currentTime = System.currentTimeMillis() / 1000L;
            String strTime = String.valueOf(currentTime);
            objAuthInitServerMessage.time = SACryptoUtil.rsaEncrypt(strTime, publicKey);
            objAuthInitServerMessage.erroryn = "n";
            resultJson = SAMessageUtil.toJSON(objAuthInitServerMessage);
            SALogUtil.fine(String.valueOf(_SASimpleAuthTask_) + " authinit server (JSON) :: " + resultJson);
            SAEventReg reg2 = new SAEventReg(callBack2);
            reg2.doSetChallengeInSession(strChallenge);
        } catch (SASimpleAuthException e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, e.getMsg(), e.getCode());
        } catch (Exception e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, e.toString());
        }
        SALogUtil.fine("auth_Init_server :: end");
//        clearListener();
        return resultJson;
    }

    public static String auth_server(String strReqTag, String reqJson, String challenge, HttpServletRequest request, HttpServletResponse response, SACallbackSearch callBack) throws SASimpleAuthException {
        SALogUtil.fine("auth_server :: start");
        String resultJson = null;
        try {
            SAAuthClientMessage objAuthClientMessage = new SAAuthClientMessage();
            SAMessageUtil.parseJSONData(reqJson, objAuthClientMessage);
            SALogUtil.fine("objAuthClientMessage :: " + objAuthClientMessage);
            SALogUtil.fine(String.valueOf(_SASimpleAuthTask_) + "auth_client (JSON) :: " + SAMessageUtil.toJSON(objAuthClientMessage));
            if (isEmpty(challenge))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_SS_CHALLENGE_NULL, SAErrorMessage.ERR_CODE_SS_CHALLENGE_NULL);
            if (isEmpty(objAuthClientMessage.tag))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_TAG_NULL, SAErrorMessage.ERR_CODE_TAG_NULL);
            if (isEmpty(objAuthClientMessage.ad))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_AD_NULL, SAErrorMessage.ERR_CODE_AD_NULL);
            if (isEmpty(objAuthClientMessage.signdata))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_SIGNDATA_NULL, SAErrorMessage.ERR_CODE_SIGNDATA_NULL);
            String strEncryptedSigndata = objAuthClientMessage.signdata;
            String ad = objAuthClientMessage.ad;
            String strSessionChallenge = challenge;
            String strSessionChallengeKdf = SACryptoUtil.kdf512(SAHexUtil.hexStrToByteArr(strSessionChallenge));
            String aesKey = strSessionChallengeKdf.substring(0, 32);
            String aesIV = strSessionChallengeKdf.substring(32, 64);
            String authData = SACryptoUtil.aesDecrypt(aesKey, aesIV, ad);
            String sha256RegiData = SAHexUtil.byteArrToHexString(SAHashUtil.sha256(authData));
            SAAuthPlainTextMessage objAuthPlainTextMessage = new SAAuthPlainTextMessage();
            SAMessageUtil.parseJSONData(authData, objAuthPlainTextMessage);
            SALogUtil.fine("appid :: " + objAuthPlainTextMessage.appid);
            SALogUtil.fine("challenge :: " + objAuthPlainTextMessage.challenge);
            SALogUtil.fine("id :: " + objAuthPlainTextMessage.id);
            SALogUtil.fine("time :: " + objAuthPlainTextMessage.time);
            SALogUtil.fine("type :: " + objAuthPlainTextMessage.type);
            SALogUtil.fine("uuid :: " + objAuthPlainTextMessage.uuid);
            if (isEmpty(objAuthPlainTextMessage.id))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_AD_ID_NULL, SAErrorMessage.ERR_CODE_AD_ID_NULL);
            if (isEmpty(objAuthPlainTextMessage.appid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_AD_APPID_NULL, SAErrorMessage.ERR_CODE_APPID_NULL);
            if (isEmpty(objAuthPlainTextMessage.type))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_AD_TYPE_NULL, SAErrorMessage.ERR_CODE_AD_TYPE_NULL);
            if (isEmpty(objAuthPlainTextMessage.challenge))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_AD_CHALLENGE_NULL, SAErrorMessage.ERR_CODE_AD_CHALLENGE_NULL);
            if (isEmpty(objAuthPlainTextMessage.time))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_AD_TIME_NULL, SAErrorMessage.ERR_CODE_AD_TIME_NULL);
            if (isEmpty(objAuthPlainTextMessage.uuid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_UUID_NULL, SAErrorMessage.ERR_CODE_AD_UUID_NULL);
            if (!isValidType(objAuthPlainTextMessage.type))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_TYPE_INVALID, SAErrorMessage.ERR_CODE_TYPE_INVALID);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(SAProperty.COL_NM_ID, objAuthPlainTextMessage.id);
            map.put(SAProperty.COL_NM_APPID, objAuthPlainTextMessage.appid);
            map.put(SAProperty.COL_NM_STATUS, SAProperty.STATUS_Y);
            HashMap<String, String> reqMap = new HashMap<String, String>();
            SAEventReg reg = new SAEventReg(callBack);
            reqMap = reg.doSimpleAuthInfoSearch(map, request, response);
            if (reqMap == null || reqMap.size() < 1)
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_DB_SEARCH, SAErrorMessage.ERR_CODE_DB_SEARCH);
            String pubkey_db = reqMap.get(SAProperty.COL_NM_PUBKEY);
            String uuid_db = reqMap.get(SAProperty.COL_NM_UUID);
            String cusno_db = reqMap.get(SAProperty.COL_NM_CUSNO);
            if (isEmpty(pubkey_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_PUBKEY_NULL, SAErrorMessage.ERR_CODE_SERVER_PUBKEY_NULL);
            if (isEmpty(uuid_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_UUID_NULL, SAErrorMessage.ERR_CODE_SERVER_UUID_NULL);
            if (isEmpty(cusno_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_CUSNO_NULL, SAErrorMessage.ERR_CODE_SERVER_CUSNO_NULL);
            PublicKey publicKey = SACryptoUtil.byteToPublicKey(SAHexUtil.hexStrToByteArr(pubkey_db));
            String strDecryptedSigndata = SACryptoUtil.rsaDecrypt(strEncryptedSigndata, publicKey);
            if (isEmpty(strDecryptedSigndata) || isEmpty(sha256RegiData) || !strDecryptedSigndata.equals(sha256RegiData))
                throw new SASimpleAuthException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_SIGN_VARIFICATION, SAErrorMessage.ERR_CODE_SIGN_VARIFICATION);
            String strChallengeKdfClient = objAuthPlainTextMessage.challenge;
            String strClientUuid = objAuthPlainTextMessage.uuid;
            if (!strChallengeKdfClient.equals(strSessionChallengeKdf))
                throw new SASimpleAuthException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_CHALLENGE_NOT_MATCH, SAErrorMessage.ERR_CODE_CHALLENGE_NOT_MATCH);
            if (!strClientUuid.toUpperCase().equals(uuid_db.toUpperCase()) &&
                    !strClientUuid.equals(uuid_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_UUID_NOT_MATCH, SAErrorMessage.ERR_CODE_UUID_NOT_MATCH);
            long time = SAUtil.parseLong(objAuthPlainTextMessage.time);
            boolean isValidTime = isValidTime(time, SAProperty.TIMEOUT);
            if (!isValidTime)
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_TIME_INVALID, SAErrorMessage.ERR_CODE_TIME_INVALID);
            SAResultMessage objAuthServerMessage = new SAResultMessage();
            objAuthServerMessage.tag = SAHexUtil.tagToHex(SATagsEnum.TAG_AUTH);
            objAuthServerMessage.erroryn = "n";
            objAuthServerMessage.id = objAuthPlainTextMessage.id;
            objAuthServerMessage.cusno = cusno_db;
            objAuthServerMessage.appid = objAuthPlainTextMessage.appid;
            objAuthServerMessage.type = objAuthPlainTextMessage.type;
            objAuthServerMessage.uuid = uuid_db;
            objAuthServerMessage.status = SAProperty.STATUS_Y;
            resultJson = SAMessageUtil.toJSON(objAuthServerMessage);
            SALogUtil.fine(String.valueOf(_SASimpleAuthTask_) + " auth server (JSON) :: " + resultJson);
        } catch (SASimpleAuthException e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, e.getMsg(), e.getCode());
        } catch (Exception e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, e.toString());
        }
        SALogUtil.fine("auth_server :: end");
        return resultJson;
    }

    public static String unreg(String strReqTag, String reqJson, HttpServletRequest request, HttpServletResponse response, SACallbackSearch callBack1, SACallbackUnreg callBack2) throws SASimpleAuthException {
        String resultJson = null;
        boolean isClear = false;
        SAUnRegClientMessage unRegClientMessage = new SAUnRegClientMessage();
        try {
            SAMessageUtil.parseJSONData(reqJson, unRegClientMessage);
            if (isEmpty(unRegClientMessage.tag))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_TAG_NULL, SAErrorMessage.ERR_CODE_TAG_NULL);
            if (isEmpty(unRegClientMessage.id))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_ID_NULL, SAErrorMessage.ERR_CODE_ID_NULL);
            if (isEmpty(unRegClientMessage.appid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_APPID_NULL, SAErrorMessage.ERR_CODE_APPID_NULL);
            String strId = unRegClientMessage.id;
            String strAppId = unRegClientMessage.appid;
            SALogUtil.fine("strId :: " + strId);
            SALogUtil.fine("strAppId :: " + strAppId);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(SAProperty.COL_NM_ID, strId);
            map.put(SAProperty.COL_NM_APPID, strAppId);
            map.put(SAProperty.COL_NM_STATUS, SAProperty.STATUS_Y);
            HashMap<String, String> reqMap = new HashMap<String, String>();
            SAEventReg reg1 = new SAEventReg(callBack1);
            reqMap = reg1.doSimpleAuthInfoSearch(map, request, response);
            if (reqMap == null || reqMap.size() < 1)
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_DB_SEARCH, SAErrorMessage.ERR_CODE_DB_SEARCH);
            String id_server_db = reqMap.get(SAProperty.COL_NM_ID);
            String appid_server_db = reqMap.get(SAProperty.COL_NM_APPID);
            String uuid_server_db = reqMap.get(SAProperty.COL_NM_UUID);
            String cusno_server_db = reqMap.get(SAProperty.COL_NM_CUSNO);
            String type_server_db = reqMap.get(SAProperty.COL_NM_TYPE);
            if ((!isEmpty(strId) || !isEmpty(id_server_db)) &&
                    !strId.toUpperCase().equals(id_server_db.toUpperCase()) &&
                    !strId.equals(id_server_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_ID_NOT_MATCH, SAErrorMessage.ERR_CODE_ID_NOT_MATCH);
            if ((!isEmpty(strAppId) || !isEmpty(appid_server_db)) &&
                    !strAppId.equals(appid_server_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_APPID_NOT_MATCH, SAErrorMessage.ERR_CODE_APPID_NOT_MATCH);
            if (isEmpty(uuid_server_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_SERVER_UUID_NULL, SAErrorMessage.ERR_CODE_SERVER_UUID_NULL);
            if (isEmpty(cusno_server_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_SERVER_CUSNO_NULL, SAErrorMessage.ERR_CODE_SERVER_CUSNO_NULL);
            SAEventReg reg2 = new SAEventReg(callBack2);
            isClear = reg2.doSimpleAuthInfoUnreg(map, request, response);
            SAResultMessage unRegServerMessage = new SAResultMessage();
            if (isClear) {
                unRegServerMessage.tag = SAHexUtil.tagToHex(SATagsEnum.TAG_UNREG);
                unRegServerMessage.erroryn = "n";
                unRegServerMessage.id = id_server_db;
                unRegServerMessage.cusno = cusno_server_db;
                unRegServerMessage.appid = appid_server_db;
                unRegServerMessage.type = type_server_db;
                unRegServerMessage.uuid = uuid_server_db;
                unRegServerMessage.status = SAProperty.STATUS_Y;
                resultJson = SAMessageUtil.toJSON(unRegServerMessage);
            } else {
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_DB_UPDATE, SAErrorMessage.ERR_CODE_DB_UPDATE);
            }
        } catch (SASimpleAuthException e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, e.getMsg(), e.getCode());
        } catch (Exception e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, e.toString());
        }
        SALogUtil.fine("auth_server :: end");
        return resultJson;
    }

    private static boolean isValidTime(long oldtime, int minute) {
        long currentTime = System.currentTimeMillis() / 1000L;
        long diff = currentTime - oldtime;
        long gap = diff / 60L;
        SALogUtil.fine(String.valueOf(_SASimpleAuthTask_) + "isValidTime" + "currentTime=[" + currentTime + "] oldtime=[" + oldtime + "] diff=[" + diff + "] gap=[" + gap + "]");
        if (gap < minute)
            return true;
        return false;
    }

    private static boolean isValidType(String type) {
        if (type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4") || type.equals("5"))
            return true;
        return false;
    }

    private static boolean isEmpty(String str) {
        if (str == null || str.equals(""))
            return true;
        return false;
    }

}
