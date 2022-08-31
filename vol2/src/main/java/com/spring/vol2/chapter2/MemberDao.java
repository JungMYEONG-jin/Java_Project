package com.spring.vol2.chapter2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

public class MemberDao {

    private NamedParameterJdbcTemplate simpleJdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private SimpleJdbcCall simpleJdbcCall;

    @Autowired
    public void init(DataSource dataSource){
        this.simpleJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcCall = new SimpleJdbcCall(dataSource).withFunctionName("find_member");
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("member");
    }

}
