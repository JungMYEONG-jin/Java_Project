package com.simpleauthJPA.shinhan.security.callback;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class SAEventReg {
    private SACallbackReg callbackReg;

    private SACallbackSearch callbackSearch;

    private SACallbackUnreg callbackUnreg;

    private SACallbackChallenge callbackChallenge;

    public SAEventReg(SACallbackReg event) {
        this.callbackReg = event;
    }

    public SAEventReg(SACallbackSearch event) {
        this.callbackSearch = event;
    }

    public SAEventReg(SACallbackUnreg event) {
        this.callbackUnreg = event;
    }

    public SAEventReg(SACallbackChallenge event) {
        this.callbackChallenge = event;
    }

    public boolean doSimpleAuthInfoReg(HashMap<String, String> hashMap, HttpServletRequest request, HttpServletResponse response) {
        return this.callbackReg.cbSimpleAuthInfoReg(hashMap, request, response);
    }

    public HashMap<String, String> doSimpleAuthInfoSearch(HashMap<String, String> hashMap, HttpServletRequest request, HttpServletResponse response) {
        return this.callbackSearch.cbSimpleAuthInfoSearch(hashMap, request, response);
    }

    public boolean doSimpleAuthInfoUnreg(HashMap<String, String> hashMap, HttpServletRequest request, HttpServletResponse response) {
        return this.callbackUnreg.cbSimpleAuthInfoUnreg(hashMap, request, response);
    }

    public void doSetChallengeInSession(String challenge) {
        this.callbackChallenge.cbSetChallengeInSession(challenge);
    }
}
