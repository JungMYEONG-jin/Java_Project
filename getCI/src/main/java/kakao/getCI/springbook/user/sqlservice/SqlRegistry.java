package kakao.getCI.springbook.user.sqlservice;

import kakao.getCI.springbook.exception.SqlNotFoundException;

public interface SqlRegistry {

    void registerSql(String key, String sql);

    String findSql(String key) throws SqlNotFoundException;
}
