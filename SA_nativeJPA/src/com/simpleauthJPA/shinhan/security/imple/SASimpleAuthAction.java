package com.simpleauthJPA.shinhan.security.imple;


import com.simpleauthJPA.entity.User;
import com.simpleauthJPA.entity.UserDto;
import com.simpleauthJPA.repository.UserRepository;
import com.simpleauthJPA.service.UserLogService;
import com.simpleauthJPA.shinhan.SAAuthType;
import com.simpleauthJPA.shinhan.security.listener.SAListener;
import com.simpleauthJPA.shinhan.security.simpleauth.crypto.SACryptoUtil;
import com.simpleauthJPA.shinhan.security.simpleauth.exception.SAInvalidPasswordException;
import com.simpleauthJPA.shinhan.security.simpleauth.exception.SASimpleAuthCryptoException;
import com.simpleauthJPA.shinhan.security.simpleauth.exception.SASimpleAuthException;
import com.simpleauthJPA.shinhan.security.simpleauth.exception.SASimpleAuthMessageException;
import com.simpleauthJPA.shinhan.security.simpleauth.message.SAErrorMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.message.SAMessageUtil;
import com.simpleauthJPA.shinhan.security.simpleauth.message.auth.SAAuthClientMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.message.auth.SAAuthInitClientMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.message.auth.SAAuthInitServerMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.message.reg.SARegClientMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.message.reg.SARegInitClientMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.message.reg.SARegInitServerMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.message.result.SAResultMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.message.sign.SAAuthPlainTextMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.message.sign.SARegPlainTextMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.message.unreg.SAUnRegClientMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.tlv.SAErrsEnum;
import com.simpleauthJPA.shinhan.security.simpleauth.tlv.SATagsEnum;
import com.simpleauthJPA.shinhan.security.simpleauth.util.SAHashUtil;
import com.simpleauthJPA.shinhan.security.simpleauth.util.SAHexUtil;
import com.simpleauthJPA.shinhan.security.simpleauth.util.SAUtil;
import com.simpleauthJPA.shinhan.security.simpleauth.util.SAValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SASimpleAuthAction {
    private static String _SASimpleAuthAction_ = "_SASimpleAuthAction_ :: ";

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserLogService userLogService;

    public SAListener listener;

    public SASimpleAuthAction() {}

    public SASimpleAuthAction(SAListener listener) {
        this.listener = listener;
    }

    public void setListener(SAListener listener) {
        this.listener = listener;
    }

    public boolean isNewerVersion(HashMap<String, String> passwordInfo){
        boolean isNewer = false;
        try{
            String isContain = passwordInfo.get("isContain");

            if(isContain != null && isContain.equals("true"))
            {
                isNewer = true;
            }
            return isNewer;
        }catch (Exception e){
            return isNewer;
        }
    }

    public String reg_init_server(String strReqTag, String reqJson, String cusno, HttpSession session) throws SASimpleAuthException {
        userLogService.fine("================================reg_init_server :: start=======================================");
        String resultJson = null;
        SARegInitClientMessage objRegInitClientMessage = new SARegInitClientMessage();
        PublicKey tmpPubKeyFromClient = null;
        try {
            SAMessageUtil.parseJSONData(reqJson, objRegInitClientMessage);
            userLogService.fine(String.valueOf(_SASimpleAuthAction_) + "reg_init_server client (JSON) :: " + SAMessageUtil.toJSON(objRegInitClientMessage));
            if (SAValidateUtils.isEmpty(cusno))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_SERVER, SAErrorMessage.ERR_MSG_CUSNO_NULL, SAErrorMessage.ERR_CODE_CUSNO_NULL);
            if (SAValidateUtils.isEmpty(objRegInitClientMessage.tag))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_SERVER, SAErrorMessage.ERR_MSG_TAG_NULL, SAErrorMessage.ERR_CODE_TAG_NULL);
            if (SAValidateUtils.isEmpty(objRegInitClientMessage.tmppub))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_SERVER, SAErrorMessage.ERR_MSG_PUBKEY_NULL, SAErrorMessage.ERR_CODE_PUBKEY_NULL);
            tmpPubKeyFromClient = SACryptoUtil.byteToPublicKey(SAHexUtil.hexStrToByteArr(objRegInitClientMessage.tmppub));
            SARegInitServerMessage objRegInitServerMessage = new SARegInitServerMessage();
            objRegInitServerMessage.tag = SAHexUtil.tagToHex(SATagsEnum.TAG_INIT_REG);
            String strChallenge = SACryptoUtil.getChallengeValue512();
            objRegInitServerMessage.challenge = SACryptoUtil.rsaEncrypt(strChallenge, tmpPubKeyFromClient);
            String id = SACryptoUtil.getIDValue(cusno);
            objRegInitServerMessage.id = SACryptoUtil.rsaEncrypt(id, tmpPubKeyFromClient);
            this.listener.onSetChallengeInSession(strChallenge, session);
            long currentTime = System.currentTimeMillis() / 1000L;
            String strTime = String.valueOf(currentTime);
            objRegInitServerMessage.time = SACryptoUtil.rsaEncrypt(strTime, tmpPubKeyFromClient);
            objRegInitServerMessage.cusno = SACryptoUtil.rsaEncrypt(cusno, tmpPubKeyFromClient);
            objRegInitServerMessage.erroryn = "n";
            resultJson = SAMessageUtil.toJSON(objRegInitServerMessage);
            userLogService.fine(String.valueOf(_SASimpleAuthAction_) + " reginit server (JSON) :: " + resultJson);
            userLogService.SALog(cusno, id, strReqTag, reqJson, resultJson, "SUCCESS", "0", null);
        } catch (SASimpleAuthException e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_SERVER, e.getMsg(), e.getCode());
        } catch (Exception e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_SERVER, e.toString());
        }

        userLogService.fine("================================reg_init_server :: end=======================================");
        return resultJson;
    }

    public String reg_server(String strReqTag, String reqJson, String cusno, String challenge, HttpSession session) throws SASimpleAuthException, SAInvalidPasswordException {
        userLogService.fine("================================reg_server :: start=======================================");
        String resultJson = null;
        boolean isSuccess = false;
        SARegClientMessage objRegClientMessage = new SARegClientMessage();
        String rd = "";
        String strSessionChallenge = "";
        String challenge_kdf_server = "";
        String aesKey = "";
        String aesIV = "";
        String decryptedRd = "";
        String sha256RegiData = "";
        SARegPlainTextMessage sign_plain_text_msg = new SARegPlainTextMessage();
        PublicKey publicKey = null;
        String deSigndata = "";

        try {
            userLogService.fine("reg_client_json :: " + reqJson);
            SAMessageUtil.parseJSONData(reqJson, objRegClientMessage);

            if (SAValidateUtils.isEmpty(cusno))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_CUSNO_NULL, SAErrorMessage.ERR_CODE_CUSNO_NULL);
            if (SAValidateUtils.isEmpty(challenge))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_SS_CHALLENGE_NULL, SAErrorMessage.ERR_CODE_SS_CHALLENGE_NULL);
            if (SAValidateUtils.isEmpty(objRegClientMessage.tag))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_TAG_NULL, SAErrorMessage.ERR_CODE_TAG_NULL);
            if (SAValidateUtils.isEmpty(objRegClientMessage.rd))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_RD_NULL, SAErrorMessage.ERR_CODE_RD_NULL);
            if (SAValidateUtils.isEmpty(objRegClientMessage.signdata))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_SIGNDATA_NULL, SAErrorMessage.ERR_CODE_SIGNDATA_NULL);


            rd = objRegClientMessage.rd;
            userLogService.fine("rd :: " + rd);
            strSessionChallenge = challenge;
            challenge_kdf_server = SACryptoUtil.kdf512(SAHexUtil.hexStrToByteArr(strSessionChallenge));
            userLogService.fine("challenge_kdf_server ::" + challenge_kdf_server);
            aesKey = challenge_kdf_server.substring(0, 32);
            userLogService.fine("aesKey :: " + aesKey);
            aesIV = challenge_kdf_server.substring(32, 64);
            userLogService.fine("aesIV :: " + aesIV);
            decryptedRd = SACryptoUtil.aesDecrypt(aesKey, aesIV, rd);
            userLogService.fine("decryptedRd :: " + decryptedRd);
            sha256RegiData = SAHexUtil.byteArrToHexString(SAHashUtil.sha256(decryptedRd));
            userLogService.fine("sha256RegiData :: " + sha256RegiData);
            SAMessageUtil.parseJSONData(decryptedRd, sign_plain_text_msg);


            if (SAValidateUtils.isEmpty(sign_plain_text_msg.id))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_ID_NULL, SAErrorMessage.ERR_CODE_ID_NULL);
            if (SAValidateUtils.isEmpty(sign_plain_text_msg.appid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_APPID_NULL, SAErrorMessage.ERR_CODE_APPID_NULL);
            if (SAValidateUtils.isEmpty(sign_plain_text_msg.challenge))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_CHALLENGE_NULL, SAErrorMessage.ERR_CODE_CHALLENGE_NULL);
            if (SAValidateUtils.isEmpty(sign_plain_text_msg.uuid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_UUID_NULL, SAErrorMessage.ERR_CODE_UUID_NULL);
            if (SAValidateUtils.isEmpty(sign_plain_text_msg.time))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_TIME_NULL, SAErrorMessage.ERR_CODE_TIME_NULL);
            if (SAValidateUtils.isEmpty(sign_plain_text_msg.pubkey))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_PUBKEY_NULL, SAErrorMessage.ERR_CODE_PUBKEY_NULL);
            if (!SAValidateUtils.isValidType(sign_plain_text_msg.type))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_TYPE_INVALID, SAErrorMessage.ERR_CODE_TYPE_INVALID);

            publicKey = SACryptoUtil.byteToPublicKey(SAHexUtil.hexStrToByteArr(sign_plain_text_msg.pubkey));
            deSigndata = SACryptoUtil.rsaDecrypt(objRegClientMessage.signdata, publicKey);
            userLogService.fine("deSigndata :: " + deSigndata);
        }catch (SASimpleAuthException e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, e.getMsg(), e.getCode());
        }catch (Exception e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, e.toString());
        }


        try {
            /**
             * 21111008
             * check clientPassword
             * keys isContain clientPasswd
             */
            HashMap<String, String> passwordInfo = SAMessageUtil.getPasswordInfo(decryptedRd);
            boolean isNewer = false;
            if (passwordInfo != null) {
                isNewer = isNewerVersion(passwordInfo);

                if (isNewer) {
                    String clientPasswd = passwordInfo.get("clientPasswd");
                    if (SAValidateUtils.isEmpty(clientPasswd)) {
                        throw new SAInvalidPasswordException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_PASSWORD_NULL, SAErrorMessage.ERR_CODE_PASSWORD_NULL);
                    }

                    if (this.listener != null) {
                        boolean isValidPassword = this.listener.CheckPasswordValidation(clientPasswd, sign_plain_text_msg.type, session);

                        if (!isValidPassword) {
                            throw new SAInvalidPasswordException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_PASSWORD_INVALID, SAErrorMessage.ERR_CODE_PASSWORD_INVALID);
                        }
                    }

                }

            } else {

                throw new SAInvalidPasswordException("passwordInfo is null...");
            }
        }catch (SAInvalidPasswordException e) {
            throw new SAInvalidPasswordException(SAErrsEnum.ERR_REG_SERVER, e.getMsg(), e.getCode());
        }catch(Exception e) {
            throw new SAInvalidPasswordException(SAErrsEnum.ERR_REG_SERVER, e.toString());
        }


        try{

            if (SAValidateUtils.isEmpty(deSigndata))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_DESIGNDATA_NULL, SAErrorMessage.ERR_CODE_DESIGNDATA_NULL);
            if (SAValidateUtils.isEmpty(sha256RegiData))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_SHA256REGDATA_NULL, SAErrorMessage.ERR_CODE_SHA256REGDATA_NULL);
            if (!deSigndata.toUpperCase().equals(sha256RegiData.toUpperCase()))
                throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_SIGN_VARIFICATION, SAErrorMessage.ERR_CODE_SIGN_VARIFICATION);
            String challenge_kdf_client = sign_plain_text_msg.challenge;
            if (challenge_kdf_client.toUpperCase().equals(challenge_kdf_server.toUpperCase())) {
                long time = SAUtil.parseLong(sign_plain_text_msg.time);
                boolean isValidTime = SAValidateUtils.isValidTime(time, SAProperty.TIMEOUT);

                if (isValidTime) {
                    userLogService.fine("id :: " + sign_plain_text_msg.id);
                    userLogService.fine("appid :: " + sign_plain_text_msg.appid);
                    userLogService.fine("challenge :: " + sign_plain_text_msg.challenge);
                    userLogService.fine("uuid :: " + sign_plain_text_msg.uuid);
                    userLogService.fine("pubkey :: " + sign_plain_text_msg.pubkey);
                    userLogService.fine("type :: " + sign_plain_text_msg.type);


                    try{
                        userRepository.save(new User(sign_plain_text_msg.id, sign_plain_text_msg.appid, cusno, sign_plain_text_msg.uuid, sign_plain_text_msg.type, SAProperty.STATUS_Y, sign_plain_text_msg.pubkey, SAMessageUtil.getDate(new Date()), "999999999", null));
                        isSuccess = true;
                    }catch (Exception e){
                        throw new SASimpleAuthException(SAErrsEnum.ERR_REG_SERVER, SAErrorMessage.ERR_MSG_DB_INSERT, SAErrorMessage.ERR_CODE_DB_INSERT);
                    }

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
                    userLogService.fine(String.valueOf(_SASimpleAuthAction_) + "[reg_server_sign_plaintext](:: " + SAMessageUtil.getFieldInfo(sign_plain_text_msg));
                    userLogService.fine(String.valueOf(_SASimpleAuthAction_) + "[reg_server_sign_plaintext](JSON) :: " + SAMessageUtil.toJSON(sign_plain_text_msg));
                    userLogService.fine("_SASimpleAuthTask_+[reg_server](:: " + SAMessageUtil.getFieldInfo(objRegServerMessage));
                    userLogService.fine(String.valueOf(_SASimpleAuthAction_) + "[reg_server](JSON) :: " + SAMessageUtil.toJSON(objRegServerMessage));
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

        userLogService.fine("================================reg_server :: end=======================================");
        return resultJson;
    }

    public String auth_Init_server(String strReqTag, String reqJson, HttpSession session) throws SASimpleAuthException, SAInvalidPasswordException {
        userLogService.fine("================================auth_init_server :: start=======================================");
        String resultJson = null;
        SAAuthInitClientMessage objAuthInitClientMessage = new SAAuthInitClientMessage();
        String strId = "";
        String strAppId = "";
        String id_db = "";
        String pubkey_db = "";
        String uuid_db = "";
        String appid_db = "";
        // type add
        String type_db = "";
        PublicKey publicKey = null;
        String strClientUuid = "";

        try{
            SAMessageUtil.parseJSONData(reqJson, objAuthInitClientMessage);
            if (SAValidateUtils.isEmpty(objAuthInitClientMessage.tag))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_TAG_NULL, SAErrorMessage.ERR_CODE_TAG_NULL);
            if (SAValidateUtils.isEmpty(objAuthInitClientMessage.uuid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_UUID_NULL, SAErrorMessage.ERR_CODE_UUID_NULL);
            if (SAValidateUtils.isEmpty(objAuthInitClientMessage.id))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_ID_NULL, SAErrorMessage.ERR_CODE_ID_NULL);
            if (SAValidateUtils.isEmpty(objAuthInitClientMessage.appid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_APPID_NULL, SAErrorMessage.ERR_CODE_APPID_NULL);
            // type add
            if(!SAValidateUtils.isValidType(objAuthInitClientMessage.type))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_TYPE_INVALID, SAErrorMessage.ERR_CODE_TYPE_INVALID);
            strId = objAuthInitClientMessage.id;
            strAppId = objAuthInitClientMessage.appid;

            List<User> saAuthInitInfo = userRepository.getSAUserInfo(strId);
            if (saAuthInitInfo == null || saAuthInitInfo.isEmpty())
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_ANOTHER_DEVICE_REG, SAErrorMessage.ERR_CODE_ANOTHER_DEVICE_REG);

            User user = saAuthInitInfo.get(0);
            UserDto userDto = new UserDto(user.getId(), user.getPubkey(), user.getUuid(), user.getAppid(), user.getType());

            userLogService.fine("Result [id search ] :: " + userDto.toString());

            id_db = userDto.getId();
            pubkey_db = userDto.getPubkey();
            uuid_db = userDto.getUuid();
            appid_db = userDto.getAppid();
            type_db = userDto.getType();

            if (SAValidateUtils.isEmpty(id_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_ID_NULL, SAErrorMessage.ERR_CODE_SERVER_ID_NULL);
            if (SAValidateUtils.isEmpty(pubkey_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_PUBKEY_NULL, SAErrorMessage.ERR_CODE_SERVER_PUBKEY_NULL);
            if (SAValidateUtils.isEmpty(uuid_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_UUID_NULL, SAErrorMessage.ERR_CODE_SERVER_UUID_NULL);
            if (SAValidateUtils.isEmpty(appid_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_APPID_NULL, SAErrorMessage.ERR_CODE_SERVER_APPID_NULL);
            // type add
            if(!SAValidateUtils.isValidType(type_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_TYPE_INVALID, SAErrorMessage.ERR_CODE_TYPE_INVALID);

            if (!strId.toUpperCase().equals(id_db.toUpperCase()) &&
                    !strId.equals(id_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_ID_NOT_MATCH, SAErrorMessage.ERR_CODE_ID_NOT_MATCH);
            publicKey = SACryptoUtil.byteToPublicKey(SAHexUtil.hexStrToByteArr(pubkey_db));
            strClientUuid = SACryptoUtil.rsaDecrypt(objAuthInitClientMessage.uuid, publicKey);

            if (!strClientUuid.equals(uuid_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_UUID_NOT_MATCH, SAErrorMessage.ERR_CODE_UUID_NOT_MATCH);
            if (!strAppId.equals(appid_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_APPID_NOT_MATCH, SAErrorMessage.ERR_CODE_APPID_NOT_MATCH);

        }catch (SASimpleAuthException e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, e.getMsg(), e.getCode());
        }catch (Exception e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, e.toString());
        }


        try {

            ////////////////////// password
            HashMap<String, String> passwordInfo = SAMessageUtil.getPasswordInfo(reqJson);
            boolean isNewer = false;

            if(passwordInfo!=null)
            {
                isNewer = isNewerVersion(passwordInfo);
                userLogService.fine("isNewer: "+isNewer);
                /**
                 * finger print 1
                 * pin 2
                 * pattern 3
                 * face_id 4
                 * auto 5
                 */
                if(isNewer && (type_db.equals(SAAuthType.FINGERPRINT) || type_db.equals(SAAuthType.FACEID))) // when the version is new and bio information(fingerprint face_id)
                {
                    String clientPasswd = passwordInfo.get("clientPasswd");
                    userLogService.fine("RSA(Client Sha(id+Sha(uuid)+type)): "+clientPasswd);

                    if(SAValidateUtils.isEmpty(clientPasswd)) {
                        throw new SAInvalidPasswordException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_PASSWORD_NULL, SAErrorMessage.ERR_CODE_PASSWORD_NULL);
                    }

                    String rawClientPasswd = null;

                    try {
                        rawClientPasswd = SACryptoUtil.rsaDecrypt(clientPasswd, publicKey);
                    }catch (SASimpleAuthCryptoException e) {
                        throw new SASimpleAuthCryptoException("Client Password RSA Decrypt Failed");
                    }

                    userLogService.fine("Client Sha(id+Sha(uuid)+type): "+rawClientPasswd);
                    userLogService.fine("ClientId: "+objAuthInitClientMessage.id);
                    userLogService.fine("ClientType: "+objAuthInitClientMessage.type);
                    userLogService.fine("ClientUUID: "+strClientUuid);

                    String sid = id_db.toUpperCase();
                    String stype = type_db.toUpperCase();
                    String suuid = uuid_db.toUpperCase();

                    userLogService.fine("DB Id: "+sid);
                    userLogService.fine("DB Type: "+stype);
                    userLogService.fine("DB UUID: "+suuid);
                    String sha256uuid = SAHexUtil.byteArrToHexString(SAHashUtil.sha256(suuid)).toUpperCase();
                    String serverPasswd = SAHexUtil.byteArrToHexString(SAHashUtil.sha256(sid+sha256uuid+stype));

                    userLogService.fine("Server Sha(id+Sha(uuid)+type): "+serverPasswd);

                    if(!rawClientPasswd.toUpperCase().equals(serverPasswd.toUpperCase()) && !rawClientPasswd.equals(serverPasswd)) {
                        throw new SAInvalidPasswordException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_PASSWORD_NOT_MATCH, SAErrorMessage.ERR_CODE_PASSWORD_NOT_MATCH);
                    }

                }
            }else
            {
                userLogService.fine("passwordInfo is null");
            }
        }catch (SAInvalidPasswordException e) {
            throw new SAInvalidPasswordException(SAErrsEnum.ERR_REG_SERVER, e.getMsg(), e.getCode());
        }catch (Exception e) {
            throw new SAInvalidPasswordException(SAErrsEnum.ERR_REG_SERVER, e.toString());
        }


        try{

            String strChallenge = SACryptoUtil.getChallengeValue512();
            SAAuthInitServerMessage objAuthInitServerMessage = new SAAuthInitServerMessage();
            objAuthInitServerMessage.tag = SAHexUtil.tagToHex(SATagsEnum.TAG_INIT_AUTH);
            objAuthInitServerMessage.challenge = SACryptoUtil.rsaEncrypt(strChallenge, publicKey);
//            long currentTime = 1630642197023L;
            long currentTime = System.currentTimeMillis() / 1000L;
            String strTime = String.valueOf(currentTime);
            objAuthInitServerMessage.time = SACryptoUtil.rsaEncrypt(strTime, publicKey);
            objAuthInitServerMessage.erroryn = "n";
            resultJson = SAMessageUtil.toJSON(objAuthInitServerMessage);
            userLogService.fine(String.valueOf(_SASimpleAuthAction_) + " authinit server (JSON) :: " + resultJson);
            this.listener.onSetChallengeInSession(strChallenge, session);
        } catch (SASimpleAuthException e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, e.getMsg(), e.getCode());
        } catch (Exception e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, e.toString());
        }

        userLogService.fine("================================auth_init_server :: end=======================================");
        return resultJson;
    }

    public String auth_server(String strReqTag, String reqJson, String challenge, HttpSession session) throws SASimpleAuthException {
        userLogService.fine("================================auth_server :: start=======================================");
        String resultJson = null;
        try {
            SAAuthClientMessage objAuthClientMessage = new SAAuthClientMessage();
            SAMessageUtil.parseJSONData(reqJson, objAuthClientMessage);
            userLogService.fine("objAuthClientMessage :: " + objAuthClientMessage);
            userLogService.fine(String.valueOf(_SASimpleAuthAction_) + "auth_client (JSON) :: " + SAMessageUtil.toJSON(objAuthClientMessage));


            if (SAValidateUtils.isEmpty(challenge))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_SS_CHALLENGE_NULL, SAErrorMessage.ERR_CODE_SS_CHALLENGE_NULL);
            if (SAValidateUtils.isEmpty(objAuthClientMessage.tag))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_TAG_NULL, SAErrorMessage.ERR_CODE_TAG_NULL);
            if (SAValidateUtils.isEmpty(objAuthClientMessage.ad))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_AD_NULL, SAErrorMessage.ERR_CODE_AD_NULL);
            if (SAValidateUtils.isEmpty(objAuthClientMessage.signdata))
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

            userLogService.fine("appid :: " + objAuthPlainTextMessage.appid);
            userLogService.fine("challenge :: " + objAuthPlainTextMessage.challenge);
            userLogService.fine("id :: " + objAuthPlainTextMessage.id);
            userLogService.fine("time :: " + objAuthPlainTextMessage.time);
            userLogService.fine("type :: " + objAuthPlainTextMessage.type);
            userLogService.fine("uuid :: " + objAuthPlainTextMessage.uuid);



            if (SAValidateUtils.isEmpty(objAuthPlainTextMessage.id))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_AD_ID_NULL, SAErrorMessage.ERR_CODE_AD_ID_NULL);
            if (SAValidateUtils.isEmpty(objAuthPlainTextMessage.appid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_AD_APPID_NULL, SAErrorMessage.ERR_CODE_APPID_NULL);
            if (SAValidateUtils.isEmpty(objAuthPlainTextMessage.type))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_AD_TYPE_NULL, SAErrorMessage.ERR_CODE_AD_TYPE_NULL);
            if (SAValidateUtils.isEmpty(objAuthPlainTextMessage.challenge))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_AD_CHALLENGE_NULL, SAErrorMessage.ERR_CODE_AD_CHALLENGE_NULL);
            if (SAValidateUtils.isEmpty(objAuthPlainTextMessage.time))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_AD_TIME_NULL, SAErrorMessage.ERR_CODE_AD_TIME_NULL);
            if (SAValidateUtils.isEmpty(objAuthPlainTextMessage.uuid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_UUID_NULL, SAErrorMessage.ERR_CODE_AD_UUID_NULL);
            if (!SAValidateUtils.isValidType(objAuthPlainTextMessage.type))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_TYPE_INVALID, SAErrorMessage.ERR_CODE_TYPE_INVALID);



            List<User> users = userRepository.getSAUserInfo(objAuthPlainTextMessage.id);

            if (users == null || users.size() < 1)
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_DB_SEARCH, SAErrorMessage.ERR_CODE_DB_SEARCH);

            User user = users.get(0);
            String pubkey_db = user.getPubkey();
            String uuid_db = user.getUuid();
            String cusno_db = user.getCusno();

            if (SAValidateUtils.isEmpty(pubkey_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_PUBKEY_NULL, SAErrorMessage.ERR_CODE_SERVER_PUBKEY_NULL);
            if (SAValidateUtils.isEmpty(uuid_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_UUID_NULL, SAErrorMessage.ERR_CODE_SERVER_UUID_NULL);
            if (SAValidateUtils.isEmpty(cusno_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_SERVER_CUSNO_NULL, SAErrorMessage.ERR_CODE_SERVER_CUSNO_NULL);
            PublicKey publicKey = SACryptoUtil.byteToPublicKey(SAHexUtil.hexStrToByteArr(pubkey_db));
            String strDecryptedSigndata = SACryptoUtil.rsaDecrypt(strEncryptedSigndata, publicKey);
            if (SAValidateUtils.isEmpty(strDecryptedSigndata) || SAValidateUtils.isEmpty(sha256RegiData) || !strDecryptedSigndata.equals(sha256RegiData))
                throw new SASimpleAuthException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_SIGN_VARIFICATION, SAErrorMessage.ERR_CODE_SIGN_VARIFICATION);
            String strChallengeKdfClient = objAuthPlainTextMessage.challenge;
            String strClientUuid = objAuthPlainTextMessage.uuid;
            if (!strChallengeKdfClient.equals(strSessionChallengeKdf))
                throw new SASimpleAuthException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_CHALLENGE_NOT_MATCH, SAErrorMessage.ERR_CODE_CHALLENGE_NOT_MATCH);
            if (!strClientUuid.toUpperCase().equals(uuid_db.toUpperCase()) &&
                    !strClientUuid.equals(uuid_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, SAErrorMessage.ERR_MSG_UUID_NOT_MATCH, SAErrorMessage.ERR_CODE_UUID_NOT_MATCH);
            long time = SAUtil.parseLong(objAuthPlainTextMessage.time);
            boolean isValidTime = SAValidateUtils.isValidTime(time, SAProperty.TIMEOUT);
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
            userLogService.fine(String.valueOf(_SASimpleAuthAction_) + " auth server (JSON) :: " + resultJson);
        } catch (SASimpleAuthException e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, e.getMsg(), e.getCode());
        } catch (Exception e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_AUTH_SERVER, e.toString());
        }
        userLogService.fine("================================auth_server :: end=======================================");
        return resultJson;
    }

    public String unreg(String strReqTag, String reqJson, HttpSession session) throws SASimpleAuthException {
        String resultJson = null;
        boolean isClear = false;
        SAUnRegClientMessage unRegClientMessage = new SAUnRegClientMessage();
        try {
            SAMessageUtil.parseJSONData(reqJson, unRegClientMessage);
            if (SAValidateUtils.isEmpty(unRegClientMessage.tag))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_TAG_NULL, SAErrorMessage.ERR_CODE_TAG_NULL);
            if (SAValidateUtils.isEmpty(unRegClientMessage.id))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_ID_NULL, SAErrorMessage.ERR_CODE_ID_NULL);
            if (SAValidateUtils.isEmpty(unRegClientMessage.appid))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_APPID_NULL, SAErrorMessage.ERR_CODE_APPID_NULL);
            String strId = unRegClientMessage.id;
            String strAppId = unRegClientMessage.appid;
            userLogService.fine("strId :: " + strId);
            userLogService.fine("strAppId :: " + strAppId);

            List<User> users = userRepository.getSAUserInfo(strId);
            User user = users.get(0);

            if (users == null || users.size() < 1)
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_DB_SEARCH, SAErrorMessage.ERR_CODE_DB_SEARCH);
            String id_server_db = user.getId();
            String appid_server_db = user.getAppid();
            String uuid_server_db = user.getUuid();
            String cusno_server_db = user.getCusno();
            String type_server_db = user.getType();
            if ((!SAValidateUtils.isEmpty(strId) || !SAValidateUtils.isEmpty(id_server_db)) &&
                    !strId.toUpperCase().equals(id_server_db.toUpperCase()) &&
                    !strId.equals(id_server_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_ID_NOT_MATCH, SAErrorMessage.ERR_CODE_ID_NOT_MATCH);
            if ((!SAValidateUtils.isEmpty(strAppId) || !SAValidateUtils.isEmpty(appid_server_db)) &&
                    !strAppId.equals(appid_server_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_APPID_NOT_MATCH, SAErrorMessage.ERR_CODE_APPID_NOT_MATCH);
            if (SAValidateUtils.isEmpty(uuid_server_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_SERVER_UUID_NULL, SAErrorMessage.ERR_CODE_SERVER_UUID_NULL);
            if (SAValidateUtils.isEmpty(cusno_server_db))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_SERVER_CUSNO_NULL, SAErrorMessage.ERR_CODE_SERVER_CUSNO_NULL);

            try{
                User findUser = userRepository.findByIdAndUnregdateEquals(strId, "999999999");
                // save 시 update와 동일함
                findUser.setUnregdate(new Date().toString());
                findUser.setStatus(SAProperty.STATUS_N);
                userRepository.save(findUser); // update
                isClear = true;
            }catch (Exception e){
                throw new SASimpleAuthException(SAErrsEnum.ERR_UNREG_SERVER, SAErrorMessage.ERR_MSG_JDBC_EXCEPTION_UNREG_S, SAErrorMessage.ERR_CODE_JDBC_EXCEPTION_UNREG_S);
            }


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
        return resultJson;
    }
}
