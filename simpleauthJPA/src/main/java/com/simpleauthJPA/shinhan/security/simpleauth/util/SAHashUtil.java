package com.simpleauthJPA.shinhan.security.simpleauth.util;

import com.shinhan.security.simpleauth.exception.SASimpleAuthCryptoException;
import com.shinhan.security.simpleauth.message.SAErrorMessage;
import com.shinhan.security.simpleauth.tlv.SAErrsEnum;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SAHashUtil {
    public static String ALG_HASH_1 = "SHA-1";

    public static String ALG_HASH_256 = "SHA-256";

    public static String ALG_HASH_512 = "SHA-512";

    public static byte[] sha256(String base) throws SASimpleAuthCryptoException {
        return sha(base, ALG_HASH_256);
    }

    public static byte[] sha256(byte[] base) throws SASimpleAuthCryptoException {
        byte[] result = null;
        try {
            result = sha(base, ALG_HASH_256);
        } catch (NoSuchAlgorithmException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_MSG_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_CODE_NO_SUCH_ALGORITHM);
        }
        return result;
    }

    public static byte[] sha512(String base) throws SASimpleAuthCryptoException {
        return sha(base, ALG_HASH_512);
    }

    public static byte[] sha512(byte[] base) throws SASimpleAuthCryptoException {
        byte[] result = null;
        try {
            result = sha(base, ALG_HASH_512);
        } catch (NoSuchAlgorithmException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_MSG_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_CODE_NO_SUCH_ALGORITHM);
        }
        return result;
    }

    public static byte[] sha(String base, String alg) throws SASimpleAuthCryptoException {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(alg);
        } catch (NoSuchAlgorithmException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_MSG_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_CODE_NO_SUCH_ALGORITHM);
        }
        byte[] hash = null;
        hash = digest.digest(base.getBytes());
        return hash;
    }

    public static byte[] sha(byte[] base, String alg) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(alg);
        return digest.digest(base);
    }
}
