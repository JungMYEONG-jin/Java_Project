package com.simpleauthJPA.shinhan.security.simpleauth.exception;

import com.shinhan.security.simpleauth.tlv.SAErrsEnum;
import com.shinhan.security.simpleauth.util.SAUtil;

public class SAInvalidPasswordException extends Exception {
    private static final long serialVersionUID = 1L;

    private String errtag = "";

    private String msg = "";

    private String code = "";

    public SAInvalidPasswordException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public SAInvalidPasswordException(SAErrsEnum errsEnum, String msg) {
        super("[" + SAUtil.tagToHex(errsEnum) + "] :: " + msg);
        this.errtag = SAUtil.tagToHex(errsEnum);
        this.msg = msg;
    }

    public SAInvalidPasswordException(SAErrsEnum errsEnum, String msg, String code) {
        super("[" + SAUtil.tagToHex(errsEnum) + "] :: " + msg + "(" + code + ")");
        this.errtag = SAUtil.tagToHex(errsEnum);
        this.msg = msg;
        this.code = code;
    }

    public String getErrtag() {
        return this.errtag;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getCode() {
        return this.code;
    }
}
