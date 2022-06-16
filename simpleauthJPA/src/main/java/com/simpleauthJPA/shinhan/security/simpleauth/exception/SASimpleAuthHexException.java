package com.simpleauthJPA.shinhan.security.simpleauth.exception;


import com.simpleauthJPA.shinhan.security.simpleauth.tlv.SAErrsEnum;

public class SASimpleAuthHexException extends SASimpleAuthException {
    private static final long serialVersionUID = -8075905651807136163L;

    public SASimpleAuthHexException(String msg) {
        super(msg);
    }

    public SASimpleAuthHexException(SAErrsEnum errsEnum, String msg) {
        super(errsEnum, msg);
    }

    public SASimpleAuthHexException(SAErrsEnum errsEnum, String msg, String code) {
        super(errsEnum, msg, code);
    }
}
