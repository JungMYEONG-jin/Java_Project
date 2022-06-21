package kakao.getCI.com.shinhan.security.simpleauth.exception;

import kakao.getCI.com.shinhan.security.simpleauth.tlv.SAErrsEnum;

public class SASimpleAuthCryptoKeyException extends SASimpleAuthException {
    private static final long serialVersionUID = -3405613736967618112L;

    public SASimpleAuthCryptoKeyException(String msg) {
        super(msg);
    }

    public SASimpleAuthCryptoKeyException(SAErrsEnum errsEnum, String msg) {
        super(errsEnum, msg);
    }

    public SASimpleAuthCryptoKeyException(SAErrsEnum errsEnum, String msg, String code) {
        super(errsEnum, msg, code);
    }
}
