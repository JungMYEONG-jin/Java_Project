package user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.hello.handler.TransactionHandler;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.TxProxyFactoryBean;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceImpl;
import springbook.user.service.UserServiceTx;

import javax.sql.DataSource;


import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = Runnable.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    UserDao userDao;

    @Autowired
    DataSource dataSource;

    @Autowired
    MailSender mailSender;

    @Autowired
    ApplicationContext context;

    private List<User> users;

    User user;

    @BeforeEach
    void init()
    {
        userDao.deleteAll();
        users = Arrays.asList(
                new User("A001", "NJ", "test123", Level.BASIC, 49, 0),
                new User("A002", "Mon", "test123", Level.BASIC, 50, 0),
                new User("A003", "Son", "test123", Level.SILVER, 60, 29),
                new User("A004", "Yo", "test123", Level.SILVER, 60, 30),
                new User("A005", "Qon", "test123", Level.GOLD, 100, 100)
        );
//        userService.setUserDao(userDao);

        user = new User();

    }


    @Test
    void beanTest()
    {
        assertThat(this.userServiceImpl).isNotNull();
    }

    @Test
    void updateLevelTest() throws Exception
    {

        for(User user : users)
            userDao.add(user);

        userServiceImpl.upgradeLevels();

        users = userDao.getAll();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    public void checkLevel(User user, Level level)
    {
        User findUser = userDao.get(user.getId());
        assertThat(user.getLevel()).isEqualTo(level);
    }


    @Test
    void add()
    {
        User withLevelUser = users.get(4);
        User withOutLevel = users.get(0);
        withOutLevel.setLevel(null);

        userServiceImpl.add(withLevelUser);
        userServiceImpl.add(withOutLevel);

        User levelUser = userDao.get(withLevelUser.getId());
        User withoutLevelUser = userDao.get(withOutLevel.getId());

        assertThat(levelUser.getLevel()).isEqualTo(Level.GOLD);
        assertThat(withoutLevelUser.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    void upgradeLevel()
    {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if(level.getNext()==null)
                continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.getNext());
        }
    }





    @Test
    void cannotUpgradeLevel()
    {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.getNext()!=null)
                continue;
            user.setLevel(level);
            Assertions.assertThrows(IllegalStateException.class, ()->{user.upgradeLevel();});
        }
    }



    @Test
    public void upgradeLevels() throws Exception {
        for (User user : users) {
            userDao.add(user);
        }

        userServiceImpl.upgradeLevels();

        checkLevelUpgrade(users.get(0), false);
        checkLevelUpgrade(users.get(1), true);
        checkLevelUpgrade(users.get(2), false);
        checkLevelUpgrade(users.get(3), true);
        checkLevelUpgrade(users.get(4), false);
    }




    private void checkLevelUpgrade(User user, boolean upgraded) {

        User findUser = userDao.get(user.getId());
        if(upgraded)
        {
            assertThat(findUser.getLevel()).isEqualTo(user.getLevel().getNext());
        }else
        {
            assertThat(findUser.getLevel()).isEqualTo(user.getLevel());
        }

    }

    // test 대역
    static class TestUserService extends UserServiceImpl
    {
        private String id;

        public TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if(user.getId().equals(this.id))
                throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException
    {

    }

    @Autowired
    PlatformTransactionManager transactionManager;


    static class MockMailSender implements MailSender
    {
        List<String> requests = new ArrayList<>();

        public List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            requests.add(simpleMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage[] simpleMessages) throws MailException {

        }
    }


    @Test
    public void upgradeLevelsWithMail() throws Exception {

        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

//        for (User user : users) {
//            userDao.add(user);
//        }

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size()).isEqualTo(2);


        List<String> requests = mockMailSender.getRequests();
        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.get(0)).isEqualTo(users.get(1).getMail());
        assertThat(requests.get(1)).isEqualTo(users.get(3).getMail());
    }







    @Test
    @DirtiesContext
    void upgradeAllOrNothing() throws Exception {


        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setMailSender(this.mailSender);

        TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", TxProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserService);
        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try{
            txUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (TestUserServiceException e) {

        }

        checkLevelUpgrade(users.get(1), false);
    }

    static class MockUserDao implements UserDao
    {
        private List<User> users;
        private List<User> updated = new ArrayList<>();

        private MockUserDao(List<User> users)
        {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }



        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<User> getAll() {
            return this.users;
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll_jdbcTemplate_inner() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add_sqlException(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount_jdbcTemplate() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setDataSource(DataSource dataSource) {
            throw new UnsupportedOperationException();
        }


        @Override
        public void update(User user) {
            updated.add(user);
        }
    }



}
