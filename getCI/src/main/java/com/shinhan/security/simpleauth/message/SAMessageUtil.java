package com.shinhan.security.simpleauth.message;

import com.shinhan.security.simpleauth.exception.SASimpleAuthMessageException;
import com.shinhan.security.simpleauth.message.auth.SAAuthInitClientMessage;
import com.shinhan.security.simpleauth.tlv.SAErrsEnum;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

public class SAMessageUtil {
    public static String getFieldInfo(Object object) {
        StringBuffer result = new StringBuffer();
        Class<?> mClass = object.getClass();
        Field[] fields = mClass.getDeclaredFields();
        result.append("\n*********************\n");
        result.append(mClass.getName());
        result.append("\n*********************\n");

        byte b;
        int i;
        Field[] arrayOfField1;
        for (i = (arrayOfField1 = fields).length, b = 0; b < i; ) {
            Field field = arrayOfField1[b];
            result.append("[" + field.getName() + "]");
            result.append("\n");
            try {
                result.append(field.get(object));
            } catch (Exception exception) {}
            result.append("\n");
            b++;
        }
        result.append("*********************\n");
        return result.toString();
    }

    public static void parseJSONData(String jsonString, Object object) throws SASimpleAuthMessageException {
        JSONObject jsonObject = null;
        try {
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(jsonString);
        }  catch (ParseException e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_PARSE_JSON, SAErrorMessage.ERR_MSG_PARSE_JSON, SAErrorMessage.ERR_CODE_PARSE_JSON);
        }
        ArrayList<String> keyArr = new ArrayList<String>();
        Iterator<?> keys = jsonObject.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String)keys.next();
            keyArr.add(key);
        }
        Class<?> cls = object.getClass();
        Field[] fields = cls.getDeclaredFields();
        byte b;
        int i;
        Field[] arrayOfField1;
        for (i = (arrayOfField1 = fields).length, b = 0; b < i; ) {
            Field field = arrayOfField1[b];
            for (String fieldName : keyArr) {
                if (fieldName.equals(field.getName()))
                    try {
                        cls.getField(fieldName).set(object, jsonObject.get(fieldName));
                    } catch (Exception e) {
                        throw new SASimpleAuthMessageException(SAErrsEnum.ERR_PARSE_JSON, SAErrorMessage.ERR_MSG_PARSE_JSON, SAErrorMessage.ERR_CODE_PARSE_JSON);
                    }
            }
            b++;
        }
    }

    public static String toJSON(Object object) throws SASimpleAuthMessageException {
        JSONObject jsonObject = new JSONObject();
        Class<?> cls = object.getClass();
        Field[] fields = cls.getDeclaredFields();
        byte b;
        int i;
        Field[] arrayOfField1;
        for (i = (arrayOfField1 = fields).length, b = 0; b < i; ) {
            Field field = arrayOfField1[b];
            try {
                jsonObject.put(field.getName(), field.get(object));
            } catch (Exception e) {
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_TO_JSON, SAErrorMessage.ERR_MSG_TO_JSON, SAErrorMessage.ERR_CODE_TO_JSON);
            }
            b++;
        }
        return jsonObject.toString();
    }

    public static String getAuthKey(String reqJson) throws SASimpleAuthMessageException {
        SAAuthInitClientMessage objAuthInitClientMessage = new SAAuthInitClientMessage();
        try {
            parseJSONData(reqJson, objAuthInitClientMessage);
            if (isEmpty(objAuthInitClientMessage.id))
                throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_ID_NULL, SAErrorMessage.ERR_CODE_ID_NULL);
            return objAuthInitClientMessage.id;
        } catch (Exception e) {
            throw new SASimpleAuthMessageException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_ID_NULL, SAErrorMessage.ERR_CODE_ID_NULL);
        }
    }

    private static boolean isEmpty(String str) {
        if (str == null || str.equals(""))
            return true;
        return false;
    }
}
