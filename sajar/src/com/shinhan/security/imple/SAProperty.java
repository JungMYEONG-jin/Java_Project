package com.shinhan.security.imple;

public class SAProperty {
    public static String COL_NM_ID = "id";

    public static String COL_NM_CUSNO = "cusno";

    public static String COL_NM_PUBKEY = "pubkey";

    public static String COL_NM_UUID = "uuid";

    public static String COL_NM_APPID = "appid";

    public static String COL_NM_TYPE = "type";

    public static String COL_NM_STATUS = "status";

    public static String COL_NM_REG_DTTM = "reg_dttm";

    public static String COL_NM_DROP_DTTM = "drop_dttm";

    public static String COL_NM_LAST_AUTH_DTTM = "lastauth_dttm";

    public static String COL_NM_ETC1 = "etc1";

    public static String COL_NM_ETC2 = "etc2";

    public static String COL_NM_ETC3 = "etc3";

    public static String COL_NM_ETC4 = "etc4";

    public static String COL_NM_ETC5 = "etc5";

    public static String COL_LOG_SEQ = "seq";

    public static String COL_LOG_CUSNO = "cusno";

    public static String COL_LOG_ID = "id";

    public static String COL_LOG_INJSON = "injson";

    public static String COL_LOG_OUTJSON = "outjson";

    public static String COL_LOG_TAG = "tag";

    public static String COL_LOG_DATA = "msg";

    public static String COL_LOG_TIMESTAMP = "timestamp";

    public static String COL_LOG_ERRORCODE = "errorcode";

    public static String COL_LOG_STACKTRACE = "stacktrace";

    public static String COL_LOG_OSTYPE = "ostype";

    public static String COL_LOG_OSVERSION = "osversion";

    public static String COL_LOG_MODEL = "model";

    public static String COL_LOG_APPVERSION = "appversion";

    public static String COL_LOG_CORP = "corp";

    public static String COL_LOG_RESULT = "result";

    public static String COL_LOG_TYPE = "type";

    public static String COL_LOG_SAVERSION = "saversion";

    public static String COL_LOG_ETC1 = "etc1";

    public static String COL_LOG_ETC2 = "etc2";

    public static String COL_LOG_ETC3 = "etc3";

    public static String TB_NAME = "MBI_SIMPLEAUTH";

    public static String LOG_TB_NAME = "MBI_SIMPLEAUTH_LOG";

    public static String LOG_SEQUENCE_NAME = "SIMPLEAUTH_LOG_SEQ";

    public static String INDEX1_NAME = "MBI_SIMPLEAUTH_IDX_01";

    public static String INDEX2_NAME = "MBI_SIMPLEAUTH_IDX_02";

    public static String DB_POOL_NAME = "mbbPool";

    public static boolean isLog = false;

    public static final String LOG_TAG = "simpleauth";

    public static int TIMEOUT = 5;

    public static String STATUS_Y = "1";

    public static String STATUS_N = "9";

    private static SAProperty instance;

    public static SAProperty getInstance() {
        if (instance == null)
            instance = new SAProperty();
        return instance;
    }

    public static void setSA_ColumName(String id, String cusno, String pubkey, String uuid, String appid, String type, String status, String reg_dttm, String drop_dttm, String last_auth_dttm) {
        COL_NM_ID = id;
        COL_NM_CUSNO = cusno;
        COL_NM_PUBKEY = pubkey;
        COL_NM_UUID = uuid;
        COL_NM_APPID = appid;
        COL_NM_TYPE = type;
        COL_NM_STATUS = status;
        COL_NM_REG_DTTM = reg_dttm;
        COL_NM_DROP_DTTM = drop_dttm;
        COL_NM_LAST_AUTH_DTTM = drop_dttm;
    }

    public static void setSA_EtcColumName(String etc1) {
        COL_NM_ETC1 = etc1;
    }

    public static void setSA_EtcColumName(String etc1, String etc2) {
        COL_NM_ETC1 = etc1;
        COL_NM_ETC2 = etc2;
    }

    public static void setSA_EtcColumName(String etc1, String etc2, String etc3) {
        COL_NM_ETC1 = etc1;
        COL_NM_ETC2 = etc2;
        COL_NM_ETC3 = etc3;
    }

    public static void setSA_EtcColumName(String etc1, String etc2, String etc3, String etc4) {
        COL_NM_ETC1 = etc1;
        COL_NM_ETC2 = etc2;
        COL_NM_ETC3 = etc3;
        COL_NM_ETC4 = etc4;
    }

    public static void setSA_EtcColumName(String etc1, String etc2, String etc3, String etc4, String etc5) {
        COL_NM_ETC1 = etc1;
        COL_NM_ETC2 = etc2;
        COL_NM_ETC3 = etc3;
        COL_NM_ETC4 = etc4;
        COL_NM_ETC5 = etc5;
    }

    public static void setSA_TBName(String tbName) {
        TB_NAME = tbName;
    }

    public static void setSA_DBPoolName(String DBPoolName) {
        DB_POOL_NAME = DBPoolName;
    }

    public static void setSA_DBLogTBName(String DBLogTableName) {
        LOG_TB_NAME = DBLogTableName;
    }

    public static void setSA_DBIndexName(String index1, String index2) {
        INDEX1_NAME = index1;
        INDEX2_NAME = index2;
    }

    public static void setSA_StatusValue(String status_y, String status_n) {
        STATUS_Y = status_y;
        STATUS_N = status_n;
    }

    public static void setTimeout(int timeout) {
        TIMEOUT = timeout;
    }

    public static void setSA_Log(boolean islog) {
        isLog = islog;
    }

    public static void setSA_LogSequenceName(String seqName) {
        LOG_SEQUENCE_NAME = seqName;
    }

    public static void setSA_LogColumName(String seq, String cusno, String id, String tag, String injson, String outjson, String msg, String code, String stacktrace, String timestamp, String ostype, String osversion, String model, String appversion, String corp, String result, String type, String saversion) {
        COL_LOG_SEQ = seq;
        COL_LOG_CUSNO = cusno;
        COL_LOG_ID = id;
        COL_LOG_TAG = tag;
        COL_LOG_INJSON = injson;
        COL_LOG_OUTJSON = outjson;
        COL_LOG_DATA = msg;
        COL_LOG_TIMESTAMP = timestamp;
        COL_LOG_ERRORCODE = code;
        COL_LOG_STACKTRACE = stacktrace;
        COL_LOG_OSVERSION = osversion;
        COL_LOG_OSTYPE = ostype;
        COL_LOG_MODEL = model;
        COL_LOG_APPVERSION = appversion;
        COL_LOG_CORP = corp;
        COL_LOG_RESULT = result;
        COL_LOG_TYPE = type;
        COL_LOG_SAVERSION = saversion;
    }

    public static void setSA_LogColumName(String seq, String cusno, String id, String tag, String injson, String outjson, String msg, String code, String stacktrace, String timestamp, String ostype, String osversion, String model, String appversion, String corp, String result) {
        COL_LOG_SEQ = seq;
        COL_LOG_CUSNO = cusno;
        COL_LOG_ID = id;
        COL_LOG_TAG = tag;
        COL_LOG_INJSON = injson;
        COL_LOG_OUTJSON = outjson;
        COL_LOG_DATA = msg;
        COL_LOG_TIMESTAMP = timestamp;
        COL_LOG_ERRORCODE = code;
        COL_LOG_STACKTRACE = stacktrace;
        COL_LOG_OSVERSION = osversion;
        COL_LOG_OSTYPE = ostype;
        COL_LOG_MODEL = model;
        COL_LOG_APPVERSION = appversion;
        COL_LOG_CORP = corp;
        COL_LOG_RESULT = result;
    }

    public static void setSA_EtcLogName(String etc1) {
        COL_LOG_ETC1 = etc1;
    }

    public static void setSA_EtcLogName(String etc1, String etc2) {
        COL_LOG_ETC1 = etc1;
        COL_LOG_ETC2 = etc2;
    }

    public static void setSA_EtcLogName(String etc1, String etc2, String etc3) {
        COL_LOG_ETC1 = etc1;
        COL_LOG_ETC2 = etc2;
        COL_LOG_ETC3 = etc3;
    }

    public void setSAColumName(String id, String cusno, String pubkey, String uuid, String appid, String type, String status, String reg_dttm, String drop_dttm, String last_auth_dttm) {
        COL_NM_ID = id;
        COL_NM_CUSNO = cusno;
        COL_NM_PUBKEY = pubkey;
        COL_NM_UUID = uuid;
        COL_NM_APPID = appid;
        COL_NM_TYPE = type;
        COL_NM_STATUS = status;
        COL_NM_REG_DTTM = reg_dttm;
        COL_NM_DROP_DTTM = drop_dttm;
        COL_NM_LAST_AUTH_DTTM = drop_dttm;
    }

    public void setSAEtcColumName(String etc1) {
        COL_NM_ETC1 = etc1;
    }

    public void setSAEtcColumName(String etc1, String etc2) {
        COL_NM_ETC1 = etc1;
        COL_NM_ETC2 = etc2;
    }

    public void setSAEtcColumName(String etc1, String etc2, String etc3) {
        COL_NM_ETC1 = etc1;
        COL_NM_ETC2 = etc2;
        COL_NM_ETC3 = etc3;
    }

    public void setSAEtcColumName(String etc1, String etc2, String etc3, String etc4) {
        COL_NM_ETC1 = etc1;
        COL_NM_ETC2 = etc2;
        COL_NM_ETC3 = etc3;
        COL_NM_ETC4 = etc4;
    }

    public void setSAEtcColumName(String etc1, String etc2, String etc3, String etc4, String etc5) {
        COL_NM_ETC1 = etc1;
        COL_NM_ETC2 = etc2;
        COL_NM_ETC3 = etc3;
        COL_NM_ETC4 = etc4;
        COL_NM_ETC5 = etc5;
    }

    public void setSATBName(String tbName) {
        TB_NAME = tbName;
    }

    public void setSADBPoolName(String DBPoolName) {
        DB_POOL_NAME = DBPoolName;
    }

    public void setSADBLogTBName(String DBLogTableName) {
        LOG_TB_NAME = DBLogTableName;
    }

    public void setSADBIndexName(String index1, String index2) {
        INDEX1_NAME = index1;
        INDEX2_NAME = index2;
    }

    public void setSAStatusValue(String status_y, String status_n) {
        STATUS_Y = status_y;
        STATUS_N = status_n;
    }

    public void setSATimeout(int timeout) {
        TIMEOUT = timeout;
    }

    public void setSALog(boolean islog) {
        isLog = islog;
    }

    public void setSALogSequenceName(String seqName) {
        LOG_SEQUENCE_NAME = seqName;
    }

    public void setSALogColumName(String seq, String cusno, String id, String tag, String injson, String outjson, String msg, String code, String stacktrace, String timestamp, String ostype, String osversion, String model, String appversion, String corp, String result, String type, String saversion) {
        COL_LOG_SEQ = seq;
        COL_LOG_CUSNO = cusno;
        COL_LOG_ID = id;
        COL_LOG_TAG = tag;
        COL_LOG_INJSON = injson;
        COL_LOG_OUTJSON = outjson;
        COL_LOG_DATA = msg;
        COL_LOG_TIMESTAMP = timestamp;
        COL_LOG_ERRORCODE = code;
        COL_LOG_STACKTRACE = stacktrace;
        COL_LOG_OSVERSION = osversion;
        COL_LOG_OSTYPE = ostype;
        COL_LOG_MODEL = model;
        COL_LOG_APPVERSION = appversion;
        COL_LOG_CORP = corp;
        COL_LOG_RESULT = result;
        COL_LOG_TYPE = type;
        COL_LOG_SAVERSION = saversion;
    }

    public void setSAEtcLogName(String etc1) {
        COL_LOG_ETC1 = etc1;
    }

    public void setSAEtcLogName(String etc1, String etc2) {
        COL_LOG_ETC1 = etc1;
        COL_LOG_ETC2 = etc2;
    }

    public void setSAEtcLogName(String etc1, String etc2, String etc3) {
        COL_LOG_ETC1 = etc1;
        COL_LOG_ETC2 = etc2;
        COL_LOG_ETC3 = etc3;
    }
}
