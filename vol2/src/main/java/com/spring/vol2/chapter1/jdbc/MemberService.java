package com.spring.vol2.chapter1.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberDao memberDao;
    private TransactionTemplate transactionTemplate;

    @Autowired
    public void init(PlatformTransactionManager platformTransactionManager){
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
    }

}
