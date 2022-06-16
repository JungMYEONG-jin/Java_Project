package com.simpleauthJPA.shinhan.security.simpleauth.util;

import com.shinhan.security.imple.SAProperty;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SALogUtil {
    public static void fine(String msg) {
        if (SAProperty.isLog)
            System.out.println("[simpleauth]" + msg);
    }

    public static String getSAStackTrace(Exception e) {
        String result = "";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        e.printStackTrace(ps);
        result = out.toString();
        return result;
    }

    public static int SALog(String cusno, String id, String tag, String injson, String outjson, String msg, String errorcode, String stacktrace) {
        Connection con = null;
        PreparedStatement stmt = null;
        int result_cnt = 0;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException classNotFoundException) {

        } catch (Exception exception) {

        } catch (Throwable throwable) {}
        try {
            Context envContext = new InitialContext();
            DataSource dsPool = (DataSource)envContext.lookup(SAProperty.DB_POOL_NAME);
            con = dsPool.getConnection();
            con.setAutoCommit(true);
            String in_query = "INSERT INTO " + SAProperty.LOG_TB_NAME + "(" + SAProperty.COL_LOG_SEQ + "," + SAProperty.COL_LOG_CUSNO + "," + SAProperty.COL_LOG_ID + "," + SAProperty.COL_LOG_TAG + "," + SAProperty.COL_LOG_INJSON + "," + SAProperty.COL_LOG_OUTJSON + "," + SAProperty.COL_LOG_DATA + "," + SAProperty.COL_LOG_ERRORCODE + "," + SAProperty.COL_LOG_STACKTRACE + "," + SAProperty.COL_LOG_TIMESTAMP + ") values (" + SAProperty.LOG_SEQUENCE_NAME + ".NEXTVAL" + ",?,?,?,?,?,?,?,?,TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))";
            stmt = con.prepareStatement(in_query);
            stmt.setString(1, cusno);
            stmt.setString(2, id);
            stmt.setString(3, tag);
            stmt.setString(4, injson);
            stmt.setString(5, outjson);
            stmt.setString(6, msg);
            stmt.setString(7, errorcode);
            stmt.setString(8, stacktrace);
            result_cnt = stmt.executeUpdate();
        } catch (SQLException sQLException) {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        } catch (Exception exception) {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception1) {}
        } catch (Throwable throwable) {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        }
        return result_cnt;
    }

    public static int SALog(String cusno, String id, String tag, String msg, String errorcode, String stacktrace) {
        Connection con = null;
        PreparedStatement stmt = null;
        int result_cnt = 0;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException classNotFoundException) {

        } catch (Exception exception) {

        } catch (Throwable throwable) {}
        try {
            Context envContext = new InitialContext();
            DataSource dsPool = (DataSource)envContext.lookup(SAProperty.DB_POOL_NAME);
            con = dsPool.getConnection();
            con.setAutoCommit(true);
            String in_query = "INSERT INTO " + SAProperty.LOG_TB_NAME + "(" + SAProperty.COL_LOG_SEQ + "," + SAProperty.COL_LOG_CUSNO + "," + SAProperty.COL_LOG_ID + "," + SAProperty.COL_LOG_TAG + "," + SAProperty.COL_LOG_DATA + "," + SAProperty.COL_LOG_ERRORCODE + "," + SAProperty.COL_LOG_STACKTRACE + "," + SAProperty.COL_LOG_TIMESTAMP + ") values (" + SAProperty.LOG_SEQUENCE_NAME + ".NEXTVAL" + ",?,?,?,?,?,?,TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))";
            stmt = con.prepareStatement(in_query);
            stmt.setString(1, cusno);
            stmt.setString(2, id);
            stmt.setString(3, tag);
            stmt.setString(4, msg);
            stmt.setString(5, errorcode);
            stmt.setString(6, stacktrace);
            result_cnt = stmt.executeUpdate();
        } catch (SQLException sQLException) {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        } catch (Exception exception) {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception1) {}
        } catch (Throwable throwable) {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        }
        return result_cnt;
    }

    public static int SALog(String cusno, String id, String tag, String injson, String outjson, String msg, String errorcode, String stacktrace, String osType, String osVersion, String model, String appVersion, String corp, String result) {
        Connection con = null;
        PreparedStatement stmt = null;
        int result_cnt = 0;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException classNotFoundException) {

        } catch (Exception exception) {

        } catch (Throwable throwable) {}
        try {
            Context envContext = new InitialContext();
            DataSource dsPool = (DataSource)envContext.lookup(SAProperty.DB_POOL_NAME);
            con = dsPool.getConnection();
            con.setAutoCommit(true);
            String in_query = "INSERT INTO " + SAProperty.LOG_TB_NAME + "(" + SAProperty.COL_LOG_SEQ + "," + SAProperty.COL_LOG_OSTYPE + "," + SAProperty.COL_LOG_OSVERSION + "," + SAProperty.COL_LOG_MODEL + "," + SAProperty.COL_LOG_APPVERSION + "," + SAProperty.COL_LOG_CUSNO + "," + SAProperty.COL_LOG_ID + "," + SAProperty.COL_LOG_TAG + "," + SAProperty.COL_LOG_INJSON + "," + SAProperty.COL_LOG_OUTJSON + "," + SAProperty.COL_LOG_DATA + "," + SAProperty.COL_LOG_ERRORCODE + "," + SAProperty.COL_LOG_STACKTRACE + "," + SAProperty.COL_LOG_TIMESTAMP + "," + SAProperty.COL_LOG_CORP + "," + SAProperty.COL_LOG_RESULT + ") values (" + SAProperty.LOG_SEQUENCE_NAME + ".NEXTVAL" + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))";
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
            result_cnt = stmt.executeUpdate();
        } catch (SQLException sQLException) {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        } catch (Exception exception) {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception1) {}
        } catch (Throwable throwable) {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        }
        return result_cnt;
    }

    public static int SALog(String cusno, String id, String tag, String msg, String errorcode, String stacktrace, String osType, String osVersion, String model, String appVersion, String corp, String result) {
        Connection con = null;
        PreparedStatement stmt = null;
        int result_cnt = 0;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException classNotFoundException) {

        } catch (Exception exception) {

        } catch (Throwable throwable) {}
        try {
            Context envContext = new InitialContext();
            DataSource dsPool = (DataSource)envContext.lookup(SAProperty.DB_POOL_NAME);
            con = dsPool.getConnection();
            con.setAutoCommit(true);
            String in_query = "INSERT INTO " + SAProperty.LOG_TB_NAME + "(" + SAProperty.COL_LOG_SEQ + "," + SAProperty.COL_LOG_OSTYPE + "," + SAProperty.COL_LOG_OSVERSION + "," + SAProperty.COL_LOG_MODEL + "," + SAProperty.COL_LOG_APPVERSION + "," + SAProperty.COL_LOG_CUSNO + "," + SAProperty.COL_LOG_ID + "," + SAProperty.COL_LOG_TAG + "," + SAProperty.COL_LOG_DATA + "," + SAProperty.COL_LOG_ERRORCODE + "," + SAProperty.COL_LOG_STACKTRACE + "," + SAProperty.COL_LOG_TIMESTAMP + "," + SAProperty.COL_LOG_CORP + "," + SAProperty.COL_LOG_RESULT + ") values (" + SAProperty.LOG_SEQUENCE_NAME + ".NEXTVAL" + ",?,?,?,?,?,?,?,?,?,?,?,?,TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))";
            stmt = con.prepareStatement(in_query);
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
            result_cnt = stmt.executeUpdate();
        } catch (SQLException sQLException) {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        } catch (Exception exception) {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception1) {}
        } catch (Throwable throwable) {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (Exception exception) {}
        }
        return result_cnt;
    }
}
