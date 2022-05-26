package springbook.issuetracker.sqlservice.updatable;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import springbook.exception.SqlNotFoundException;
import springbook.exception.SqlUpdateFailureException;
import springbook.issuetracker.sqlservice.UpdatableSqlRegistry;

import javax.sql.DataSource;
import java.util.Map;

public class EmbeddedSqlRegistry implements UpdatableSqlRegistry {

    JdbcTemplate jdbc;

    public void setDataSource(DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        int affected = jdbc.update("update sqlmap set sql_ = ? where key_ = ?", sql, key);
        if (affected == 0){
            throw new SqlUpdateFailureException(key + "에 해당하는 값을 찾을 수 없습니다.");
        }
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {

        for (String s : sqlmap.keySet()) {
            updateSql(s, sqlmap.get(s));
        }


    }

    @Override
    public void registerSql(String key, String sql) {
        jdbc.update("insert into sqlmap(key_, sql_) values(?,?)", key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        try{
            return jdbc.queryForObject("select sql_ from sqlmap where key_ = ?", String.class, key);
        }catch (EmptyResultDataAccessException e){
            throw new SqlNotFoundException(key + "에 해당하는 값을 찾을 수 없습니다.", e);
        }
    }




}
