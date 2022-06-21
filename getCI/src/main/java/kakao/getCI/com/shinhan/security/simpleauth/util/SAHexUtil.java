package kakao.getCI.com.shinhan.security.simpleauth.util;

import kakao.getCI.com.shinhan.security.simpleauth.exception.SASimpleAuthHexException;
import kakao.getCI.com.shinhan.security.simpleauth.message.SAErrorMessage;
import kakao.getCI.com.shinhan.security.simpleauth.tlv.SAErrsEnum;
import kakao.getCI.com.shinhan.security.simpleauth.tlv.SATagsEnum;

public class SAHexUtil {
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    public static byte[] hexStrToByteArr(String hexStr) throws SASimpleAuthHexException {
        if (hexStr == null)
            return null;
        int length = hexStr.length();
        if (length % 2 == 1)
            throw new SASimpleAuthHexException(SAErrsEnum.ERR_ILLEGAL_ARGUMENT, SAErrorMessage.ERR_MSG_ILLEGAL_ARGUMENT, SAErrorMessage.ERR_CODE_ILLEGAL_ARGUMENT);
        length /= 2;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int index = i * 2;
            bytes[i] = (byte)Short.parseShort(hexStr.substring(index, index + 2), 16);
        }
        return bytes;
    }

    public static String byteArrToHexString(byte[] bytes) {
        if (bytes == null)
            return null;
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i];
            hexChars[i * 2] = hexCode[(v & 0xF0) >> 4];
            hexChars[i * 2 + 1] = hexCode[v & 0xF];
        }
        String output = new String(hexChars);
        SAUtil.release(bytes);
        SAUtil.release(hexChars);
        return output;
    }

    public static String tagToHex(SATagsEnum tagsEnum) {
        byte[] idByte = SAByteUtil.toShortByte((short)tagsEnum.id);
        String output = byteArrToHexString(idByte);
        SAUtil.release(idByte);
        return output;
    }

    public static char[] tagToHexChar(SATagsEnum tagsEnum) {
        byte[] idByte = SAByteUtil.toShortByte((short)tagsEnum.id);
        char[] output = { (char)idByte[0], (char)idByte[1] };
        SAUtil.release(idByte);
        return output;
    }
}
