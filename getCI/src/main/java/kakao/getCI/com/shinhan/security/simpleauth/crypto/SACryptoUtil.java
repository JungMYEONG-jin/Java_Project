package kakao.getCI.com.shinhan.security.simpleauth.crypto;

import kakao.getCI.com.shinhan.security.simpleauth.exception.SASimpleAuthCryptoException;
import kakao.getCI.com.shinhan.security.simpleauth.exception.SASimpleAuthCryptoKeyException;
import kakao.getCI.com.shinhan.security.simpleauth.exception.SASimpleAuthHexException;
import kakao.getCI.com.shinhan.security.simpleauth.message.SAErrorMessage;
import kakao.getCI.com.shinhan.security.simpleauth.tlv.SAErrsEnum;
import kakao.getCI.com.shinhan.security.simpleauth.util.SAHashUtil;
import kakao.getCI.com.shinhan.security.simpleauth.util.SAHexUtil;
import kakao.getCI.com.shinhan.security.simpleauth.util.SAUtil;
import kakao.getCI.com.shinhan.util.DSRandomUtil;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SACryptoUtil {
    public static String rsaEncrypt(String plainText, Key key) throws SASimpleAuthCryptoException {
        byte[] encryptedBytes = null;
        Cipher c = null;
        try {
            c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_MSG_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_CODE_NO_SUCH_ALGORITHM);
        } catch (NoSuchPaddingException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_NO_SUCH_PADDING, SAErrorMessage.ERR_MSG_NO_SUCH_PADDING, SAErrorMessage.ERR_CODE_NO_SUCH_PADDING);
        }
        try {
            c.init(1, key);
        } catch (InvalidKeyException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_INVALID_KEY, SAErrorMessage.ERR_MSG_INVALID_KEY, SAErrorMessage.ERR_CODE_INVALID_KEY);
        }
        try {
            encryptedBytes = c.doFinal(plainText.getBytes());
        } catch (IllegalBlockSizeException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_ILLEGAL_BLOCK_SIZE, SAErrorMessage.ERR_MSG_ILLEGAL_BLOCK_SIZE, SAErrorMessage.ERR_CODE_ILLEGAL_BLOCK_SIZE);
        } catch (BadPaddingException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_BAD_PADDING, SAErrorMessage.ERR_MSG_BAD_PADDING, SAErrorMessage.ERR_CODE_BAD_PADDING);
        }
        String output = SAHexUtil.byteArrToHexString(encryptedBytes);
        SAUtil.release(encryptedBytes);
        return output;
    }

    public static String rsaDecrypt(String encHexData, Key key) throws SASimpleAuthCryptoException, SASimpleAuthHexException {
        byte[] decodedBytes = null;
        Cipher c = null;
        try {
            c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_MSG_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_CODE_NO_SUCH_ALGORITHM);
        } catch (NoSuchPaddingException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_NO_SUCH_PADDING, SAErrorMessage.ERR_MSG_NO_SUCH_PADDING, SAErrorMessage.ERR_CODE_NO_SUCH_PADDING);
        }
        try {
            c.init(2, key);
        } catch (InvalidKeyException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_INVALID_KEY, SAErrorMessage.ERR_MSG_INVALID_KEY, SAErrorMessage.ERR_CODE_INVALID_KEY);
        }
        byte[] dd = SAHexUtil.hexStrToByteArr(encHexData);
        try {
            decodedBytes = c.doFinal(dd);
        } catch (IllegalBlockSizeException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_ILLEGAL_BLOCK_SIZE, SAErrorMessage.ERR_MSG_ILLEGAL_BLOCK_SIZE, SAErrorMessage.ERR_CODE_ILLEGAL_BLOCK_SIZE);
        } catch (BadPaddingException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_BAD_PADDING, SAErrorMessage.ERR_MSG_BAD_PADDING, SAErrorMessage.ERR_CODE_BAD_PADDING);
        }
        String result = new String(decodedBytes);
        SAUtil.release(decodedBytes);
        return result;
    }

    public static PrivateKey byteToPrivateKey(byte[] bytePrivateKey) throws SASimpleAuthCryptoKeyException {
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new SASimpleAuthCryptoKeyException(SAErrsEnum.ERR_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_MSG_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_CODE_NO_SUCH_ALGORITHM);
        }
        PrivateKey privateKey = null;
        try {
            privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytePrivateKey));
        } catch (InvalidKeySpecException e) {
            throw new SASimpleAuthCryptoKeyException(SAErrsEnum.ERR_INVALID_KEY_SPEC, SAErrorMessage.ERR_MSG_INVALID_KEY_SPEC, SAErrorMessage.ERR_CODE_INVALID_KEY_SPEC);
        }
        SAUtil.release(bytePrivateKey);
        return privateKey;
    }

    public static PublicKey byteToPublicKey(byte[] bytePublicKey) throws SASimpleAuthCryptoKeyException {
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new SASimpleAuthCryptoKeyException(SAErrsEnum.ERR_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_MSG_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_CODE_NO_SUCH_ALGORITHM);
        }
        PublicKey publicKey = null;
        try {
            publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(bytePublicKey));
        } catch (InvalidKeySpecException e) {
            throw new SASimpleAuthCryptoKeyException(SAErrsEnum.ERR_INVALID_KEY_SPEC, SAErrorMessage.ERR_MSG_INVALID_KEY_SPEC, SAErrorMessage.ERR_CODE_INVALID_KEY_SPEC);
        }
        SAUtil.release(bytePublicKey);
        return publicKey;
    }

    public static String kdf256(byte[] data) throws SASimpleAuthCryptoException {
        if (data == null)
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_DATA_IS_NULL, SAErrorMessage.ERR_MSG_DATA_IS_NULL, SAErrorMessage.ERR_CODE_DATA_IS_NULL);
        byte[] tmp = new byte[32];
        byte[] salt = new byte[32];
        byte[] digest = new byte[32];
        salt = SAHashUtil.sha256(data);
        digest = SAHashUtil.sha256(salt);
        for (int i = 0; i < 1023; i++) {
            for (int j = 0; j < salt.length; j++)
                tmp[j] = (byte)(salt[j] ^ digest[j]);
            salt = tmp;
            digest = SAHashUtil.sha256(salt);
        }
        String output = SAHexUtil.byteArrToHexString(digest);
        return output;
    }

    public static String kdf512(byte[] data) throws SASimpleAuthCryptoException {
        if (data == null)
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_DATA_IS_NULL, SAErrorMessage.ERR_MSG_DATA_IS_NULL, SAErrorMessage.ERR_CODE_DATA_IS_NULL);
        byte[] tmp = new byte[64];
        byte[] salt = new byte[64];
        byte[] digest = new byte[64];
        salt = SAHashUtil.sha512(data);
        digest = SAHashUtil.sha512(salt);
        for (int i = 0; i < 1023; i++) {
            for (int j = 0; j < salt.length; j++)
                tmp[j] = (byte)(salt[j] ^ digest[j]);
            salt = tmp;
            digest = SAHashUtil.sha512(salt);
        }
        String output = SAHexUtil.byteArrToHexString(digest);
        return output;
    }

    public static String getChallengeValue512() throws SASimpleAuthCryptoException {
        byte[] tmp = new byte[64];
        byte[] salt = new byte[64];
        byte[] digest = new byte[64];
//        SecureRandom secureRandom = null;
        MessageDigest messageDigest = null;
//        secureRandom = new SecureRandom();
        try {
            messageDigest = MessageDigest.getInstance("sha-512");
        } catch (NoSuchAlgorithmException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_MSG_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_CODE_NO_SUCH_ALGORITHM);
        }
//        messageDigest.update(secureRandom.generateSeed(64));
        messageDigest.update(new DSRandomUtil().generateSeed(64));
        salt = messageDigest.digest();
        digest = messageDigest.digest(salt);
        for (int i = 0; i < 1023; i++) {
            for (int j = 0; j < salt.length; j++)
                tmp[j] = (byte)(salt[j] ^ digest[j]);
            salt = tmp;
            digest = messageDigest.digest(salt);
        }
        String output = SAHexUtil.byteArrToHexString(digest);
        SAUtil.release(digest);
        return output;
    }

    public static String getIDValue(String cusnoStr) throws SASimpleAuthHexException, SASimpleAuthCryptoException {
        byte[] cusno = cusnoStr.getBytes();
        byte[] randomValue = SAHexUtil.hexStrToByteArr(getChallengeValue512());
        int initIdLength = cusno.length + randomValue.length;
        byte[] initId = new byte[initIdLength];
        System.arraycopy(cusno, 0, initId, 0, cusno.length);
        System.arraycopy(randomValue, 0, initId, cusno.length, randomValue.length);
        byte[] id = SAHashUtil.sha256(initId);
        String output = SAHexUtil.byteArrToHexString(id);
        SAUtil.release(randomValue);
        SAUtil.release(id);
        return output;
    }

    public static String genAESKey() throws SASimpleAuthCryptoException {
        SecureRandom secureRandom;
        Key key = null;
        KeyGenerator gen = null;
        String hexKey = null;
        try {
            gen = KeyGenerator.getInstance("AES");
//            secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom = new DSRandomUtil().getmSecureRandom();
        } catch (NoSuchAlgorithmException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_MSG_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_CODE_NO_SUCH_ALGORITHM);
        }
        gen.init(128, secureRandom);
        key = gen.generateKey();
        key.getEncoded();
        hexKey = SAHexUtil.byteArrToHexString(key.getEncoded());
        return hexKey;
    }

    public static String genAESIV() throws SASimpleAuthCryptoException {
        return genAESKey();
    }

    public static String aesEncrypt(String hexKey, String hexIV, byte[] plainText) throws SASimpleAuthHexException, SASimpleAuthCryptoException {
        byte[] rowKey = null;
        byte[] rowIV = null;
        rowKey = SAHexUtil.hexStrToByteArr(hexKey);
        rowIV = SAHexUtil.hexStrToByteArr(hexIV);
        SecretKeySpec secretKeySpec = new SecretKeySpec(rowKey, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(rowIV);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        } catch (NoSuchAlgorithmException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_MSG_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_CODE_NO_SUCH_ALGORITHM);
        } catch (NoSuchPaddingException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_BAD_PADDING, SAErrorMessage.ERR_MSG_BAD_PADDING, SAErrorMessage.ERR_CODE_BAD_PADDING);
        }
        try {
            cipher.init(1, secretKeySpec, ivSpec);
        } catch (InvalidKeyException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_INVALID_KEY, SAErrorMessage.ERR_MSG_INVALID_KEY, SAErrorMessage.ERR_CODE_INVALID_KEY);
        } catch (InvalidAlgorithmParameterException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_INVALID_KEY_SPEC, SAErrorMessage.ERR_MSG_INVALID_KEY_SPEC, SAErrorMessage.ERR_CODE_INVALID_KEY_SPEC);
        }
        byte[] encData = null;
        try {
            encData = cipher.doFinal(plainText);
        } catch (IllegalBlockSizeException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_ILLEGAL_BLOCK_SIZE, SAErrorMessage.ERR_MSG_ILLEGAL_BLOCK_SIZE, SAErrorMessage.ERR_CODE_ILLEGAL_BLOCK_SIZE);
        } catch (BadPaddingException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_BAD_PADDING, SAErrorMessage.ERR_MSG_BAD_PADDING, SAErrorMessage.ERR_CODE_BAD_PADDING);
        }
        return SAHexUtil.byteArrToHexString(encData);
    }

    public static String aesDecrypt(String hexKey, String hexIV, String hexEncData) throws SASimpleAuthHexException, SASimpleAuthCryptoException {
        byte[] rowKey = SAHexUtil.hexStrToByteArr(hexKey);
        byte[] rowIV = SAHexUtil.hexStrToByteArr(hexIV);
        byte[] encData = SAHexUtil.hexStrToByteArr(hexEncData);
        SecretKeySpec secretKeySpec = new SecretKeySpec(rowKey, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(rowIV);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        } catch (NoSuchAlgorithmException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_MSG_NO_SUCH_ALGORITHM, SAErrorMessage.ERR_CODE_NO_SUCH_ALGORITHM);
        } catch (NoSuchPaddingException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_BAD_PADDING, SAErrorMessage.ERR_MSG_BAD_PADDING, SAErrorMessage.ERR_CODE_BAD_PADDING);
        }
        try {
            cipher.init(2, secretKeySpec, ivSpec);
        } catch (InvalidKeyException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_INVALID_KEY, SAErrorMessage.ERR_MSG_INVALID_KEY, SAErrorMessage.ERR_CODE_INVALID_KEY);
        } catch (InvalidAlgorithmParameterException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_INVALID_KEY_SPEC, SAErrorMessage.ERR_MSG_INVALID_KEY_SPEC, SAErrorMessage.ERR_CODE_INVALID_KEY_SPEC);
        }
        byte[] plainText = null;
        try {
            plainText = cipher.doFinal(encData);
        } catch (IllegalBlockSizeException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_ILLEGAL_BLOCK_SIZE, SAErrorMessage.ERR_MSG_ILLEGAL_BLOCK_SIZE, SAErrorMessage.ERR_CODE_ILLEGAL_BLOCK_SIZE);
        } catch (BadPaddingException e) {
            throw new SASimpleAuthCryptoException(SAErrsEnum.ERR_BAD_PADDING, SAErrorMessage.ERR_MSG_BAD_PADDING, SAErrorMessage.ERR_CODE_BAD_PADDING);
        }
        return new String(plainText);
    }
}
