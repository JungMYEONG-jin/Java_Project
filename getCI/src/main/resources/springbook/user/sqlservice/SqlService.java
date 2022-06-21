package kakao.getCI.springbook.user.sqlservice;

import kakao.getCI.springbook.exception.SqlRetrievalFailureException;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
