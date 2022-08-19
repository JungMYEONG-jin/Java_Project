package com.spring.vol2.chapter2;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class AbstractSimpleJdbcDaoSupport extends JdbcDaoSupport {
    protected NamedParameterJdbcTemplate template;

    protected void initTemplateConfig(){
        this.template = new NamedParameterJdbcTemplate(getDataSource());
        initJdbcObjects();
    }

    protected void initJdbcObjects() {
        // 상속 받는 DAO에서 오버라이드해서 사용할 수 있는 초기화 메소드
    }
}


