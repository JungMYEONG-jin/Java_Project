package kakao.getCI.springbook.user.sqlservice.updatable;

import kakao.getCI.springbook.exception.SqlNotFoundException;
import kakao.getCI.springbook.exception.SqlUpdateFailureException;
import kakao.getCI.springbook.issuetracker.sqlservice.UpdatableSqlRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {

    private Map<String, String> sqlMap = new ConcurrentHashMap<>();

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        if(sqlMap.get(key) == null){
            throw new SqlNotFoundException(key + "에 해당하는 SQL을 찾을 수 없습니다,");
        }
        sqlMap.put(key, sql);
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        for (String s : sqlmap.keySet()) {
            updateSql(s, sqlMap.get(s));
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if(sql == null)
            throw new SqlNotFoundException(key + "를 이용해서 SQL을 찾을 수 없습니다,");
        else
            return sql;
    }
}
