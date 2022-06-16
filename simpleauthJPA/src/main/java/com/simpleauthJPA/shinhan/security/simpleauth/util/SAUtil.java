package com.simpleauthJPA.shinhan.security.simpleauth.util;

import com.simpleauthJPA.shinhan.security.simpleauth.tlv.SAErrsEnum;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SAUtil {
    public static int parseInt(String strNum) {
        int result = 0;
        if (strNum == null || strNum.length() < 1)
            return result;
        try {
            result = Integer.parseInt(strNum);
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static long parseLong(String strNum) {
        long result = 0L;
        if (strNum == null || strNum.length() < 1)
            return result;
        try {
            result = Long.parseLong(strNum);
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static String tagToHex(SAErrsEnum errsEnum) {
        byte[] idByte = SAByteUtil.toShortByte((short)errsEnum.id);
        String output = SAHexUtil.byteArrToHexString(idByte);
        release(idByte);
        return output;
    }

    public static void release(byte[] data) {
        if (data == null)
            return;
        for (int i = 0; i < data.length; i++)
            data[i] = 0;
    }

    public static void release(char[] data) {
        if (data == null)
            return;
        for (int i = 0; i < data.length; i++)
            data[i] = Character.MIN_VALUE;
    }

    public static JSONObject strToJSON(String strJSON) {
        JSONObject jsonObj = null;
        JSONParser jsonParser = new JSONParser();
        try {
            jsonObj = (JSONObject)jsonParser.parse(strJSON);
        } catch (Exception e) {
            SALogUtil.fine(" [SAUtil strToJSON ParseException ]" + SALogUtil.getSAStackTrace(e));
        }
        return jsonObj;
    }
}
