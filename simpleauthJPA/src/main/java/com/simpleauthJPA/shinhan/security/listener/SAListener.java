package com.simpleauthJPA.shinhan.security.listener;

import com.simpleauthJPA.entity.User;
import com.simpleauthJPA.shinhan.security.simpleauth.exception.SASimpleAuthException;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

public interface SAListener {
    void onSetChallengeInSession(String paramString, HttpSession paramHttpSession);

    Object onSimpleAuthUserSearch(Object user) throws SASimpleAuthException;

    boolean onSimpleAuthInfoReg(Object user) throws SASimpleAuthException;

    Object processInit(String paramString, HttpSession paramHttpSession);

    Object registInit(String paramString1, String paramString2, HttpSession paramHttpSession);

    Object regist(String paramString1, String paramString2, HttpSession paramHttpSession);

    Object authorizeInit(String paramString1, String paramString2, HttpSession paramHttpSession);

    Object authorize(String paramString1, String paramString2, HttpSession paramHttpSession);

    Object unregist(String paramString1, String paramString2, HttpSession paramHttpSession);

    boolean CheckPasswordValidation(String password, String authType, HttpSession paramHttpSession);
}
