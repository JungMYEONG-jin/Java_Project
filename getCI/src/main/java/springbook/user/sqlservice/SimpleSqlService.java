package springbook.user.sqlservice;

import springbook.exception.SqlNotFoundException;
import springbook.exception.SqlRetrievalFailureException;

import java.util.Map;

public class SimpleSqlService implements SqlService{

    private Map<String, String> sqlMap;

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);
        if(sql == null)
        {
            throw new SqlNotFoundException(key+" not existed in the map!!");
        }else {
            return sql;
        }
    }
}
