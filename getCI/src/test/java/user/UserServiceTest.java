package user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.exception.TestUserServiceException;
import springbook.hello.handler.TransactionHandler;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.*;

import javax.sql.DataSource;


import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = Runnable.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserService testUserService;
    @Autowired
    UserDao userDao;
    @Autowired MailSender mailSender;
    @Autowired PlatformTransactionManager transactionManager; // auto bean
    @Autowired ApplicationContext context;


    List<User> users;	// test fixture

    @BeforeEach
    public void init() {
        users = Arrays.asList(new User("id11", "mj", "paord123", Level.GOLD, 30, 20),
                new User("id12", "mk", "pass22", Level.SILVER, 0, 10));

    }

    @Test
    public void readOnlyTransactionAttribute() {
        testUserService.getAll();
    }

    @Test
    public void transactionSync()
    {
        userService.deleteAll(); // 1번째

        userService.add(users.get(0)); // 2번째
        userService.add(users.get(1)); // 3번째

        // 총 3번의 트랜잭션 이걸 한번에 처리할수 없을까??
    }

    // 한번에 트랜잭션 처리
    @Test
    public void transactionOneTimeSync()
    {
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

        // 앞에서 만들어진 트랜잭션에 모두 참여...
        userService.deleteAll(); //

        userService.add(users.get(0)); //
        userService.add(users.get(1)); //

        transactionManager.commit(txStatus); // 앞에서 시작한 트랜잭션 커밋

    }

    @Test
    public void transactionOneTimeSyncReadOnly()
    {
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setReadOnly(true); // set read only setting
        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

        // 앞에서 만들어진 트랜잭션에 모두 참여...
        userService.deleteAll(); //

        userService.add(users.get(0)); //
        userService.add(users.get(1)); //

        transactionManager.commit(txStatus); // 앞에서 시작한 트랜잭션 커밋

    }

    @Test
    public void rollbackTest()
    {
        userDao.deleteAll();
        Assertions.assertThat(userDao.getCount()).isEqualTo(0);

        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);


        userService.add(users.get(0)); //
        userService.add(users.get(1)); //

        Assertions.assertThat(userDao.getCount()).isEqualTo(2);


        transactionManager.rollback(txStatus); // 앞에서 시작한 트랜잭션 rollback
        Assertions.assertThat(userDao.getCount()).isEqualTo(0);

    }


    @Test
    public void rollbackTest2()
    {

        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        userService.deleteAll();
        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

        try{

            userService.add(users.get(0)); //
            userService.add(users.get(1)); //
            Assertions.assertThat(userDao.getCount()).isEqualTo(2);
        }finally {
            transactionManager.rollback(txStatus); // 앞에서 시작한 트랜잭션 rollback
            Assertions.assertThat(userDao.getCount()).isEqualTo(0);
        }


    }

    @Test
    @Transactional
    @Rollback
    public void transactionalAnnotationTest()
    {
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }

    @Test
    @Transactional(readOnly = true)
    public void readOnlyTest()
    {
        // read only 속성으로 인해 삭제 실행시 에러가 나야함.
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }





}
