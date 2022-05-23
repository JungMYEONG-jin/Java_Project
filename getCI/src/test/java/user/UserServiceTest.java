package user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
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
    @Autowired UserDao userDao;
    @Autowired MailSender mailSender;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired ApplicationContext context;


    List<User> users;	// test fixture



    @Test
    public void readOnlyTransactionAttribute() {
        testUserService.getAll();
    }




}
