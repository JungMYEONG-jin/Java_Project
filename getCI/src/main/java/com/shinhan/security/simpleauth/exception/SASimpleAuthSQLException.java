package com.shinhan.security.simpleauth.exception;

import com.shinhan.security.simpleauth.tlv.SAErrsEnum;

public class SASimpleAuthSQLException extends SASimpleAuthException {
    private static final long serialVersionUID = 2385998377804941583L;

    public SASimpleAuthSQLException(String msg) {
        super(msg);
    }

    public SASimpleAuthSQLException(SAErrsEnum errsEnum, String msg) {
        super(errsEnum, msg);
    }

    public SASimpleAuthSQLException(SAErrsEnum errsEnum, String msg, String code) {
        super(errsEnum, msg, code);
    }
}
