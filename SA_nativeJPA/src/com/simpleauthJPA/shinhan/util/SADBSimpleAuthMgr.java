package com.simpleauthJPA.shinhan.util;



import com.simpleauthJPA.shinhan.security.imple.SAProperty;
import com.simpleauthJPA.shinhan.security.simpleauth.exception.SASimpleAuthSQLException;
import com.simpleauthJPA.shinhan.security.simpleauth.message.SAErrorMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.tlv.SAErrsEnum;
import com.simpleauthJPA.shinhan.security.simpleauth.util.SALogUtil;

import java.sql.*;
import java.util.HashMap;

public class SADBSimpleAuthMgr {
    public static int deleteSAInfo(HashMap<String, String> inputMap) throws SASimpleAuthSQLException {
        String query = "UPDATE " + SAProperty.TB_NAME +
                " SET " + SAProperty.COL_NM_STATUS + " = ? , DROP_DTTM=  TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')" +
                " WHERE " + SAProperty.COL_NM_ID + " = ? and " + SAProperty.COL_NM_DROP_DTTM + " ='99999999999999'";
        Connection con = null;
        PreparedStatement stmt = null;
        int result_cnt = 0;
        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SASimpleAuthSQLException(
                    SAErrsEnum.ERR_JDBC_D, SAErrorMessage.ERR_MSG_JDBC_CLASSNOTFOUND_D, SAErrorMessage.ERR_CODE_JDBC_CLASSNOTFOUND_D);
        } catch (Exception e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_D, SAErrorMessage.ERR_MSG_JDBC_EXCEPTION_D, SAErrorMessage.ERR_CODE_JDBC_EXCEPTION_D);
        } catch (Throwable e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_D, SAErrorMessage.ERR_MSG_JDBC_THROWABLE_D, SAErrorMessage.ERR_CODE_JDBC_THROWABLE_D);
        }
        try {
//            Context envContext = new InitialContext();
//            DataSource dsPool = (DataSource)envContext.lookup(SAProperty.DB_POOL_NAME);
            con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/"+ SAProperty.DB_POOL_NAME, "sa", "");
            con.setAutoCommit(true);
            stmt = con.prepareStatement(query);
            stmt.setString(1, SAProperty.STATUS_N);
            stmt.setString(2, inputMap.get(SAProperty.COL_NM_ID));
            result_cnt = stmt.executeUpdate();
        } catch (Exception e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_D, SAErrorMessage.ERR_MSG_JDBC_EXCEPTION_D, SAErrorMessage.ERR_CODE_JDBC_EXCEPTION_D);
        } catch (Throwable e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_D, SAErrorMessage.ERR_MSG_JDBC_THROWABLE_D, SAErrorMessage.ERR_CODE_JDBC_THROWABLE_D);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception exception) {}
            try {
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        }
        return result_cnt;
    }

    public static HashMap<String, String> getSAAuthInitInfo(HashMap<String, String> inputMap) throws SASimpleAuthSQLException {
        String query = "SELECT * FROM " + SAProperty.TB_NAME +
                " WHERE " + SAProperty.COL_NM_ID + "= ? and " + SAProperty.COL_NM_DROP_DTTM + " ='99999999999999'";
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, String> propMap = new HashMap<String, String>();
        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_AUTHINIT_S, SAErrorMessage.ERR_MSG_JDBC_CLASSNOTFOUND_AUTH_S, SAErrorMessage.ERR_CODE_JDBC_CLASSNOTFOUND_AUTH_S);
        } catch (Exception e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_AUTHINIT_S, SAErrorMessage.ERR_MSG_JDBC_EXCEPTION_AUTH_S, SAErrorMessage.ERR_CODE_JDBC_EXCEPTION_AUTH_S);
        } catch (Throwable e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_AUTHINIT_S, SAErrorMessage.ERR_MSG_JDBC_THROWABLE_AUTH_S, SAErrorMessage.ERR_CODE_JDBC_THROWABLE_AUTH_S);
        }
        try {
//            Context envContext = new InitialContext();
//            DataSource dsPool = (DataSource)envContext.lookup(SAProperty.DB_POOL_NAME);
            con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/"+ SAProperty.DB_POOL_NAME, "sa", "");
            con.setAutoCommit(true);
            stmt = con.prepareStatement(query);
            stmt.setString(1, inputMap.get(SAProperty.COL_NM_ID));
            rs = stmt.executeQuery();
            while (rs.next()) {
                propMap.put(SAProperty.COL_NM_CUSNO, rs.getString(SAProperty.COL_NM_CUSNO));
                propMap.put(SAProperty.COL_NM_ID, rs.getString(SAProperty.COL_NM_ID));
                propMap.put(SAProperty.COL_NM_PUBKEY, rs.getString(SAProperty.COL_NM_PUBKEY));
                propMap.put(SAProperty.COL_NM_UUID, rs.getString(SAProperty.COL_NM_UUID));
                propMap.put(SAProperty.COL_NM_APPID, rs.getString(SAProperty.COL_NM_APPID));
                propMap.put(SAProperty.COL_NM_TYPE, rs.getString(SAProperty.COL_NM_TYPE));
                propMap.put(SAProperty.COL_NM_STATUS, rs.getString(SAProperty.COL_NM_STATUS));
            }
        } catch (SQLException e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_AUTHINIT_S, SAErrorMessage.ERR_MSG_JDBC_SQLEXCEPTION_AUTH_S, SAErrorMessage.ERR_CODE_JDBC_SQLEXCEPTION_AUTH_S);
        } catch (Exception e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_AUTHINIT_S, SAErrorMessage.ERR_MSG_JDBC_EXCEPTION_AUTH_S, SAErrorMessage.ERR_CODE_JDBC_EXCEPTION_AUTH_S);
        } catch (Throwable e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_AUTHINIT_S, SAErrorMessage.ERR_MSG_JDBC_THROWABLE_AUTH_S, SAErrorMessage.ERR_CODE_JDBC_THROWABLE_AUTH_S);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception exception) {}
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception exception) {}
            try {
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        }
        return propMap;
    }

    public static HashMap<String, String> getSAAuthInfo(HashMap<String, String> inputMap) throws SASimpleAuthSQLException {
        String query = "SELECT * FROM " + SAProperty.TB_NAME +
                " WHERE " + SAProperty.COL_NM_ID + "= ? and " + SAProperty.COL_NM_DROP_DTTM + " ='99999999999999'";
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, String> propMap = new HashMap<String, String>();
        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_AUTH_S, SAErrorMessage.ERR_MSG_JDBC_CLASSNOTFOUND_AUTH_S, SAErrorMessage.ERR_CODE_JDBC_CLASSNOTFOUND_AUTH_S);
        } catch (Exception e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_AUTH_S, SAErrorMessage.ERR_MSG_JDBC_EXCEPTION_AUTH_S, SAErrorMessage.ERR_CODE_JDBC_EXCEPTION_AUTH_S);
        } catch (Throwable e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_AUTH_S, SAErrorMessage.ERR_MSG_JDBC_THROWABLE_AUTH_S, SAErrorMessage.ERR_CODE_JDBC_THROWABLE_AUTH_S);
        }
        try {
//            Context envContext = new InitialContext();
//            DataSource dsPool = (DataSource)envContext.lookup(SAProperty.DB_POOL_NAME);
            con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/"+ SAProperty.DB_POOL_NAME, "sa", "");
            con.setAutoCommit(true);
            stmt = con.prepareStatement(query);
            stmt.setString(1, inputMap.get(SAProperty.COL_NM_ID));
            rs = stmt.executeQuery();
            while (rs.next()) {
                propMap.put(SAProperty.COL_NM_CUSNO, rs.getString(SAProperty.COL_NM_CUSNO));
                propMap.put(SAProperty.COL_NM_ID, rs.getString(SAProperty.COL_NM_ID));
                propMap.put(SAProperty.COL_NM_PUBKEY, rs.getString(SAProperty.COL_NM_PUBKEY));
                propMap.put(SAProperty.COL_NM_UUID, rs.getString(SAProperty.COL_NM_UUID));
                propMap.put(SAProperty.COL_NM_APPID, rs.getString(SAProperty.COL_NM_APPID));
                propMap.put(SAProperty.COL_NM_TYPE, rs.getString(SAProperty.COL_NM_TYPE));
                propMap.put(SAProperty.COL_NM_STATUS, rs.getString(SAProperty.COL_NM_STATUS));
            }
        } catch (SQLException e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_AUTH_S, SAErrorMessage.ERR_MSG_JDBC_SQLEXCEPTION_AUTH_S, SAErrorMessage.ERR_CODE_JDBC_SQLEXCEPTION_AUTH_S);
        } catch (Exception e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_AUTH_S, SAErrorMessage.ERR_MSG_JDBC_EXCEPTION_AUTH_S, SAErrorMessage.ERR_CODE_JDBC_EXCEPTION_AUTH_S);
        } catch (Throwable e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_AUTH_S, SAErrorMessage.ERR_MSG_JDBC_THROWABLE_AUTH_S, SAErrorMessage.ERR_CODE_JDBC_THROWABLE_AUTH_S);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception exception) {}
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception exception) {}
            try {
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        }
        return propMap;
    }

    public static HashMap<String, String> getSAUnregInfo(HashMap<String, String> inputMap) throws SASimpleAuthSQLException {
        String query = "SELECT * FROM " + SAProperty.TB_NAME + " WHERE " + SAProperty.COL_NM_ID + "=? and " + SAProperty.COL_NM_DROP_DTTM + "='99999999999999'";
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, String> propMap = new HashMap<String, String>();
        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_UNREG_S, SAErrorMessage.ERR_MSG_JDBC_CLASSNOTFOUND_UNREG_S, SAErrorMessage.ERR_CODE_JDBC_CLASSNOTFOUND_UNREG_S);
        } catch (Exception e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_UNREG_S, SAErrorMessage.ERR_MSG_JDBC_EXCEPTION_UNREG_S, SAErrorMessage.ERR_CODE_JDBC_EXCEPTION_UNREG_S);
        } catch (Throwable e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_UNREG_S, SAErrorMessage.ERR_MSG_JDBC_THROWABLE_UNREG_S, SAErrorMessage.ERR_CODE_JDBC_THROWABLE_UNREG_S);
        }
        try {
//            Context envContext = new InitialContext();
//            DataSource dsPool = (DataSource)envContext.lookup(SAProperty.DB_POOL_NAME);
            con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/"+ SAProperty.DB_POOL_NAME, "sa", "");
            con.setAutoCommit(true);
            stmt = con.prepareStatement(query);
            stmt.setString(1, inputMap.get(SAProperty.COL_NM_ID));
            rs = stmt.executeQuery();
            while (rs.next()) {
                propMap.put(SAProperty.COL_NM_CUSNO, rs.getString(SAProperty.COL_NM_CUSNO));
                propMap.put(SAProperty.COL_NM_ID, rs.getString(SAProperty.COL_NM_ID));
                propMap.put(SAProperty.COL_NM_PUBKEY, rs.getString(SAProperty.COL_NM_PUBKEY));
                propMap.put(SAProperty.COL_NM_UUID, rs.getString(SAProperty.COL_NM_UUID));
                propMap.put(SAProperty.COL_NM_APPID, rs.getString(SAProperty.COL_NM_APPID));
                propMap.put(SAProperty.COL_NM_TYPE, rs.getString(SAProperty.COL_NM_TYPE));
                propMap.put(SAProperty.COL_NM_STATUS, rs.getString(SAProperty.COL_NM_STATUS));
            }
        } catch (SQLException e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_UNREG_S, SAErrorMessage.ERR_MSG_JDBC_SQLEXCEPTION_UNREG_S, SAErrorMessage.ERR_CODE_JDBC_SQLEXCEPTION_UNREG_S);
        } catch (Exception e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_UNREG_S, SAErrorMessage.ERR_MSG_JDBC_EXCEPTION_UNREG_S, SAErrorMessage.ERR_CODE_JDBC_EXCEPTION_UNREG_S);
        } catch (Throwable e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_UNREG_S, SAErrorMessage.ERR_MSG_JDBC_THROWABLE_UNREG_S, SAErrorMessage.ERR_CODE_JDBC_THROWABLE_UNREG_S);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception exception) {}
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception exception) {}
            try {
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        }
        return propMap;
    }

    public static int insertSAInfo(HashMap<String, String> map) throws SASimpleAuthSQLException {
        Connection con = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        int result_cnt = 0;
        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_I, SAErrorMessage.ERR_MSG_JDBC_CLASSNOTFOUND_I, SAErrorMessage.ERR_CODE_JDBC_CLASSNOTFOUND_I);
        } catch (Exception e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_I, SAErrorMessage.ERR_MSG_JDBC_EXCEPTION_I, SAErrorMessage.ERR_CODE_JDBC_EXCEPTION_I);
        } catch (Throwable e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_I, SAErrorMessage.ERR_MSG_JDBC_THROWABLE_I, SAErrorMessage.ERR_CODE_JDBC_THROWABLE_I);
        }
        try {
//            Context envContext = new InitialContext();
//            DataSource dsPool = (DataSource)envContext.lookup(SAProperty.DB_POOL_NAME);
            con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/"+ SAProperty.DB_POOL_NAME, "sa", "");
            con.setAutoCommit(true);
            String query = "UPDATE " + SAProperty.TB_NAME +
                    " SET " + SAProperty.COL_NM_STATUS + "=?, DROP_DTTM =  TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')" +
                    " WHERE " + SAProperty.COL_NM_ID + " IN (" +
                    " SELECT " + SAProperty.COL_NM_ID +
                    " FROM " + SAProperty.TB_NAME +
                    " WHERE " + SAProperty.COL_NM_CUSNO + "!=? AND " + SAProperty.COL_NM_UUID + "=? AND DROP_DTTM = '99999999999999'" +
                    " UNION " +
                    " SELECT  " + SAProperty.COL_NM_ID +
                    " FROM " + SAProperty.TB_NAME +
                    " WHERE " + SAProperty.COL_NM_CUSNO + "=? AND " + SAProperty.COL_NM_UUID + "!=? AND DROP_DTTM = '99999999999999'" +
                    " UNION " +
                    " SELECT " + SAProperty.COL_NM_ID +
                    " FROM " + SAProperty.TB_NAME +
                    " WHERE " + SAProperty.COL_NM_CUSNO + "=? AND " + SAProperty.COL_NM_UUID + "=? AND " + SAProperty.COL_NM_TYPE + "=? AND DROP_DTTM = '99999999999999')";
            SALogUtil.fine("query : " + query);
            stmt1 = con.prepareStatement(query);
            stmt1.setString(1, SAProperty.STATUS_N);
            stmt1.setString(2, map.get(SAProperty.COL_NM_CUSNO));
            stmt1.setString(3, map.get(SAProperty.COL_NM_UUID));
            stmt1.setString(4, map.get(SAProperty.COL_NM_CUSNO));
            stmt1.setString(5, map.get(SAProperty.COL_NM_UUID));
            stmt1.setString(6, map.get(SAProperty.COL_NM_CUSNO));
            stmt1.setString(7, map.get(SAProperty.COL_NM_UUID));
            stmt1.setString(8, map.get(SAProperty.COL_NM_TYPE));
            result_cnt = stmt1.executeUpdate();
            String in_query2 = "INSERT INTO " + SAProperty.TB_NAME + "(" + SAProperty.COL_NM_ID + "," + SAProperty.COL_NM_APPID + "," + SAProperty.COL_NM_CUSNO + "," + SAProperty.COL_NM_UUID + "," + SAProperty.COL_NM_TYPE + "," + SAProperty.COL_NM_STATUS + "," + SAProperty.COL_NM_PUBKEY + "," + SAProperty.COL_NM_REG_DTTM + "," + SAProperty.COL_NM_DROP_DTTM + ") values (?,?,?,?,?,?,?,TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'),'99999999999999')";
            stmt2 = con.prepareStatement(in_query2);
            stmt2.setString(1, map.get(SAProperty.COL_NM_ID));
            stmt2.setString(2, map.get(SAProperty.COL_NM_APPID));
            stmt2.setString(3, map.get(SAProperty.COL_NM_CUSNO));
            stmt2.setString(4, map.get(SAProperty.COL_NM_UUID));
            stmt2.setString(5, map.get(SAProperty.COL_NM_TYPE));
            stmt2.setString(6, map.get(SAProperty.COL_NM_STATUS));
            stmt2.setString(7, map.get(SAProperty.COL_NM_PUBKEY));
            result_cnt = stmt2.executeUpdate();
        } catch (SQLException e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_I, SAErrorMessage.ERR_MSG_JDBC_SQLEXCEPTION_I, SAErrorMessage.ERR_CODE_JDBC_SQLEXCEPTION_I);
        } catch (Exception e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_I, SAErrorMessage.ERR_MSG_JDBC_EXCEPTION_I, SAErrorMessage.ERR_CODE_JDBC_EXCEPTION_I);
        } catch (Throwable e) {
            throw new SASimpleAuthSQLException(SAErrsEnum.ERR_JDBC_I, SAErrorMessage.ERR_MSG_JDBC_THROWABLE_I, SAErrorMessage.ERR_CODE_JDBC_THROWABLE_I);
        } finally {
            try {
                if (stmt1 != null)
                    stmt1.close();
            } catch (Exception exception) {}
            try {
                if (stmt2 != null)
                    stmt2.close();
            } catch (Exception exception) {}
            try {
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        }
        return result_cnt;
    }

    public static int SALog(String cusno, String id, String tag, String injson, String outjson, String msg, String errorcode, String stacktrace, String osType, String osVersion, String model, String appVersion, String corp, String result, String type, String saversion) {
        SALogUtil.fine("cusno : " + cusno);
        SALogUtil.fine("id : " + id);
        SALogUtil.fine("tag : " + tag);
        SALogUtil.fine("injson : " + injson);
        SALogUtil.fine("outjson : " + outjson);
        SALogUtil.fine("msg : " + msg);
        SALogUtil.fine("errorcode : " + errorcode);
        SALogUtil.fine("stacktrace : " + stacktrace);
        SALogUtil.fine("osType : " + osType);
        SALogUtil.fine("osVersion : " + osVersion);
        SALogUtil.fine("model : " + model);
        SALogUtil.fine("appVersion : " + appVersion);
        SALogUtil.fine("corp : " + corp);
        SALogUtil.fine("result : " + result);
        SALogUtil.fine("type : " + type);
        SALogUtil.fine("saversion : " + saversion);
        Connection con = null;
        PreparedStatement stmt = null;
        int result_cnt = 0;
        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            SAExceptionLog1(cusno, id, tag, injson, outjson, msg, "1111", SALogUtil.getSAStackTrace(e), osType, osVersion, model, appVersion, corp, result, type, saversion);
            return result_cnt;
        } catch (Exception exception) {

        } catch (Throwable throwable) {}
        try {
//            Context envContext = new InitialContext();
//            DataSource dsPool = (DataSource)envContext.lookup(SAProperty.DB_POOL_NAME);
            con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/"+ SAProperty.DB_POOL_NAME, "sa", "");
            con.setAutoCommit(true);
            String in_query = "INSERT INTO " + SAProperty.LOG_TB_NAME +
                    "(" + SAProperty.COL_LOG_SEQ + "," + SAProperty.COL_LOG_OSTYPE + "," + SAProperty.COL_LOG_OSVERSION + "," + SAProperty.COL_LOG_MODEL + "," + SAProperty.COL_LOG_APPVERSION + "," + SAProperty.COL_LOG_CUSNO + "," + SAProperty.COL_LOG_ID + "," + SAProperty.COL_LOG_TAG + "," + SAProperty.COL_LOG_INJSON + "," + SAProperty.COL_LOG_OUTJSON + "," + SAProperty.COL_LOG_DATA + "," + SAProperty.COL_LOG_ERRORCODE + "," + SAProperty.COL_LOG_STACKTRACE + "," + SAProperty.COL_LOG_CORP + "," + SAProperty.COL_LOG_RESULT + "," + SAProperty.COL_LOG_TYPE + "," + SAProperty.COL_LOG_SAVERSION + "," + SAProperty.COL_LOG_TIMESTAMP + ")" +
                    "VALUES (" + SAProperty.LOG_SEQUENCE_NAME + ".NEXTVAL" + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))";
            stmt = con.prepareStatement(in_query);
            stmt.setString(1, osType);
            stmt.setString(2, osVersion);
            stmt.setString(3, model);
            stmt.setString(4, appVersion);
            stmt.setString(5, cusno);
            stmt.setString(6, id);
            stmt.setString(7, tag);
            stmt.setString(8, injson);
            stmt.setString(9, outjson);
            stmt.setString(10, msg);
            stmt.setString(11, errorcode);
            stmt.setString(12, stacktrace);
            stmt.setString(13, corp);
            stmt.setString(14, result);
            stmt.setString(15, type);
            stmt.setString(16, saversion);
            result_cnt = stmt.executeUpdate();
        } catch (SQLException e) {
            SALogUtil.fine("SQLException : " + SALogUtil.getSAStackTrace(e));
            SAExceptionLog1(cusno, id, tag, injson, outjson, msg, "1111", SALogUtil.getSAStackTrace(e), osType, osVersion, model, appVersion, corp, result, type, saversion);
            return result_cnt;
        } catch (Exception e) {
            SALogUtil.fine("Exception : " + SALogUtil.getSAStackTrace(e));
            SAExceptionLog1(cusno, id, tag, injson, outjson, msg, "1111", SALogUtil.getSAStackTrace(e), osType, osVersion, model, appVersion, corp, result, type, saversion);
            return result_cnt;
        } catch (Throwable throwable) {

        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception exception) {}
            try {
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        }
        return result_cnt;
    }

    public static int SALog(String cusno, String id, String tag, String msg, String errorcode, String stacktrace, String osType, String osVersion, String model, String appVersion, String corp, String result, String type, String saversion) {
        SALogUtil.fine("cusno : " + cusno);
        SALogUtil.fine("id : " + id);
        SALogUtil.fine("tag : " + tag);
        SALogUtil.fine("msg : " + msg);
        SALogUtil.fine("errorcode : " + errorcode);
        SALogUtil.fine("stacktrace : " + stacktrace);
        SALogUtil.fine("osType : " + osType);
        SALogUtil.fine("osVersion : " + osVersion);
        SALogUtil.fine("model : " + model);
        SALogUtil.fine("appVersion : " + appVersion);
        SALogUtil.fine("corp : " + corp);
        SALogUtil.fine("result : " + result);
        SALogUtil.fine("type : " + type);
        SALogUtil.fine("saversion : " + saversion);
        Connection con = null;
        PreparedStatement stmt = null;
        int result_cnt = 0;
        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            return result_cnt;
        } catch (Exception exception) {

        } catch (Throwable throwable) {}
        try {
//            Context envContext = new InitialContext();
//            DataSource dsPool = (DataSource)envContext.lookup(SAProperty.DB_POOL_NAME);
            con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/"+ SAProperty.DB_POOL_NAME, "sa", "");
            con.setAutoCommit(true);
            String query = "INSERT INTO " + SAProperty.LOG_TB_NAME +
                    "(" + SAProperty.COL_LOG_SEQ + "," + SAProperty.COL_LOG_OSTYPE + "," + SAProperty.COL_LOG_OSVERSION + "," + SAProperty.COL_LOG_MODEL + "," + SAProperty.COL_LOG_APPVERSION + "," + SAProperty.COL_LOG_CUSNO + "," + SAProperty.COL_LOG_ID + "," + SAProperty.COL_LOG_TAG + "," + SAProperty.COL_LOG_DATA + "," + SAProperty.COL_LOG_ERRORCODE + "," + SAProperty.COL_LOG_STACKTRACE + "," + SAProperty.COL_LOG_CORP + "," + SAProperty.COL_LOG_RESULT + "," + SAProperty.COL_LOG_TYPE + "," + SAProperty.COL_LOG_SAVERSION + "," + SAProperty.COL_LOG_TIMESTAMP + ") " +
                    "VALUES (" + SAProperty.LOG_SEQUENCE_NAME + ".NEXTVAL" + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))";
            stmt = con.prepareStatement(query);
            stmt.setString(1, osType);
            stmt.setString(2, osVersion);
            stmt.setString(3, model);
            stmt.setString(4, appVersion);
            stmt.setString(5, cusno);
            stmt.setString(6, id);
            stmt.setString(7, tag);
            stmt.setString(8, msg);
            stmt.setString(9, errorcode);
            stmt.setString(10, stacktrace);
            stmt.setString(11, corp);
            stmt.setString(12, result);
            stmt.setString(13, type);
            stmt.setString(14, saversion);
            result_cnt = stmt.executeUpdate();
        } catch (SQLException e) {
            SALogUtil.fine("SQLException : " + SALogUtil.getSAStackTrace(e));
            SAExceptionLog2(cusno, id, tag, msg, "1111", SALogUtil.getSAStackTrace(e), osType, osVersion, model, appVersion, corp, result, type, saversion);
            return result_cnt;
        } catch (Exception e) {
            SALogUtil.fine("Exception : " + SALogUtil.getSAStackTrace(e));
            SAExceptionLog2(cusno, id, tag, msg, "1111", SALogUtil.getSAStackTrace(e), osType, osVersion, model, appVersion, corp, result, type, saversion);
            return result_cnt;
        } catch (Throwable e) {
            return result_cnt;
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception exception) {}
            try {
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        }
        return result_cnt;
    }

    public static int SAExceptionLog1(String cusno, String id, String tag, String injson, String outjson, String msg, String errorcode, String stacktrace, String osType, String osVersion, String model, String appVersion, String corp, String result, String type, String saversion) {
        Connection con = null;
        PreparedStatement stmt = null;
        int result_cnt = 0;
        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            return result_cnt;
        } catch (Exception e) {
            return result_cnt;
        } catch (Throwable e) {
            return result_cnt;
        }
        try {
//            Context envContext = new InitialContext();
//            DataSource dsPool = (DataSource)envContext.lookup(SAProperty.DB_POOL_NAME);
            con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/"+ SAProperty.DB_POOL_NAME, "sa", "");
            con.setAutoCommit(true);
            String query = "INSERT INTO " + SAProperty.LOG_TB_NAME +
                    "(" + SAProperty.COL_LOG_SEQ + "," + SAProperty.COL_LOG_OSTYPE + "," + SAProperty.COL_LOG_OSVERSION + "," + SAProperty.COL_LOG_MODEL + "," + SAProperty.COL_LOG_APPVERSION + "," + SAProperty.COL_LOG_CUSNO + "," + SAProperty.COL_LOG_ID + "," + SAProperty.COL_LOG_TAG + "," + SAProperty.COL_LOG_INJSON + "," + SAProperty.COL_LOG_OUTJSON + "," + SAProperty.COL_LOG_DATA + "," + SAProperty.COL_LOG_ERRORCODE + "," + SAProperty.COL_LOG_STACKTRACE + "," + SAProperty.COL_LOG_CORP + "," + SAProperty.COL_LOG_RESULT + "," + SAProperty.COL_LOG_TYPE + "," + SAProperty.COL_LOG_SAVERSION + "," + SAProperty.COL_LOG_TIMESTAMP + ") " +
                    "VALUES (" + SAProperty.LOG_SEQUENCE_NAME + ".NEXTVAL" + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))";
            stmt = con.prepareStatement(query);
            stmt.setString(1, osType);
            stmt.setString(2, osVersion);
            stmt.setString(3, model);
            stmt.setString(4, appVersion);
            stmt.setString(5, cusno);
            stmt.setString(6, id);
            stmt.setString(7, tag);
            stmt.setString(8, injson);
            stmt.setString(9, outjson);
            stmt.setString(10, msg);
            stmt.setString(11, errorcode);
            stmt.setString(12, stacktrace);
            stmt.setString(13, corp);
            stmt.setString(14, result);
            stmt.setString(15, type);
            stmt.setString(16, saversion);
            result_cnt = stmt.executeUpdate();
        } catch (SQLException sQLException) {

        } catch (Exception exception) {

        } catch (Throwable throwable) {

        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception exception) {}
            try {
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        }
        return result_cnt;
    }

    public static int SAExceptionLog2(String cusno, String id, String tag, String msg, String errorcode, String stacktrace, String osType, String osVersion, String model, String appVersion, String corp, String result, String type, String saversion) {
        Connection con = null;
        PreparedStatement stmt = null;
        int result_cnt = 0;
        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            return result_cnt;
        } catch (Exception e) {
            return result_cnt;
        } catch (Throwable e) {
            return result_cnt;
        }
        try {
//            Context envContext = new InitialContext();
//            DataSource dsPool = (DataSource)envContext.lookup(SAProperty.DB_POOL_NAME);
            con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/"+ SAProperty.DB_POOL_NAME, "sa", "");
            con.setAutoCommit(true);
            String query = "INSERT INTO " + SAProperty.LOG_TB_NAME +
                    "(" + SAProperty.COL_LOG_SEQ + "," + SAProperty.COL_LOG_OSTYPE + "," + SAProperty.COL_LOG_OSVERSION + "," + SAProperty.COL_LOG_MODEL + "," + SAProperty.COL_LOG_APPVERSION + "," + SAProperty.COL_LOG_CUSNO + "," + SAProperty.COL_LOG_ID + "," + SAProperty.COL_LOG_TAG + "," + SAProperty.COL_LOG_DATA + "," + SAProperty.COL_LOG_ERRORCODE + "," + SAProperty.COL_LOG_STACKTRACE + "," + SAProperty.COL_LOG_CORP + "," + SAProperty.COL_LOG_RESULT + "," + SAProperty.COL_LOG_TYPE + "," + SAProperty.COL_LOG_SAVERSION + "," + SAProperty.COL_LOG_TIMESTAMP + ") + " +
                    "VALUES (" + SAProperty.LOG_SEQUENCE_NAME + ".NEXTVAL" + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))";
            stmt = con.prepareStatement(query);
            stmt.setString(1, osType);
            stmt.setString(2, osVersion);
            stmt.setString(3, model);
            stmt.setString(4, appVersion);
            stmt.setString(5, cusno);
            stmt.setString(6, id);
            stmt.setString(7, tag);
            stmt.setString(8, msg);
            stmt.setString(9, errorcode);
            stmt.setString(10, stacktrace);
            stmt.setString(11, corp);
            stmt.setString(12, result);
            stmt.setString(13, type);
            stmt.setString(14, saversion);
            result_cnt = stmt.executeUpdate();
        } catch (SQLException sQLException) {

        } catch (Exception exception) {

        } catch (Throwable throwable) {

        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception exception) {}
            try {
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        }
        return result_cnt;
    }
}
