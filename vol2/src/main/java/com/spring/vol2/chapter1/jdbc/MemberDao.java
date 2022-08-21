package com.spring.vol2.chapter1.jdbc;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;

public class MemberDao {

    private HibernateTemplate hibernateTemplate;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory){
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    public void addMember(Member member){
        hibernateTemplate.persist(member);
    }
}
