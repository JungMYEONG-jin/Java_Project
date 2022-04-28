package com.shinhan.security.simpleauth.exception;

import com.shinhan.security.simpleauth.tlv.SAErrsEnum;
import com.shinhan.security.simpleauth.util.SAUtil;

public class SASimpleAuthException extends Exception {
    private static final long serialVersionUID = 1L;

    private String errtag = "";

    private String msg = "";

    private String code = "";

    public SASimpleAuthException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public SASimpleAuthException(SAErrsEnum errsEnum, String msg) {
        super("[" + SAUtil.tagToHex(errsEnum) + "] :: " + msg);
        this.errtag = SAUtil.tagToHex(errsEnum);
        this.msg = msg;
    }

    public SASimpleAuthException(SAErrsEnum errsEnum, String msg, String code) {
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
