package kakao.getCI.com.shinhan.security.simpleauth.exception;

import kakao.getCI.com.shinhan.security.simpleauth.tlv.SAErrsEnum;

public class SASimpleAuthCryptoException extends SASimpleAuthException {
    private static final long serialVersionUID = -3866630243333593156L;

    public SASimpleAuthCryptoException(String msg) {
        super(msg);
    }

    public SASimpleAuthCryptoException(SAErrsEnum errsEnum, String msg) {
        super(errsEnum, msg);
    }

    public SASimpleAuthCryptoException(SAErrsEnum errsEnum, String msg, String code) {
        super(errsEnum, msg, code);
    }
}
