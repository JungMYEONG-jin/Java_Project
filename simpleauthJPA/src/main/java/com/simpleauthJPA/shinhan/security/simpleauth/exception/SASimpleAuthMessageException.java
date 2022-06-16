package com.simpleauthJPA.shinhan.security.simpleauth.exception;

import com.shinhan.security.simpleauth.tlv.SAErrsEnum;

public class SASimpleAuthMessageException extends SASimpleAuthException {
    private static final long serialVersionUID = 5195896985589391953L;

    public SASimpleAuthMessageException(String msg) {
        super(msg);
    }

    public SASimpleAuthMessageException(SAErrsEnum errsEnum, String msg) {
        super(errsEnum, msg);
    }

    public SASimpleAuthMessageException(SAErrsEnum errsEnum, String msg, String code) {
        super(errsEnum, msg, code);
    }
}
