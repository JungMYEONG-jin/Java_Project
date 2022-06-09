package kakao.getCI.springbook.issuetracker.sqlservice;

import kakao.getCI.springbook.exception.SqlUpdateFailureException;
import kakao.getCI.springbook.user.sqlservice.SqlRegistry;

import java.util.Map;

public interface UpdatableSqlRegistry extends SqlRegistry {

    public void updateSql(String key, String sql) throws SqlUpdateFailureException;
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;
}
