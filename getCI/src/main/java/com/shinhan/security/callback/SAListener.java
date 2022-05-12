package com.shinhan.security.callback;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

public interface SAListener {
    void onSetChallengeInSession(String paramString, HttpSession paramHttpSession);

    boolean onSimpleAuthInfoReg(HashMap<String, String> paramHashMap, String paramString, HttpSession paramHttpSession);

    HashMap<String, String> onSimpleAuthInfoAuthInitSearch(HashMap<String, String> paramHashMap, String paramString, HttpSession paramHttpSession);

    HashMap<String, String> onSimpleAuthInfoAuthSearch(HashMap<String, String> paramHashMap, String paramString, HttpSession paramHttpSession);

    HashMap<String, String> onSimpleAuthInfoUnregSearch(HashMap<String, String> paramHashMap, String paramString, HttpSession paramHttpSession);

    boolean onSimpleAuthInfoUnreg(HashMap<String, String> paramHashMap, String paramString, HttpSession paramHttpSession);

    Object processInit(String paramString, HttpSession paramHttpSession);

    Object registInit(String paramString1, String paramString2, HttpSession paramHttpSession);

    Object regist(String paramString1, String paramString2, HttpSession paramHttpSession);

    Object authorizeInit(String paramString1, String paramString2, HttpSession paramHttpSession);

    Object authorize(String paramString1, String paramString2, HttpSession paramHttpSession);

    Object unregist(String paramString1, String paramString2, HttpSession paramHttpSession);

    boolean CheckPasswordValidation(String password, HttpSession paramHttpSession);
}
