package kakao.getCI.com.shinhan.security.listener;

import kakao.getCI.com.shinhan.security.imple.SAProperty;
import kakao.getCI.com.shinhan.security.simpleauth.exception.SASimpleAuthSQLException;
import kakao.getCI.com.shinhan.security.simpleauth.message.SAErrorMessage;
import kakao.getCI.com.shinhan.security.simpleauth.tlv.SAErrsEnum;
import kakao.getCI.com.shinhan.security.simpleauth.util.SALogUtil;

import java.sql.*;
import java.util.HashMap;

public interface DBService {
    public Object deleteSAInfo(Object inputMap);

    public Object getSAAuthInitInfo(Object inputMap);

    public Object getSAAuthInfo(Object inputMap);

    public Object getSAUnregInfo(Object inputMap);

    public Object insertSAInfo(Object map);

    public Object SALog(Object...var);

    public Object SAExceptionLog1(Object... var);

    public Object SAExceptionLog2(Object... var);

}
